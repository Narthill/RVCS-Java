package com.narthil.rvcs.controller;

import java.util.Map;
import java.util.Set;

import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.dto.param.AddMemberParam;
import com.narthil.rvcs.dto.param.ComCreatParam;
import com.narthil.rvcs.dto.param.OrgCreatParam;
// import com.narthil.rvcs.dto.param.UserIdParam;
import com.narthil.rvcs.pojo.MemberInfo;
import com.narthil.rvcs.pojo.OrgInfo;
import com.narthil.rvcs.service.MemberService;
import com.narthil.rvcs.service.OrgService;
import com.narthil.rvcs.util.TokenParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * OrgController
 */
@RestController
@Api(tags = "组织控制器")
@RequestMapping(value = "/api/org")
public class OrgController {
    @Autowired
    OrgService orgService;
    @Autowired
    MemberService memberService;

    @Autowired
    // token解析工具，用来获取用户名
    private TokenParser currentUser;

    @PostMapping(value = "/createCompany")
    @ApiOperation(value = "创建公司", notes = "创建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "原token", required = true, paramType = "header", defaultValue = "Bearer +原token"),
            @ApiImplicitParam(name = "comInfo", value = "公司信息", required = true, paramType = "body", dataType = "ComCreatParam") })
    public ResponseEntity<Map<String, Object>> createCompany(@RequestBody ComCreatParam comInfo)
            throws AuthenticationException {

        OrgInfo company = new OrgInfo() {
            {
                setRoot(true);
                setName(comInfo.getName());
                setDesc(comInfo.getDesc());
            }
        };
        ResultInfo<Map<String, Object>> res = orgService.createCompany(company, currentUser.getUserId());
        return new ResponseEntity<Map<String, Object>>(res.getData(), HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping(value = "/createOrg")
    @ApiOperation(value = "创建组织节点", notes = "创建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "原token", required = true, paramType = "header", defaultValue = "Bearer +原token"),
            @ApiImplicitParam(name = "orgCreatParam", value = "组织信息", required = true, paramType = "body", dataType = "OrgCreatParam") })
    public ResponseEntity<Map<String, Object>> createOrg(@RequestBody OrgCreatParam orgCreatParam)
            throws AuthenticationException {

        OrgInfo orgTemp = new OrgInfo() {
            {
                setRoot(false);
                setName(orgCreatParam.getName());
                setDesc(orgCreatParam.getDesc());
                setRootId(orgCreatParam.getRootId());
                setParent(orgCreatParam.getParent());
            }
        };
        ResultInfo<Map<String, Object>> res = orgService.createOrg(orgTemp, currentUser.getUserId());
        return new ResponseEntity<Map<String, Object>>(res.getData(), HttpStatus.valueOf(res.getStatus()));
    }

    @GetMapping(value = "/getComInfo")
    @ApiOperation(value = "获取当前用户的所在公司信息", notes = "获取")
    @ApiImplicitParam(name = "authorization", value = "原token", required = true, paramType = "header", defaultValue = "Bearer +原token")
    // 获取当前用户所在的所有公司的信息
    public ResponseEntity<Set<OrgInfo>> getComInfo() throws AuthenticationException {
        ResultInfo<Set<OrgInfo>> res = orgService.findComInfo(currentUser.getUserId());
        return new ResponseEntity<Set<OrgInfo>>(res.getData(), HttpStatus.valueOf(res.getStatus()));
    }

    @GetMapping(value = "/getOrgInfo")
    @ApiOperation(value = "获取公司的节点以及成员信息", notes = "获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "原token", required = true, paramType = "header", defaultValue = "Bearer +原token"),
            @ApiImplicitParam(name = "orgId", value = "组织id", required = true, paramType = "path", dataType = "String") })
    public ResponseEntity<Map<String, Object>> getOrgInfo(@RequestParam("orgId") String orgId)
            throws AuthenticationException {
        ResultInfo<Map<String, Object>> res = orgService.findOrgAndMember(orgId);
        return new ResponseEntity<Map<String, Object>>(res.getData(), HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping(value = "/addMember")
    @ApiOperation(value = "为节点添加成员,权限默认", notes = "添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "原token", required = true, paramType = "header", defaultValue = "Bearer +原token"),
            @ApiImplicitParam(name = "addMemberParam", value = "成员信息", required = true, paramType = "body", dataType = "AddMemberParam") })
    public ResponseEntity<Map<String, Object>> addMember(@RequestBody AddMemberParam addMemberParam)
            throws AuthenticationException {
        MemberInfo member = new MemberInfo() {
            {
                setUserId(addMemberParam.getMemberId());
                setOrgId(addMemberParam.getOrgId());
                setRootId(addMemberParam.getRootId());
                setAuth(0);
                setAuthDesc("默认权限");
            }
        };
        ResultInfo<Map<String, Object>> res = memberService.addMember(member, currentUser.getUserId());
        return new ResponseEntity<Map<String, Object>>(res.getData(), HttpStatus.valueOf(res.getStatus()));
    }

    // updateMember 简单
    //
    // updateMemberAuth 简单

    // updateOrg 较难 ，节点移动则父级节点的child信息也要修改

    // updateCom 简单

    // deleteMember 简单
    
    // deleteOrg 较难 ，联动删除

    // deleteCom 简单

    // findComLike模糊查询

    // 请求加入表 字段 userId,rootId,接收方为root下的1、2级别管理员
}