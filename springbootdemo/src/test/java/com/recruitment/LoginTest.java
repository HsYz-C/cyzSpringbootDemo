package com.recruitment;

import com.recruitment.common.Result;
import com.recruitment.mapper.UserMapper;
import com.recruitment.model.User;
import com.recruitment.service.UserService;
import com.recruitment.util.JwtUtil;
import com.recruitment.vo.LoginVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 登录接口测试
 *
 * <p>数据库里只有 demo 账号且其密码是明文（与 BCrypt 校验不匹配），
 * 因此测试自己注册临时用户，跑完再删除，不污染数据库。</p>
 *
 * @author Web架构技术教学组
 */
@SpringBootTest
class LoginTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    /** 本次测试创建的用户，用于跑完清理 */
    private final List<Long> createdUserIds = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Long id : createdUserIds) {
            userMapper.deleteById(id);
        }
        createdUserIds.clear();
    }

    /**
     * 注册一个临时用户，保证测试不依赖库里已有的账号
     */
    private User registerTempUser() {
        String username = "jwt_test_" + System.currentTimeMillis();
        User register = new User();
        register.setUsername(username);
        register.setPassword("123456");
        register.setNickname("JWT测试用户");
        register.setEmail(username + "@example.com");
        register.setPhone("13800138000");

        Result<User> registered = userService.register(register);
        System.out.println("【注册临时用户】" + username + " → code=" + registered.getCode());
        assertEquals(200, registered.getCode().intValue());
        assertNotNull(registered.getData());

        createdUserIds.add(registered.getData().getId());
        return registered.getData();
    }

    // 测试点1：登录成功返回 LoginVO（token + 用户信息）
    @Test
    void testLoginSuccessGeneratesToken() {
        System.out.println("========== 测试点1：登录成功生成 Token ==========");

        User created = registerTempUser();

        User request = new User();
        request.setUsername(created.getUsername());
        request.setPassword("123456");
        System.out.println("【输入】username = " + request.getUsername() + "，password = " + request.getPassword());

        Result<LoginVO> result = userService.login(request);
        System.out.println("【返回 code】" + result.getCode());
        System.out.println("【返回 message】" + result.getMessage());
        assertEquals(200, result.getCode().intValue());

        LoginVO loginVO = result.getData();
        assertNotNull(loginVO);

        System.out.println("---------- LoginVO 结构 ----------");
        System.out.println("LoginVO.token         = " + loginVO.getToken());
        System.out.println("LoginVO.user.id       = " + loginVO.getUser().getId());
        System.out.println("LoginVO.user.username = " + loginVO.getUser().getUsername());
        System.out.println("LoginVO.user.nickname = " + loginVO.getUser().getNickname());
        System.out.println("LoginVO.user.password = " + loginVO.getUser().getPassword());
        System.out.println("---------------------------------");

        // Token 非空
        String token = loginVO.getToken();
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 返回的用户信息正确，且密码已被脱敏
        assertEquals(created.getId(), loginVO.getUser().getId());
        assertEquals(created.getUsername(), loginVO.getUser().getUsername());
        assertNull(loginVO.getUser().getPassword());

        // Token 可验签，且能解析出正确的 userId
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(created.getId(), jwtUtil.getUserIdFromToken(token));

        System.out.println("========== 测试完成：登录成功并正确生成 Token ✅ ==========");
    }

    // 测试点2：密码错误时登录失败，不生成 Token
    @Test
    void testLoginFailWrongPassword() {
        System.out.println("========== 测试点2：密码错误登录失败 ==========");

        User created = registerTempUser();

        User request = new User();
        request.setUsername(created.getUsername());
        request.setPassword("wrongPassword");
        System.out.println("【输入】username = " + request.getUsername() + "，password = " + request.getPassword());

        Result<LoginVO> result = userService.login(request);
        System.out.println("【返回 code】" + result.getCode());
        System.out.println("【返回 message】" + result.getMessage());

        assertNotEquals(200, result.getCode().intValue());
        assertNull(result.getData());

        System.out.println("========== 测试完成：密码错误时拒绝登录 ✅ ==========");
    }

    // 测试点3：用户名不存在时登录失败
    @Test
    void testLoginFailUserNotFound() {
        System.out.println("========== 测试点3：用户名不存在登录失败 ==========");

        User request = new User();
        request.setUsername("no_such_user_" + System.currentTimeMillis());
        request.setPassword("123456");

        Result<LoginVO> result = userService.login(request);
        System.out.println("【返回 code】" + result.getCode());
        System.out.println("【返回 message】" + result.getMessage());

        assertNotEquals(200, result.getCode().intValue());
        assertNull(result.getData());

        System.out.println("========== 测试完成：用户不存在时拒绝登录 ✅ ==========");
    }
}
