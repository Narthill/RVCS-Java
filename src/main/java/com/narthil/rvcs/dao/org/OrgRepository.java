package com.narthil.rvcs.dao.org;

import com.narthil.rvcs.pojo.OrgInfo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * OrgRepository
 */
public interface OrgRepository extends MongoRepository<OrgInfo,Long>{
    // List<OrgInfo> findByRootId(String rootId);
    OrgInfo findById(String orgId);
}