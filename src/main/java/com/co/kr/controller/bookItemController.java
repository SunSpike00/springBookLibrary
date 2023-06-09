package com.co.kr.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.ItemDomain;
import com.co.kr.domain.ItemFileDomain;
import com.co.kr.domain.ItemGenreDomain;
import com.co.kr.domain.ItemMainDomain;
import com.co.kr.domain.ItemShopDomain;
import com.co.kr.domain.ItemViewDomain;
import com.co.kr.service.ItemFileService;
import com.co.kr.service.ItemService;
import com.co.kr.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class bookItemController {

	//https://dummyimage.com/450x300/dee2e6/6c757d.jpg
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	ItemFileService itemFileService;
	
	// 사용자가 만드는 create 부분
	@RequestMapping("/bookCreate")
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		// select 목록들 세팅 하기
		// genre
		List<ItemGenreDomain> genre = itemService.GenreList();
		// shop
		List<ItemShopDomain> shop = itemService.shopList();
		
		log.info("도서 정보 생성 페이지 접속");
		
		mav.addObject("genre", genre);
		mav.addObject("shop", shop);
		mav.setViewName("bookStore/bookCreate.html");
		return mav;
	}
	
	@RequestMapping("/bookRegister")
	public ModelAndView adminItemRegister(ItemDomain itemDomain, MultipartHttpServletRequest request, HttpServletRequest httpReq) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = httpReq.getSession();
		itemDomain.setMbId(session.getAttribute("id").toString());
		
		
		int bkId = itemService.itemCreate(itemDomain);
		itemFileService.itemCoverFileUpload(request, httpReq, bkId);
		itemFileService.itemFilesUpload(request, httpReq, bkId);
		
		log.info("도서 번호 {} 도서 정보 생성", bkId);
		
		mav.setViewName("redirect:/myLibrary");
		return mav;
	}
	
	
	@RequestMapping("/bookInfo")
	public ModelAndView viewDetail(@RequestParam("bkId")String bkId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		mav = bkViewOneCall(bkId, request);
		
		mav.setViewName("bookStore/bookInfo.html");
		return mav;
	}
	
	public ModelAndView bkViewOneCall(String bkId, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		map.put("bkId", Integer.parseInt(bkId));
		
		ItemViewDomain itemViewDomain = itemService.itemOne(map);
		
		log.info("도서 번호 {} 도서 정보 상세 조회", bkId);
		
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
	
	@RequestMapping("/bookEdit")
	public ModelAndView edit(@RequestParam("bkId")String bkId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bkId",  Integer.parseInt(bkId));
		
		log.info("도서 번호 {} 도서 수정 페이지 접속", bkId);
		
		//edit 세팅용
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
		
		session.setAttribute("bookFile", bookfileList);
		session.setAttribute("bookCover", coverFile);
		mav.setViewName("bookStore/bookUpdate.html");
		return mav;
	}
	
	@PostMapping("/bookEditSave")
	public ModelAndView editsave(ItemDomain itemDomain, MultipartHttpServletRequest request, HttpServletRequest httpReq) {
		ModelAndView mav = new ModelAndView();
		
		int bkId = itemService.itemUpdate(itemDomain);
		itemFileService.itemCoverFileUpdate(request, httpReq, bkId);
		itemFileService.itemFilesUpdate(request, httpReq, bkId);
		
		log.info("도서 번호 {} 도서 수정 완료", bkId);

		mav.setViewName("redirect:/bookInfo?bkId=" + bkId);
		return mav;
	}
	
	@RequestMapping("/bookRemove")
	public ModelAndView delete(@RequestParam("bkId")String bkId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bkId", Integer.parseInt(bkId));
		
		log.info("도서 번호 {} 도서 정보 삭제", bkId);
		
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

		session.removeAttribute("bookFile");
		session.removeAttribute("bookCover");
		
		mav.setViewName("redirect:/bkStore");
		
		return mav;
	}
	
	@RequestMapping("/bookSearch")
	public ModelAndView bookSearch(@RequestParam("keyWord")String keyWord, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		log.info("아이디 {}가 검색 조회 실행", request.getAttribute("id"));
		
		mav = bkSearchList(keyWord);
		mav.setViewName("bookStore/bookSearch.html");
		return mav;
	}
	
	public ModelAndView bkSearchList(String keyWord) {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("last_id", itemService.bookViewMaxKey());
    	map.put("contentnum", 8);
    	map.put("keyWord", keyWord);
    	List<ItemMainDomain> items = itemService.bookSearchMain(map);
    	
    	log.info("도서 정보 리스트 페이지, {}개 도서 정보 조회", items.size());
    	
		mav.addObject("bookList", items);
    	return mav;
	}
	
	@ResponseBody
	@PostMapping("bookajax")
	public Map<String, Object> ajaxList(@RequestParam Map<String, Object> data) {
		
		String id = (String) data.get("bkId");
		
		log.info("ajax 통신 실행, 마지막 요소 도서 번호 {}", id);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//자기 자신 중복 제거 하기 위해 -1
    	map.put("last_id", Integer.parseInt(id)-1);
    	map.put("contentnum", 8);
    	List<ItemMainDomain> items = new ArrayList<>();
		if(data.get("code").toString().equals("bkStore") || data.get("code").toString().equals("index")) {
	    	if((items = itemService.bookViewMain(map)).isEmpty()) {
	    		items = null;
	    	}
		} else if(data.get("code").toString().equals("bkSearch")) {
			String keyWord = (String) data.get("keyWord");
			map.put("keyWord", keyWord);
			if((items = itemService.bookSearchMain(map)).isEmpty()) {
				items = null;
			}
		}
    	
    	Map<String, Object> retval = new HashMap<String, Object>();
    	retval.put("items", items);
		return retval;
	}
}
