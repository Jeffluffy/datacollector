package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FingerprintDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by lufen on 2017/8/6.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FingerprintImplServiceTest {

    @Autowired
    FingerprintImplService fingerprintImplService;

    @Test
    public void getMatrix() throws Exception {
        fingerprintImplService.getMatrix();
    }

}