package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder(builderMethodName = "builder")
public class ItemMainDomain {
	
	// 메인 페이지 사용
	int bkId;
	String bkTitle;
	String bkPrice;
	String bkNewFileName;
	String genreName;
	String shopName;
}
