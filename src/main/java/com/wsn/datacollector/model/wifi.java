package com.wsn.datacollector.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by daaisailan on 2017/7/28.
 */
//
public class wifi {
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @Id
    @GeneratedValue
    Integer level;
    String bssid;
    String ssid;


}
