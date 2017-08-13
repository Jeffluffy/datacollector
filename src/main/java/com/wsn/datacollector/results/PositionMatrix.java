package com.wsn.datacollector.results;

import com.wsn.datacollector.model.Position;
import com.wsn.datacollector.utils.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by daaisailan on 2017/7/22.
 */
public class PositionMatrix {

    private int row;//矩阵行数
    private int col;//矩阵列数

    private List<Position> positions = null;//矩阵中的点
    private int[] ids = null;

    private double[][] matrix = null;

    public PositionMatrix(int row,List<Position> positions) {

        if(row==0||positions==null||positions.size()==0){
            throw new RuntimeException("新建PositionMatrix矩阵失败,传入参数不合法!");
        }

        this.row = row;
        this.col = row;
        this.positions = positions;

        initMatrix();
    }

    private boolean initMatrix(){
        int size = positions.size();
        double[][] matrixTemp = new double[size][size];
        ids = new int[size];
        for(int i=0;i<size;++i){
            Position positionX = positions.get(i);
            ids[i] = positionX.getId();
            for(int j=0;j<size;++j){
                Position positionY = positions.get(j);
//                System.out.println("计算两点之间的距离:"+positionX+"-----"+positionY);
                double distance = Utils.calculateDistance(positionX,positionY);
//                if((positionX.getId() != positionY.getId())&&(distance==0)){//检测id不相等但是计算结果为0的错误计算
//                    throw new RuntimeException("计算错误:"+positionX+"========="+positionY);
//                }
                matrixTemp[i][j] = distance;
//                matrixTemp[j][i] = distance;
            }
        }
        setMatrix(matrixTemp);
        return true;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        return "PositionMatrix{" +
                "row=" + row +
                ", col=" + col +
                ", positions=" + positions +
                ", ids=" + Arrays.toString(ids) +
                ", matrix=" + Arrays.toString(matrix) +
                '}';
    }
}
