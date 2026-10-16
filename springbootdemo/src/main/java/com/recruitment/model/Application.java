package com.recruitment.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 投递记录
 *
 * @author Web架构技术教学组
 */
@Data
@TableName("application")
public class Application {
    public static final int STATUS_APPLIED = 0;   // 已投递
    public static final int STATUS_CANCELED = 1;  // 已取消

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long jobId;
    private int status;
    private LocalDateTime applyTime;
    private LocalDateTime cancelTime;
}
