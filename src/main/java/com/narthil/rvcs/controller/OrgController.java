package com.narthil.rvcs.controller;
import java.util.Map;
import java.util.Set;

import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.dto.param.AddMemberParam;
import com.narthil.rvcs.dto.param.ComCreatParam;
import com.narthil.rvcs.dto.param.OrgCreatParam;
import com.narthil.rvcs.dto.param.OrgIdParam;
import com.narthil.rvcs.dto.param.UserIdParam;
import com.narthil.rvcs.pojo.MemberInfo;
import com.narthil.rvcs.pojo.OrgInfo;
import com.narthil.rvcs.service.MemberService;
import com.narthil.rvcs.service.OrgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * OrgController
 */
@RestController
@Api(tags = "组织控制器")
@RequestMapping(value = "/org")
public class OrgController {
    @Autowired
    OrgService orgService;
    @Autowired
    MemberService memberService;

    @PostMapping(value = "/createCompany")
    @ApiOperation(value = "创建公司", notes = "创建")
    @ApiImplicitParam(name = "comInfo", value = "公司信息", required = true, paramType = "body", dataType = "ComCreatParam")
    public ResultInfo<Map<String,Object>> createCompany(@RequestBody ComCreatParam comInfo)throws AuthenticationException{

        OrgInfo company=new OrgInfo(){
            {
                setRoot(true);
                setName(comInfo.getName());
                setDesc(comInfo.getDesc());
            }
        };
        return orgService.createCompany(company, comInfo.getUserId());
    }

    @PostMapping(value = "/createOrg")
    @ApiOperation(value = "创建组织节点", notes = "创建")
    @ApiImplicitParam(name = "orgCreatParam", value = "组织信息", required = true, paramType = "body", dataType = "OrgCreatParam")
    public ResultInfo<Map<String,Object>> createOrg(@RequestBody OrgCreatParam orgCreatParam)throws AuthenticationException{

        OrgInfo orgTemp=new OrgInfo(){
            {
                setRoot(false);
                setName(orgCreatParam.getName());
                setDesc(orgCreatParam.getDesc());
                setRootId(orgCreatParam.getRootId());
                setParent(orgCreatParam.getParent());
            }
        };

        return orgService.createOrg(orgTemp, orgCreatParam.getUserId());
    }

    @PostMapping(value = "/getComInfo")
    @ApiOperation(value = "获取当前用户的所在公司信息", notes = "获取")
    @ApiImplicitParam(name = "userIdParam", value = "用户id", required = true, paramType = "body", dataType = "UserIdParam")
    // 获取当前用户所在的所有公司的信息
    public ResultInfo<Set<OrgInfo>> getComInfo(@RequestBody UserIdParam userIdParam)throws AuthenticationException{
        return orgService.findComInfo(userIdParam.getUserId());
    }

    @PostMapping(value = "/getOrgInfo")
    @ApiOperation(value = "获取公司的节点以及成员信息", notes = "获取")
    @ApiImplicitParam(name = "orgIdParam", value = "组织信息", required = true, paramType = "body", dataType = "OrgIdParam")
    public ResultInfo<Map<String,Object>> getOrgInfo(@RequestBody OrgIdParam orgIdParam)throws AuthenticationException{
        return orgService.findOrgAndMember(orgIdParam.getOrgId());
    }

    @PostMapping(value = "/addMember")
    @ApiOperation(value = "为节点添加成员,权限默认", notes = "添加")
    @ApiImplicitParam(name = "addMemberParam", value = "成员信息", required = true, paramType = "body", dataType = "AddMemberParam")
    public ResultInfo<Map<String,Object>> addMember(@RequestBody AddMemberParam addMemberParam)throws AuthenticationException{

        MemberInfo member=new MemberInfo(){
            {
                setUserId(addMemberParam.getMemberId());
                setRootId(addMemberParam.getRootId());
                setOrgId(addMemberParam.getOrgId());
                setAuth(0);
                setAuthDesc("默认权限");
            }
        };
        return memberService.addMember(member, addMemberParam.getUserId());
    }
    // updateMember
    // updateMemberAuth
    // updateOrg
    // updateCom
    // deleteMember
    // deleteOrg
    // deleteCom
    

    // findComLike模糊查询
    // 请求加入表 字段 userId,rootId,接收方为root下的1、2级别管理员
}