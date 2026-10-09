package com.recruitment.service.impl;

import com.recruitment.common.Result;
import com.recruitment.data.MockDataStore;
import com.recruitment.model.Resume;
import com.recruitment.model.User;
import com.recruitment.service.ResumeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 简历服务实现（内存版）
 * @author Web架构技术教学组
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Override
    public Result<Resume> create(Resume resume) {
        if (resume == null || resume.getUserId() == null) {
            return Result.error("用户ID不能为空");
        }
        if (resume.getRealName() == null || resume.getRealName().trim().isEmpty()) {
            return Result.error("真实姓名不能为空");
        }

        User user = MockDataStore.getUserById(resume.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }

        resume.setId(MockDataStore.nextResumeId());
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdateTime(LocalDateTime.now());
        MockDataStore.saveResume(resume);

        return Result.success("创建成功", resume);
    }

    @Override
    public Result<Resume> getById(Long id) {
        if (id == null) {
            return Result.error("简历ID不能为空");
        }

        Resume resume = MockDataStore.getResumeById(id);
        if (resume == null) {
            return Result.error("简历不存在");
        }

        return Result.success(resume);
    }

    @Override
    public Result<Resume> update(Long id, Resume resume) {
        if (id == null) {
            return Result.error("简历ID不能为空");
        }

        Resume existing = MockDataStore.getResumeById(id);
        if (existing == null) {
            return Result.error("简历不存在");
        }

        // 更新字段（保留原有的id、userId、createTime）
        if (resume.getRealName() != null) {
            existing.setRealName(resume.getRealName());
        }
        if (resume.getGender() != null) {
            existing.setGender(resume.getGender());
        }
        if (resume.getAvatar() != null) {
            existing.setAvatar(resume.getAvatar());
        }
        if (resume.getPhone() != null) {
            existing.setPhone(resume.getPhone());
        }
        if (resume.getEmail() != null) {
            existing.setEmail(resume.getEmail());
        }
        if (resume.getCity() != null) {
            existing.setCity(resume.getCity());
        }
        if (resume.getEducation() != null) {
            existing.setEducation(resume.getEducation());
        }
        if (resume.getExperience() != null) {
            existing.setExperience(resume.getExperience());
        }
        if (resume.getExpectedSalary() != null) {
            existing.setExpectedSalary(resume.getExpectedSalary());
        }
        if (resume.getSkills() != null) {
            existing.setSkills(resume.getSkills());
        }
        if (resume.getIntroduction() != null) {
            existing.setIntroduction(resume.getIntroduction());
        }
        existing.setUpdateTime(LocalDateTime.now());

        return Result.success("更新成功", existing);
    }

    @Override
    public Result<String> delete(Long id) {
        if (id == null) {
            return Result.error("简历ID不能为空");
        }

        Resume resume = MockDataStore.getResumeById(id);
        if (resume == null) {
            return Result.error("简历不存在");
        }

        MockDataStore.deleteResume(id);
        return Result.success("删除成功", null);
    }

    @Override
    public Result<List<Resume>> listByUserId(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        User user = MockDataStore.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        List<Resume> resumes = MockDataStore.getResumesByUserId(userId);
        return Result.success(resumes);
    }
}