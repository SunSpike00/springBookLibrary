package com.co.kr.code;

import lombok.Getter;

@Getter
public enum Table {

	MEMBER_LIST("member_list"),
	BOOK_LIST("book_list"),
	BOOK_COVER("book_cover"),
	BOOK_FILES("book_files"),
	GENRE("genre"),
	SHOP("shop");
	//db 추가 테이블 작성
	
	private String table;

	Table(String table){
		this.table = table;
	}
	
}