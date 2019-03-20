package com.narthil.rvcs.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ResultInfo
 */
@Data
@ApiModel(value="ResultInfo", description = "返回数据")
public class ResultInfo<T> {

    @ApiModelProperty(value="状态码")
    private int status;
    @ApiModelProperty(value="状态信息")
    private String message;
    @ApiModelProperty(value="数据",example="{'字段':'值'}")
    private T data ;

    public void setStatus(int code,String message){

        switch (code) {
            case 0:
                this.status=0;
                this.message=message;
                break;
            case 1:
                this.status=1;
                this.message=message;
                break;
            case -1:
                this.status=-1;
                this.message=message;
                break;   
            default:
                this.status=-1;
                this.message=message;
                break;
        }
    }
}