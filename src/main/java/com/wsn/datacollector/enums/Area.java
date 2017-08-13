package com.wsn.datacollector.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeng on 2017/7/22.
 */
public enum Area {

    Corridor1("Corridor1"),
    Corridor2("Corridor2"),

    B401("B401"),
    B402("B402"),
    B403("B403"),
    B404("B404"),
    B405("B405"),
    B406("B406"),
    B407("B407"),
    B408("B408"),
    B409("B409"),
    B410("B410"),
    B412("B412");

    private String value;
    private Area(String value){
        this.value = value;
    }

    public boolean isCorridor(){
        return this.equals(Corridor1)||this.equals(Corridor2)?true:false;
    }
    public boolean isRoom(){
        return !isCorridor();
    }
    public static List<Area> getAllRoomTypes(){
        List<Area> areas = new ArrayList<>();
        areas.add(B401);
        areas.add(B402);
        areas.add(B403);
        areas.add(B404);
        areas.add(B405);
        areas.add(B406);
        areas.add(B407);
        areas.add(B408);
        areas.add(B409);
        areas.add(B410);
        areas.add(B412);
        return areas;
    }
    public static List<Area> getAllCorridorTypes(){
        List<Area> areas = new ArrayList<>();
        areas.add(Corridor1);
        areas.add(Corridor2);
        return areas;
    }
}
