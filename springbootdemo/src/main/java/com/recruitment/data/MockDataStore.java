package com.recruitment.data;

import com.recruitment.model.Application;
import com.recruitment.model.Favorite;
import com.recruitment.model.Job;
import com.recruitment.model.Resume;
import com.recruitment.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 临时数据存储（内存版）
 * 用于第02步数据绑定阶段，避免引入数据库
 *
 * @author Web架构技术教学组
 */
public final class MockDataStore {

    private static final AtomicLong USER_ID = new AtomicLong(1);
    private static final AtomicLong RESUME_ID = new AtomicLong(1);
    private static final AtomicLong FAVORITE_ID = new AtomicLong(1);
    private static final AtomicLong APPLICATION_ID = new AtomicLong(1);

    private static final Map<Long, User> USERS_BY_ID = new ConcurrentHashMap<>();
    private static final Map<String, User> USERS_BY_USERNAME = new ConcurrentHashMap<>();
    private static final Map<Long, Job> JOBS_BY_ID = new ConcurrentHashMap<>();
    private static final Map<Long, Resume> RESUMES_BY_ID = new ConcurrentHashMap<>();

    private static final List<Favorite> FAVORITES = new CopyOnWriteArrayList<>();
    private static final List<Application> APPLICATIONS = new CopyOnWriteArrayList<>();

    static {
        seedUsers();
        seedJobs();
    }

    private MockDataStore() {
    }

    // ========== ID生成 ==========
    public static long nextUserId() {
        return USER_ID.getAndIncrement();
    }

    public static long nextResumeId() {
        return RESUME_ID.getAndIncrement();
    }

    public static long nextFavoriteId() {
        return FAVORITE_ID.getAndIncrement();
    }

    public static long nextApplicationId() {
        return APPLICATION_ID.getAndIncrement();
    }

    // ========== 用户相关 ==========
    public static void saveUser(User user) {
        USERS_BY_ID.put(user.getId(), user);
        USERS_BY_USERNAME.put(user.getUsername(), user);
    }

    public static User getUserById(Long id) {
        return USERS_BY_ID.get(id);
    }

    public static User getUserByUsername(String username) {
        return USERS_BY_USERNAME.get(username);
    }

    // ========== 职位相关 ==========
    public static Collection<Job> getJobs() {
        return JOBS_BY_ID.values();
    }

    public static Job getJobById(Long id) {
        return JOBS_BY_ID.get(id);
    }

    public static void saveJob(Job job) {
        JOBS_BY_ID.put(job.getId(), job);
    }

    // ========== 简历相关 ==========
    public static void saveResume(Resume resume) {
        RESUMES_BY_ID.put(resume.getId(), resume);
    }

    public static Resume getResumeById(Long id) {
        return RESUMES_BY_ID.get(id);
    }

    public static void deleteResume(Long id) {
        RESUMES_BY_ID.remove(id);
    }

    public static List<Resume> getResumesByUserId(Long userId) {
        List<Resume> result = new ArrayList<>();
        for (Resume resume : RESUMES_BY_ID.values()) {
            if (resume.getUserId().equals(userId)) {
                result.add(resume);
            }
        }
        return result;
    }

    // ========== 收藏相关 ==========
    public static List<Favorite> getFavorites() {
        return FAVORITES;
    }

    // ========== 投递相关 ==========
    public static List<Application> getApplications() {
        return APPLICATIONS;
    }

    // ========== 初始化测试数据 ==========
    private static void seedUsers() {
        User demo = new User();
        demo.setId(nextUserId());
        demo.setUsername("demo");
        demo.setPassword("123456");
        demo.setNickname("示例用户");
        demo.setEmail("demo@example.com");
        demo.setPhone("13800000000");
        demo.setCreateTime(LocalDateTime.now());
        saveUser(demo);
    }

    private static void seedJobs() {
        List<Job> jobs = new ArrayList<>();

        // 阿里巴巴
        jobs.add(Job.builder()
                .id(1L)
                .companyName("阿里巴巴")
                .companyLogo("/logo/alibaba.png")
                .companyIntro("全球领先的互联网公司，业务包括电商、云计算、数字媒体等")
                .jobTitle("Java开发工程师")
                .jobType("全职")
                .salary("15K-30K")
                .city("杭州")
                .address("余杭区文一西路969号")
                .experience("3-5年")
                .education("本科")
                .jobDesc("负责后端服务开发与维护，熟悉Spring Boot。")
                .jobRequirement("有Java开发经验，理解分布式系统。")
                .welfare("五险一金,年终奖,免费三餐")
                .contactName("HR李女士")
                .contactPhone("0571-88158000")
                .viewCount(0)
                .applyCount(0)
                .build());

        // 腾讯科技
        jobs.add(Job.builder()
                .id(2L)
                .companyName("腾讯科技")
                .companyLogo("/logo/tencent.png")
                .companyIntro("中国最大的互联网综合服务提供商，业务涵盖社交、游戏、金融等")
                .jobTitle("前端开发工程师")
                .jobType("全职")
                .salary("18K-30K")
                .city("深圳")
                .address("南山区科技园")
                .experience("1-3年")
                .education("本科")
                .jobDesc("负责前端页面开发，熟悉Vue/React。")
                .jobRequirement("掌握HTML/CSS/JavaScript，了解前端工程化。")
                .welfare("五险一金,年终奖,免费班车")
                .contactName("HR王先生")
                .contactPhone("0755-86013388")
                .viewCount(0)
                .applyCount(0)
                .build());

        // 字节跳动
        jobs.add(Job.builder()
                .id(3L)
                .companyName("字节跳动")
                .companyLogo("/logo/bytedance.png")
                .companyIntro("全球领先的移动互联网公司，旗下有抖音、今日头条等产品")
                .jobTitle("Go开发工程师")
                .jobType("全职")
                .salary("20K-40K")
                .city("北京")
                .address("海淀区知春路")
                .experience("3-5年")
                .education("本科")
                .jobDesc("负责后端服务开发，熟悉Go语言与微服务。")
                .jobRequirement("有高并发系统经验，熟悉常见中间件。")
                .welfare("五险一金,年终奖,免费三餐")
                .contactName("HR张女士")
                .contactPhone("010-12345678")
                .viewCount(0)
                .applyCount(0)
                .build());

        for (Job job : jobs) {
            saveJob(job);
        }
    }
}
