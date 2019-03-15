package com.narthil.rvcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.narthil.rvcs.dao.UserRepository;
import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.dto.StatusInfo;
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
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public boolean register(UserInfo user){
        try {
            final String username = user.getUsername();
            if (userRepository.findByUsername(username) != null) {
                return false;
            }else{
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                final String rawPassword = user.getPassword();
                user.setPassword(encoder.encode(rawPassword));
                List<String> roles = new ArrayList<>();
                roles.add("USER");
                user.setRoles(roles);
                userRepository.insert(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        // String username = jwtTokenUtil.getUsernameFromToken(token);
        // JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        // System.out.println(username);
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return "error";
    }

    @Override
    public ResultInfo<Map<String,Object>> getUserInfoNotPwd(String username) {
        UserInfo user = userRepository.findByUsername(username);
        Map<String,Object> dataMap=new HashMap<String,Object>();
        ResultInfo<Map<String,Object>> userResult=new ResultInfo<Map<String,Object>>();
        StatusInfo status=new StatusInfo();
        if(user!=null){
            dataMap.put("id", user.getId());
            dataMap.put("username", user.getUsername());
            dataMap.put("email", user.getEmail());
            dataMap.put("name", user.getName());
            userResult.setData(dataMap);
            status.setStatus(1);
        }else{
            status.setStatus(0);
        }
        userResult.setStatus(status);
        return userResult;
    }

    public StatusInfo updateUserInfo(UserInfo user){
        return null;
    }
}