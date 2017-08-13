package com.wsn.datacollector.utils;

import org.hibernate.annotations.Synchronize;

import java.util.Date;

/**
 * Created by daaisailan on 2017/7/29.
 */
public class GetPathNumber {
     public static synchronized long getPathNumber(){
          return new Date().getTime();
     }

}
