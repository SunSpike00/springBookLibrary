package com.co.kr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.ItemMainDomain;
import com.co.kr.domain.ItemViewDomain;
import com.co.kr.domain.UserDomain;
import com.co.kr.service.ItemService;
import com.co.kr.service.UserService;
import com.co.kr.util.CommonUtils;
import com.co.kr.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;
	
	// 진입점
	@RequestMapping("/")
	public ModelAndView index() {

		ModelAndView mav = new ModelAndView();
		log.info("게스트 접속");
		mav = bkViewList();
		mav.setViewName("index.html");
		return mav;
	}
	
	//로그인 페이지
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		log.info("{} 세션 게스트 로그인 준비", session.getId());
		
		mav.setViewName("user/login.html");
		return mav;
	}
	
	//로그아웃
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		log.info("{} 님이 로그아웃 했습니다.", session.getAttribute("id"));
		
		session.invalidate(); // 전체삭제
		mav.setViewName("redirect:/");
		return mav;
	};
	
	//회원가입 페이지
	@RequestMapping("/sign")
	public ModelAndView sign(HttpServletRequest request) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		log.info("{} 세션 게스트 회원 가입 준비", session.getId());
		
		mav.setViewName("user/register.html");
		return mav;
	}
	
	//회원가입
	@PostMapping("/register")
	public ModelAndView register(UserVO userVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		log.info("{} 세션 회원 가입 시도", session.getId());
		
		//현재아이피 추출
		String IP = CommonUtils.getClientIP(request);
			
		//전체 갯수
		int totalcount = userService.mbGetAll();
		
		//db insert 준비
		UserDomain userDomain = UserDomain.builder()
			.mbId(userVO.getId())
			.mbPw(userVO.getPw())
			.mbLevel((totalcount == 0) ? "3" : "1") // 최초가입자를 level 3 admin 부여
			.mbIp(IP)
			.mbUse("Y")
			.build();
		
		// 저장
		userService.mbCreate(userDomain);
		
		log.info("아이디 {} 가입 완료.", userDomain.getMbId());
		
		session.setAttribute("ip",IP);
		session.setAttribute("id", userDomain.getMbId());
		session.setAttribute("level", userDomain.getMbLevel());
		
		mav.setViewName("redirect:/bkStore"); // URL 잘못된 형태로 출력되기 때문에 redirect를 써서 한다.
		
		return mav;
	}
	
	
	@RequestMapping("/userSetting")
	public ModelAndView setting(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbId", (String)session.getAttribute("id"));
		
		UserDomain userDomain = userService.mbGetId(map);
		
		log.info("아이디 {} 회원 정보 수정 페이지 접속", session.getAttribute("id"));

		mav.addObject("userinfo", userDomain);
		mav.setViewName("user/userSetting.html");
		return mav;
	}
	
	
	@PostMapping("/userUpdate")
	public ModelAndView Update(UserVO userVO, HttpServletRequest request, HttpServletResponse response) {
			
		ModelAndView mav = new ModelAndView();
		
		log.info("아이디 {} 회원 정보 수정 실행", userVO.getId());
		
		//db 업데이트
		UserDomain userDomain = null; //초기화
		String IP = CommonUtils.getClientIP(request);
		userDomain = UserDomain.builder()
				.mbSeq(Integer.parseInt(userVO.getSeq()))
				.mbId(userVO.getId())
				.mbPw(userVO.getPw())
				.mbLevel(userVO.getLevel())
				.mbIp(IP)
				.mbUse("Y")
				.build();
		userService.mbUpdate(userDomain);
		
		mav.setViewName("redirect:/bkStore");
		return mav;
	}
	
	@GetMapping("/setting/userDelete/{mbSeq}")
	public ModelAndView Delete(@PathVariable("mbSeq") String mbSeq,  HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		//db 삭제
		Map<String, String> map = new HashMap<String, String>();
		
		log.info("회원 번호 {} 회원 탈퇴 실행", mbSeq);
		
		map.put("mbSeq", mbSeq);
		userService.mbRemove(map);
		
		session.invalidate();
		mav.setViewName("redirect:/");
		return mav;
	}
	
	//로그인 시도 및 도서 정보 페이지 이동
	@RequestMapping(value = "bookStore")
	public ModelAndView bookStore(UserVO userVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//session 처리 
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		
		//해당 회원 정보 가져옴
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbId", userVo.getId());

		UserDomain userDomain = userService.mbGetId(map);

		//현재아이피 추출 및 접속 IP 값을 DB에 등록
		String IP = CommonUtils.getClientIP(request);
		Map<String, String> ip = new HashMap<String, String>();
		ip.put("mbIp", IP);
		
		userService.mbUpdateIp(ip);
		
		//session 저장
		session.setAttribute("ip",IP);
		session.setAttribute("id", userDomain.getMbId());
		session.setAttribute("level", userDomain.getMbLevel());
		
		log.info("아이디 {} 로그인 성공", userDomain.getMbId());
		
		mav = bkViewList();
		mav.setViewName("bookStore/bookList.html"); 

		return mav;
	}
	
	//도서 상품 페이지 이동
	@RequestMapping(value = "bkStore")
	public ModelAndView bkStore() {
		
		ModelAndView mav = new ModelAndView();
		
		mav = bkViewList();
		mav.setViewName("bookStore/bookList.html"); 
		return mav;
	}
	
	@RequestMapping("/myLibrary")
	public ModelAndView adminItemList(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		log.info("아이디 {} 내 서재 페이지로 이동", session.getAttribute("id"));
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbId", session.getAttribute("id").toString());
		List<ItemViewDomain> items = itemService.bookMyList(map);
		
		mav.addObject("items", items);
		mav.setViewName("user/myLibrary.html");
		return mav;
	}
	
	public ModelAndView bkViewList() {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("last_id", itemService.bookViewMaxKey());
    	map.put("contentnum", 8);
		List<ItemMainDomain> bookList = itemService.bookViewMain(map);
		
		log.info("메인 페이지 도서 리스트 {}개 불러옴", bookList.size());
		
		mav.addObject("bookList", bookList);
		return mav;
	}
}