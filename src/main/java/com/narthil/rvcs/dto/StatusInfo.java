package com.narthil.rvcs.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * StatusInfo
 */
@Data
@ApiModel(value="StatusInfo", description = "状态消息")
public class StatusInfo {
    @ApiModelProperty(value="状态码")
    private int statusCode;
    @ApiModelProperty(value="状态信息")
    private String statusInfo;

    public void setStatus(int code){
        if(code==0){
            this.statusCode=0;
            this.statusInfo="请求失败";
        }else{
            this.statusCode=1;
            this.statusInfo="请求成功";
        }

    }
}