package com.gon.dashboards.configuration;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Request log interceptor
 */


@Configuration
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {

            String url = request.getRequestURI();
            JSONObject jsonObject = new JSONObject();
            Map<String, String[]> parameterNamesArgs = request.getParameterMap();
            for (String var : parameterNamesArgs.keySet()) {
                String[] strings = parameterNamesArgs.get(var);
                jsonObject.put(var, strings[0] == null ? "" : strings[0]);
            }
            String logStr="\n"
                    + "**********************"+"\n"
                    +"Request method:"+"\n"
                    +"{}"+"\n"
                    +"Request parameters:"+"\n"
                    +"{}"+"\n"
                    + "**********************"+"\n"
                    ;
            log.info(logStr,url,jsonObject.toJSONString());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }



}
