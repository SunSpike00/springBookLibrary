package com.co.kr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.co.kr.domain.ItemDomain;
import com.co.kr.domain.ItemGenreDomain;
import com.co.kr.domain.ItemMainDomain;
import com.co.kr.domain.ItemShopDomain;
import com.co.kr.domain.ItemViewDomain;

public interface ItemService {

	// 아이템 CRUD
	// 1개 리스트 생성(Create 부분)
	public int itemCreate(ItemDomain itemDomain);
	
	public List<ItemMainDomain> bookViewMain(Map<String, Object> map);
	
	public List<ItemMainDomain> bookSearchMain(Map<String, Object> map);
	
	// 전체 리스트 조회(main page 뿌려주기)
	public List<ItemViewDomain> bookItemList();
	
	//
	public List<ItemViewDomain> bookMyList(Map<String, String> map);
	
	// book id 값 가장 큰 거 가져오기
	public int bookViewMaxKey();
	
	// 1개 리스트 조회(Detail 부분 뿌려주기)
	public ItemViewDomain itemOne(Map<String, Object> map);
	
	// 1개 리스트 수정 값(select 태그 사용해야 해서....)(Detail 부분 뿌려주기)
	public ItemDomain itemIdDetail(Map<String, Object> map);
	
	// 1개 리스트 수정(Deatail Update)
	public int itemUpdate(ItemDomain itemDomain);
	
	// 1개 리스트 삭제(Deatil Remove)
	public void itemRemove(Map<String, Object> map);
	
	
	
	////////////////////미구현//////////////////////////
	
	// 장르 CRUD
	// 1개 리스트 생성(Create 부분)
	public void genreItemCreate(ItemGenreDomain itemGenreDomain);
	
	// 전체 리스트 조회(main page 뿌려주기)
	public List<ItemGenreDomain> GenreList();
	
	// 1개 리스트 조회(Detail 부분 뿌려주기)
	public ItemGenreDomain genreItemOne(Map<String, String> map);
	
	// 1개 리스트 수정(Deatail Update)
	public void genreItemUpdate(ItemGenreDomain itemGenreDomain);
	
	// 1개 리스트 삭제(Deatil Remove)
	public void genreItemRemove(Map<String, String> map);
	
	
	// 매장 CRUD
	// 1개 리스트 생성(Create 부분)
	public void shopItemCreate(ItemShopDomain itemShopDomain);
	
	// 전체 리스트 조회(main page 뿌려주기)
	public List<ItemShopDomain> shopList();
	
	// 1개 리스트 조회(Detail 부분 뿌려주기)
	public ItemShopDomain shopItemOne(Map<String, String> map);
	
	// 1개 리스트 수정(Deatail Update)
	public void shopItemUpdate(ItemShopDomain itemShopDomain);
	
	// 1개 리스트 삭제(Deatil Remove)
	public void shopItemRemove(Map<String, String> map);
}
