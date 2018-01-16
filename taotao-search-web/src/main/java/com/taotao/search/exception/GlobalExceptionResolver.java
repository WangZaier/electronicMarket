package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


/**
 * 全局一场处理器
 * @author wangzhizhi
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{
	
	public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		logger.info("进入全局处理器");
		logger.debug("测试的handler类型" + handler.getClass());
		//控制台打印异常
		ex.printStackTrace();
		//向日志文件中写入一场
		logger.error("系统发生异常", ex);
		//写邮件
		//发短信
		//展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message","您的电脑有问题");
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}
}
