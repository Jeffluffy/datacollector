package com.wsn.datacollector.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by daaisailan on 2017/7/28.
 */
//在找26个mac的时候使用的model
public class wifiHot {
    @Id
    @GeneratedValue
    Integer counter;
    String bssid;
    Integer level;
    String ssid;


    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

}
