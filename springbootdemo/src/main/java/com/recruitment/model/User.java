package com.recruitment.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 * @author Web架构技术教学组
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private LocalDateTime createTime;
}
