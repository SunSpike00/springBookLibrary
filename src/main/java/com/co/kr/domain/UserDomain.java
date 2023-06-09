package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder(builderMethodName = "builder")
public class UserDomain {
	
	// 회원 CRUD용
	private Integer mbSeq;
	private String mbId;
	private String mbPw;
	private String mbLevel;		// 기본 값 1
	private String mbIp;
	private String mbUse;		// 기본 값 Y
	private String mbCreateAt;
	private String mbUpdateAt;
	
}