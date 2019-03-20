package com.narthil.rvcs.dao.member;

import com.narthil.rvcs.pojo.MemberInfo;

/**
 * MemberDao
 */
public interface MemberDao {
    int getAuthNum(String rootId,String userId);
    MemberInfo findByUserIdAndRootId(String rootId,String userId);
}