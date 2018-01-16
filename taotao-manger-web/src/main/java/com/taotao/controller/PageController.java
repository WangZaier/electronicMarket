package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 页面显示Controller
 * @author wangzhizhi
 *
 */
@Controller
public class PageController {
	
	/**
	 * 页面初始化会发送一个ajax,我们返回index逻辑试图
	 * @return
	 */
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	/**
	 * 这是我们url页面跳转
	 * 比如index.跳转到item-add,我们返回逻辑试图,又比如我们需要打文件上传页面.也是发送请求我们返回逻辑试图，然后在页面展示
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
