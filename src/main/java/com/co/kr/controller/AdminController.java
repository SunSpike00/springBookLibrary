package com.co.kr.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.ItemDomain;
import com.co.kr.domain.ItemFileDomain;
import com.co.kr.domain.ItemGenreDomain;
import com.co.kr.domain.ItemShopDomain;
import com.co.kr.domain.ItemViewDomain;
import com.co.kr.domain.UserDomain;
import com.co.kr.service.ItemFileService;
import com.co.kr.service.ItemService;
import com.co.kr.service.UserService;
import com.co.kr.util.CommonUtils;
import com.co.kr.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdminController {

	@Autowired
	UserService userService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	ItemFileService itemFileService;
	
	@RequestMapping("/adminMember")
	public ModelAndView adminMember(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();

		Map<String, Integer> map = new HashMap<String, Integer>();
		List<UserDomain> userList = userService.mbAllList(map);
		
		log.info("관리자 회원 조회 페이지 접속");
		
		mav.addObject("users", userList);
		mav.setViewName("admin/adminUserInfo.html");
		return mav;
	}
	
	
	@RequestMapping("/adminCreate")
	public ModelAndView amdinMemberCreate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		
		log.info("관리자 회원 생성 페이지 접속");
		
		mav.setViewName("admin/adminRegister.html");
		return mav;
	}
	
	@PostMapping("/adminRegister")
	public ModelAndView adminMemberRegister(UserVO userVO, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView();

		// 현재아이피 추출
		String IP = CommonUtils.getClientIP(request);
		
		// db insert 준비
		UserDomain userDomain = UserDomain.builder()
				.mbId(userVO.getId())
				.mbPw(userVO.getPw())
				.mbLevel("1")
				.mbIp(IP)
				.mbUse("Y")
				.build();
		
		log.info("관리자 권한으로 회원 생성");
		
		// 저장
		userService.mbCreate(userDomain);

		mav.setViewName("redirect:/adminMember"); // URL 잘못된 형태로 출력되기 때문에 redirect를 써서 한다.
		return mav;
	}
	
	@RequestMapping("/adminEdit")
	public ModelAndView adminMemberEdit(@RequestParam("mbSeq") String mbSeq, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbSeq", mbSeq);
		
		UserDomain user = userService.mbSelectList(map);
		
		log.info("관리자 회원 수정 페이지 접속");
		
		mav.addObject("user", user);
		mav.setViewName("admin/adminUpdate.html");
		return mav;
	}
	
	@RequestMapping("/adminUpdate")
	public ModelAndView adminMemberUpdate(UserDomain userDomain, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		userService.mbUpdate(userDomain);
		
		log.info("관리자 권한으로 회원 수정 실행");
		
		mav.setViewName("redirect:/adminMember");
		return mav;
	}
	
	
	@RequestMapping("/adminRemove")
	public ModelAndView adminMemberRemove(@RequestParam("mbSeq") String mbSeq) {
		ModelAndView mav = new ModelAndView();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("mbSeq", mbSeq);
		
		log.info("관리자 권한으로 삭제 실행");
		
		userService.mbRemove(map);
		
		mav.setViewName("redirect:/adminMember");
		return mav;
	}
	
	
	
	
	
	
	//////////////도서 관리 CRUD//////////////
	
	@RequestMapping("/adminItemCreate")
	public ModelAndView adminItemCreate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		// select 목록들 세팅 하기
		// genre
		List<ItemGenreDomain> genre = itemService.GenreList();
		
		// shop
		List<ItemShopDomain> shop = itemService.shopList();
		
		log.info("관리자, 도서 정보 생성 페이지 접속");
		
		mav.addObject("genre", genre);
		mav.addObject("shop", shop);
		mav.setViewName("admin/adminItemCreate.html");
		return mav;
	}
	
	@RequestMapping("/adminItemRegister")
	public ModelAndView adminItemRegister(ItemDomain itemDomain, MultipartHttpServletRequest request, HttpServletRequest httpReq) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = httpReq.getSession();
		itemDomain.setMbId(session.getAttribute("id").toString());
		
		int bkId = itemService.itemCreate(itemDomain);
		itemFileService.itemCoverFileUpload(request, httpReq, bkId);
		itemFileService.itemFilesUpload(request, httpReq, bkId);
		
		log.info("관리자, 도서 번호 {} 도서 정보 생성", bkId);
		
		mav.setViewName("redirect:/adminItem");
		return mav;
	}
	
	@RequestMapping("/adminItem")
	public ModelAndView adminItemList() {
		ModelAndView mav = new ModelAndView();

		List<ItemViewDomain> items = itemService.bookItemList();
		
		log.info("관리자 도서 정보 리스트 페이지, {}개 도서 정보 조회", items.size());
		
		mav.addObject("items", items);
		mav.setViewName("admin/adminItemInfo.html");
		return mav;
	}
	
	// 관리자 아이템 수정 기능
	@GetMapping("adminItemEdit")
	public ModelAndView adminItemEdit(@RequestParam("bkId")String bkId, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bkId", Integer.parseInt(bkId));
		
		log.info("관리자 도서 수정 페이지 접속, {}번 도서 정보 수정", bkId);
		
		//register 세팅하기 위한 내용들
		ItemDomain itemDomain = itemService.itemIdDetail(map);
		ItemFileDomain coverFile = itemFileService.itemCoverFileOneCall(map);
		if(coverFile != null) {
			// 경로 세팅
			String coverPath = coverFile.getBkFilePath().replaceAll("\\\\", "/");
			coverFile.setBkFilePath(coverPath);
		}
		
		List<ItemFileDomain> bookfileList = itemFileService.itemFilesCall(map);
		if(bookfileList != null) {
			for (ItemFileDomain list : bookfileList) {
				String path = list.getBkFilePath().replaceAll("\\\\", "/");
				list.setBkFilePath(path);
			}
		}

		// select 목록들 세팅 하기
		// genre
		List<ItemGenreDomain> genre = itemService.GenreList();
		
		// shop
		List<ItemShopDomain> shop = itemService.shopList();
		
		mav.addObject("item", itemDomain);
		mav.addObject("cover", coverFile);
		mav.addObject("fileSize", bookfileList.size());
		mav.addObject("genre", genre);
		mav.addObject("shop", shop);
		
		//아무 것도 없으면 세션 값 사용
		session.setAttribute("bookFile", bookfileList);
		session.setAttribute("bookCover", coverFile);
		mav.setViewName("admin/adminItemUpdate.html");
		return mav;
	}
	
	@RequestMapping("adminItemUpdate")
	public ModelAndView adminItemUpdate(ItemDomain itemDomain,  MultipartHttpServletRequest request, HttpServletRequest httpReq) {
		ModelAndView mav = new ModelAndView();
		
		int bkId = itemService.itemUpdate(itemDomain);
		itemFileService.itemCoverFileUpdate(request, httpReq, bkId);
		itemFileService.itemFilesUpdate(request, httpReq, bkId);
		
		log.info("관리자 권한 도서 번호 {} 도서 정보 수정 완료", bkId);
		mav.setViewName("redirect:/adminItem");
		return mav;
	}
	
	@RequestMapping("/adminItemRemove")
	public ModelAndView adminItemRemove(@RequestParam("bkId") String bkId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bkId", Integer.parseInt(bkId));
		
		log.info("{}번 도서 정보 제거", bkId);
		
		itemService.itemRemove(map);
		
		ItemFileDomain coverFile = itemFileService.itemCoverFileOneCall(map);
		
		if(coverFile != null) {
			try {
				coverFile.getBkFilePath();
				Path coverPath = Paths.get(coverFile.getBkFilePath());
				Files.deleteIfExists(coverPath);
				itemFileService.itemCoverFileRemove(coverFile);
			} catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		List<ItemFileDomain> fileList = itemFileService.itemFilesCall(map);
		
		if(fileList != null) {
			for (ItemFileDomain list : fileList) {
				Path bkFilePath = Paths.get(list.getBkFilePath());
				try {
					Files.deleteIfExists(bkFilePath);
					itemFileService.itemFilesRemove(list);
				} catch (IOException e) {
		            e.printStackTrace();
		        }
			}
		}
		
		//세션 정보 제거
		session.removeAttribute("bookFile");
		session.removeAttribute("bookCover");
		
		mav.setViewName("redirect:/adminItem");
		return mav;
	}
	
	public ModelAndView bkViewOneCall(String bkId, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		map.put("bkId", Integer.parseInt(bkId));
		log.info("관리자 권한 도서 번호 {} 도서 정보 상세 조회", bkId);
		
		ItemViewDomain itemViewDomain = itemService.itemOne(map);
		
		ItemFileDomain cover = itemFileService.itemCoverFileOneCall(map);
		String coverPath = cover.getBkFilePath().replaceAll("\\\\", "/");
		cover.setBkFilePath(coverPath);
		
		List<ItemFileDomain> bookList = itemFileService.itemFilesCall(map);
		
		for (ItemFileDomain list : bookList) {
			String path = list.getBkFilePath().replaceAll("\\\\", "/");
			list.setBkFilePath(path);
		}
		
		// setting
		mav.addObject("bookDetail", itemViewDomain);
		mav.addObject("cover", cover);
		mav.addObject("bookList", bookList);
		
		session.setAttribute("cover", cover);
		session.setAttribute("bookList", bookList);
		
		return mav;
	}
	
}
