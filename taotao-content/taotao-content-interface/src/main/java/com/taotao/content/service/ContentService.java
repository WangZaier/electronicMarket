package com.taotao.content.service;

import java.util.List;

import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	
	TaotaoResult addContent(TbContent content);
	
	List<TbContent> getContentByCid(long cid);
}
