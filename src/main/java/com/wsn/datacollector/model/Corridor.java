package com.wsn.datacollector.model;

import com.wsn.datacollector.enums.Area;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeng on 2017/7/22.
 */
public class Corridor {


    private Area area;

    private List<Position> positions;
    private List<Position> junctions;

    private int xValue;//走廊的长度，长边
    private int yValue;//走廊的宽度，窄边

    public Corridor(Area area,int xValue, int yValue,List<Position> junctions,List<Position> positions){

        this.area = area;
        this.xValue = xValue;
        this.yValue = yValue;
        this.junctions = junctions;
        this.positions = positions;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public int getxValue() {
        return xValue;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    public List<Position> getJunctions() {
        return junctions;
    }

    public void setJunctions(List<Position> junctions) {
        this.junctions = junctions;
    }

    @Override
    public String toString() {
        return "Corridor{" +
                "area=" + area +
                ", positions=" + positions +
                ", junctions=" + junctions +
                ", xValue=" + xValue +
                ", yValue=" + yValue +
                '}';
    }
}
