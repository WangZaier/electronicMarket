package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 用户处理Controller
 * @author wangzhizhi
 *
 */

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	/**
	 * 判断用户的登陆参数是否有效 ,这里的有效仅指登陆的时候数据库验证
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param , @PathVariable int type) {
		TaotaoResult result = userService.checkData(param, type);
		
		return result;
	}
	
	/**
	 * 这是注册的的方法 , 验证了数据的有效性 例如：是否为空,是否重复
	 * @param tbUser
	 * @return
	 */
	@RequestMapping(value="/user/register" , method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser tbUser) {
		TaotaoResult result = userService.register(tbUser);
		return result;
	}
	
	@RequestMapping(value="/user/login" , method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username  , String password , 
			HttpServletResponse response , HttpServletRequest request) {
		TaotaoResult result = userService.login(username, password);
		
		//吧token写入cookie
		CookieUtils.setCookie(request, response, TOKEN_KEY , result.getData().toString());
		return result;
	}
	
	
}

