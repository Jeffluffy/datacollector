package com.wsn.datacollector.utils;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

//
//在某射击场有N个靶，每个靶上都有一个分数，存在score数组中。击中第i个靶的得分为score[left] * score[i] * score[right]，
// 同时原left和right两个靶变为相邻的靶。其中得分为0的靶是不能射击的，当left不存在或者不能射击时，得分为 score[i] * score[right]，
// 同理right也遵循此规则; 当left和right都不存在或者不能射击时，得分为score[i]。请计算出击中所有能射击的靶，最多能得多少分？

public class Main {

/** 请完成下面这个函数，实现题目要求的功能 **/
    /** 当然，你也可以不按照这个模板来作答，完全按照自己的想法来 ^-^  **/
    static int maxScore(int[] score) {

        
        return 0;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int res;

        int _score_size = 0;
        _score_size = Integer.parseInt(in.nextLine().trim());
        int[] _score = new int[_score_size];
        int _score_item;
        for(int _score_i = 0; _score_i < _score_size; _score_i++) {
            _score_item = Integer.parseInt(in.nextLine().trim());
            _score[_score_i] = _score_item;
        }

        res = maxScore(_score);
        System.out.println(String.valueOf(res));

    }
}