package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.UserMapper;
import com.recruitment.model.User;
import com.recruitment.service.UserService;
import com.recruitment.util.JwtUtil;
import com.recruitment.util.RedisUtil;
import com.recruitment.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;

/**
 * 用户服务实现
 *
 * @author Web架构技术教学组
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;  // 注入 Mapper

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    // 缓存key前缀
    private static final String CACHE_USER_INFO = "user:info:";
    // 缓存过期时间（秒）
    private static final long CACHE_EXPIRE = 300;

    @Override
    public Result<User> register(User request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.error("用户名和密码不能为空");
        }

        // 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.selectOne(wrapper) != null) {
            return Result.error("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 注意这里修正了拼写
        user.setPassword(encoder.encode(request.getPassword())); // 这里一定要加密！
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);  // 插入数据库

        return Result.success(maskPassword(user));
    }

    @Override
    public Result<LoginVO> login(User request) {
        /*登录：
         * 1.判断用户名或密码是否为空
         * 2.查询用户是否存在
         * 3.使用 BCrypt 校验密码
         * 4.校验通过后生成 JWT Token，与脱敏后的用户信息一起返回*/
        if (request == null || isBlank(request.getUsername()) ||
                isBlank(request.getPassword())){
            return Result.error(500,"用户名或密码不能为空");
        }

        //User user=MockDataStore.getUserByUsername(request.getUsername());
        LambdaQueryWrapper<User> wrapper =new LambdaQueryWrapper<>();
        //sql语句：select * from user where username=request.username;
        wrapper.eq(User::getUsername,request.getUsername());
        User user = userMapper.selectOne(wrapper);

        //使用BCrypt验证密码（不能直接用eq，用match）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(user==null||!encoder.matches(request.getPassword(),user.getPassword())){
            return Result.error(500,"用户名或密码错误");
        }

        // 生成JWT Token（角色写入 token，供 AOP 权限校验使用）
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        LoginVO loginVO = new LoginVO(token, maskPassword(user));

        return Result.success(loginVO);
    }


    @Override
    public Result<User> getUserInfo(Long userId) {
        // 先从缓存获取
        String cacheKey = CACHE_USER_INFO + userId;
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof User) {
            return Result.success((User) cached);
        }

        User user = userMapper.selectById(userId);  // 根据ID查询
        if (user == null) {
            return Result.error("用户不存在");
        }

        User safe = maskPassword(user);
        // 存入缓存
        redisUtil.set(cacheKey, safe, CACHE_EXPIRE);
        return Result.success(safe);
    }

    /**
     * 隐藏密码
     */
    private User maskPassword(User user) {
        User safe = new User();
        safe.setId(user.getId());
        safe.setUsername(user.getUsername());
        safe.setNickname(user.getNickname());
        safe.setEmail(user.getEmail());
        safe.setPhone(user.getPhone());
        safe.setCreateTime(user.getCreateTime());
        safe.setRole(user.getRole());
        return safe;
    }

    /**
     * 判断字符串是否为空
     */
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
