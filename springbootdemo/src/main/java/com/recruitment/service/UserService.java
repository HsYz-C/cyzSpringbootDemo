package com.recruitment.service;

import com.recruitment.common.Result;
import com.recruitment.model.User;
import com.recruitment.vo.LoginVO;

/**
 * 用户服务接口
 * @author Web架构技术教学组
 */
public interface UserService {
    Result<User> register(User request);
    Result<LoginVO> login(User request);
    Result<User> getUserInfo(Long userId);
}
