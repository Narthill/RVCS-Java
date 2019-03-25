package com.narthil.rvcs.dao.org.impl;

import com.narthil.rvcs.dao.org.OrgDao;
import com.narthil.rvcs.pojo.OrgInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * OrgDaoImpl
 */
@Repository
public class OrgDaoImpl implements OrgDao{

    @Autowired
    MongoTemplate mongoTemplate;

    // 插入节点
    public boolean insertChildNode(String parentId,String childId){
        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = new Update().push("children",childId);

        OrgInfo parent=mongoTemplate.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true),OrgInfo.class,"OrgInfo");
        
        // System.out.println(parent);
        if (parent!=null) {
            return true;
        }else{
            return false;
        }
        
    }

    // 返回一个公司的信息
    public OrgInfo findComInfo(String id){
        Query query = new Query(Criteria.where("_id").is(id).and("root").is(true));
        OrgInfo comInfo=mongoTemplate.findOne(query,OrgInfo.class,"OrgInfo");
        return comInfo;
    }



}