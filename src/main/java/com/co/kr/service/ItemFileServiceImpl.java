package com.co.kr.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.ItemFileDomain;
import com.co.kr.mapper.ItemFileMapper;
import com.co.kr.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;


@Transactional
@Service
@Slf4j
public class ItemFileServiceImpl implements ItemFileService{

	@Autowired
	ItemFileMapper itemFileMapper;
	
	
	// 커버 업로드 호출용(create)
	@Override
	public void itemCoverFileUpload(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId) {
		multFileProcess(request, httpReq, "bookCoverImg", "bookCover", bkId);
	}
	
	// 커버 조회 호출용(read)
	@Override
	public List<ItemFileDomain> itemCoverFilesCall() {
		List<ItemFileDomain> list = itemFileMapper.itemCoverFilesCall();
		return list;
	};
	
	// 커버 1개만 가져오기
	@Override
	public ItemFileDomain itemCoverFileOneCall(Map<String, Object> map) {
		return itemFileMapper.itemCoverFileOneCall(map);
	};
	
	// 커버 수정 호출용(update)
	@Override
	public void itemCoverFileUpdate(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId) {
		
		HttpSession session = httpReq.getSession();
		MultipartFile coverFile = request.getFile("bookCover");
		
		if(!coverFile.isEmpty()) {
			if(session.getAttribute("bookCover") != null) {
				ItemFileDomain tmpFile = (ItemFileDomain) session.getAttribute("bookCover");
				Path bkfilePath = Paths.get(tmpFile.getBkFilePath());
				try {
		            Files.deleteIfExists(bkfilePath); // 물리 데이터 삭제

		            itemCoverFileRemove(tmpFile); //db 삭제

				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
		multFileProcess(request, httpReq, "bookCoverImg", "bookCover", bkId);
	};
	
	// 커버 삭제 호출용(delete)
	@Override
	public void itemCoverFileRemove(ItemFileDomain itemFileDomain) {
		itemFileMapper.itemCoverFileRemove(itemFileDomain);
	};
	
	
	// 이미지 목록 업로드 호출용
	@Override
	public void itemFilesUpload(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId) {
		multFileProcess(request, httpReq, "bookImg", "bookFile", bkId);
	};
	
	// 이미지 목록 조회 호출용
	@Override
	public List<ItemFileDomain> itemFilesCall(Map<String, Object> map) {
		List<ItemFileDomain> list = itemFileMapper.itemFilesCall(map);
		return list;
	};
	
	// 이미지 목록 수정 호출용
	@Override
	public void itemFilesUpdate(MultipartHttpServletRequest request, HttpServletRequest httpReq, int bkId) {
		
		HttpSession session = httpReq.getSession();
		List<MultipartFile> multipartFiles = request.getFiles("bookFile");
		
		for (MultipartFile multipartFile : multipartFiles) {	// 업로드 파일들
			
			if(!multipartFile.isEmpty()) {
				List<ItemFileDomain> tempList = null;
				
				if(session.getAttribute("bookFile") != null) {	// 세션에 해당 내용 존재 유무
					tempList = (List<ItemFileDomain>) session.getAttribute("bookFile");
					
					for (ItemFileDomain list : tempList) {
						Path bkfilePath = Paths.get(list.getBkFilePath());

				        try {
				            // 파일 삭제
				            Files.deleteIfExists(bkfilePath); // notfound시 exception 발생안하고 false 처리
				            //삭제 
							itemFilesRemove(list); //데이터 삭제
							
				        } catch (IOException e) {
				            e.printStackTrace();
				        }
					}
				}
			}
		}
		multFileProcess(request, httpReq, "bookImg", "bookFile", bkId);
	};
	
	// 이미지 목록 삭제 호출용
	@Override
	public void itemFilesRemove(ItemFileDomain itemFileDomain) {
		itemFileMapper.itemFilesRemove(itemFileDomain);
	};
	
	
	// 다중 파일 처리 함수
	public void multFileProcess(MultipartHttpServletRequest request, HttpServletRequest httpReq, String pathName, String fileName, int bkId) {
			
		HttpSession session = httpReq.getSession();
		List<MultipartFile> multipartFiles = request.getFiles(fileName);
		
		String mbId = session.getAttribute("id").toString();
		
		// 저장 root 경로만들기 
		Path rootPath = Paths.get(new File("C://").toString(), pathName, File.separator).toAbsolutePath().normalize();
		File pathCheck = new File(rootPath.toString());
		
		// folder chcek
		if(!pathCheck.exists()) pathCheck.mkdirs();
		
		for(MultipartFile itemFile : multipartFiles) {
			
			if(!multipartFiles.isEmpty()) {
				
				//확장자 추출
				String originalFileExtension;
				String contentType = itemFile.getContentType();
				String origFilename = itemFile.getOriginalFilename();
				
				//확장자 조재안을경우
				if(ObjectUtils.isEmpty(contentType)){
					break;
				} else { // 확장자가 jpeg, png인 파일들만 받아서 처리
					if(contentType.contains("image/jpeg")) {
						originalFileExtension = ".jpg";
					} else if(contentType.contains("image/png")) {
						originalFileExtension = ".png";
					} else {
						break;
					}
				}
				
				//파일명을 업로드한 날짜로 변환하여 저장
				String uuid = UUID.randomUUID().toString();
				String current = CommonUtils.currentTime();
				String newFileName = uuid + current + originalFileExtension;
				String bkNewFileName = "resources/" + pathName + "/"+ newFileName; // WebConfig에 동적 이미지 폴더 생성 했기때문
				
				//최종경로까지 지정
				Path targetPath = rootPath.resolve(newFileName);
				
				// 새로운 파일 설정
				File file = new File(targetPath.toString());
				
				try {					
					
					//원본 파일 해당 위치로 복사 저장.
					itemFile.transferTo(file);
					// 파일 권한 설정(쓰기, 읽기)
					file.setWritable(true);
					file.setReadable(true);
					
					ItemFileDomain itemFileDomain = ItemFileDomain.builder()
							.bkId(bkId)
							.mbId(mbId)
							.bkOriginalFileName(origFilename)
							.bkNewFileName(bkNewFileName) 
							.bkFilePath(targetPath.toString())
							.bkFileSize((int)itemFile.getSize())
							.build();
					
					if(pathName == "bookCoverImg") {
						itemFileMapper.itemCoverFileUpload(itemFileDomain);
						log.info("커퍼 파일 업로드 완료.");
					} else if(pathName == "bookImg") {
						itemFileMapper.itemFilesUpload(itemFileDomain);
						log.info("파일 업로드 완료.");
					} else {
						log.info("확인되지 않은 path 명입니다.");
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
}
