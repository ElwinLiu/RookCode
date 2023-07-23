package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.*;
import com.example.server.dto.Question.*;
import com.example.server.dto.User.ProgressResp;
import com.example.server.mapper.*;
import com.example.server.pojo.*;
import com.example.server.service.IQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions> implements IQuestionsService {
    @Autowired(required = false)
    private QuestionsMapper questionsMapper;
    @Autowired(required = false)
    private QuestionTagsMapper tagsMapper;
    @Autowired(required = false)
    private QuestionTagsMapMapper tagsMapMapper;
    @Autowired(required = false)
    private QuestionTagsMapper questionTagsMapper;
    @Autowired(required = false)
    private SolutionsMapper solutionsMapper;
    @Autowired(required = false)
    private RecordsMapper recordsMapper;
    @Autowired(required = false)
    private UsersMapper usersMapper;

    // 根据页数获取题目
    @Override
    public Resp<List<QuestionResp>> getQuestions(int page) {
        // 设置查询条件
        QueryWrapper<Questions> queryWrapper = new QueryWrapper<>();
        Page<Questions> pageNum = new Page<>(page, 15); // 查询页面，每页15条数据
        // 开始查询
        List<Questions> questions = questionsMapper.selectPage(pageNum, queryWrapper).getRecords();

        // 生成List<QuestionResp>
        List<QuestionResp> questionResps = new ArrayList<>();
        for (Questions question : questions) {
            // 获取该question的标签
            List<String> tags = getQuestionTagsById(question.getId());
            // 生成QuestionResp
            QuestionResp questionResp = new QuestionResp(question.getId(),
                    question.getTitle(), "", tags, question.getDifficulty());
            // 插入questionResps数组
            questionResps.add(questionResp);
        }

        return Resp.success(questionResps);
    }

    // 根据id获取题目
    @Override
    public Resp<QuestionResp> getQuestion(int id) {
        // 设置查询条件
        QueryWrapper<Questions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        // 异常处理
        int count = questionsMapper.selectCount(queryWrapper);
        if (count == 0) {
            // 没有查询到数据，返回null或抛出异常
            return Resp.fail("不存在该id的题目");
        }

        // 开始查询
        Questions question = questionsMapper.selectOne(queryWrapper);
        // 获取该question的标签
        List<String> tags = getQuestionTagsById(question.getId());
        QuestionResp resp = new QuestionResp(question.getId(), question.getTitle(),
                question.getContent(), tags, question.getDifficulty());
        return Resp.success(resp);
    }

    @Override
    public Resp<ShowProblemListResp> showProblemList(String account, String difficulty, String state, List<String> tags, String input, int page) {
        // 获取用户id
        int user_id = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 检查有没有做过题目
        if ((state.equals("solved") || state.equals("tried")) && recordsMapper.selectCount(new QueryWrapper<Records>().eq("user_id", user_id)) == 0) {
            return Resp.success(new ShowProblemListResp(0, null));
        }

        // 构建分页对象
        Page<Questions> pageObj = new Page<>(page, 15);

        // 构建查询条件
        QueryWrapper<Questions> queryWrapper = new QueryWrapper<>();
        int p_difficulty = 0;

        // 查询难度等于传入的difficulty值或者difficulty为"none"
        if (!"none".equals(difficulty)) {

            // 解析difficulty字符串
            switch (difficulty) {
                case "easy":
                    p_difficulty = 1;
                    break;
                case "medium":
                    p_difficulty = 2;
                    break;
                case "hard":
                    p_difficulty = 3;
                    break;
                default:
                    break;
            }

            // 按题目难度筛选
            queryWrapper.eq("difficulty", p_difficulty);
        }

        // 查询状态等于传入的state值或者state为"none"
        List<Integer> questionIds; // 联合查询中间变量
        if (!"none".equals(state)) {
            switch (state) {
                case "unanswered":
                    queryWrapper.notExists("SELECT * FROM records WHERE records.question_id = questions.id AND records.user_id = " + user_id);
                    break;
                case "solved":
                    // 获取满足条件的题目id
                    questionIds = recordsMapper.selectQuestionIdsByUserIdAndState1(user_id);
                    // 如果ids为空，则没有满足条件的题目，直接返回
                    if (questionIds.isEmpty()) return Resp.success(new ShowProblemListResp(0, null));
                    queryWrapper.in("id", questionIds);
                    break;
                case "tried":
                    // 获取满足条件的题目id
                    questionIds = recordsMapper.selectQuestionIdsByUserIdAndState0(user_id);
                    // 如果ids为空，则没有满足条件的题目，直接返回
                    if (questionIds.isEmpty()) return Resp.success(new ShowProblemListResp(0, null));
                    queryWrapper.in("id", questionIds);
                    break;
                default:
                    break;
            }
        }

        // 查询所有满足条件的标签ID
        List<Integer> tagIds = new ArrayList<>();
        for (String tag : tags) {
            QueryWrapper<QuestionTags> tagQueryWrapper = new QueryWrapper<>();
            tagQueryWrapper.eq("name", tag);
            QuestionTags questionTags = questionTagsMapper.selectOne(tagQueryWrapper);
            if (questionTags != null) {
                tagIds.add(questionTags.getId());
            }
        }

        // 类型转化
        List<String> tagIdStrs = tagIds.stream().map(Object::toString).collect(Collectors.toList());

        // 查询包含传入的tag值的题目
        if (!tagIds.isEmpty()) {
            queryWrapper.inSql("id", "SELECT question_id FROM question_tags_map WHERE tag IN (" + String.join(",", tagIdStrs) + ") GROUP BY question_id HAVING COUNT(DISTINCT tag) = " + tagIds.size());

        }

        // 查询标题包含传入的input值的题目
        if (input != null && !input.isEmpty()) {
            queryWrapper.like("title", input);
        }

        // 获取题目列表
        List<Questions> questions = questionsMapper.selectPage(pageObj, queryWrapper).getRecords();

        // 创建返回结果对象
        ShowProblemListResp resp = new ShowProblemListResp();
        List<ProblemResp> problemResps = new ArrayList<>();
        int r_total_page = (int) Math.ceil((double) questionsMapper.selectCount(queryWrapper) / 15);

        for (Questions question : questions) {
            // 参数收集
            int r_id = question.getId();
            String r_title = question.getTitle();
            int r_solution_num = solutionsMapper.selectCount(new QueryWrapper<Solutions>().eq("question_id", r_id));
            int r_pass_rate;
            String r_difficulty;
            String r_state;

            // 处理r_pass_rate
            int allRecords = recordsMapper.selectCount(new QueryWrapper<Records>().eq("question_id", r_id));
            int passedRecords = 100 * recordsMapper.selectCount(new QueryWrapper<Records>().eq("question_id", r_id).eq("state", 1));
            if (allRecords == 0) r_pass_rate = 100;
            else r_pass_rate = passedRecords / allRecords;

            // 处理r_difficulty，将难度参数转换为字符串
            switch (question.getDifficulty()) {
                case 1:
                    r_difficulty = "easy";
                    break;
                case 2:
                    r_difficulty = "medium";
                    break;
                case 3:
                    r_difficulty = "hard";
                    break;
                default:
                    r_difficulty = "unknown";
                    break;
            }

            // 处理r_state，查看是否提交过
            if (recordsMapper.selectCount(new QueryWrapper<Records>().eq("question_id", question.getId()).eq("user_id", user_id)) == 0) {
                r_state = "unanswered";
            } else {
                // 查看是否通过，至少一次提交通过即为通过
                boolean hasAccepted = recordsMapper.selectList(new LambdaQueryWrapper<Records>()
                                .eq(Records::getQuestionId, question.getId())
                                .eq(Records::getUserId, user_id)
                                .eq(Records::getState, 1))
                        .stream()
                        .anyMatch(record -> record.getState() == 1);
                if (hasAccepted) {
                    r_state = "solved";
                } else {
                    r_state = "tried";
                }
            }

            ProblemResp problemResp = new ProblemResp(r_id, r_title, r_solution_num, r_pass_rate, r_difficulty, r_state);
            problemResps.add(problemResp);
        }

        resp.setProblemList(problemResps);
        resp.setTotal_page(r_total_page);

        return Resp.success(resp);
    }

    @Override
    public Resp<ProgressResp> getProgress(String account) {
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            return Resp.fail("不存在该用户");
        }

        // 获得各个难度的题目数量
        int r_easy_total = questionsMapper.selectCount(new QueryWrapper<Questions>().eq("difficulty", 1));
        int r_medium_total = questionsMapper.selectCount(new QueryWrapper<Questions>().eq("difficulty", 2));
        int r_hard_total = questionsMapper.selectCount(new QueryWrapper<Questions>().eq("difficulty", 3));
        // 获取用户各难度的完成情况
        int r_easy_solved = solvedNum(account, 1);
        int r_medium_solved = solvedNum(account, 2);
        int r_hard_solved = solvedNum(account, 3);

        ProgressResp resp = new ProgressResp(r_easy_solved, r_easy_total,
                r_medium_solved, r_medium_total, r_hard_solved, r_hard_total);

        return Resp.success(resp);
    }

    @Override
    public Resp<QuestionInfoResp> getQuestionInfo(int id) {
        QuestionInfoResp resp = recordsMapper.getQuestionInfo(id);
        return Resp.success(resp);
    }

    /*
     * @Author YFMan
     * @Description 发布题目业务逻辑
     * @Date 2023/4/25 10:55
     * @Param [questions]
     * @return com.example.server.dto.Resp<java.lang.String>
     **/
    @Override
    public Resp<Integer> publishQuestion(QuestionTagsDTO questionTagsDTO) {
        // 添加测试用例，需要 id 为 null，如果不为 null，会对已有 entity 进行修改
 /*       if (questionTags.getId() != null) {
            questionTags.setId(null);
        }*/

        // 设置待插入题目的参数
        Questions questions = new Questions();
        questions.setTitle(questionTagsDTO.getTitle());
        questions.setContent(questionTagsDTO.getContent());
        questions.setDifficulty(questionTagsDTO.getDifficulty());
        //boolean isSuccessPubQuestion = save(questions);
        questionsMapper.saveQuestionReturnId(questions);

        // 获取题目id
        Integer questionsId = questions.getId();

        // 获取题目标签
        List<Integer> tagsList = new ArrayList<>();

        // 如果题目传入标签不为空
        if (questionTagsDTO.getTags().size() != 0) {
            tagsList = convertTagsToIds(questionTagsDTO.getTags());
        }

        // 插入标签-题目id映射表
        for (Integer tag : tagsList) {
            // 建立一个标签对象
            QuestionTagsMap questionTagsMap = new QuestionTagsMap(questionsId, tag);
            // 插入一个标签
            int isSuccessPubTags = tagsMapMapper.insert(questionTagsMap);
        }

        // 添加成功
        if (questionsId != null) {
            return Resp.success(questionsId);
        }
        // 添加失败
        else {
            return Resp.fail("发布失败！");
        }
    }

    /*
     * @Author YFMan
     * @Description 修改题目业务逻辑
     * @Date 2023/4/25 10:56
     * @Param [questions]
     * @return com.example.server.dto.Resp<java.lang.String>
     **/
    @Override
    public Resp<String> updateQuestion(QuestionTagsDTO questionTagsDTO) {
        // 如果 id 为空，返回 id 为空信息
        if (questionTagsDTO.getId() == null) {
            return Resp.success("修改题目失败！id为空！");
        }

        // 创建 question 对象
        Questions questions = new Questions();
        questions.setId(questionTagsDTO.getId());
        questions.setTitle(questionTagsDTO.getTitle());
        questions.setContent(questionTagsDTO.getContent());
        questions.setDifficulty(questionTagsDTO.getDifficulty());

        int countNum = deleteQuestionTags(questions.getId());
        boolean isSuccess = updateById(questions);

        if (!isSuccess) { // 如果更新失败，证明 questionId 不存在
            return Resp.fail("题目不存在！");
        }

        // 记录标签是否成功插入
        boolean isSuccessPubTags = true;
        // 如果tags不为空
        if (questionTagsDTO.getTags().size() > 0) {
            // 获取最新的标签
            List<Integer> tags = convertTagsToIds(questionTagsDTO.getTags());
            // 发布最新的标签
            isSuccessPubTags = pubQuestionTags(questions.getId(), tags);
        }


        if (isSuccessPubTags) {
            return Resp.success("成功修改题目！");
        } else {
            return Resp.fail("修改题目失败！");
        }
    }

    /*
     * @Author YFMan
     * @Description 删除题目业务逻辑
     * @Date 2023/4/25 10:56
     * @Param [id] 题目 id
     * @return com.example.server.dto.Resp<java.lang.String>
     **/
    @Override
    public Resp<String> deleteQuestion(Integer id) {
        int countNum = deleteQuestionTags(id);
        // 根据 题目id 删除对应题目，如果 id 存在删除成功，否则删除失败
        boolean isSuccess = removeById(id);

        if (isSuccess) {
            return Resp.success("成功删除题目！");
        } else {
            return Resp.fail("删除失败！");
        }
    }

    /*
     * @Author YFMan
     * @Description 更新题目难度服务
     * @Date 2023/4/27 12:21
     * @Param [updateDifficultyDTO]
     * @return com.example.server.dto.Resp<java.lang.String>
     **/
    @Override
    public Resp<String> updateQuestionDifficulty(UpdateDifficultyDTO updateDifficultyDTO) {
        UpdateWrapper<Questions> updateWrapper = new UpdateWrapper<>();
        // 设置 要更新的实体类 id
        updateWrapper.eq("id", updateDifficultyDTO.getQuestionId());
        // 设置 要更新的字段值
        updateWrapper.set("difficulty", updateDifficultyDTO.getDifficulty());
        int isSuccess = questionsMapper.update(null, updateWrapper);
        if (isSuccess > 0) {
            return Resp.success("修改题目难度成功！");
        } else {
            return Resp.fail("修改题目难度失败！");
        }
    }

    @Override
    public Resp<String> updateQuestionTags(UpdateTagsRequest updateTagsRequest) {
        // 删除旧的标签
        int countNum = deleteQuestionTags(updateTagsRequest.getQuestionId());
        // 将 Names 转化为 Ids
        List<Integer> tags = convertTagsToIds(updateTagsRequest.getTags());
        // 发布新的标签
        boolean isSuccessPubTags = pubQuestionTags(updateTagsRequest.getQuestionId(), tags);
        if(isSuccessPubTags){
            return Resp.success("题目标签修改成功！");
        }else{
            return Resp.fail("题目标签修改失败！");
        }
    }

    private boolean pubQuestionTags(Integer questionsId, List<Integer> tagsList) {
        for (Integer tag : tagsList) {
            // 建立一个标签对象
            QuestionTagsMap questionTagsMap = new QuestionTagsMap(questionsId, tag);
            // 插入一个标签
            int isSuccessPubTags = tagsMapMapper.insert(questionTagsMap);
        }
        return true;
    }

    /*
     * @Author YFMan
     * @Description 根据 questionId 删除对应的标签，在删除题目，修改题目（修改=删除+新建）的时候使用
     * @Date 2023/4/26 21:41
     * @Param [questionId]
     * @return java.lang.Integer 删除的 questionTag 的数量
     **/
    private Integer deleteQuestionTags(Integer questionId) {
        QueryWrapper<QuestionTagsMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id", questionId);
        return tagsMapMapper.delete(queryWrapper);
    }

    /*
     * @Author YFMan
     * @Description 将TagsName转化为TagsId
     * @Date 2023/4/27 11:52
     * @Param [tagNameList]
     * @return java.util.List<java.lang.Integer>
     **/
    private List<Integer> convertTagsToIds(List<String> tagNameList) {
        LambdaQueryWrapper<QuestionTags> queryWrapper = new LambdaQueryWrapper<QuestionTags>()
                .in(QuestionTags::getName, tagNameList)
                .select(QuestionTags::getId);

        return questionTagsMapper.selectList(queryWrapper).stream()
                .map(QuestionTags::getId)
                .collect(Collectors.toList());
    }


    /******************************* 操作数据库 *******************************/

    /**
     * 根据题目难度和用户账号获取用户的完成数量
     *
     * @param account    用户账号
     * @param difficulty 题目难度1-3
     * @return 完成数量
     */
    public int solvedNum(String account, int difficulty) {
        int user_id = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();
        return recordsMapper.SolvedQustionCnt(user_id, difficulty);
    }

    // 获取单个question的标签内容
    public List<String> getQuestionTagsById(int id) {
        // 设置查询条件,获取tags的id
        QueryWrapper<QuestionTagsMap> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("question_id", id);
        // 创建question_tag_map数组
        List<QuestionTagsMap> questionTagsMaps = new ArrayList<>();
        // 获取数据
        questionTagsMaps = tagsMapMapper.selectList(queryWrapper0);

        // 创建question标签数组
        List<String> tags = new ArrayList<>();

        // 根据标签id获取标签内容
        for (QuestionTagsMap tagsId : questionTagsMaps) {
            // 设置查询条件,根据id获取tags的内容
            QueryWrapper<QuestionTags> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", tagsId.getTag());
            // 获取数据
            tags.add(tagsMapper.selectOne(queryWrapper1).getName());
        }

        return tags;
    }
}
