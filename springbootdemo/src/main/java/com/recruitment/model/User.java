package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("user")  // 指定数据库表名
public class User {
    @TableId(type = IdType.AUTO)  // 主键自增
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private LocalDateTime createTime;
}
