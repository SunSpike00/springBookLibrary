package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder(builderMethodName = "builder")
public class ItemViewDomain {
	
	// 디테일 부분 조회 할 때 사용
	private String bkId;
	private String bkTitle;
	private String genreName;
	private String bkStock;
	private String bkPrice;
	private String shopName;
	private String bkAuthor;
	private String bkPublish;
	private String bkContent;
	private String mbId;
}
