package com.narthil.rvcs.ws.dto;

import java.util.Date;

import lombok.Data;

/**
 * PushMessage
 */
@Data
public class PushMessage {

    private String source;
    private String message;
    private String target;
    private String groupId;
}