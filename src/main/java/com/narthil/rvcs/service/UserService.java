package com.narthil.rvcs.service;


import com.narthil.rvcs.pojo.UserInfo;

import java.util.Map;

import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.dto.StatusInfo;

/**
 * UserService
 */
public interface UserService {

    String login(String username, String password);
    boolean register(UserInfo user);
    String refreshToken(String oldToken);
    ResultInfo<Map<String,Object>> getUserInfoNotPwd(String username);
    StatusInfo updateUserInfo(UserInfo user);
}