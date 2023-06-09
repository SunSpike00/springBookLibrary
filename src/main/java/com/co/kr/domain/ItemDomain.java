package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder(builderMethodName = "builder")
public class ItemDomain {
	
	// 생성용, 수정용 세팅
	private String bkId;
	private String bkTitle;
	private String genreId;
	private String bkStock;
	private String bkPrice;
	private String shopId;
	private String bkAuthor;
	private String bkPublish;
	private String bkContent;
	private String mbId;
}
