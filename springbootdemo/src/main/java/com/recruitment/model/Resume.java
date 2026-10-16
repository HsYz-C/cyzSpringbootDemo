package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历实体
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("resume")
public class Resume {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;

    /** 真实姓名 */
    private String realName;

    /** 性别：0-女 1-男 */
    private Integer gender;

    /** 头像URL */
    private String avatar;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 当前城市 */
    private String city;

    /** 最高学历 */
    private String education;

    /** 工作经验年限 */
    private String experience;

    /** 期望薪资 */
    private String expectedSalary;

    /** 技能标签（逗号分隔） */
    private String skills;

    /** 自我评价 */
    private String introduction;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
