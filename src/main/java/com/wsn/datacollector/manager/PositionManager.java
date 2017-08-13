package com.wsn.datacollector.manager;

import com.wsn.datacollector.model.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daaisailan on 2017/7/23.
 */
public class PositionManager {
    //所有的点的管理
    private static Map<String,Position> positionMap = new HashMap();

    public static Position getJunction(int id) {
        Position position1 = positionMap.get(""+id);
        if(position1 == null) throw new RuntimeException("不存在该点!\n    id:"+id);
        else return  position1;
    }

    /**
     * 理论上不应该将同一个点添加到map两次。
     * @param position
     * @return
     */
    public static boolean putPosition(Position position) {
        String key = "" + position.getId();
        if(positionMap.put(key,position) != null){
            throw new RuntimeException("添加位置点失败，该key已经存在:"+ key+";要插入的点为：" +position.toString());
        }
        return true;
    }
}
