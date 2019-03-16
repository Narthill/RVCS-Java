package com.narthil.rvcs.dao.user.impl;
import com.narthil.rvcs.dao.user.UserDao;
import com.narthil.rvcs.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * UserDaoImpl
 */
// @Component
@Repository
public class UserDaoImpl implements UserDao{

    @Autowired
    MongoTemplate mongoTemplate;

    public UserInfo updateUserInfo(UserInfo user){
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        
        if(user.getEmail()!=null){
            update.set("email", user.getEmail());
        }
        if(user.getUsername()!=null){
            update.set("username", user.getUsername());
        }

        if(user.getName()!=null){
            update.set("name", user.getName());
        }

        if(user.getPassword()!=null){
            update.set("password", user.getPassword());
        }

        UserInfo updatedUser=new UserInfo();
        updatedUser=mongoTemplate.findAndModify(query, update,FindAndModifyOptions.options().returnNew(true),UserInfo.class);
        if (updatedUser!=null) {
            return updatedUser;
        }
        return null;
    }
}