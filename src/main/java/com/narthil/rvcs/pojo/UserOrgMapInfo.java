package com.narthil.rvcs.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * UserOrgMapInfo 用户与组织映射以及权限信息表
 */
@Document(collection = "UserOrgMapInfo")
@Data
public class UserOrgMapInfo {

    @Id
    private String id;
    private String userId;
    private String orgId;
    private String authId;
}