package com.wsn.datacollector.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by daaisailan on 2017/7/28.
 */
/*存储的主要是26个点的具体的值*/
@Entity
public class WifiLocation {
    @Id
    @GeneratedValue
    Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    String bssid;
    String ssid;

}
