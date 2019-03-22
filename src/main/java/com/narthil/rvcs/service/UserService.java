package com.narthil.rvcs.service;
import com.narthil.rvcs.pojo.UserInfo;
import java.util.Map;

import com.narthil.rvcs.dto.ResultInfo;

/**
 * UserService
 */
public interface UserService {

    ResultInfo<Map<String,Object>> login(String username, String password);
    ResultInfo<Object> register(UserInfo user);
    ResultInfo<Map<String,Object>> updateUserInfo(UserInfo user);
    ResultInfo<Map<String,Object>> getInfoByUsername(String username);
    ResultInfo<Map<String,Object>> getInfoByUserId(String userId);
    ResultInfo<Map<String,Object>> getFriendsList(String userId);
    ResultInfo<Map<String,Object>> addFriend(String userId,String friendId);
    ResultInfo<Map<String, Object>> deleteFriend(String userId,String friendId);
    
    // ResultInfo<Object> refreshToken(String oldToken);
    // ResultInfo<Map<String,Object>> getInfoById(String id);
}