package com.recruitment.controller;

import com.recruitment.common.Result;
import com.recruitment.context.UserContext;
import com.recruitment.model.User;
import com.recruitment.service.UserService;
import com.recruitment.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author Web架构技术教学组
 */
@Tag(name = "用户管理", description = "用户注册、登录相关接口")
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
    @Operation(summary = "用户注册", description = "新用户注册账号")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User request) {
        return userService.register(request);
    }

    /**
     * 用户登录
     * POST /api/users/login
     */
    @Operation(summary = "用户登录", description = "登录成功后返回 JWT Token 和用户信息")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody User request) {
        return userService.login(request);
    }

    /**
     * 获取当前登录用户信息
     * GET /api/users/info
     */
    @Operation(summary = "获取当前用户信息", description = "根据请求头中的 Token 返回当前登录用户信息")
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = UserContext.getCurrentUserId();
        return userService.getUserInfo(userId);
    }
}
