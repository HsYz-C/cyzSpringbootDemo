package com.recruitment.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 投递记录
 * @author Web架构技术教学组
 */
@Data
public class Application {
    public static final int STATUS_APPLIED = 0;   // 已投递
    public static final int STATUS_CANCELED = 1;  // 已取消

    private Long id;
    private Long userId;
    private Long jobId;
    private int status;
    private LocalDateTime applyTime;
    private LocalDateTime cancelTime;
}
