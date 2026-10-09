package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.model.User;
import com.recruitment.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author Web架构技术教学组
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * POST /api/users/register
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User request) {
        return userService.register(request);
    }

    /**
     * 用户登录
     * POST /api/users/login
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User request) {
        return userService.login(request);
    }
}
