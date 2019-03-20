package com.narthil.rvcs.dao.org;

import com.narthil.rvcs.pojo.OrgInfo;

/**
 * OrgDao
 */
public interface OrgDao {
    boolean insertChildNode(String parentId,String childId);
    OrgInfo findComInfo(String id);
}