package com.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.Result;
import com.recruitment.mapper.ResumeMapper;
import com.recruitment.mapper.UserMapper;
import com.recruitment.model.Resume;
import com.recruitment.model.User;
import com.recruitment.service.ResumeService;
import com.recruitment.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 简历服务实现
 *
 * @author Web架构技术教学组
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    // 缓存key前缀
    private static final String CACHE_RESUME_DETAIL = "resume:detail:";
    private static final String CACHE_RESUME_LIST = "resume:list:";
    // 缓存过期时间（秒）
    private static final long CACHE_EXPIRE = 300;

    @Override
    public Result<Resume> create(Resume resume) {
        if (resume == null || resume.getUserId() == null) {
            return Result.error("用户ID不能为空");
        }
        if (resume.getRealName() == null || resume.getRealName().trim().isEmpty()) {
            return Result.error("真实姓名不能为空");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(resume.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }

        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdateTime(LocalDateTime.now());
        resumeMapper.insert(resume);

        // 新增后清空该用户的简历列表缓存
        redisUtil.delete(CACHE_RESUME_LIST + resume.getUserId());

        return Result.success("创建成功", resume);
    }

    @Override
    public Result<Resume> getById(Long id) {
        if (id == null) {
            return Result.error("简历ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = CACHE_RESUME_DETAIL + id;
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof Resume) {
            return Result.success((Resume) cached);
        }

        Resume resume = resumeMapper.selectById(id);
        if (resume == null) {
            return Result.error("简历不存在");
        }

        // 存入缓存
        redisUtil.set(cacheKey, resume, CACHE_EXPIRE);

        return Result.success(resume);
    }

    @Override
    public Result<Resume> update(Long id, Resume resume) {
        if (id == null) {
            return Result.error("简历ID不能为空");
        }

        Resume existing = resumeMapper.selectById(id);
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
        resumeMapper.updateById(existing);

        // 更新后清空详情和列表缓存
        redisUtil.delete(CACHE_RESUME_DETAIL + id);
        redisUtil.delete(CACHE_RESUME_LIST + existing.getUserId());

        return Result.success("更新成功", existing);
    }

    @Override
    public Result<String> delete(Long id) {
        if (id == null) {
            return Result.error("简历ID不能为空");
        }

        Resume resume = resumeMapper.selectById(id);
        if (resume == null) {
            return Result.error("简历不存在");
        }

        resumeMapper.deleteById(id);

        // 删除后清空详情和列表缓存
        redisUtil.delete(CACHE_RESUME_DETAIL + id);
        redisUtil.delete(CACHE_RESUME_LIST + resume.getUserId());

        return Result.success("删除成功", null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<List<Resume>> listByUserId(Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = CACHE_RESUME_LIST + userId;
        Object cached = redisUtil.get(cacheKey);
        if (cached instanceof List) {
            List<Resume> list = (List<Resume>) cached;
            return list.isEmpty()
                    ? Result.success("该用户暂无简历", list)
                    : Result.success("查询成功", list);
        }

        // 任务10：用户不存在、或用户存在但没有简历时，都返回空列表 + 清晰的无数据提示，
        // 不返回错误状态，也绝不返回其他用户的简历（查询条件已按 userId 隔离）。
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.success("该用户暂无简历", Collections.emptyList());
        }

        // 查询用户的所有简历
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resume::getUserId, userId);
        List<Resume> resumes = resumeMapper.selectList(wrapper);
        if (resumes == null) {
            resumes = Collections.emptyList();
        }

        // 存入缓存（用 ArrayList 便于 JSON 反序列化）
        redisUtil.set(cacheKey, new ArrayList<>(resumes), CACHE_EXPIRE);

        if (resumes.isEmpty()) {
            return Result.success("该用户暂无简历", resumes);
        }

        return Result.success("查询成功", resumes);
    }
}
