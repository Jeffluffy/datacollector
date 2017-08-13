package com.wsn.datacollector.controller;

import com.wsn.datacollector.dao.FingerprintDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by daaisailan on 2017/8/1.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestControllerTest {

    @Autowired
    TestController testController;

    @Test
    public void calculateFanshu1() throws Exception {
        testController.calculateFanshu1();
    }

}