package com.damu.febs.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.damu.febs.common.entity.FebsResponse;
import com.damu.febs.common.utils.FebsUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FebsAuthExceptionEntryPoint implements AuthenticationEntryPoint {

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        FebsResponse febsResponse = new FebsResponse();
//
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(401);
//        response.getOutputStream().write(JSONObject.toJSONString(febsResponse.message("token无效")).getBytes());
//    }

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        FebsResponse febsResponse = new FebsResponse();
//
//        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getOutputStream().write(JSONObject.toJSONString(febsResponse.message("token无效")).getBytes());
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        FebsResponse febsResponse = new FebsResponse();
        FebsUtil.makeResponse(
                response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                HttpServletResponse.SC_UNAUTHORIZED, febsResponse.message("token无效")
        );
    }
}