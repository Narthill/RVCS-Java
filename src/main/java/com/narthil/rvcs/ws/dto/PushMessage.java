package com.narthil.rvcs.ws.dto;

import lombok.Data;

/**
 * PushMessage
 */
@Data
public class PushMessage {

    private String userId;
    private String message;
    private String targetId;
    private String groupId;
}