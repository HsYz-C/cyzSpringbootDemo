package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.UserMapper;
import com.recruitment.model.User;
import com.recruitment.service.UserService;
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
    public Result<User> login(User request) {
        /*登录：
         * 1.判断用户名或密码是否为空
         * 用户名在mock数据中是否存在
         * 判断密码是否匹配*/
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
            return Result.error(500,"密码错误");
        }
        return Result.success(user);
    }


    @Override
    public Result<User> getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);  // 根据ID查询
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(maskPassword(user));
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
        return safe;
    }

    /**
     * 判断字符串是否为空
     */
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
