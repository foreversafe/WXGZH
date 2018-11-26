package com.example.demo.wx.trans;

import java.util.List;

/**
 * @author HouZhiBo
 * @version 创建时间：2018年11月14日 下午12:45:20
 * @ClassName 类名称
 * @Description 类描述
 */
public class WebDto {

	private String key;
	private List<String> value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "WebDto [key=" + key + ", value=" + value + "]";
	}

}
