package com.narthil.rvcs.dao.member;
import java.util.List;

import com.narthil.rvcs.pojo.MemberInfo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MemberRepositor
 */
public interface MemberRepository extends MongoRepository<MemberInfo,Long>{
    List<MemberInfo> findByOrgId(String orgId);
    // List<MemberInfo> findByRootId(String rootId);
    List<MemberInfo> findByUserId(String userId);
}