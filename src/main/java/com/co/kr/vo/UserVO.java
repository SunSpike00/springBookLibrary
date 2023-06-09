package com.co.kr.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class UserVO {
	private String seq;
	private String id;
	private String pw;
	private String pwCheck;
	private String admin;
	private String level;
}
