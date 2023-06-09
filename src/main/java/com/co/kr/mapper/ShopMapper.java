package com.co.kr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.ItemShopDomain;

@Mapper
public interface ShopMapper {

	//shop create
	public void shopItemCreate(ItemShopDomain itemShopDomain);
	
	//shop list
	public List<ItemShopDomain> shopItems();
	
	//shop list one
	public ItemShopDomain shopItemOne(Map<String, String> map);
	
	//shop update
	public void shopItemUpdate(ItemShopDomain itemShopDomain);
	
	//shop delete
	public void shopItemRemove(Map<String, String> map);
}
