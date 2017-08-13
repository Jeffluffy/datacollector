package com.wsn.datacollector.dao;

import com.wsn.datacollector.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by daaisailan on 2017/7/27.
 */
public interface UserDao extends JpaRepository<User,Long> {
}
