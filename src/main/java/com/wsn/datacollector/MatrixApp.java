package com.wsn.datacollector;

import com.wsn.datacollector.enums.Area;
import com.wsn.datacollector.mapper.CorridorIntersectorPointMapper;
import com.wsn.datacollector.mapper.JunctionMapper;
import com.wsn.datacollector.mapper.RoomIntersectorPointMapper;
import com.wsn.datacollector.model.Corridor;
import com.wsn.datacollector.model.Position;
import com.wsn.datacollector.model.Room;
import com.wsn.datacollector.manager.AreaManager;
import com.wsn.datacollector.manager.PositionManager;
import com.wsn.datacollector.results.PositionMatrix;
import com.wsn.datacollector.utils.Utils;
import mdsj.MDSJ;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daaisailan on 2017/7/23.
 */
//计算距离矩阵入口程序
public class MatrixApp {

    public static void main(String args[]) throws IOException {

        boolean ret = true;
        File file = new File("input2.txt");
        FileInputStream fInStream = new FileInputStream(file);
        InputStreamReader inReader = new InputStreamReader(fInStream);
        BufferedReader reader = new BufferedReader(inReader);

        List<Room> rooms = new ArrayList<>();
        List<Corridor> corridors = new ArrayList<>();
        List<Position> allPositions = new ArrayList<>();

        int allPositionsNum = 0;

        int spaceNumber = Integer.parseInt(reader.readLine());//房间和走廊总数量
        System.out.println("走廊和房间数量:"+spaceNumber);

        long begin  = System.currentTimeMillis();
        while (--spaceNumber >= 0) {
            String line = reader.readLine();
            if(line==null||"".equals(line)) continue;
            String[] results = line.trim().split("\\s+");//多个空格隔开
            if (results.length != 4) {
                throw new RuntimeException("输入数据不合法：" + line);
            }

            int horizon = Integer.parseInt(results[0]);
            int vertical = Integer.parseInt(results[1]);
            allPositionsNum += horizon*vertical;

            Area areaType = Utils.getAreaType(results[2]);
            if(areaType.isRoom()){
                int[] doorCoordinate = Utils.parseDoorCoordinate(results[3]);
                Position door = null;
                List<Position> roomPositions = new ArrayList<>(vertical*horizon);
                for(int i=0;i<horizon;++i){
                    for(int j=0;j<vertical;++j){//初始化一个区域的点
                        Position position = new Position(i,j,areaType);
                        allPositions.add(position);
                        roomPositions.add(position);
                        PositionManager.putPosition(position);
                        if ((i == doorCoordinate[0] && (j == doorCoordinate[1]))){//记录门点
                            door = position;
                            RoomIntersectorPointMapper.putRoomIntersectorPoint(position);//插入房间连接点
                            System.out.println("插入房间连接点:"+position);
                        }
                    }
                }
                Room room = new Room(areaType,horizon,vertical,door,roomPositions);
                AreaManager.putRoom(room);
                rooms.add(room);
            }else{//创建走廊类型对象
                String[][] corridorCoordinates = Utils.parseCorridorCordinate(results[3]);
                List<Position> junctionCoordinates = new ArrayList<>(corridorCoordinates.length);
                List<Position> corridorPositions = new ArrayList<>(vertical*horizon);
                for(int i=0;i<horizon;i++){
                    for(int j=0;j<vertical;++j){
                        Position position = new Position(i,j,areaType);
                        allPositions.add(position);
                        corridorPositions.add(position);
                        PositionManager.putPosition(position);
                        for (int k=0;k<corridorCoordinates.length;k++){
                            if ((i == Integer.parseInt(corridorCoordinates[k][0]) && (j == Integer.parseInt(corridorCoordinates[k][1])))){//记录走廊连接点
                                junctionCoordinates.add(position);
                                CorridorIntersectorPointMapper.putCorridorIntersectorPoint(position,Area.valueOf(corridorCoordinates[k][2]));
                            }
                        }
                    }
                }
                Corridor corridor = new Corridor(areaType,horizon,vertical,junctionCoordinates,corridorPositions);
                AreaManager.putCorridor(corridor);
                corridors.add(corridor);
            }
        }

        reader.close();
        reader=null;

        //JunctionMapper初始化
        JunctionMapper.initJunctionMap();

        file = new File("output.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fOutStream = new FileOutputStream(file);
        OutputStreamWriter outWriter = new OutputStreamWriter(fOutStream);
        BufferedWriter writer = new BufferedWriter(outWriter);

        System.out.println("计算矩阵中...");
        PositionMatrix positionMatrix = new PositionMatrix(allPositionsNum,allPositions);

        writer.write("点阵id:"+ Arrays.toString(positionMatrix.getIds())+"\n");
        System.out.println("点阵id:"+positionMatrix.getIds().length+":"+ Arrays.toString(positionMatrix.getIds()));

        double[][] matrix = positionMatrix.getMatrix();
        System.out.println("输出矩阵:");
        writer.write("输出矩阵\n");
        int row = positionMatrix.getRow();
        System.out.println("矩阵宽度u:"+row);
        for (int i=0;i<row;i++){
            for(int j=0;j<row;j++){
                writer.write(matrix[i][j]+"  ");
                System.out.printf("%-9.6f    ",matrix[i][j]);
//                System.out.print(matrix[i][j]+"  ");
            }
            writer.write(";\n");
            System.out.println("");
        }
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println("计算结束，共计用时:"+(end-begin)/1000.0 + "秒!");
        //MDS
        File fileMds = new File("outputMDSResult_2D.txt");
        if (!fileMds.exists()){
            fileMds.createNewFile();
        }
        FileOutputStream fOutStreamMds = new FileOutputStream(fileMds);
        OutputStreamWriter outWriterMds = new OutputStreamWriter(fOutStreamMds);
        BufferedWriter writerMds = new BufferedWriter(outWriterMds);

        double[][] matrixMdsResult = MDSJ.classicalScaling(matrix);
        System.out.println("输出MDS的二维坐标");
        StringBuilder resultString = new StringBuilder();
        StringBuilder resultString2 = new StringBuilder();
        for (int i = 0;i < row ; i++){
            System.out.println(matrixMdsResult[0][i]+" "+matrixMdsResult[1][i]);
            resultString.append(matrixMdsResult[0][i]+",");
            resultString2.append(matrixMdsResult[1][i]+",");
            //resultString.append(matrixMdsResult[0][i]+" ,"+matrixMdsResult[1][i]+";\r\n");
        }
       writerMds.write(resultString.toString()+"\r\n "+"y坐标"+"\r\n "+resultString2.toString());
       // writerMds.write(resultString.toString());
        writerMds.close();
        long end2 = System.currentTimeMillis();
        System.out.println("计算结束，共计用时:"+(end2-begin)/1000.0 + "秒!  矩阵宽度："+ row);
    }
}
