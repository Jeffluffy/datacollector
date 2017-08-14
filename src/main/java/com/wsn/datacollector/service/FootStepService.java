package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FootStepDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lufen on 2017/8/13.
 */
@Service
public class FootStepService {

    @Autowired
    private FootStepDao footStepDao;

}
