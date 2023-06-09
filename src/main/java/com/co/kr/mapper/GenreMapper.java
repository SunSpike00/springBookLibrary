package com.co.kr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.ItemGenreDomain;

@Mapper
public interface GenreMapper {
	
	//genre create
	public void genreItemCreate(ItemGenreDomain itemGenreDomain);
	
	//genre list
	public List<ItemGenreDomain> genreItems();
	
	//genre list one
	public ItemGenreDomain genreItemOne(Map<String, String> map);
	
	//genre update
	public void genreItemUpdate(ItemGenreDomain itemGenreDomain);

	//genre delete
	public void genreItemRemove(Map<String, String> map);
}
