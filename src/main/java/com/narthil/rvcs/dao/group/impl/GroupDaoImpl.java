package com.narthil.rvcs.dao.group.impl;

import com.narthil.rvcs.dao.group.GroupDao;
import com.narthil.rvcs.pojo.GroupInfo;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * GroupDaoImpl
 */
@Repository
public class GroupDaoImpl implements GroupDao {

    @Autowired
    MongoTemplate mongoTemplate;

    public GroupInfo updateGroupInfo(String groupId,String groupName){
        Query query = new Query(Criteria.where("_id").is(groupId));
        Update update = new Update();

        if(!groupName.isEmpty()){
            update.set("name",groupName);
        }

        GroupInfo updateGroup=mongoTemplate.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true),GroupInfo.class,"GroupInfo");
        if (updateGroup!=null) {
            return updateGroup;
        }
        return null;
    }

    public GroupInfo addGroupMember(String groupId,String memberId){
        
        Query query = new Query(Criteria.where("_id").is(groupId));
        Update update = new Update().addToSet("members",memberId);
        GroupInfo group=mongoTemplate.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true),GroupInfo.class,"GroupInfo");
        return group;
    }

    public GroupInfo deleteMember(String groupId,String memberId){
        
        return null;
    }


}