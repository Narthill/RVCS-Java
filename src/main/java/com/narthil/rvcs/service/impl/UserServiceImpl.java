package com.narthil.rvcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.narthil.rvcs.dao.user.UserDao;
import com.narthil.rvcs.dao.user.UserRepository;
import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.security.JwtTokenUtil;
// import com.narthil.rvcs.security.JwtUser;
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

                userResult.setStatus(201,"注册成功");
            }
            return userResult;
        } catch (Exception e) {
            userResult.setStatus(400,"未知错误");
            return userResult;
        }

    }

    
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
                userResult.setStatus(201,"修改成功");
            }else{
                userResult.setStatus(400,"修改失败");
            }
        }
        return userResult;
    }

    
    public ResultInfo<Map<String, Object>> addFriend(String userId,String friendId){
        ResultInfo<Map<String, Object>> userResult=new ResultInfo<Map<String, Object>>();
        UserInfo userBefore=userRepository.findById(userId);
        List<String> friendlist=userBefore.getFriends();
        // 查询是否已经添加该好友
        for(String friendTemp:friendlist){
            if(friendTemp.equals(friendId)){
                userResult.setStatus(400,"已经添加该好友");
                return userResult;
            }
        }

        UserInfo user=userDao.addfriend(userId, friendId);
        UserInfo friend=userDao.addfriend(friendId, userId);
        if(user!=null&&friend!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("friends", user.getFriends());

            userResult.setData(dataMap);
            userResult.setStatus(201,"添加好友成功");
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
        if(user!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("friends", user.getFriends());
            
            userResult.setData(dataMap);
            userResult.setStatus(200,"获取通讯录成功");
        }else{
            userResult.setStatus(404,"获取通讯录失败");
        }
        return userResult;
    }

    // 
    public ResultInfo<Map<String, Object>> getGroupsList(String userId){
        ResultInfo<Map<String, Object>> userResult=new ResultInfo<Map<String, Object>>();

        UserInfo user=userRepository.findById(userId);
        if(user!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap.put("group", user.getGroup()) ;
            
            userResult.setData(dataMap);
            userResult.setStatus(200,"获取群列表成功");
        }else{
            userResult.setStatus(404,"获取群列表失败");
        }
        return userResult;
    }


}