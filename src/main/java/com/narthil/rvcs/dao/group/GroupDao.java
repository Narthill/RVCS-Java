package com.narthil.rvcs.dao.group;

import com.narthil.rvcs.pojo.GroupInfo;

/**
 * GroupDao
 */
public interface GroupDao {
    GroupInfo updateGroupInfo(String groupId,String groupName);
    GroupInfo deleteMember(String groupId,String memberId);
    GroupInfo addGroupMember(String groupId,String memberId);
}