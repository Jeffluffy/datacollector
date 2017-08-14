package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FingerprintDao;
import com.wsn.datacollector.dao.FootStepDao;
import com.wsn.datacollector.model.Fingerprint;
import com.wsn.datacollector.model.FingerprintDecorator;
import com.wsn.datacollector.model.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lufen on 2017/8/6.
 */
@Service
public class FingerprintImplService {

    @Autowired
    FingerprintDao fingerprintDao;

    @Autowired
    FootStepDao footStepDao;

    public List<Path> getPathList(){

        List<Fingerprint> fingerprints = fingerprintDao.findAll();

        Map<String,FingerprintDecorator> fingerprintDecoratorMap = new HashMap<>(fingerprints.size());
        List<FingerprintDecorator> fingerprintDecorators = new LinkedList<>();

        //用于存储两两点之间的权值
        Map<String,Double> footstepMap = new HashMap<>();

        //通过所属路径分组所有fingerprint，同时将所有的fingerprint放入HashMap 和 List中
        Map<String,List<FingerprintDecorator>> grupedFingerprints = fingerprints.parallelStream()
                .map(fingerprint -> {
                    FingerprintDecorator fingerprintDecorator = new FingerprintDecorator(fingerprint);
                    String key = fingerprintDecorator.getPath()+"-"+fingerprintDecorator.getPosition();
                    fingerprintDecoratorMap.put(key,fingerprintDecorator);
                    fingerprintDecorators.add(fingerprintDecorator);
                    return fingerprintDecorator;
                })
                .collect(Collectors.groupingBy(fingerprintDecorator->fingerprintDecorator.getPath().toString()));

        //TODO 初始化相同路径上的点之间的Footsetps，放入footstepMap
        //初始化相同路径上的相邻点
        fingerprintDecorators.parallelStream().forEach(fingerprintDecorator -> {
            String key = fingerprintDecorator.getPath()+"-"+(fingerprintDecorator.getPosition()-1);
            FingerprintDecorator  f =fingerprintDecoratorMap.get(key);
            if( f != null){//判断是否有上一个点
                fingerprintDecorator.addNeighbors(f);
            }
            key = fingerprintDecorator.getPath()+"-"+(fingerprintDecorator.getPosition()+1);
            f =fingerprintDecoratorMap.get(key);
            if( f != null){//判断是否有下一个点
                fingerprintDecorator.addNeighbors(f);
            }
        });

        //初始化带权边管理的Map ：key：key1_key2 , value: weight
        fingerprintDecorators.parallelStream().forEach(fingerprintDecorator -> {

            String key1 = fingerprintDecorator.getPath()+"-"+fingerprintDecorator.getPosition();
            footstepMap.put(key1+"_"+key1,new Double(1L));

            fingerprintDecorator.getNeighbors().forEach(neighbor -> {
                String key2 = neighbor.getPath()+"-"+neighbor.getPosition();
                footstepMap.put(key1+"_"+key2,new Double(1L));
                footstepMap.put(key2+"_"+key1,new Double(1L));
            });

        });

        //合并相同路径上的相似点
        Set<String> pathIds = grupedFingerprints.keySet();
        pathIds.forEach(pathId->{
            FingerprintDecorator[] fingerprintsOfOnePath = (FingerprintDecorator[]) grupedFingerprints.get(pathId).parallelStream()
            .sorted(Comparator.comparing(FingerprintDecorator::getPosition)).toArray();
            for(FingerprintDecorator f1 : fingerprintsOfOnePath){
                for(FingerprintDecorator f2 : fingerprintsOfOnePath){
                    if(f1 == f2) continue;
                    //TODO
                }
            }
        });
        return null;
    }

    public static void main(String[] args){
        Integer integer = new Integer("");
        System.out.println(integer);
    }

}
