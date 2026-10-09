package com.recruitment.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**
 * 测试控制器
 * 用于验证Spring Boot项目是否正常启动
 *
 * @author Web架构技术教学组
 * @version 1.0
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    /**
     * 简单的Hello World接口
     * 访问地址: http://localhost:8080/api/hello
     */
    @GetMapping
    public String hello() {
        return "欢迎来到招聘网站！Spring Boot 3.2 + Java 17";
    }
    /**
     * 返回系统信息的接口
     * 访问地址: http://localhost:8080/api/hello/info
     */
    @GetMapping("/info")
    public Map<String, Object> systemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("projectName", "招聘网站");
        info.put("version", "1.0.0");
        info.put("springBootVersion", "3.2.2");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("serverTime", LocalDateTime.now());
        info.put("status", "运行中");
        return info;
    }
}