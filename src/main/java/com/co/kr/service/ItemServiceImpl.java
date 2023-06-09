package com.co.kr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.co.kr.domain.ItemDomain;
import com.co.kr.domain.ItemGenreDomain;
import com.co.kr.domain.ItemMainDomain;
import com.co.kr.domain.ItemShopDomain;
import com.co.kr.domain.ItemViewDomain;
import com.co.kr.mapper.GenreMapper;
import com.co.kr.mapper.ItemMapper;
import com.co.kr.mapper.ShopMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class ItemServiceImpl implements ItemService  {

	@Autowired
	ItemMapper itemMapper;
	
	@Autowired
	GenreMapper genreMapper;
	
	@Autowired
	ShopMapper shopMapper;
	
	// 아이템 CRUD
	// 1개 리스트 생성(Create 부분)
	@Override
	public int itemCreate(ItemDomain itemDomain) {
		itemMapper.bookItemCreate(itemDomain);
		log.info("키 발급 : {}", itemDomain.getBkId());
		return Integer.parseInt(itemDomain.getBkId());
	};
	
	@Override
	public List<ItemMainDomain> bookViewMain(Map<String, Object> map){
		return itemMapper.bookViewMain(map);
	};
	
	public List<ItemMainDomain> bookSearchMain(Map<String, Object> map){
		return itemMapper.bookSearchMain(map);
	};
	
	// 전체 리스트 조회(admin page 뿌려주기)
	@Override
	public List<ItemViewDomain> bookItemList() {
		return itemMapper.bookItemList();
	};
	
	//
	public List<ItemViewDomain> bookMyList(Map<String, String> map){
		return itemMapper.bookMyList(map);
	};
	
	@Override
	public int bookViewMaxKey() {
		
		String result = itemMapper.bookViewMaxKey();
		return result == null ? 0 : Integer.parseInt(result);
	};

	// 1개 리스트 조회(Detail 부분 뿌려주기)
	@Override
	public ItemViewDomain itemOne(Map<String, Object> map) {
		return itemMapper.bookItemOne(map);
	};
	
	// 1개 리스트 register 수정 값(select 태그 사용해야 해서....)(Detail 부분 뿌려주기)
	@Override
	public ItemDomain itemIdDetail(Map<String, Object> map) {
		return itemMapper.bookItemIdOne(map);
	};
	
	// 1개 리스트 수정(Deatail Update)
	@Override
	public int itemUpdate(ItemDomain itemDomain) {
		itemMapper.bookItemUpdate(itemDomain);
		log.info("수정 할 키 대상 : {}", itemDomain.getBkId());
		return Integer.parseInt(itemDomain.getBkId());
	};
	
	// 1개 리스트 삭제(Deatil Remove)
	@Override
	public void itemRemove(Map<String, Object> map) {
		itemMapper.bookItemRemove(map);
	};
	
	
	
	// 장르 CRUD
	// 1개 리스트 생성(Create 부분)
	@Override
	public void genreItemCreate(ItemGenreDomain itemGenreDomain) {
		genreMapper.genreItemCreate(itemGenreDomain);
	};
	
	// 전체 리스트 조회(main page 뿌려주기)
	@Override
	public List<ItemGenreDomain> GenreList(){
		return genreMapper.genreItems();
	};
	
	// 1개 리스트 조회(Detail 부분 뿌려주기)
	@Override
	public ItemGenreDomain genreItemOne(Map<String, String> map) {
		return genreMapper.genreItemOne(map);
	};
	
	// 1개 리스트 수정(Deatail Update)
	@Override
	public void genreItemUpdate(ItemGenreDomain itemGenreDomain) {
		genreMapper.genreItemUpdate(itemGenreDomain);
	};
	
	// 1개 리스트 삭제(Deatil Remove)
	@Override
	public void genreItemRemove(Map<String, String> map) {
		genreMapper.genreItemRemove(map);
	};
	
	
	
	
	// 매장 CRUD
	// 1개 리스트 생성(Create 부분)
	@Override
	public void shopItemCreate(ItemShopDomain itemShopDomain) {
		shopMapper.shopItemCreate(itemShopDomain);
	};
	
	// 전체 리스트 조회(main page 뿌려주기)
	@Override
	public List<ItemShopDomain> shopList(){
		return shopMapper.shopItems();
	};
	
	// 1개 리스트 조회(Detail 부분 뿌려주기)
	@Override
	public ItemShopDomain shopItemOne(Map<String, String> map) {
		return shopMapper.shopItemOne(map);
	};
	
	// 1개 리스트 수정(Deatail Update)
	@Override
	public void shopItemUpdate(ItemShopDomain itemShopDomain) {
		shopMapper.shopItemUpdate(itemShopDomain);
	};
	
	// 1개 리스트 삭제(Deatil Remove)
	@Override
	public void shopItemRemove(Map<String, String> map) {
		shopMapper.shopItemRemove(map);
	};
}
