package com.narthil.rvcs.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.narthil.rvcs.dto.ResultInfo;

/**
 * 自定401返回值
 *
 * @author hackyo
 * Created on 2017/12/9 20:10.
 */
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException{
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");

        Map<String,Object> res=new HashMap<>();
        res.put("status", "error");
        res.put("statusText","未登录或者登录失败");

        String json = new Gson().toJson(res);
        System.out.println(json);
        response.getWriter().write(json);
    }

}