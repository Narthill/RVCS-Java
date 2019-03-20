package com.narthil.rvcs.dao.user;
import com.narthil.rvcs.pojo.UserInfo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
// import org.springframework.data.rest.core.annotation.RepositoryRestResource;
/**
 * UserRepository
 */

// @RepositoryRestResource(collectionResourceRel = "UserInfo", path = "users")
public interface UserRepository extends MongoRepository<UserInfo,Long> {
    UserInfo findByUsername(@Param("username")String username);
    UserInfo findById(String id);

    // // 用户信息去除password字段返回
    // @Query(value="{'username':?0}",fields="{'password':0}")
    // UserInfoResult findByUsernameNotPassword(@Param("username") String username);
}