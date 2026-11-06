package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 职位实体（公司+职位合并）
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("job")
@Schema(description = "职位信息")
public class Job {
    @Schema(description = "职位ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司Logo")
    private String companyLogo;

    @Schema(description = "公司简介")
    private String companyIntro;

    @Schema(description = "职位名称")
    private String jobTitle;

    @Schema(description = "职位类型")
    private String jobType;

    @Schema(description = "薪资范围")
    private String salary;

    @Schema(description = "工作城市")
    private String city;

    @Schema(description = "工作地址")
    private String address;

    @Schema(description = "经验要求")
    private String experience;

    @Schema(description = "学历要求")
    private String education;

    @Schema(description = "职位描述")
    private String jobDesc;

    @Schema(description = "任职要求")
    private String jobRequirement;

    @Schema(description = "福利待遇")
    private String welfare;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "浏览次数")
    private int viewCount;

    @Schema(description = "投递次数")
    private int applyCount;
}
