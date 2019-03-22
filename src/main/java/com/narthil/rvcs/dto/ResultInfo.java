package com.narthil.rvcs.dto;

import java.util.HashMap;
import java.util.Map;

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
    private String statusText;
    @ApiModelProperty(value="ok")
    private String ok;
    @ApiModelProperty(value="数据",example="{'字段':'值'}")
    private T data ;

    public void setStatus(int code,String message){
        this.status=code;
        this.statusText=message;

        if(code>=200&&code<=299){
            this.ok="ok";
        }else{
            this.ok="error";
        }
    }

    // public Map<String,Object> getStatusInfo(){
    //     Map<String,Object> res=new HashMap<String,Object>(){
    //         {
    //             put("status", status);
    //             put("statusText", statusText);
    //         }
    //     };

    //     return res;
    // }
}