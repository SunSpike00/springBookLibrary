package com.co.kr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.co.kr.domain.UserDomain;
import com.co.kr.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserMapper userMapper;

	@Override
	public void mbCreate(UserDomain userDomain) {
		log.info("아이디 {} 회원 정보 데이터베이스에 저장", userDomain.getMbId());
		userMapper.mbCreate(userDomain);
	}

	@Override
	public UserDomain mbSelectList(Map<String, String> map) {
		log.info("관리자가 회원 번호 {} 정보 데이터베이스에서 불러옴", map.get("mbSeq"));
		return userMapper.mbSelectList(map);
	}

	@Override
	public List<UserDomain> mbAllList(Map<String, Integer> map) {
		log.info("관리자가 전체 회원 목록을 데이터베이스에서 불러옴");
		return userMapper.mbAllList(map);
	}

	@Override
	public void mbUpdateIp(Map<String, String> map) {
		log.info("회원이 접속한 지역 ip 값으로 데이터베이스에 업데이트함");
		userMapper.mbUpdateIp(map);
	}
	
	@Override
	public void mbUpdate(UserDomain userDomain) {
		log.info("아이디 {}가 변경한 정보를 데이터베이스에 업데이트함", userDomain.getMbId());
		userMapper.mbUpdate(userDomain);
	}
	
	@Override
	public void mbRemove(Map<String, String> map) {
		log.info("회원 번호 {}의 정보를 데이터베이스에서 제거함", map.get("mbSeq"));
		userMapper.mbRemove(map);
	}

	@Override
	public UserDomain mbGetId(Map<String, String> map) {
		log.info("아이디 {}의 회원 정보를 데이터베이스에서 불러옴", map.get("mbId"));
		return userMapper.mbGetId(map);
	}

	@Override
	public int mbDuplicationCheck(Map<String, String> map) {
		// TODO Auto-generated method stub
		log.info("입력한 아이디가 중복인지 확인");
		return userMapper.mbDuplicationCheck(map);
	}

	@Override
    public int mbLoginCheck(Map<String, String> map) {
		log.info("입력한 아이디와 비밀번호가 일치하는지 확인");
		return userMapper.mbLoginCheck(map);
    }
	
	@Override
	public int mbGetAll() {
		// TODO Auto-generated method stub
		log.info("등록된 회원이 있는지 데이터베이스에서 확인");
		return userMapper.mbGetAll();
	}

}