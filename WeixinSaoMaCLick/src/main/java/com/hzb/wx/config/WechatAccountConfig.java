package com.hzb.wx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月15日 上午9:21:16
 * @ClassName 类名称
 * @Description 类描述获取配置信息
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
	private String appid;
	private String secret;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
