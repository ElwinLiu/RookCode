package com.example.server.service.impl;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.dto.*;
import com.example.server.dto.Manager.UserListResp;
import com.example.server.dto.Manager.UserResp;
import com.example.server.dto.Question.LanguageResp;
import com.example.server.dto.Record.PassInfoListResp;
import com.example.server.dto.Record.PassInfoResp;
import com.example.server.dto.User.*;
import com.example.server.mapper.FollowsMapper;
import com.example.server.mapper.RecordsMapper;
import com.example.server.mapper.SolutionsMapper;
import com.example.server.mapper.UsersMapper;
import com.example.server.pojo.Follows;
import com.example.server.pojo.Records;
import com.example.server.pojo.Solutions;
import com.example.server.pojo.Users;
import com.example.server.service.IUsersService;
import com.example.server.util.*;
import com.github.pagehelper.PageHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.server.util.CommonUtils.urlToMultipartFile;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    //	引入邮件接口
    @Autowired
    private JavaMailSender mailSender;
    @Autowired(required = false)
    private UsersMapper usersMapper;
    @Autowired
    private DefaultKaptcha defaultKaptcha; // 账号登录验证码
    @Autowired(required = false)
    private SolutionsMapper solutionsMapper;
    @Autowired(required = false)
    private RecordsMapper recordsMapper;

    // followsMapper
    @Autowired(required = false)
    private FollowsMapper followsMapper;
    private final RedisTemplate<String, String> redisTemplate; // 将验证码存入redis中
    private final String default_avatar = "https://elwinliu-blog-bucket.oss-cn-hangzhou.aliyuncs.com/cugSE-RookCode%2F2023%2F05%2F08%2F4739c62f164147d1a9e338f43fc2ba89default_avatar.webp";
    private static final long CAPTCHA_CODE_EXPIRED = 60 * 1000 * 5; // 5分钟有效

    // 获得发件人信息
    @Value("${spring.mail.username}")
    private String from;

    // 构造redis模板对象
    public UsersServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 用户登录，返回token
    @Override
    public Resp<LoginResp> login(String account, String password, String code, HttpServletRequest request) {
        // 获取请求的key
        String key = getCaptchaKey(request); // 获取key

        // 通过key获取redis中的验证码
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) return Resp.fail("验证码已经过期！");
        else if (!value.equals(code)) return Resp.fail("验证码不正确！");

        // 在这里执行用户身份验证，如果验证通过则返回用户信息
        Users user = getUsersByAccount(account, password);
        if (user == null) {
            // 如果身份验证失败，返回错误响应
            return Resp.fail("用户名或密码错误");
        }
        user.setPassword("*****");
        // 如果身份创建成功，创建 JWT
        String token = JwtUtils.generateToken(user.getAccount());

        // 判断是否为管理员
        boolean is_admin = is_admin(account);

        // 返回JWT和用户信息
        return Resp.success(new LoginResp(user, token, is_admin));
    }

    // 生成验证码图片
    @Override
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        //-------------------生成验证码 begin --------------------------

        // 获取浏览器缓存的key
        String key = getCaptchaKey(request);

        // 将验证码存入Redis
        String text = defaultKaptcha.createText(); // 获取验证码的文本内容
        redisTemplate.opsForValue().set(key, text, CAPTCHA_CODE_EXPIRED, TimeUnit.MINUTES); // 将生成的验证码存入redis，有效期5分钟

        // 根据文本验证码内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            // 输出流输出图片， 格式为jpg
            ImageIO.write(image, "jpg", servletOutputStream);
            // 发送到浏览器
            servletOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 生成邮箱注册验证码
    @Override
    public Resp<String> registerCode(String email) {
        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 设置发送方
        message.setTo(email);  // 设置接收方
        message.setSubject("您本次的验证码是"); // 设置标题

        String verCode = VerCodeGenerate.generateVerCode(); // 生成随机6位验证码

        redisTemplate.opsForValue().set(email, verCode, Duration.ofMinutes(5)); // 将生成的验证码存入redis，有效期5分钟

        // 邮件消息模板设置
        message.setText("尊敬的xxx,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        // 发送邮件
        mailSender.send(message);

        return Resp.success("发送成功");
    }

    // 注册邮箱，校验验证码以及是否重复注册
    @Override
    public Resp<String> register(String account, String password, String code) {
        // 从redis中获取之前保存的验证码
        String savedCode = redisTemplate.opsForValue().get(account);
        // 比对两个验证码，并反馈
        if (savedCode != null && savedCode.equals(code)) {
            if (insertUser(new Users(account, password)) == 1) {
                return Resp.success("注册成功");
            } else {
                return Resp.fail("您已经注册过了");
            }
        }
        return Resp.fail("验证码错误！");
    }

    @Override
    public Resp<String> changeNickName(String name, String account) {
        // 获取用户id
        int id = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // 更新昵称
        if (updateNicknameById(id, name) == 1) {
            return Resp.success("成功修改用户名为" + name);
        } else {
            return Resp.fail("不存在该用户！");
        }
    }

    // 上传用户头像, username用于校验是否为本人操作
    @Override
    public Resp<AvatarResp> uploadAvatar(MultipartFile file, String account) {
        // 获取用户id
        int id = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account)).getId();

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = AliOssUtil.ENDPOINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = AliOssUtil.KEYID;
        String accessKeySecret = AliOssUtil.KEYSECRET;
        String bucketName = AliOssUtil.BUCKETNAME;

        try {
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1.由于文件名重复会覆盖，生成随机文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            //2.把文件按照日期分类
            String root = "cugSE-RookCode";
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            fileName = root + "/" + datePath + "/" + fileName;

            //第二个参数：上传到oss的文件路径和文件名称  /aa/bb1.jpg
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            // 由于上传到oss以后会自动对路径进行一次url编码，因此我们传出的resp也需要相应编码
            bucketName = URLEncoder.encode(bucketName, StandardCharsets.UTF_8.toString());
            endpoint = URLEncoder.encode(endpoint, StandardCharsets.UTF_8.toString());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());

            //把上传之后oss返回的文件url返回（）
            //url格式：https://edu-guli-study.oss-cn-beijing.aliyuncs.com/%25U%7EHW%2502P2OH6FXR%29%5B8%60T2A.png
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;


            if (updateAvatarById(id, url) == 1) {
                return Resp.success(new AvatarResp(url));
            } else {
                return Resp.fail("更新失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Resp.fail("图像上传失败！");
        }
    }

    /**
     * 根据用户token获取account，传入该函数，查询用户的基本信息
     *
     * @param account
     * @return
     */
    @Override
    public Resp<GetUserByTokenResp> getUserByToken(String account) {
        // 根据account查询用户
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));

        // 若没有用户
        if (user == null) {
            return Resp.fail("用户信息拉取失败！");
        }

        // 创建返回结果
        GetUserByTokenResp resp = new GetUserByTokenResp(user.getNickname(), user.getAccount(), user.getAvatar(), user.getDescription());

        return Resp.success(resp);
    }

    /**
     * 获取缓存的key
     *
     * @param request
     * @return
     */
    private String getCaptchaKey(HttpServletRequest request) {
        // 获取用户ip地址
        String ip = CommonUtils.getIpAddr(request);
        // 获取浏览器请求头
        String userAgent = request.getHeader("User-Agent");
        String key = "user-service:captcha:" + CommonUtils.MD5(ip + userAgent);
        return key;
    }

    /**
     * 根据id和account比对是否为同一个用户
     * 返回0：不存在该用户
     * 返回1：没有权限操控其他用户
     */
    @Override
    public int checkAuth(int id, String account) {
        Users user = getUsersById(id);
        if (user == null) {
            return 0;
        }
        if (!user.getAccount().equals(account)) {
            return 1;
        }
        return 2;
    }

    @Override
    public Resp<UserDetailResp> getUserDetail(String myAccount, String account, boolean r_is_self) {
        // 根据account查询用户
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            // 若查无此用户
            return Resp.fail("不存在该用户");
        }
        int user_id = user.getId();

        // 创建变量
        String r_nickname = user.getNickname();
        String r_account = user.getAccount();
        String r_avatar = user.getAvatar();
        String r_description = user.getDescription();
        int r_view = 0;
        int r_like = 0;
        List<LanguageResp> r_langs = new ArrayList<>();

        // 获取点赞数和浏览量
        // 构造查询条件
        QueryWrapper<Solutions> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(
                "SUM(view_num) as total_view_num",
                "SUM(like_num) as total_like_num"
        ).eq("author_id", user_id);
        // 执行查询
        Map<String, Object> result = solutionsMapper.selectMaps(queryWrapper).get(0);
        if (result != null) {
            // 获取查询结果
            r_view = ((BigDecimal) result.get("total_view_num")).intValue();
            r_like = ((BigDecimal) result.get("total_like_num")).intValue();
        }

        // 获取各语言的解题总数
        // C语言
        int lang_c_num = recordsMapper.selectCount(new QueryWrapper<Records>()
                .eq("user_id", user_id)
                .eq("lang_id", 1)
                .select("distinct question_id"));
        LanguageResp lang_c = new LanguageResp("C", lang_c_num);
        r_langs.add(lang_c);
        // C++
        int lang_cpp_num = recordsMapper.selectCount(new QueryWrapper<Records>()
                .eq("user_id", user_id)
                .eq("lang_id", 2)
                .select("distinct question_id"));
        LanguageResp lang_cpp = new LanguageResp("C++", lang_cpp_num);
        r_langs.add(lang_cpp);
        // C++
        int lang_py_num = recordsMapper.selectCount(new QueryWrapper<Records>()
                .eq("user_id", user_id)
                .eq("lang_id", 3)
                .select("distinct question_id"));
        LanguageResp lang_py = new LanguageResp("Python", lang_py_num);
        r_langs.add(lang_py);
        // Java
        int lang_java_num = recordsMapper.selectCount(new QueryWrapper<Records>()
                .eq("user_id", user_id)
                .eq("lang_id", 4)
                .select("distinct question_id"));
        LanguageResp lang_java = new LanguageResp("Java", lang_java_num);
        r_langs.add(lang_java);

        // 获取当前用户的id
        int my_id = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", myAccount)).getId();

        // 获取主页用户的id
        int home_id = user.getId();

        // 判断 my_id 是否已关注 home_id
        QueryWrapper<Follows> followQueryWrapper = new QueryWrapper<>();
        followQueryWrapper.eq("follower_id", home_id).eq("followee_id", my_id);

        // 若已关注
        boolean r_isFollow = (followsMapper.selectCount(followQueryWrapper) > 0);


        // 创建返回对象
        UserDetailResp resp = new UserDetailResp(r_is_self, r_nickname,
                r_account, r_avatar, r_description,
                r_view, r_like, r_langs, r_isFollow);

        return Resp.success(resp);
    }

    @Override
    public Resp<List<YearSubmitResp>> getYearSubmit(String account) {
        // 根据account查询用户
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            // 若查无此用户
            return Resp.fail("不存在该用户");
        }
        int user_id = user.getId();

        // 获取当前时间的前一年
        LocalDate oneYearBefore = LocalDate.now().minusYears(1);

        // 条件查询
        LambdaQueryWrapper<Records> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Records::getUserId, user_id)  // 按用户id筛选
                .ge(Records::getSubmitDate, oneYearBefore.atStartOfDay());  // 按提交时间筛选

        // 执行查询
        List<Records> recordsList = recordsMapper.selectList(queryWrapper);

        // 将结果按照日期分组并统计提交数量
        Map<LocalDate, Integer> submitMap = recordsList.stream()
                .collect(Collectors.groupingBy(r -> r.getSubmitDate().toLocalDate(), Collectors.summingInt(e -> 1)));
        // 将结果转换成List<YearSubmitResp>
        List<YearSubmitResp> yearSubmitList = submitMap.entrySet().stream()
                .map(e -> new YearSubmitResp(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return Resp.success(yearSubmitList);

    }

    @Override
    public Resp<String> updateUserInfo(String nickname, String description, String avatar, String account) {
        // 将图片转为MultipartFile
        MultipartFile multipartFile;
        final String[] base64Array;
        if (avatar.startsWith("data:image")) {
            // 处理 base64 字符串
            base64Array = avatar.split(",");
            multipartFile = extractBase64(base64Array);
        } else {
            // 处理网络路径
            try {
                multipartFile = urlToMultipartFile(avatar);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 上传头像
        uploadAvatar(multipartFile, account);

        // 更新昵称和简介
        Users user = new Users();
        user.setNickname(nickname);
        user.setDescription(description);

        // 执行更新操作
        usersMapper.update(user, new QueryWrapper<Users>().eq("account", account));

        return Resp.success("yes");
    }

    @Override
    public Resp<PassInfoListResp> getPassInfo(int page, String account) {

        // 根据用户账号获取用户的id
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account));
        if (user == null) {
            // 若查无此用户
            return Resp.fail("不存在该用户");
        }
        int user_id = user.getId();

        // 获取用户通过的提交记录列表
        List<PassInfoResp> passList = new ArrayList<>();
        int cnt = recordsMapper.getPassInfoListCnt(user_id);
        int r_page = (int) Math.ceil((double) cnt / 15);
        if (cnt > 0) {
            // 静态分页
            PageHelper.startPage(page, 15);
            passList = recordsMapper.getPassInfoList(user_id);
        }

        // 创建返回结果
        PassInfoListResp resp = new PassInfoListResp(r_page, passList);

        return Resp.success(resp);
    }

    @Override
    public boolean is_admin(String account) {
        byte[] a = new byte[]{'1'};

        return Arrays.equals(usersMapper.selectOne(new QueryWrapper<Users>()
                        .eq("account", account))
                .getIsAdmin(), a);
    }

    @Override
    public Resp<UserListResp> getUserList(int page, String account) {
        // 鉴权
        if (!is_admin(account)) return Resp.fail("请求失败，您不是管理员！");

        // 定义每页的条数
        int pageSize = 15;
        // 创建分页对象
        Page<Users> PG = new Page<>(page, pageSize);

        // 获取用户列表
        int cnt = usersMapper.selectCount(new QueryWrapper<Users>().isNull("is_admin")); // 获取总数
        List<Users> users = usersMapper.selectPage(PG, new QueryWrapper<Users>().isNull("is_admin")).getRecords();
        List<UserResp> userRespList = users.stream()
                .map(user -> new UserResp(user.getAvatar(), user.getAccount(), user.getNickname(), user.getDescription(), user.getId()))
                .collect(Collectors.toList());

        // 创建返回对象
        UserListResp resp = new UserListResp(userRespList, cnt);

        return Resp.success(resp);
    }

    @Override
    public Resp<String> deleteUser(int id, String account) {
        if (!is_admin(account)) return Resp.fail("请求失败，您不是管理员！");
        if (usersMapper.deleteById(id) == 0) {
            return Resp.fail("删除失败，不存在该用户！");
        } else {
            return Resp.success("删除成功！");
        }
    }

    private MultipartFile extractBase64(String[] base64Array) {
        String dataUir, data;
        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {
            //根据你base64代表的具体文件构建
            dataUir = "data:image/jpg;base64";
            data = base64Array[0];
        }
        return new Base64ToMultipartFile(data, dataUir);
    }

    /******************************* 操作数据库 *******************************/
    // 通过账号获取User信息
    @Override
    public Users getUsersByAccount(String account, String password) {
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("account", account).eq("password", password));
    }

    // 通过用户id更新用户头像
    @Override
    public int updateAvatarById(int id, String url) {
        // 构建更新条件
        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);

        // 构建更新内容
        Users updateUser = new Users();
        updateUser.setAvatar(url);

        // 调用update方法进行更新
        try {
            usersMapper.update(updateUser, updateWrapper);
            return 1;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    // 通过用户id更新用户昵称
    @Override
    public int updateNicknameById(int id, String name) {
        // 构建更新条件
        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);

        // 构建更新内容
        Users updateUser = new Users();
        updateUser.setNickname(name);

        // 调用update方法进行更新
        try {
            usersMapper.update(updateUser, updateWrapper);
            // 更新成功
            return 1;
        } catch (NullPointerException e) {
            // 更新失败
            return 0;
        }
    }

    @Override
    public Users getUsersById(int id) {
        try {
            return usersMapper.selectOne(new QueryWrapper<Users>().eq("id", id));
        } catch (NullPointerException e) {
            return null;
        }
    }

    // 插入用户（邮箱注册），若插入成功返回1，如果已经存在或其他错误原因，则返回0
    @Override
    public int insertUser(Users user) {
        user.setAvatar(default_avatar);
        user.setNickname(user.getAccount());
        try {
            return usersMapper.insert(user);
        } catch (DuplicateKeyException e) {
            // 处理重复键异常，例如打印日志、返回错误信息等
            return 0;
        }
    }
}
