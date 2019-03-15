package com.narthil.rvcs.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * AuthInfo
 */
@Document(collection = "AuthInfo")
@Data
public class AuthInfo {

    @Id
    private String id;
    private String name;
    private String desc;
}