package com.co.kr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.ItemFileDomain;

@Mapper
public interface ItemFileMapper {
	
	// 커버 업로드 호출용(create)
	public int itemCoverFileUpload(ItemFileDomain itemFileDomain);
	
	// 커버 조회 호출용(read)
	public List<ItemFileDomain> itemCoverFilesCall();
	
	// 커버 조회 호출용(read)
	public ItemFileDomain itemCoverFileOneCall(Map<String, Object> map);
	
	// 커버 수정 호출용(update)
	public int itemCoverFileUpdate(ItemFileDomain itemFileDomain);
	
	// 커버 삭제 호출용(delete)
	public void itemCoverFileRemove(ItemFileDomain itemFileDomain);
	
	// 이미지 목록 업로드 호출용
	public int itemFilesUpload(ItemFileDomain itemFileDomain);
	
	// 이미지 목록 조회 호출용
	public List<ItemFileDomain> itemFilesCall(Map<String, Object> map);
	
	// 이미지 목록 수정 호출용
	public int itemFilesUpdate(ItemFileDomain itemFileDomain);
	
	// 이미지 목록 삭제 호출용
	public void itemFilesRemove(ItemFileDomain itemFileDomain);
	
	
	
}
