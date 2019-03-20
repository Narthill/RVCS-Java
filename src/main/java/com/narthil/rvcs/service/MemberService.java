package com.narthil.rvcs.service;

import java.util.Map;

import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.pojo.MemberInfo;

/**
 * MemberService
 */
public interface MemberService {
    ResultInfo<Map<String,Object>> addMember(MemberInfo member,String userId);
    ResultInfo<Object> updateAuth(String rootId,String userId,String adminId,int auth);
} 