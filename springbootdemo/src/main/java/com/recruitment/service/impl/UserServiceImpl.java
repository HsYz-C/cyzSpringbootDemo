package com.recruitment.service.impl;

import com.recruitment.common.Result;
import com.recruitment.data.MockDataStore;
import com.recruitment.model.User;
import com.recruitment.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现（内存版）
 * @author Web架构技术教学组
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public Result<User> register(User request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.error("用户名和密码不能为空");
        }

        if (MockDataStore.getUserByUsername(request.getUsername()) != null) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setId(MockDataStore.nextUserId());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCreateTime(LocalDateTime.now());
        MockDataStore.saveUser(user);

        return Result.success(maskPassword(user));
    }

    @Override
    public Result<User> login(User request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.error("用户名和密码不能为空");
        }

        User user = MockDataStore.getUserByUsername(request.getUsername());
        if (user == null || !request.getPassword().equals(user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        return Result.success(maskPassword(user));
    }

    @Override
    public Result<User> getUserInfo(Long userId) {
        User user = MockDataStore.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(maskPassword(user));
    }

    /**
     * 脱敏处理，返回给前端时隐藏密码
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

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}