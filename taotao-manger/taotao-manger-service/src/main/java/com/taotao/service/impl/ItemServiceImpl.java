package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cmmon.pojo.EasyUIDataGridResult;
import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

//商品管理服务
@Service
public class ItemServiceImpl implements ItemService {

	private static final Class TbItem = null;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;

	// 注入 redis 客户端
	@Autowired
	private JedisClient jedisClient;

	// 注入JMS模版 ，用来建立发送消息链接
	@Autowired
	private JmsTemplate jmsTemplate;

	// 注入 我们的消息发送模式 ， 默认采用名称注入,如果没有那么会使用类型注入
	@Resource(name = "itemAddTopic")
	private Destination destination;

	// 商品redis缓存前缀
	@Value("${ITEM_INFO}")
	private String ITEM_INFO;

	// 缓存过期时间
	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;

	/**
	 * 单条记录查询 , 做了redis缓存
	 */
	@Override
	public TbItem getItemId(long itemId) {
		// 查询数据库之前先查询缓存
		try {
			// 通过id查询Json串
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
			// 如果不为空,将Json转换为Pojo
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				// 返回数据
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有,查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			// 把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			// 设置过期时间,提高缓存利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * 列表查询
	 * 
	 * @page 需要查询的页码
	 * @rows 每页的行号
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {

		// 设置查询
		PageHelper.startPage(page, rows);

		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);

		// 取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		return result;
	}

	/**
	 * 这是添加商品的方法
	 * 
	 * @item 直接传入参数 TbItem 我们直接存储到数据库
	 * @desc 这个参数是 ItemDesc 里的备注,也就是富文本框的参数 我们存储item的时候会手工存储 itemdesc 手动做好他们的关联关系
	 */
	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		// 生成商品id
		long itemId = IDUtils.genItemId();
		// 补全item的属性
		item.setId(itemId);
		// 商品状态,1-正常,2-下架,3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(item);
		// 创建一个商品米描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全pojo的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDesc.setCreated(new Date());
		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);

		/**
		 * 向activeMq发送商品添加消息 也就是说我们在添加商品的时候我们把这个id发送到队列里 然后让消费者接收通过id把这条信息添加到solr上,
		 * 而不需要我们从manger-web调用的search-service的手动上传,而是自动的
		 * 但是这里一般情况是在solr库手动上传过一次了以后,因为这里向solr添加的都是后面添加的
		 */
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 发送商品ID
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});

		// 返回结果
		return TaotaoResult.ok();
	}
	
	/**
	 * 删除多条列表数据的方法 传入List集合 就是id的集合
	 */
	@Override
	public void deleteItem(List<Long> ids) {

		// 创建两个个存放条件的类 因为我们需要批量删除两张表中的数据
		TbItemExample itemExample = new TbItemExample();
		TbItemDescExample descExample = new TbItemDescExample();
		// 创建条件
		com.taotao.pojo.TbItemExample.Criteria itemCriteria = itemExample.createCriteria();
		com.taotao.pojo.TbItemDescExample.Criteria descCriteria = descExample.createCriteria();
		// 添加条件
		itemCriteria.andIdIn(ids);
		descCriteria.andItemIdIn(ids);

		// 根据条件删除内容
		itemMapper.deleteByExample(itemExample);
		itemDescMapper.deleteByExample(descExample);

	}

	/**
	 * 删除多条列表数据的方法 传入List集合 就是id的集合
	 */
	@Override
	public void deleteOnceItem(Long id) {
		// 删除两张表中的数据
		itemDescMapper.deleteByPrimaryKey(id);
		itemMapper.deleteByPrimaryKey(id);

	}

	/**
	 * 这是我们查询商品详情的方法 就是ItemDesc
	 */
	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// 查询数据库之前先查询缓存
		try {
			// 通过id查询Json串
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
			// 如果不为空,将Json转换为Pojo
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				// 返回数据
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有,查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			// 把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			// 设置过期时间,提高缓存利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
