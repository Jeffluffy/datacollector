package com.wsn.datacollector.model;

import com.wsn.datacollector.enums.Area;

import java.util.List;

/**
 * Created by lufeng on 2017/7/22.
 */
public class Room {

    private Area area;

    private List<Position> positions;

    private int xValue;//房间的宽度
    private int yValue;//房间的深度

    private Position doorPosition;//默认只有一个门
    private Position corridorPosition;//对应的走廊的点

    public Room(Area area, int xValue, int yValue,Position doorPosition, List<Position> positions) {
        this.area = area;
        this.xValue = xValue;
        this.yValue = yValue;
        this.doorPosition = doorPosition;
        this.positions = positions;
    }

    public Position getCorridorPosition() {
        return corridorPosition;
    }

    public void setCorridorPosition(Position corridorPosition) {
        this.corridorPosition = corridorPosition;
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

    public Position getDoorPosition() {
        return doorPosition;
    }

    public void setDoorPosition(Position doorPosition) {
        this.doorPosition = doorPosition;
    }

    @Override
    public String toString() {
        return "Room{" +
                "area=" + area +
                ", positions=" + positions +
                ", xValue=" + xValue +
                ", yValue=" + yValue +
                ", doorPosition=" + doorPosition +
                ", corridorPosition=" + corridorPosition +
                '}';
    }
}
