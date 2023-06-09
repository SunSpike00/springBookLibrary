package com.co.kr.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.co.kr.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginDataInterceptor implements HandlerInterceptor  {
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		// 로그인 시도 한 값 유효성 체크
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbId", request.getParameter("id"));
		map.put("mbPw", request.getParameter("pw"));
		
		int dupleCheck = userService.mbLoginCheck(map);
		
		if(dupleCheck == 0) {
			String alertText = "없는 아이디이거나 패스워드가 잘못되었습니다. 가입해주세요";
			String redirectPath = "/login";
			CommonUtils.redirect(alertText, redirectPath, response);
			log.info("세션 {}을 가진 게스트 로그인 실패",request.getSession().getId());
			return false;
		}
		
		log.info("로그인 준비 성공");
		return true;
	}
}
