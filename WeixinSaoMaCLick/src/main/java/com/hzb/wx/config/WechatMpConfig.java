package com.hzb.wx.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月15日 上午9:34:23
 * @ClassName 类名称
 * @Description 类描述微信参数配置
 */
@Component
public class WechatMpConfig {
	@Autowired
	private WechatAccountConfig wechatAccountConfig;

	@Bean
	public WxMpService wxMpService() {
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
		return wxMpService;
	}

	@Bean
	public WxMpConfigStorage wxMpConfigStorage() {
		WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
		wxMpInMemoryConfigStorage.setAppId(wechatAccountConfig.getAppid());
		wxMpInMemoryConfigStorage.setSecret(wechatAccountConfig.getSecret());
		return wxMpInMemoryConfigStorage;
	}
}
