package com.hzb.wx.controller;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hzb.wx.contas.CookieConstant;
import com.hzb.wx.util.CookieUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月15日 上午9:19:37
 * @ClassName 类名称
 * @Description 类描述微信网页授权
 */

@Controller
@RequestMapping("/wechat")
public class WechatController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WxMpService wxMpService;

	/**
	 * @Description: 微信授权
	 */
	@GetMapping("/authorize")
	public String authorize(@RequestParam("returnUrl") String returnUrl) {
		String url = "http://hzb.nat300.top/wechat/userInfo";
		String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO,
				URLEncoder.encode(returnUrl));
		logger.info("【微信网页授权】获取code,redirectUrl={}", redirectUrl);
		return "redirect:" + redirectUrl;
	}

	/**
	 * @Description: 微信授权回调用户信息
	 */
	@GetMapping("/userInfo")
	public String userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl) {
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
		try {
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			logger.info("【微信网页授权】获取AccessToken,AccessToken={}", wxMpOAuth2AccessToken);
			WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			logger.info("【微信网页授权获】获取用户信息：{}", wxMpUser);
		} catch (WxErrorException e) {
			logger.error("【微信网页授权】{}", e);
		}
		String openId = wxMpOAuth2AccessToken.getOpenId();
		return "redirect:" + returnUrl + "?openid=" + openId;
	}

	@GetMapping("login")
	public String login(@RequestParam("openid") String openid, HttpServletResponse response, Map<String, Object> map) {
		// 1,openid 去和数据库里面的数据匹配验证 这里只是演示就删掉没有了

		map.put("openid", openid);
		// 2,设置token至cookie 并设置过期时间
		CookieUtil.set(response, CookieConstant.TOKEN, "123", CookieConstant.EXPIRE);
		return "/index";
	}
}
