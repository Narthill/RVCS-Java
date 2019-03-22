package com.narthil.rvcs.dao.group;
import com.narthil.rvcs.pojo.GroupInfo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * ContactRepository
 */
public interface GroupRepository extends MongoRepository<GroupInfo, Long> {

}