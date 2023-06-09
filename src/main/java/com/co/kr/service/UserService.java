package com.co.kr.service;

import java.util.List;
import java.util.Map;

import com.co.kr.domain.UserDomain;


public interface UserService {
	
	 // selectId
    public UserDomain mbSelectList(Map<String, String> map);
    
    // selectAll
    public List<UserDomain> mbAllList(Map<String, Integer> map);
    
    // selectAll Conut
    public int mbGetAll();
    
    //신규
    public void mbCreate(UserDomain userDomain);
    
    //getMbIdCheck
    public UserDomain mbGetId(Map<String, String> map);
    
    //mbDuplicationCheck
    public int mbDuplicationCheck(Map<String, String> map);
    
    //mbLoginCheck
    public int mbLoginCheck(Map<String, String> map);
    
    //update Ip
    public void mbUpdateIp(Map<String, String> map);
    
    //update
    public void mbUpdate(UserDomain userDomain); 
    
    //delete 
    public void mbRemove(Map<String, String> map); 
    
}