package com.wsn.datacollector.model;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.utils.Utils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by lufeng on 2017/7/22. `
 */
@Entity
public class Position {

    @Id
    @GeneratedValue
    int id; //坐标唯一id

    int x;
    int y;
    Area area;

    public Position(int x, int y, Area area) {
        id = Utils.generatePositionId();
        this.x = x;
        this.y = y;
        this.area = area;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", area=" + area +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
