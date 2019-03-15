package com.narthil.rvcs.pojo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * OrgInfo
 */
@Document(collection = "OrgInfo")
@Data
public class OrgInfo {

    @Id
    private String id;
    private String name;
    private String desc;
    private boolean root;
    private String parent;
    private List<String> children;
}