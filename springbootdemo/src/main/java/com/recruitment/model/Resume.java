package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.recruitment.validation.CreateGroup;
import com.recruitment.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历实体
 *
 * <p>校验分组：CreateGroup 用于新建（必填校验），UpdateGroup 用于更新（只校验传了的字段）。</p>
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("resume")
@Schema(description = "简历信息")
public class Resume {
    @Schema(description = "简历ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空", groups = CreateGroup.class)
    private Long userId;

    /** 真实姓名 */
    @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不能为空", groups = CreateGroup.class)
    @Size(max = 20, message = "真实姓名长度不能超过20", groups = {CreateGroup.class, UpdateGroup.class})
    private String realName;

    /** 性别：0-女 1-男 */
    @Schema(description = "性别：0-女，1-男")
    @Min(value = 0, message = "性别只能是0或1", groups = {CreateGroup.class, UpdateGroup.class})
    @Max(value = 1, message = "性别只能是0或1", groups = {CreateGroup.class, UpdateGroup.class})
    private Integer gender;

    /** 头像URL */
    @Schema(description = "头像URL")
    @Size(max = 255, message = "头像地址长度不能超过255", groups = {CreateGroup.class, UpdateGroup.class})
    private String avatar;

    /** 联系电话 */
    @Schema(description = "联系电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {CreateGroup.class, UpdateGroup.class})
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确", groups = {CreateGroup.class, UpdateGroup.class})
    private String email;

    /** 当前城市 */
    @Schema(description = "所在城市")
    @Size(max = 50, message = "城市长度不能超过50", groups = {CreateGroup.class, UpdateGroup.class})
    private String city;

    /** 最高学历 */
    @Schema(description = "学历")
    @Size(max = 50, message = "学历长度不能超过50", groups = {CreateGroup.class, UpdateGroup.class})
    private String education;

    /** 工作经验年限 */
    @Schema(description = "工作经验")
    @Size(max = 100, message = "工作经验长度不能超过100", groups = {CreateGroup.class, UpdateGroup.class})
    private String experience;

    /** 期望薪资 */
    @Schema(description = "期望薪资")
    @Size(max = 50, message = "期望薪资长度不能超过50", groups = {CreateGroup.class, UpdateGroup.class})
    private String expectedSalary;

    /** 技能标签（逗号分隔） */
    @Schema(description = "技能标签（逗号分隔）")
    @Size(max = 500, message = "技能标签长度不能超过500", groups = {CreateGroup.class, UpdateGroup.class})
    private String skills;

    /** 自我评价 */
    @Schema(description = "自我介绍")
    @Size(max = 1000, message = "自我介绍长度不能超过1000", groups = {CreateGroup.class, UpdateGroup.class})
    private String introduction;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
