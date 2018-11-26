package com.example.demo.wx.accesstoken;
/**
* @author HouZhiBo
* @version 创建时间：2018年11月13日 下午5:42:05
* @ClassName 类名称
* @Description 类描述
*/
public class AccessToken {
    private String tokenName; //获取到的凭证
    private int expireSecond;    //凭证有效时间  单位:秒

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getExpireSecond() {
        return expireSecond;
    }

    public void setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
    }
}
