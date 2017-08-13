package com.wsn.datacollector.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lufen on 2017/8/13.
 */
@Entity
@Table( name = "footStep")
public class FootStep {

    @Id
    @GeneratedValue
    private Long id ;

    private Long path; //所属路径id

    private Double steps; //路径的平均步长，对于每条路径，采样点之间的步长用路径总步长除以( 采样点个数-1 )，对于有n个采样点的路径，应该有（ n-1 )条边

}
