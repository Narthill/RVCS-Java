package com.narthil.rvcs.util;

import javax.servlet.http.HttpServletRequest;

import com.narthil.rvcs.dao.user.UserRepository;
import com.narthil.rvcs.pojo.UserInfo;
import com.narthil.rvcs.security.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TokenInfo
 */
@Component
public class TokenParser {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UserRepository userRepository;
    
    public String getToken(){
        return request.getHeader(tokenHeader);
    }

    public String getUsername(){
        String authHeader = request.getHeader(tokenHeader);
        String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        return username;
    }

    public String getUserId(){
        String authHeader = request.getHeader(tokenHeader);
        String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        UserInfo user = userRepository.findByUsername(username);
        return user.getId();
    }
}