package com.co.kr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.ItemDomain;
import com.co.kr.domain.ItemMainDomain;
import com.co.kr.domain.ItemViewDomain;

@Mapper
public interface ItemMapper {
	
	//book_list create
	public int bookItemCreate(ItemDomain itemDomain);
	
	//book_list list all
	public List<ItemMainDomain> bookViewMain(Map<String, Object> map);
	
	public List<ItemMainDomain> bookSearchMain(Map<String, Object> map);
	
	public List<ItemViewDomain> bookItemList();
	
	public List<ItemViewDomain> bookMyList(Map<String, String> map);
	
	public String bookViewMaxKey();
	
	//book_list list one
	public ItemViewDomain bookItemOne(Map<String, Object> map);
	
	public ItemDomain bookItemIdOne(Map<String, Object> map);
	
	//book_list update
	public void bookItemUpdate(ItemDomain itemDomain); 
	
	//book_list remove
	public void bookItemRemove(Map<String, Object> map);
	
}
