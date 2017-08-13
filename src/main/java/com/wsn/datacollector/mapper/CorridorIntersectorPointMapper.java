package com.wsn.datacollector.mapper;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.model.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * 走廊1到房间或者另一个走廊2的连接点1
 * Created by daaisailan on 2017/7/22.
 */
public class CorridorIntersectorPointMapper {
    private static Map<String,Position> corridorIntersectorPointMapper = new HashMap();

    public static Position getCorridorIntersectorPosition(Area fromCorridor,Area toArea) {
        Position position = corridorIntersectorPointMapper.get(fromCorridor.toString()+toArea.toString());
        return position;
    }

    public static boolean putCorridorIntersectorPoint(Position position,Area toArea){
        String key = position.getArea().toString() + toArea.toString();
        Position echo = corridorIntersectorPointMapper.put(key,position);
        if(null != echo){
            throw new RuntimeException("插入走廊类型"+position.getArea().toString()+"的连接点失败！已经存在连接点:"+ echo.toString()+"。     \n欲插入的点为："+position.toString()+"连接的区域："+toArea);
        }
        return true;
    }
}
