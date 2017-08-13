package com.wsn.datacollector.manager;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.model.Corridor;
import com.wsn.datacollector.model.Position;
import com.wsn.datacollector.model.Room;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daaisailan on 2017/7/23.
 */
public class AreaManager {
    //所有的点的管理
    private static Map<String,Object> areaMap = new HashMap();

    public static boolean putRoom(Room room){
        String key = room.getArea().toString();
        if(areaMap.put(key,room) != null){
            throw new RuntimeException("添加房间失败，该key已经存在:"+ key+";要插入的房间为：" +room.toString());
        }
        return true;
    }
    public static boolean putCorridor(Corridor corridor){
        String key = corridor.getArea().toString();
        if(areaMap.put(key,corridor) != null){
            throw new RuntimeException("添加走廊失败，该key已经存在:"+ key+";要插入的走廊为：" +corridor.toString());
        }
        return true;
    }


    public static Room getRoomByType(Area areaType) {
        Room room=(Room) areaMap.get(""+areaType.toString());
        if(room == null) throw new RuntimeException("不存在该房间:"+areaType.toString());
        else return  room;
    }

    public static Corridor getCorridorByType(Area areaType) {
        Corridor corridor=(Corridor) areaMap.get(""+areaType.toString());
        if(corridor == null) throw new RuntimeException("不存在该走廊:"+areaType.toString());
        else return corridor;
    }
}
