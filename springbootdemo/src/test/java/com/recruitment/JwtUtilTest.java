package com.recruitment;

import com.recruitment.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JwtUtil 单元测试
 *
 * @author Web架构技术教学组
 */
@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    // 测试点1：生成 Token 不为空
    @Test
    void testGenerateToken() {
        System.out.println("========== 测试点1：生成 Token ==========");
        System.out.println("【输入】userId = 1");

        String token = jwtUtil.generateToken(1L, "USER");

        String[] parts = token.split("\\.");
        String header = new String(Base64.getUrlDecoder().decode(parts[0]));
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

        System.out.println("【生成结果】Token = " + token);
        System.out.println("【Header  解码】" + header);
        System.out.println("【Payload 解码】" + payload);
        System.out.println("【Signature】" + parts[2]);
        System.out.println("【断言】Token 不为空 → " + (token != null && !token.isEmpty() ? "✅ 通过" : "❌ 失败"));

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    // 测试点2：合法 Token 验证通过
    @Test
    void testValidateToken_valid() {
        System.out.println("========== 测试点2：合法 Token 验证 ==========");
        System.out.println("【步骤1】用 userId=1 生成合法 Token");
        String token = jwtUtil.generateToken(1L, "USER");
        System.out.println("【Token】" + token);

        System.out.println("【步骤2】用同一密钥重新计算签名，与 Token 末尾对比");
        System.out.println("【步骤3】检查过期时间（expiration），当前时间是否超过 exp");

        boolean result = jwtUtil.validateToken(token);

        System.out.println("【步骤4】签名一致 + 未过期 → 验证" + (result ? "✅ 通过" : "❌ 失败"));
        System.out.println("【返回值】" + result);

        assertTrue(result);
    }

    // 测试点3：非法 Token 验证失败
    @Test
    void testValidateToken_invalid() {
        System.out.println("========== 测试点3：非法 Token 验证 ==========");
        String fakeToken = "this.is.fake";
        System.out.println("【输入】伪造 Token = " + fakeToken);
        System.out.println("【步骤1】尝试 Base64 解码 Header 部分：\"this\"");
        System.out.println("【步骤2】解码失败或格式不合法 → 抛出 MalformedJwtException");
        System.out.println("【步骤3】catch 捕获异常 → return false");

        boolean result = jwtUtil.validateToken(fakeToken);

        System.out.println("【返回值】" + result + " → " + (!result ? "✅ 通过（非法 Token 被正确拒绝）" : "❌ 失败"));

        assertFalse(result);
    }

    // 测试点4：从 Token 中解析用户ID
    @Test
    void testGetUserIdFromToken() {
        System.out.println("========== 测试点4：解析用户ID ==========");
        Long userId = 5L;
        System.out.println("【步骤1】存入 userId = " + userId);

        String token = jwtUtil.generateToken(userId, "USER");
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        System.out.println("【步骤2】生成 Token，Payload = " + payload);
        System.out.println("【步骤3】validateToken 验签通过，取 Payload");
        System.out.println("【步骤4】claims.getSubject() 取出 sub 字段（String 类型）");
        System.out.println("【步骤5】Long.parseLong(sub) 转为 Long 类型");

        Long parsed = jwtUtil.getUserIdFromToken(token);

        System.out.println("【解析结果】" + parsed);
        System.out.println("【断言】存入 " + userId + " == 解析出 " + parsed + " → " + (userId.equals(parsed) ? "✅ 通过" : "❌ 失败"));

        assertEquals(userId, parsed);
    }

    // 测试点5：非法 Token 解析返回 null
    @Test
    void testGetUserIdFromToken_invalid() {
        System.out.println("========== 测试点5：非法 Token 解析用户ID ==========");
        String fakeToken = "invalid.token.string";
        System.out.println("【输入】非法 Token = " + fakeToken);
        System.out.println("【步骤1】parseSignedClaims 验签失败 → 抛出异常");
        System.out.println("【步骤2】catch 捕获异常 → return null");

        Long parsed = jwtUtil.getUserIdFromToken(fakeToken);

        System.out.println("【返回值】" + parsed + " → " + (parsed == null ? "✅ 通过（返回 null）" : "❌ 失败"));

        assertNull(parsed);
    }

    // 测试点6：Token 中写入并解析角色
    @Test
    void testGetRoleFromToken() {
        System.out.println("========== 测试点6：解析角色 ==========");

        String token = jwtUtil.generateToken(1L, "ADMIN");
        String role = jwtUtil.getRoleFromToken(token);

        System.out.println("【步骤1】用 userId=1、role=ADMIN 生成 Token");
        System.out.println("【步骤2】解析出的角色 = " + role);
        System.out.println("【断言】role == ADMIN → " + ("ADMIN".equals(role) ? "✅ 通过" : "❌ 失败"));

        assertEquals("ADMIN", role);
    }
}
