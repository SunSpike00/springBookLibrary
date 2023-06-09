package com.co.kr.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserPwCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		
		// 유저 정보 업데이트
		if(!request.getParameter("pw").equals(request.getParameter("pwCheck"))) {
			String alertText = "패스워드가 일치하지 않습니다.";
			String redirectPath = "/setting";
			CommonUtils.redirect(alertText, redirectPath, response);
			log.info("아이디 {}을 가진 회원 정보 수정 비밀번호 불일치 발생",request.getParameter("id"));
			return false;
		}
		
		log.info("회원 정보 수정 성공");
		return true;
	}

}
