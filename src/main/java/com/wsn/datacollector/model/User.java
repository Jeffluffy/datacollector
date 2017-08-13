package com.wsn.datacollector.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by daaisailan on 2017/7/27.
 */
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue
    long id;
    String nickname;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
