package com.wsn.datacollector.utils;

/**
 * Created by daaisailan on 2017/8/13.
 */
public class Floyd {
    private static int INF = Integer.MAX_VALUE;

    public int[][] floyd (int[][] matrix){
        int size = matrix.length;
        int [][] dist = new int[size][size];

        //初始化距离举证
        for (int i = 0 ; i< size; i++){
            for (int j = 0; j<size; j++){
                dist[i][j] = matrix[i][j];
            }
        }
        //
        for (int k = 0; k< size; k++){
            for (int i = 0; i<size; i++){
                for (int j = 0;j<size ; j++){
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]){
                        dist[i][j] = dist[i][k] +dist[k][j];
                    }
                }
            }
        }
        //
        return dist;
    }

}
