package com.taotao.sso.service;

import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
	
	/**
	 * 参数检查
	 * @param data
	 * @param type
	 * @return
	 */
	TaotaoResult checkData(String data , int type);
	
	/**
	 * 用户注册方法
	 * @param tbUser 用户表单
	 * @return
	 */
	TaotaoResult register(TbUser tbUser);
	
	/**
	 * 用户登陆方法
	 * @param username
	 * @param password
	 * @return
	 */
	TaotaoResult login(String username , String password);
	
	TaotaoResult getUserByToken(String token);

}
