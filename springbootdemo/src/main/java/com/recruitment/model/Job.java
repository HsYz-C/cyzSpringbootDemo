package com.recruitment.model;

import lombok.Builder;
import lombok.Data;

/**
 * 职位实体（公司+职位合并）
 *
 * @author Web架构技术教学组
 */
@Data
@Builder
public class Job {
    private Long id;
    private String companyName;
    private String companyLogo;
    private String companyIntro;

    private String jobTitle;
    private String jobType;
    private String salary;
    private String city;
    private String address;
    private String experience;
    private String education;
    private String jobDesc;
    private String jobRequirement;
    private String welfare;
    private String contactName;
    private String contactPhone;

    private int viewCount;
    private int applyCount;
}
