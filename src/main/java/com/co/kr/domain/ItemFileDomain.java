package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class ItemFileDomain {
	
	// 파일 세팅
	private Integer bkId;
	private String mbId;
	private String bkOriginalFileName;
	private String bkNewFileName; //동일 이름 업로드 될 경우
	private String bkFilePath;
	private Integer bkFileSize;
}
