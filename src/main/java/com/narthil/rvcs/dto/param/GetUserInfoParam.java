package com.narthil.rvcs.dto.param;

import org.springframework.data.annotation.Id;

import lombok.Data;

/**
 * GetUserInfoParam
 */
@Data
public class GetUserInfoParam {

    @Id private String id;
    private String email;
    private String username;
}