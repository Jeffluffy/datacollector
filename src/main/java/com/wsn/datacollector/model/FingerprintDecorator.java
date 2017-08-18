package com.wsn.datacollector.model;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by lufen on 2017/8/6.
 */
public class FingerprintDecorator {

    private Fingerprint fingerprint;

    private int[] valueArr = new int[26];//指纹值数组

    private Set<FingerprintDecorator> neighbors = new HashSet<>();//相邻指纹的map

    public FingerprintDecorator(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
        try{
            for(int i=0; i<26; ++i ){
                Method method = Fingerprint.class.getMethod("getL"+(i+1));
                Integer value = (Integer) method.invoke(fingerprint);
                if(value == null){
                    valueArr[i] = 0;
                }else{
                    valueArr[i] = value;
                }
            }
        }catch (Exception e){
            System.out.println("e.message:" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateFingerprintValue(FingerprintDecorator fingerprintDecorator) {

        int[] valArr = fingerprintDecorator.getValueArr();
        for(int i=0; i<valueArr.length; i++) {
            valueArr[i] = (valueArr[i] + valArr[i])/2 ;
        }
    }

    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public String getKey() {
        return getPath()+"-"+getPosition();
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

    public int[] getValueArr() {
        return valueArr;
    }

    public void setValueArr(int[] valueArr) {
        this.valueArr = valueArr;
    }

    public Set<FingerprintDecorator> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<FingerprintDecorator> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbors(FingerprintDecorator fingerprintDecorator){
        this.neighbors.add(fingerprintDecorator);
    }
    public void removeNeighbors(FingerprintDecorator fingerprintDecorator){
        neighbors.remove(fingerprintDecorator);
    }

    public Integer getId() {
        return fingerprint.getId();
    }

    public void setId(Integer id) {
        fingerprint.setId(id) ;
    }
    public Integer getPosition() {
        return fingerprint.getPosition();
    }

    public void setPosition(Integer position) {
        fingerprint.setPosition(position);
    }

    public Long getPath() {
        return fingerprint.getPath();
    }

    public void setPath(Long path) {
        fingerprint.setPath(path);
    }

    public Long getTime() {
        return fingerprint.getTime();
    }

    public void setTime(Long time) {
        fingerprint.setTime(time);
    }

    //TODO get set methods
}
