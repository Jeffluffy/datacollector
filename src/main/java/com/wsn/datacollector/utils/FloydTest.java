package com.wsn.datacollector.utils;

/**
 * Created by daaisailan on 2017/8/13.
 */
public class FloydTest {
    private static int INF = Integer.MAX_VALUE;

    public  static void main (String[] args){
        int[][] matrix={
                {0,INF,2},
                {5,0,8},
                {INF,3,0},

        };
        int[][] dist = floyd(matrix);
        print(dist);
    }

    public static int[][] floyd (int[][] matrix){
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

    public static void print(int [][] m){
        for(int i=0;i<m.length;i++){
            for(int j=0; j<m[0].length;j++){
                System.out.print("  "+m[i][j]+"   ");
            }
            System.out.println(" ");
        }
    }



}
