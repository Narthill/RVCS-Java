package com.narthil.rvcs.controller;

import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.dto.param.UserInfoParam;
import com.narthil.rvcs.dto.param.UserLoginParam;
import com.narthil.rvcs.dto.param.UserModifyParam;
import com.narthil.rvcs.dto.param.UserRegisterParam;

import java.util.Map;

import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "用户控制器")
@RequestMapping(value = "/user")
// @PreAuthorize("hasRole('USER')")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户查找
     *
     * @param username 用户名
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @ApiOperation(value = "获取用户信息", notes = "登录用户通过用户名获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户姓名", required = true, paramType = "body", dataType = "UserInfoParam"),
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header", defaultValue = "Bearer +对应token") })
    @PostMapping(value = "/getUserInfo", produces = "application/json")
    public ResultInfo<Map<String, Object>> getUserInfo(@RequestBody UserInfoParam user) {
        String username = user.getUsername();
        return userService.getUserInfoNotPwd(username);
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @ApiOperation(value = "用户登录", notes = "登录")
    @ApiImplicitParam(name = "user", value = "用户名与密码", required = true, paramType = "body", dataType = "UserLoginParam")
    @PostMapping(value = "/login")
    public String getToken(@RequestBody UserLoginParam user) throws AuthenticationException {
        String username = user.getUsername();
        String password = user.getPassword();
        return userService.login(username, password);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 操作结果
     * @throws AuthenticationException 错误信息
     */
    @ApiOperation(value = "用户注册", notes = "注册")
    @ApiImplicitParam(name = "user", value = "用户信息", required = true, paramType = "body", dataType = "UserRegisterParam")
    @PostMapping(value = "/register")
    public ResultInfo<Object> register(@RequestBody UserRegisterParam user) throws AuthenticationException {
        UserInfo userTemp = new UserInfo();
        userTemp.setName(user.getName());
        userTemp.setEmail(user.getEmail());
        userTemp.setUsername(user.getUsername());
        userTemp.setPassword(user.getPassword());
        return userService.register(userTemp);
    }

    /**
     * 刷新密钥
     *
     * @param authorization 原密钥
     * @return 新密钥
     * @throws AuthenticationException 错误信息
     */
    @ApiOperation(value = "刷新token", notes = "刷新token")
    @ApiImplicitParam(name = "authorization", value = "原token", required = true, paramType = "header", defaultValue = "Bearer +原token")
    @GetMapping(value = "/refreshToken")
    public String refreshToken(@RequestHeader String authorization) throws AuthenticationException {
        return userService.refreshToken(authorization);
    }
    
    @PostMapping(value = "/updateUserInfo")
    public ResultInfo<Map<String, Object>> updateUserInfo(@RequestBody UserModifyParam user) throws AuthenticationException {
        UserInfo userTemp = new UserInfo(){
            {
                setId(user.getId());
                setUsername(user.getUsername());
                setName(user.getName());
                setEmail(user.getEmail());
                setPassword(user.getPassword());
            }
        };
        return userService.updateUserInfo(userTemp);
    }
}