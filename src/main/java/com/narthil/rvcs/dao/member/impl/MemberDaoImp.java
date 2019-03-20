package com.narthil.rvcs.dao.member.impl;

import com.narthil.rvcs.dao.member.MemberDao;
import com.narthil.rvcs.pojo.MemberInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;

/**
 * MemberDaoImp
 */
@Repository
public class MemberDaoImp implements MemberDao {

        @Autowired
        MongoTemplate mongoTemplate;
        // 根据根id和用户id查询当前用户权限
        public int getAuthNum(String rootId,String userId){
            Query query = new Query(Criteria.where("rootId").is(rootId).and("userId").is(userId));
            MemberInfo member=mongoTemplate.findOne(query,MemberInfo.class,"MemberInfo");
            if(member!=null){
                return member.getAuth();
            }
            return 0;
        }

        // 该方法在业务层中用于判断该成员是否已经在该公司
        public MemberInfo findByUserIdAndRootId(String rootId,String userId){
            Query query = new Query(Criteria.where("rootId").is(rootId).and("userId").is(userId));
            MemberInfo member=mongoTemplate.findOne(query,MemberInfo.class,"MemberInfo");
            if(member!=null){
                return member;
            }
            return null;
        }
}