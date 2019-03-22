package com.narthil.rvcs.service;
import java.util.Map;
import java.util.Set;

import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.pojo.OrgInfo;

/**
 * OrgService
 */
public interface OrgService {

    ResultInfo<Map<String,Object>> createCompany(OrgInfo company,String userId);
    ResultInfo<Map<String,Object>> createOrg(OrgInfo node,String userId);
    ResultInfo<Map<String,Object>> findOrgAndMember(String orgId);
    // 查当前用户所在的所有公司的信息
    ResultInfo<Set<OrgInfo>> findComInfo(String userId);
    // 人员调动
    
    // 赋权
}