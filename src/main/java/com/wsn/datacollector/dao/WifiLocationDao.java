package com.wsn.datacollector.dao;

import com.wsn.datacollector.model.WifiLocation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by daaisailan on 2017/7/28.
 */
public interface WifiLocationDao extends JpaRepository<WifiLocation, Integer> {
}
