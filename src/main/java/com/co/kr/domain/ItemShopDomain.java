package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder(builderMethodName = "builder")
public class ItemShopDomain {
	
	// 셀렉트 태그 세팅
	String shopId;
	String shopName;
	
}
