package com.narthil.rvcs.dao.user;

import com.narthil.rvcs.pojo.UserInfo;

/**
 * UserDao
 */
public interface UserDao {

    UserInfo updateUserInfo(UserInfo user);
    UserInfo addfriend(String userId,String friendId);
    UserInfo deletefriend(String userId,String friendId);
    UserInfo addGroup(String userId,String groupId);
    UserInfo deleteGroup(String userId,String groupId);
}