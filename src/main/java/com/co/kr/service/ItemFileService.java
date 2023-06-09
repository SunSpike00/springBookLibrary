package com.co.kr.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.ItemFileDomain;

public interface ItemFileService {

	
	// 커버 업로드 호출용(create)
	public void itemCoverFileUpload(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId);
	
	// 커버 조회 호출용(read)
	public List<ItemFileDomain> itemCoverFilesCall();
	
	// 커버 1개 조회 호출용(read)
	public ItemFileDomain itemCoverFileOneCall(Map<String, Object> map);
	
	// 커버 수정 호출용(update)
	public void itemCoverFileUpdate(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId);
	
	// 커버 삭제 호출용(delete)
	public void itemCoverFileRemove(ItemFileDomain itemFileDomain);
	
	// 이미지 목록 업로드 호출용
	public void itemFilesUpload(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId);
	
	// 이미지 목록 조회 호출용
	public List<ItemFileDomain> itemFilesCall(Map<String, Object> map);
	
	// 이미지 목록 수정 호출용
	public void itemFilesUpdate(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId);
	
	// 이미지 목록 삭제 호출용
	public void itemFilesRemove(ItemFileDomain itemFileDomain);
}
