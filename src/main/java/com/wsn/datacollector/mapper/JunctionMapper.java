package com.wsn.datacollector.mapper;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.model.Corridor;
import com.wsn.datacollector.model.Position;
import com.wsn.datacollector.model.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daaisailan on 2017/7/23.
 */
public class JunctionMapper {
    //连接点对之间的映射
    private static Map<String,Position> junctionMap = new HashMap();

    public static Position getJunction(Position roomPosition) {

        String key = null;
        Area type = roomPosition.getArea();
        if(type.isCorridor()){
            key = roomPosition.getId()+""+type.toString();
        }

        Position position1 = junctionMap.get(""+roomPosition.getId());
        if(position1 == null) throw new RuntimeException("没有获取到点的映射点!请检查连接点映射是否正确!\n    "+roomPosition.toString());
        else return  position1;
    }
    public static Position getJunction(Position corridorPosition,Area targetArea) {
//        System.out.println(corridorPosition+"================"+targetArea.toString());
        String key = corridorPosition.getId()+targetArea.toString();
        Position position1 = junctionMap.get(key);
        return position1;
    }

    public static boolean initJunctionMap(List<Room> rooms, List<Corridor> corridors){
//        for(Room room : rooms){
//            Position door = room.getDoorPosition();
//            Position corridorPosition = null;
//            int minDistance = Integer.MAX_VALUE;
//            for(Corridor corridor:corridors){
//                for(Position position:corridor.getJunctions()){
//                    int distance = Math.abs(position.getX()-door.getX()) + Math.abs(position.getY()-door.getY());
//                    if(minDistance>distance){
//                        minDistance = distance;
//                        corridorPosition = position;
//                    }
//                }
//            }
//            putJunction(door,corridorPosition);
//        }

        return true;
    }

    public static boolean initJunctionMap(){

        List<Area> roomTypes = Area.getAllRoomTypes();
        List<Area> corridorTypes = Area.getAllCorridorTypes();
        for(Area roomType : roomTypes){//房间与走廊之间的连接点的映射
            Position roomIntersectorPoint = RoomIntersectorPointMapper.getRoomIntersectorPosition(roomType);
            for(Area corridorType:corridorTypes){
                Position corridorIntersectorPosition = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(corridorType,roomType);
                if (corridorIntersectorPosition!=null){
                    putJunction(roomIntersectorPoint,corridorIntersectorPosition);
                }
            }
        }

        //走廊与走廊之间的连接点的映射
        Position position1 = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(Area.Corridor1,Area.Corridor2);
        Position position2 = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(Area.Corridor2,Area.Corridor1);
        System.out.println("走廊1-》走廊2的连接点："+position1);
        System.out.println("走廊2-》走廊1的连接点："+position2);
        putJunction(position1,position2);

        return true;
    }

    public static boolean putJunction(Position position1,Position position2) {
        String key1 = null;
        if(position1.getArea().isCorridor()){
            key1 = position1.getId()+""+position2.getArea().toString();
        }else{
            key1 = position1.getId()+"";
        }

        String key2 = null;
        if(position2.getArea().isCorridor()){
            key2 = position2.getId()+""+position1.getArea().toString();
        }else{
            key2 = position2.getId()+"";
        }

        if(null !=junctionMap.put(key1,position2)){
            throw new RuntimeException("添加映射点失败，该key已经存在:"+ key1+";要插入的点为：" +position2.toString()+"键点为："+position1.toString());
        }
        if(null !=junctionMap.put(key2,position1)){
            throw new RuntimeException("添加映射点失败，该key已经存在:"+ key2+";要插入的点为：" +position1.toString() +"键点为："+position2.toString());
        }
        System.out.println("插入映射点对："+position1+"《----》"+position2);

        return true;
    }
}
