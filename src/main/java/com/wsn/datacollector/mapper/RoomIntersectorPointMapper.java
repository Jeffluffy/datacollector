package com.wsn.datacollector.mapper;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.model.Position;
import javafx.geometry.Pos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daaisailan on 2017/7/22.
 */
public class RoomIntersectorPointMapper {

    private static Map<String,Position> roomIntersectorPointMapper = new HashMap();

    public static Position getRoomIntersectorPosition(Area area) {
        return roomIntersectorPointMapper.get(area.toString());
    }

    public static boolean putRoomIntersectorPoint(Position position){
        String key = ""+position.getArea().toString();
        Position echo = roomIntersectorPointMapper.put(key,position);
        if(null != echo){
            throw new RuntimeException("插入房间类型"+position.getArea().toString()+"的连接点失败！已经存在连接点:"+ echo.toString()+"\n。欲插入的点为："+position.toString());
        }
        return true;
    }
}
