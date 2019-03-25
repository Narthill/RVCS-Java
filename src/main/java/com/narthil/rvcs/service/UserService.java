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
    ResultInfo<Object> addFriend(String userId,String friendId);
    ResultInfo<Map<String, Object>> deleteFriend(String userId,String friendId);

    ResultInfo<Map<String, Object>> getGroupsList(String userId);
    ResultInfo<Map<String, Object>> enterGroup(String userId,String groupId);
    ResultInfo<Map<String, Object>> newGroup(String userId,String groupName);
    ResultInfo<Map<String, Object>> getGroupInfo(String groupId);
    ResultInfo<Map<String, Object>> updateGroup(String userId,String groupId,String groupName);
    ResultInfo<Map<String, Object>> deleteGroup(String userId,String groupId);
    ResultInfo<Map<String, Object>> addGroupMember(String userId,String memberId,String groupId);
    ResultInfo<Map<String, Object>> deleteGroupMember(String userId,String memberId,String groupId);
    // ResultInfo<Object> refreshToken(String oldToken);
}