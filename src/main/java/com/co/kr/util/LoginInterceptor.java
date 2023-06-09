package com.co.kr.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		//로그인 여부 검사
		if(request.getSession().getAttribute("id") == null) {
			String alertText = "로그인 되어 있지 않습니다. 로그인 해주시기 바랍니다.";
			String redirectPath = "/login";
			CommonUtils.redirect(alertText, redirectPath, response);
			log.info("세션 {}을 가진 이용자 접속 유지 중",request.getSession().getId());
			return false;
		}
		
		log.info("{} 접속 정보 유지 중", request.getSession().getAttribute("id"));
	    return true;
	}
}