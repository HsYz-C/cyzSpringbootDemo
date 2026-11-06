package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历实体
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
    private Long userId;

    /** 真实姓名 */
    @Schema(description = "真实姓名")
    private String realName;

    /** 性别：0-女 1-男 */
    @Schema(description = "性别：0-女，1-男")
    private Integer gender;

    /** 头像URL */
    @Schema(description = "头像URL")
    private String avatar;

    /** 联系电话 */
    @Schema(description = "联系电话")
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 当前城市 */
    @Schema(description = "所在城市")
    private String city;

    /** 最高学历 */
    @Schema(description = "学历")
    private String education;

    /** 工作经验年限 */
    @Schema(description = "工作经验")
    private String experience;

    /** 期望薪资 */
    @Schema(description = "期望薪资")
    private String expectedSalary;

    /** 技能标签（逗号分隔） */
    @Schema(description = "技能标签（逗号分隔）")
    private String skills;

    /** 自我评价 */
    @Schema(description = "自我介绍")
    private String introduction;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
