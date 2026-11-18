package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("user")  // 指定数据库表名
@Schema(description = "用户信息")
public class User {
    @Schema(description = "用户ID")
    @TableId(type = IdType.AUTO)  // 主键自增
    private Long id;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 20, message = "用户名长度必须在6-20之间")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    @Schema(description = "昵称")
    @Size(max = 30, message = "昵称长度不能超过30")
    private String nickname;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "角色：USER/ADMIN")
    private String role;
}
