package com.wsn.datacollector.utils;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.mapper.CorridorIntersectorPointMapper;
import com.wsn.datacollector.mapper.JunctionMapper;
import com.wsn.datacollector.mapper.RoomIntersectorPointMapper;
import com.wsn.datacollector.model.Fingerprint;
import com.wsn.datacollector.model.FingerprintDecorator;
import com.wsn.datacollector.model.Position;

import java.io.File;
import java.io.IOException;

/**
 * Created by daaisailan on 2017/7/22.
 */
public class Utils {

    private static int positionIdCount = 0;

    //生成全局的位置坐标的id
    public static int generatePositionId(){
        synchronized (Utils.class){
            return positionIdCount++;
        }
    }

    public static Area getAreaType(String type){
        Area areaType = Area.valueOf(type);
        return areaType;
    }

    public static double calculateDistance(Position x, Position y){

        Area xArea = x.getArea();
        Area yArea = y.getArea();
        double distance=0;

        if(xArea.toString().equals(yArea.toString())){
            distance += calculateDistanceInSameArea(x,y);
        }else{

            Position xJunction,yJunction;
            Position junction1,junction2;
             //下面分为几种情况来求
            if(xArea.isRoom()&&yArea.isRoom()){
                xJunction = RoomIntersectorPointMapper.getRoomIntersectorPosition(xArea);//房间只有一个门，映射唯一
                yJunction = RoomIntersectorPointMapper.getRoomIntersectorPosition(yArea);
                junction1 = JunctionMapper.getJunction(xJunction);
                junction2 = JunctionMapper.getJunction(yJunction);
            }else if(xArea.isRoom()&&yArea.isCorridor()){
                xJunction = RoomIntersectorPointMapper.getRoomIntersectorPosition(xArea);//房间只有一个门，映射唯一
                yJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(yArea,xArea);//如果走廊和房间之间还隔着一个走廊的话则会返回null
                if(yJunction == null) {
                    if(yArea.toString().equals(Area.Corridor1.toString())){
                        yJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(Area.Corridor1,Area.Corridor2);
                    }else{
                        yJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(Area.Corridor2,Area.Corridor1);
                    }
                }
                junction1 = JunctionMapper.getJunction(xJunction);
                junction2 = JunctionMapper.getJunction(yJunction,xJunction.getArea());
                if(junction2==null){
                    if(yJunction.getArea().toString().equals(Area.Corridor1.toString())){
                        junction2 = JunctionMapper.getJunction(yJunction,Area.Corridor2);
                    }else{
                        junction2 = JunctionMapper.getJunction(yJunction,Area.Corridor1);
                    }
                    distance += 1;
                }

            }else if(xArea.isCorridor()&&yArea.isRoom()){
                xJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(xArea,yArea);//如果走廊和房间之间还隔着一个走廊的话则会返回null
                if(xJunction == null) {
                    if(xArea.toString().equals(Area.Corridor1.toString())){
                        xJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(Area.Corridor1,Area.Corridor2);
                    }else{
                        xJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(Area.Corridor2,Area.Corridor1);
                    }
                    distance += 1;
                }
                yJunction = RoomIntersectorPointMapper.getRoomIntersectorPosition(yArea);
                junction1 = JunctionMapper.getJunction(xJunction,yJunction.getArea());
                if(junction1==null){
                    if(xJunction.getArea().toString().equals(Area.Corridor1.toString())){
                        junction1 = JunctionMapper.getJunction(xJunction,Area.Corridor2);
                    }else{
                        junction1 = JunctionMapper.getJunction(xJunction,Area.Corridor1);
                    }
                }
                junction2 = JunctionMapper.getJunction(yJunction);
            }else{
                xJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(xArea,yArea);
                yJunction = CorridorIntersectorPointMapper.getCorridorIntersectorPosition(yArea,xArea);
                if(Area.Corridor1.equals(xArea)){
                    junction1 = JunctionMapper.getJunction(xJunction,Area.Corridor2);
                }else{
                    junction1 = JunctionMapper.getJunction(xJunction,Area.Corridor1);
                }
                if(Area.Corridor1.equals(yArea)){
                    junction2 = JunctionMapper.getJunction(yJunction,Area.Corridor2);
                }else{
                    junction2 = JunctionMapper.getJunction(yJunction,Area.Corridor1);
                }
            }

            distance += Utils.calculateDistanceInSameArea(x,xJunction)+Utils.calculateDistanceInSameArea(y,yJunction);
            if(junction1.getArea().toString().equals(yJunction.getArea().toString())){//两个区域相邻
                distance += 1;
            }else {//两个区域不相邻,之间必然是隔了走廊一个或者两个
                if(junction1 == junction2){//两个门正好相对同一个走廊点，做特殊处理，不然计算结果为0
                    distance += 2;
                }else{
                    distance += calculateDistance(junction1,junction2);//递归计算
                }
            }
        }
        return distance;
    }

    private static double calculateDistanceInSameArea(Position x, Position y){

        int x1 = x.getX(),x2 = y.getX(),y1 = x.getY(),y2 = y.getY();

        int absX = Math.abs(x1-x2);
        int absY = Math.abs(y1-y2);

        if(absX==0) return absY;
        if(absY==0) return absX;
        return Math.sqrt(absX*absX+absY*absY);

    }

    public static int[] parseDoorCoordinate(String string){
        if (string == null){
            throw new RuntimeException("传入的坐标点为空!");
        }
        if(string.length()<5){
            throw new RuntimeException("传入的坐标点格式不合法:"+string);
        }
        string = string.substring(1,string.length()-1);
        String[] result = string.split(",");
        int[] resultInt = new int[2];
        resultInt[0] = Integer.parseInt(result[0]);
        resultInt[1] = Integer.parseInt(result[1]);
        return resultInt;
    }

    public static String[][] parseCorridorCordinate(String string){
        if(string == null){
            throw new RuntimeException("传入的坐标点为空!");
        }
        if(string.length()<=7){
            throw new RuntimeException("传入的坐标点格式不合法:"+string);
        }
        String[] strings = string.trim().split("&");
        String [][] result = new String[strings.length][3];
        for(int i=0;i<strings.length;++i){
            if(strings[i].length() <=7){
                throw new RuntimeException("传入的坐标点格式不合法:"+strings[i]);
            }
            result[i] = strings[i].substring(1,strings[i].length()-1).split(",");
        }
        return result;
    }

    //矩阵构造
    //1 从数据库中读取wifi指纹，按照path 分类,得到pathList<wifilist<wifi>>,
    //2 取出pathList中的一个wifiList，
    //3 循环将wifiList中的每个点之间的步数值计算出来，放到矩阵中：matrix ，matrix的下标就是该点在wifiList中的位置
    //4 如果放入的不是第一条路径

    /**
     * 计算 1范
     * @param f1
     * @param f2
     * @return
     */
    public static int calcNorm1(FingerprintDecorator f1,FingerprintDecorator f2){

        int[] fingerPrintArr1 = f1.getValueArr();
        int[] fingerPrintArr2 = f1.getValueArr();
        int length = fingerPrintArr1.length;
        int norm1 = 0;

        for(int i=0; i<length; i++){
            norm1 += Math.abs(fingerPrintArr1[i] -fingerPrintArr2[i]);
        }
        return norm1;
    }



    //测试
    public static void main(String[] args) throws IOException {
//        System.out.println(getAreaType("Corridor1").toString());
//        System.out.println(Area.Corridor2.isCorridor());
//        System.out.println(Area.Corridor1.equals(Area.Corridor2));
//
//        //测试数组初始值是否为0，结果：是
//        double[] matirx = new double[2];
//        for(double d:matirx){
//            System.out.println(d == 0);
//        }
//
//       File file = new File("output.txt");
//        if (!file.exists()){
//            file.createNewFile();
//        }
    }

}
