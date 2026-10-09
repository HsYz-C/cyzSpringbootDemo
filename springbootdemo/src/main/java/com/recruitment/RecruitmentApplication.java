package com.recruitment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 招聘网站主启动类
 *
 * @author Web架构技术教学组
 * @version 1.0
 */
@SpringBootApplication
public class RecruitmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecruitmentApplication.class, args);
        System.out.println("""
====================================
招聘网站后端启动成功！
Spring Boot 版本: 3.2.2
Java 版本: 17
====================================
""");
    }
}