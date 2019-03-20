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
import com.narthil.rvcs.security.JwtUser;
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

    @Override
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
            Map<String,Object> dataMap=new HashMap<String,Object>(){
                {
                    put("id", userTemp.getId());
                    put("username", userTemp.getUsername());
                    put("email", userTemp.getEmail());
                    put("name", userTemp.getName());
                    put("token",token);
                }
            };
            userResult.setData(dataMap);
            userResult.setStatus(1,"登录成功");
        }else{
            userResult.setStatus(0,"登录失败");
        }

        return userResult;
    }

    @Override
    public ResultInfo<Object> register(UserInfo user){
        ResultInfo<Object> userResult=new ResultInfo<Object>();
        try {
            final String username = user.getUsername();
            if (userRepository.findByUsername(username) != null) {
                userResult.setStatus(0,"重名");
            }else{
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                final String rawPassword = user.getPassword();
                user.setPassword(encoder.encode(rawPassword));
                List<String> roles = new ArrayList<String>();
                roles.add("USER");
                user.setRoles(roles);
                userRepository.insert(user);

                userResult.setStatus(1,"注册成功");
            }
            return userResult;
        } catch (Exception e) {
            userResult.setStatus(0,"位置错误");
            return userResult;
        }

    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        System.out.println(username);
        // JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        // System.out.println(user.getPassword()) ;
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return "error";
    }

    @Override
    public ResultInfo<Map<String,Object>> getInfoByUsername(String username) {
        UserInfo userTemp = userRepository.findByUsername(username);
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();
        if(userTemp!=null){
            Map<String,Object> dataMap=new HashMap<String,Object>(){
                {
                    put("id", userTemp.getId());
                    put("username", userTemp.getUsername());
                    put("email", userTemp.getEmail());
                    put("name", userTemp.getName());
                }
            };
            userResult.setData(dataMap);
            userResult.setStatus(1,"请求成功");
        }else{
            userResult.setStatus(0,"没有找到用户信息");
        }
        return userResult;
    }

    @Override
    public ResultInfo<Map<String,Object>> updateUserInfo(UserInfo user){
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();
        UserInfo userTemp = userDao.updateUserInfo(user);
        if (userTemp!=null) {
            System.out.println(user);
            System.out.println(userTemp);
            Map<String,Object> dataMap=new HashMap<String,Object>(){
                {
                    put("id", userTemp.getId());
                    put("username", userTemp.getUsername());
                    put("email", userTemp.getEmail());
                    put("name", userTemp.getName());
                }
            };
            userResult.setData(dataMap);
            userResult.setStatus(1,"修改成功");
        }else{
            userResult.setStatus(0,"修改失败");
        }
        return userResult;
    }


}