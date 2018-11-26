package com.example.demo.wx.trans;

import java.util.List;
import java.util.Map;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月14日 上午11:35:55
 * @ClassName 类名称
 * @Description 类描述
 */
public class TranslateResponseDto {

	private String tSpeakUrl;
	private List<WebDto> web;
	private Map<String, String> dict;
	private Map<String, String> webdict;
	private String query;
	private List<String> translation;
	private String errorCode;
	private Map<String, Object> basic;
	private String I;
	private String speakUrl;

	public String gettSpeakUrl() {
		return tSpeakUrl;
	}

	public void settSpeakUrl(String tSpeakUrl) {
		this.tSpeakUrl = tSpeakUrl;
	}

	public List<WebDto> getWeb() {
		return web;
	}

	public void setWeb(List<WebDto> web) {
		this.web = web;
	}

	public Map<String, String> getDict() {
		return dict;
	}

	public void setDict(Map<String, String> dict) {
		this.dict = dict;
	}

	public Map<String, String> getWebdict() {
		return webdict;
	}

	public void setWebdict(Map<String, String> webdict) {
		this.webdict = webdict;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getTranslation() {
		return translation;
	}

	public void setTranslation(List<String> translation) {
		this.translation = translation;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, Object> getBasic() {
		return basic;
	}

	public void setBasic(Map<String, Object> basic) {
		this.basic = basic;
	}

	public String getI() {
		return I;
	}

	public void setI(String i) {
		I = i;
	}

	public String getSpeakUrl() {
		return speakUrl;
	}

	public void setSpeakUrl(String speakUrl) {
		this.speakUrl = speakUrl;
	}

	@Override
	public String toString() {
		return "TranslateResponseDto [tSpeakUrl=" + tSpeakUrl + ", web=" + web + ", dict=" + dict + ", webdict="
				+ webdict + ", query=" + query + ", translation=" + translation + ", errorCode=" + errorCode
				+ ", basic=" + basic + ", I=" + I + ", speakUrl=" + speakUrl + "]";
	}

}
