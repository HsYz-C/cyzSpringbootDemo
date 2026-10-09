package com.recruitment.service;

import com.recruitment.common.Result;
import com.recruitment.model.Resume;
import java.util.List;

/**
 * 简历服务接口
 * @author Web架构技术教学组
 */
public interface ResumeService {
    /** 创建简历 */
    Result<Resume> create(Resume resume);

    /** 获取简历详情 */
    Result<Resume> getById(Long id);

    /** 更新简历 */
    Result<Resume> update(Long id, Resume resume);

    /** 删除简历 */
    Result<String> delete(Long id);

    /** 获取用户的简历列表 */
    Result<List<Resume>> listByUserId(Long userId);
}