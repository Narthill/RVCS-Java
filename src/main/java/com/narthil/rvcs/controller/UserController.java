package com.narthil.rvcs.controller;

import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.dto.param.UserLoginParam;
import com.narthil.rvcs.dto.param.UserModifyParam;
// import com.narthil.rvcs.dto.param.UserNameParam;
import com.narthil.rvcs.dto.param.UserRegisterParam;

import java.util.HashMap;
import java.util.Map;
import com.narthil.rvcs.service.UserService;
import com.narthil.rvcs.util.TokenParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "用户控制器")
@RequestMapping(value = "/api/user")
// @PreAuthorize("hasRole('USER')")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    // token解析工具，用来获取用户名
    private TokenParser currentUser;

    @ApiOperation(value = "测试接口", notes = "测试")
    @GetMapping(value = "/test")
    public UserInfo test() {
        UserInfo user = new UserInfo() {
            {
                setUsername("test");
                setPassword("test");
            }
        };
        return user;
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录", notes = "登录")
    @ApiImplicitParam(name = "user", value = "用户名与密码", required = true, paramType = "body", dataType = "UserLoginParam")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginParam user) throws AuthenticationException {

        String username = user.getUsername();
        String password = user.getPassword();
        HttpHeaders headers = new HttpHeaders();
        ResultInfo<Map<String, Object>> res = userService.login(username, password);
        headers.add("Authorization", (String)res.getData().get("token"));
        

        return new ResponseEntity<Map<String, Object>>(res.getData(),headers,HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping(value = "/register")
    @ApiOperation(value = "用户注册", notes = "注册")
    @ApiImplicitParam(name = "user", value = "用户信息", required = true, paramType = "body", dataType = "UserRegisterParam")    
    public ResponseEntity<Object> register(@RequestBody UserRegisterParam user) throws AuthenticationException {
        UserInfo userTemp = new UserInfo() {
            {
                setEmail(user.getEmail());
                setUsername(user.getUsername());
                setPassword(user.getPassword());
            }
        };

        ResultInfo<Object> res = userService.register(userTemp);

        Map<String,String> status=new HashMap<String,String>();
        status.put("status", res.getOk());
        if(res.getOk().equals("error")){
            status.put("statusText", res.getStatusText());
        }
        return new ResponseEntity<Object>(status,HttpStatus.valueOf(res.getStatus()));
    }


    @PostMapping(value = "/updateInfo")
    @ApiOperation(value = "用户信息修改", notes = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token"),
            @ApiImplicitParam(name = "user", value = "用户修改信息", required = true, paramType = "body", dataType = "UserModifyParam") })
    public ResponseEntity<Map<String, Object>> updateInfo(@RequestBody UserModifyParam user) throws AuthenticationException {
        UserInfo userTemp = new UserInfo() {
            {
                setId(currentUser.getUserId());
                setEmail(user.getEmail());
                setPassword(user.getPassword());
            }
        };
        if(!user.getUsername().equals(currentUser.getUsername())){
            userTemp.setUsername(user.getUsername());
        }
        ResultInfo<Map<String, Object>> res = userService.updateUserInfo(userTemp);
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }

    @GetMapping(value = "/getInfo")
    @ApiOperation(value = "获取用户信息", notes = "通过用户名获取用户信息")
    @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header", defaultValue = "Bearer +对应token")
    public ResponseEntity<Map<String, Object>> getInfo() throws AuthenticationException {
        String username = currentUser.getUsername();
        ResultInfo<Map<String, Object>> res = userService.getInfoByUsername(username);

        return new ResponseEntity<Map<String,Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }

    @GetMapping(value = "/getInfoById")
    @ApiOperation(value = "通过id获取用户信息", notes = "通过id获取用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header", defaultValue = "Bearer +对应token"),
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path", dataType = "String")
    })
    public ResponseEntity<Map<String, Object>> getInfoById(@RequestParam("userId") String userId) throws AuthenticationException {
        ResultInfo<Map<String, Object>> res = userService.getInfoByUserId(userId);

        return new ResponseEntity<Map<String,Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }


    @GetMapping(value = "/addFriend")
    @ApiOperation(value = "添加好友", notes = "添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token"),
            @ApiImplicitParam(name = "friendId", value = "好友id", required = true, paramType = "path", dataType = "String") })
    public ResponseEntity<Object> addFriend(@RequestParam("friendId") String friendId ) throws AuthenticationException{

        // HttpHeaders headers = new HttpHeaders();
        ResultInfo<Object> res = userService.addFriend(currentUser.getUserId(), friendId);
        // headers.add("statusText",res.getStatusText());
        return new ResponseEntity<Object>(HttpStatus.valueOf(res.getStatus()));
    }


    @RequestMapping(value = "/deleteFriend", method = RequestMethod.GET)
    @ApiOperation(value = "删除好友", notes = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token"),
            @ApiImplicitParam(name = "friendId", value = "好友id", required = true, paramType = "path", dataType = "String") })
    public ResponseEntity<Map<String, Object>> deleteFriend(@RequestParam("friendId") String friendId ) throws AuthenticationException{

        ResultInfo<Map<String, Object>> res = userService.deleteFriend(currentUser.getUserId(), friendId);
        // 204不会有任何body返回
        return new ResponseEntity<Map<String, Object>>(HttpStatus.valueOf(res.getStatus()));
    }

    @GetMapping(value = "/getFriendsList")
    @ApiOperation(value = "获取好友列表", notes = "获取")
    @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token")
    public ResponseEntity<Map<String, Object>> getFriendsList() throws AuthenticationException{

        ResultInfo<Map<String, Object>> res = userService.getFriendsList(currentUser.getUserId());
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }

    // 群
    @GetMapping(value = "/getGroupsList")
    @ApiOperation(value = "获取个人的群列表", notes = "获取")
    @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token")
    public ResponseEntity<Map<String, Object>> getGroupsList() throws AuthenticationException{

        ResultInfo<Map<String, Object>> res = userService.getGroupsList(currentUser.getUserId());
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }

    // 新建群
    @GetMapping(value = "/newGroup")
    @ApiOperation(value = "新建群", notes = "新建")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupName", value = "群组名", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token")
    })
    public ResponseEntity<Map<String, Object>> newGroup(@RequestParam("groupName") String groupName) throws AuthenticationException{
        ResultInfo<Map<String, Object>> res = userService.newGroup(currentUser.getUserId(), groupName);
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }
    
    // 获取群信息
    @GetMapping(value = "/getGroupInfo")
    @ApiOperation(value = "获取群信息", notes = "获取")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "群id", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token")
    })
    public ResponseEntity<Map<String, Object>> getGroupInfo(@RequestParam("groupId") String groupId) throws AuthenticationException{
        ResultInfo<Map<String, Object>> res = userService.getGroupInfo(groupId);
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }

    // 更新群信息
    @GetMapping(value = "/updateGroup")
    @ApiOperation(value = "修改群信息", notes = "修改")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "群id", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(name = "groupName", value = "修改的群名", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token")
    })
    public ResponseEntity<Map<String, Object>> updateGroup(@RequestParam("groupId") String groupId,@RequestParam("groupName") String groupName){
        ResultInfo<Map<String, Object>> res =userService.updateGroup(currentUser.getUserId(), groupId, groupName);
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }

    // 添加群成员，群表添加，个人的群列表也添加
    @GetMapping(value = "/addGroupMember")
    @ApiOperation(value = "添加群成员", notes = "添加")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "群id", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(name = "memberId", value = "成员id", required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(name = "authorization", value = "token", required = true, paramType = "header", defaultValue = "Bearer +token")
    })
    public ResponseEntity<Map<String, Object>> addGroupMember(@RequestParam("memberId") String memberId,@RequestParam("groupId") String groupId){
        ResultInfo<Map<String, Object>> res=userService.addGroupMember(currentUser.getUserId(), memberId, groupId);
        return new ResponseEntity<Map<String, Object>>(res.getData(),HttpStatus.valueOf(res.getStatus()));
    }
}