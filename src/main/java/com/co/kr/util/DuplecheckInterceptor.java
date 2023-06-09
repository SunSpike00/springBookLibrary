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
public class DuplecheckInterceptor implements HandlerInterceptor {

	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		
		if(!(request.getParameter("pw").equals(request.getParameter("pwCheck")))) {
			String alertText = "패스워드가 일치하지 않습니다.";
			String redirectPath = "/sign";
			CommonUtils.redirect(alertText, redirectPath, response);
			log.info("세션 {}을 가진 게스트 비밀번호 불일치 발생", request.getSession().getId());
			return false;
		}
		
		// 아이디 중복체크
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbId", request.getParameter("id"));
		
		log.info("작성한 확인 용 : {}", request.getParameter("id"));
		
		int dupleCheck = userService.mbDuplicationCheck(map);
		
		if(dupleCheck > 0) { // 가입되있으면  
			String alertText = "중복된 아이디입니다.";
			String redirectPath = "/sign";
			CommonUtils.redirect(alertText, redirectPath, response);
			
			log.info("세션 {}을 가진 게스트 아이디 중복 발생",request.getSession().getId());
			return false;
		}

		log.info("회원가입 준비 성공");
		return true;
	}
}
