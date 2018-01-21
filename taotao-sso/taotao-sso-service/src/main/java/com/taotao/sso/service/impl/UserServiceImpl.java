package com.taotao.sso.service.impl;

import java.net.PasswordAuthentication;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;

import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

@Controller
public class UserServiceImpl implements UserService{

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	//redis存储token的前缀
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	
	//redis中token的过期时间
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	/**
	 * 用户信息参数检查
	 */
	@Override
	public TaotaoResult checkData(String data, int type) {
		
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		//设置查询条件
		// 判断用户名是否可用
		if(type == 1) {
			criteria.andUsernameEqualTo(data);
		//判断手机号是否可用
		}else if (type ==2) {
			criteria.andPhoneEqualTo(data);
		//判断邮箱是否可用
		}else if(type == 3){
			criteria.andEmailEqualTo(data);
		}else {
			return TaotaoResult.build(400, "请求参数含有非法数据");
		}
		
		//进行查询
		List<TbUser> list = userMapper.selectByExample(example);
		System.out.println(list);
		if(list != null && list.size() > 0) {
			//查询到数据,说明不可用
			return 	TaotaoResult.ok(false);
		}
		
		//数据可用
		return TaotaoResult.ok(true);
	}
	
	/**
	 * 用户注册
	 */
	@Override
	public TaotaoResult register(TbUser tbUser) {
		//检验数据有效性
		//数据是否为空
		if( StringUtils.isBlank(tbUser.getUsername())) {
			// 用户名不能为空
			return TaotaoResult.build(400 , "用户名不能为空");
		}
		//判断用户名是否为空
		TaotaoResult taotaoResult = checkData(tbUser.getUsername(), 1);
		if(!(boolean) taotaoResult.getData() ) {
			return taotaoResult.build( 400 , "用户名重复");
		}
		
		//判断密码是否为空
		if( StringUtils.isBlank(tbUser.getPassword())) {
			return taotaoResult.build(400, "密码不能为空");
		}
		//手机号码和邮箱可以为空,但不能重复
		//判断手机号码有效性
		if(StringUtils.isNotBlank(tbUser.getPhone())) {
			taotaoResult = checkData(tbUser.getPhone(), 2);
			if(!(boolean)taotaoResult.getData()) {
				return taotaoResult.build(400 , "电话号码不能重复");
			}
		}
		//判断邮箱有效性
		if(StringUtils.isNotBlank(tbUser.getEmail())) {
			taotaoResult = checkData(tbUser.getEmail(), 3);
			if(!(boolean)taotaoResult.getData()) {
				return taotaoResult.build(400 , "email不能重复");
			}
		}
		
		
		//补全pojo
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//密码md5加密
		String md5Password = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5Password);
		//插入数据
		userMapper.insert(tbUser);
		return TaotaoResult.ok();
	
	
	}
	
	
	/**
	 * 用户登陆
	 */
	@Override
	public TaotaoResult login(String username, String password) {
		//检查用户名和密码是否正确
		//创建查询
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//添加查询条件
		criteria.andUsernameEqualTo(username);
		//进行查询
		List<TbUser> list = userMapper.selectByExample(example);
		
		if(list == null || list.size() == 0) {
			//返回登陆失败
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		//获取当前用户
		TbUser user = list.get(0);
		//密码进行md5加密后进行校验
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		
		//生成token,uuid
		String token = UUID.randomUUID().toString();
		//把信息存储到redis , key就是token ,value是用x户信息
		//清空密码
		user.setPassword(null);
		jedisClient.set(USER_SESSION+ ":" + token, JsonUtils.objectToJson(user));
		//设置key过期时间
		jedisClient.expire(USER_SESSION + ":" + token , SESSION_EXPIRE);
		//返回登陆成功,并需要吧token返回,还需要保存cookie
		return TaotaoResult.ok(token);
		}

	
	@Override
	public TaotaoResult getUserByToken(String token) {
		
		//用token获获取账户信息
		String json = jedisClient.get(USER_SESSION + ":" + token);
		
		if(StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "用户登录已过期");
		}
		//查询完毕重置redis过期时间
		jedisClient.expire(USER_SESSION + ":" + token, 3000);
		
		 //把json转换为用户信息
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);

		return TaotaoResult.ok(user );
	}
	
	
	
}
