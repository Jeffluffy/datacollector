package com.wsn.datacollector.dao;


import com.wsn.datacollector.model.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by daaisailan on 2017/7/28.
 */
public interface FingerprintDao extends JpaRepository<Fingerprint,Integer> {
}
