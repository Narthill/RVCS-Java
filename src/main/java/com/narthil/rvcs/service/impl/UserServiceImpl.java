package com.narthil.rvcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.narthil.rvcs.dao.group.GroupDao;
import com.narthil.rvcs.dao.group.GroupRepository;
import com.narthil.rvcs.dao.user.UserDao;
import com.narthil.rvcs.dao.user.UserRepository;
import com.narthil.rvcs.pojo.GroupInfo;
import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.dto.param.GetUserInfoParam;
import com.narthil.rvcs.security.JwtTokenUtil;
import com.narthil.rvcs.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupRepository groupRepository;

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;



    @Value("${jwt.tokenHead}")
    private String tokenHead;

    
    public UserServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;

    }

    // 登录
    public ResultInfo<Map<String,Object>> login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);

        UserInfo userTemp=userRepository.findByUsername(username);

        System.out.println(userTemp);
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();
        if(userTemp!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            // dataMap.put("id", userTemp.getId());
            // dataMap.put("username", userTemp.getUsername());
            // dataMap.put("email", userTemp.getEmail());
            dataMap.put("token",token);
            dataMap.put("status","ok");

            userResult.setData(dataMap);
            userResult.setStatus(200,"登录成功");
        }else{
            userResult.setStatus(401,"登录失败");
        }

        return userResult;
    }

    // 注册
    public ResultInfo<Object> register(UserInfo user){
        ResultInfo<Object> userResult=new ResultInfo<Object>();
        try {
            final String username = user.getUsername();
            if (userRepository.findByUsername(username) != null) {
                userResult.setStatus(400,"重名");
            }else{
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                final String rawPassword = user.getPassword();
                user.setPassword(encoder.encode(rawPassword));
                List<String> roles = new ArrayList<String>();
                roles.add("USER");
                user.setRoles(roles);
                userRepository.insert(user);

                userResult.setStatus(200,"注册成功");
            }
            return userResult;
        } catch (Exception e) {
            userResult.setStatus(400,"未知错误");
            return userResult;
        }

    }

    // 获取用户信息通过用户名
    public ResultInfo<Map<String,Object>> getInfoByUsername(String username) {
        UserInfo userTemp = userRepository.findByUsername(username);
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();
        if(userTemp!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("id", userTemp.getId());
            dataMap.put("username", userTemp.getUsername());
            dataMap.put("email", userTemp.getEmail());

            userResult.setData(dataMap);
            userResult.setStatus(200,"请求成功");
        }else{
            userResult.setStatus(404,"没有找到用户信息");
        }
        return userResult;
    }

    // 通过id获取用户信息
    public ResultInfo<Map<String,Object>> getInfoByUserId(String userId) {
        UserInfo userTemp = userRepository.findById(userId);
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();
        if(userTemp!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("id", userTemp.getId());
            dataMap.put("username", userTemp.getUsername());
            dataMap.put("email", userTemp.getEmail());

            userResult.setData(dataMap);
            userResult.setStatus(200,"请求成功");
        }else{
            userResult.setStatus(404,"没有找到用户信息");
        }
        return userResult;
    }


    // 更新用户信息
    public ResultInfo<Map<String,Object>> updateUserInfo(UserInfo user){
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();

        if(userRepository.findByUsername(user.getUsername())!=null){
            userResult.setStatus(400,"重名");
        }else{
            UserInfo userTemp = userDao.updateUserInfo(user);
            if (userTemp!=null) {
                Map<String,Object> dataMap=new HashMap<String,Object>();
                dataMap.put("id", userTemp.getId());
                dataMap.put("username", userTemp.getUsername());
                dataMap.put("email", userTemp.getEmail());
    
                userResult.setData(dataMap);
                userResult.setStatus(200,"修改成功");
            }else{
                userResult.setStatus(400,"修改失败");
            }
        }
        return userResult;
    }

    // 添加好友
    public ResultInfo<Object> addFriend(String userId,String friendId){
        ResultInfo<Object> userResult=new ResultInfo<Object>();
        UserInfo userBefore=userRepository.findById(userId);
        List<String> friendlist=userBefore.getFriends();
        // 查询是否已经添加该好友
        if(friendlist!=null){
            for(String friendTemp:friendlist){
                if(friendTemp.equals(friendId)){
                    userResult.setStatus(400,"已经添加该好友");
                    return userResult;
                }
            }
        }

        UserInfo user=userDao.addfriend(userId, friendId);
        UserInfo friend=userDao.addfriend(friendId, userId);
        if(user!=null&&friend!=null){
            userResult.setStatus(200,"添加好友成功");
        }else{
            userResult.setStatus(400,"添加好友失败");
        }
        return userResult;
    }

    // 删除好友
    public ResultInfo<Map<String, Object>> deleteFriend(String userId,String friendId){
        ResultInfo<Map<String, Object>> userResult=new ResultInfo<Map<String, Object>>();

        UserInfo user=userDao.deletefriend(userId, friendId);
        UserInfo friend=userDao.deletefriend(friendId, userId);
        if(user!=null&&friend!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            if(user.getFriends()!=null){
                dataMap.put("friends", user.getFriends());
            }else{
                dataMap.put("friends", "无");
            }
            
            // userResult.setData(dataMap);
            userResult.setStatus(204,"删除好友成功");
        }else{
            userResult.setStatus(400,"删除好友失败");
        }
        return userResult;
    }

    // 获取好友列表
    public ResultInfo<Map<String, Object>> getFriendsList(String userId){
        ResultInfo<Map<String, Object>> userResult=new ResultInfo<Map<String, Object>>();

        UserInfo user=userRepository.findById(userId);
        List<Object> friendsList=new ArrayList<>();
        
        if(user.getFriends()!=null){
            for(String friendId: user.getFriends()){
                UserInfo friendTemp=userRepository.findById(friendId);
                GetUserInfoParam param=new GetUserInfoParam();
                param.setId(friendTemp.getId());
                param.setUsername(friendTemp.getUsername());
                param.setEmail(friendTemp.getEmail());

                friendsList.add(param);
            }
        }
        if(user!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("friends", friendsList);
            
            userResult.setData(dataMap);
            userResult.setStatus(200,"获取通讯录成功");
        }else{
            userResult.setStatus(404,"获取通讯录失败");
        }
        return userResult;
    }

    // ×××××××××××××××群
    // 获取群列表
    public ResultInfo<Map<String, Object>> getGroupsList(String userId){
        ResultInfo<Map<String, Object>> userResult=new ResultInfo<Map<String, Object>>();

        UserInfo user=userRepository.findById(userId);
        if(user!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("groups", user.getGroups()) ;
            
            userResult.setData(dataMap);
            userResult.setStatus(200,"获取群列表成功");
        }else{
            userResult.setStatus(404,"获取群列表失败");
        }
        return userResult;
    }

    // 新建群
    public ResultInfo<Map<String, Object>> newGroup(String userId,String groupName){
        ResultInfo<Map<String, Object>> userResult=new ResultInfo<Map<String, Object>>();
        List<String> members=new ArrayList<String>();
        members.add(userId);
        GroupInfo group = groupRepository.insert(new GroupInfo(){{
            setName(groupName);
            setMembers(members);
        }});
        // 本人添加groupid
        UserInfo user=userDao.addGroup(userId,group.getId());

        if(group!=null&&user!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("groups", user.getGroups());

            userResult.setData(dataMap);
            userResult.setStatus(200,"新建群成功");
        }else{
            userResult.setStatus(400,"新建群失败");
        }

        return userResult;
    }

    // 获取群信息
    public ResultInfo<Map<String, Object>> getGroupInfo(String groupId){
        ResultInfo<Map<String, Object>> groupResult=new ResultInfo<Map<String, Object>>();
        GroupInfo group=groupRepository.findById(groupId);

        if(group!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("groupId", group.getId());
            dataMap.put("groupName", group.getName());
            dataMap.put("members", group.getMembers());

            groupResult.setData(dataMap);
            groupResult.setStatus(200,"获取群信息成功");
        }else{
            groupResult.setStatus(404,"获取群信息失败");
        }
        return groupResult;
    }

    // 更新群信息
    public ResultInfo<Map<String, Object>> updateGroup(String userId,String groupId,String groupName){
        ResultInfo<Map<String, Object>> groupResult=new ResultInfo<Map<String, Object>>();

        GroupInfo groupTmp=groupRepository.findById(groupId);

        boolean isExist=false;
        for (String memberId : groupTmp.getMembers()) {
            if(memberId.equals(userId)){
                isExist=true;
            }
        }

        if(isExist){
            GroupInfo group=groupDao.updateGroupInfo(groupId,groupName);
                if(group!=null){
                    Map<String,Object> dataMap=new HashMap<String,Object>();
                    dataMap.put("groupId", group.getId());
                    dataMap.put("groupName", group.getName());
                    dataMap.put("members", group.getMembers());

                    groupResult.setData(dataMap);
                    groupResult.setStatus(200,"更新群名字成功");
                }else{
                    groupResult.setStatus(400,"更新群名字失败");
                }
        }else{
            groupResult.setStatus(401,"你不在此群中，无权更改群信息");
        }
        return groupResult;
    }

    // 添加群成员，群表添加，个人的群列表也添加
    public ResultInfo<Map<String, Object>> addGroupMember(String userId,String memberId,String groupId){
        ResultInfo<Map<String, Object>> groupResult=new ResultInfo<Map<String, Object>>();

        GroupInfo groupTmp=groupRepository.findById(groupId);

        boolean isExist=false;
        for (String id : groupTmp.getMembers()) {
            if(id.equals(userId)){
                isExist=true;
            }
            if(id.equals(memberId)){
                groupResult.setStatus(400,"此人已经在群中");
                return groupResult;
            }
        }
        
        if(isExist){
            GroupInfo group=groupDao.addGroupMember(groupId, memberId);
            UserInfo member=userDao.addGroup(memberId, groupId);
            if(group!=null&&member!=null){
                Map<String,Object> dataMap=new HashMap<String,Object>();
                dataMap.put("groupId", group.getId());
                dataMap.put("groupName", group.getName());
                dataMap.put("members", group.getMembers());

                groupResult.setData(dataMap);
                groupResult.setStatus(200,"添加群成员成功");
            }else{
                groupResult.setStatus(400,"添加群成员失败");
            }
        }else{
            groupResult.setStatus(401,"你不在此群中，无权添加群成员");
        }

        return groupResult;
    }

    // TODO 加入群，更新群表member字段，更新user表group字段
    public ResultInfo<Map<String, Object>> enterGroup(String userId,String groupId){
        return null;
    }

    // TODO 删除群
    public ResultInfo<Map<String, Object>> deleteGroup(String userId,String groupId){



        return null;
    }

    // TODO 删除群成员
    public ResultInfo<Map<String, Object>> deleteGroupMember(String userId,String memberId,String groupId){
        ResultInfo<Map<String, Object>> groupResult=new ResultInfo<Map<String, Object>>();

        

        return null;
    }
}