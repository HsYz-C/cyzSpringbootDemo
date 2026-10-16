-- 创建数据库
CREATE DATABASE IF NOT EXISTS recruitment_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE recruitment_db;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `username` VARCHAR(50) NOT NULL COMMENT '用户名',
                        `password` VARCHAR(100) NOT NULL COMMENT '密码',
                        `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
                        `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                        `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 职位表
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
                       `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                       `company_name` VARCHAR(100) NOT NULL COMMENT '公司名称',
                       `company_logo` VARCHAR(255) DEFAULT NULL COMMENT '公司Logo',
                       `company_intro` TEXT DEFAULT NULL COMMENT '公司简介',
                       `job_title` VARCHAR(100) NOT NULL COMMENT '职位名称',
                       `job_type` VARCHAR(20) DEFAULT NULL COMMENT '职位类型',
                       `salary` VARCHAR(50) DEFAULT NULL COMMENT '薪资范围',
                       `city` VARCHAR(50) DEFAULT NULL COMMENT '工作城市',
                       `address` VARCHAR(255) DEFAULT NULL COMMENT '工作地址',
                       `experience` VARCHAR(50) DEFAULT NULL COMMENT '经验要求',
                       `education` VARCHAR(50) DEFAULT NULL COMMENT '学历要求',
                       `job_desc` TEXT DEFAULT NULL COMMENT '职位描述',
                       `job_requirement` TEXT DEFAULT NULL COMMENT '任职要求',
                       `welfare` VARCHAR(500) DEFAULT NULL COMMENT '福利待遇',
                       `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
                       `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
                       `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
                       `apply_count` INT NOT NULL DEFAULT 0 COMMENT '投递次数',
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- 3. 简历表
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `user_id` BIGINT NOT NULL COMMENT '用户ID',
                          `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                          `gender` TINYINT DEFAULT NULL COMMENT '性别：0-女，1-男',
                          `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
                          `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
                          `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                          `city` VARCHAR(50) DEFAULT NULL COMMENT '所在城市',
                          `education` VARCHAR(50) DEFAULT NULL COMMENT '学历',
                          `experience` VARCHAR(100) DEFAULT NULL COMMENT '工作经验',
                          `expected_salary` VARCHAR(50) DEFAULT NULL COMMENT '期望薪资',
                          `skills` VARCHAR(500) DEFAULT NULL COMMENT '技能标签',
                          `introduction` TEXT DEFAULT NULL COMMENT '自我介绍',
                          `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历表';

-- 4. 收藏表
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `user_id` BIGINT NOT NULL COMMENT '用户ID',
                            `job_id` BIGINT NOT NULL COMMENT '职位ID',
                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_user_job` (`user_id`, `job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 5. 投递记录表
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `user_id` BIGINT NOT NULL COMMENT '用户ID',
                               `job_id` BIGINT NOT NULL COMMENT '职位ID',
                               `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-已投递，1-已取消',
                               `apply_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '投递时间',
                               `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_user_job` (`user_id`, `job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投递记录表';

-- 初始化测试数据
-- 用户：demo / 123456
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `email`, `phone`) VALUES
    (1, 'demo', '123456', '示例用户', 'demo@example.com', '13800000000');

-- 职位：阿里、腾讯、字节
INSERT INTO `job` (`id`, `company_name`, `company_logo`, `company_intro`, `job_title`, `job_type`, `salary`, `city`, `address`, `experience`, `education`, `job_desc`, `job_requirement`, `welfare`, `contact_name`, `contact_phone`, `view_count`, `apply_count`) VALUES
                                                                                                                                                                                                                                                                       (1, '阿里巴巴', '/logo/alibaba.png', '全球领先的互联网公司，业务包括电商、云计算、数字媒体等', 'Java开发工程师', '全职', '15K-30K', '杭州', '余杭区文一西路969号', '3-5年', '本科', '负责后端服务开发与维护，熟悉Spring Boot。', '有Java开发经验，理解分布式系统。', '五险一金,年终奖,免费三餐', 'HR李女士', '0571-88158000', 0, 0),
                                                                                                                                                                                                                                                                       (2, '腾讯科技', '/logo/tencent.png', '中国最大的互联网综合服务提供商，业务涵盖社交、游戏、金融等', '前端开发工程师', '全职', '18K-30K', '深圳', '南山区科技园', '1-3年', '本科', '负责前端页面开发，熟悉Vue/React。', '掌握HTML/CSS/JavaScript，了解前端工程化。', '五险一金,年终奖,免费班车', 'HR王先生', '0755-86013388', 0, 0),
                                                                                                                                                                                                                                                                       (3, '字节跳动', '/logo/bytedance.png', '全球领先的移动互联网公司，旗下有抖音、今日头条等产品', 'Go开发工程师', '全职', '20K-40K', '北京', '海淀区知春路', '3-5年', '本科', '负责后端服务开发，熟悉Go语言与微服务。', '有高并发系统经验，熟悉常见中间件。', '五险一金,年终奖,免费三餐', 'HR张女士', '010-12345678', 0, 0);
