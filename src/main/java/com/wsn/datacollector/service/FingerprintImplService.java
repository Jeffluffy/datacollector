package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FingerprintDao;
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

    public List<Path> getPathList(){

        List<Fingerprint> fingerprints = fingerprintDao.findAll();

        Map<String,FingerprintDecorator> fingerprintDecoratorMap = new HashMap<>(fingerprints.size());
        List<FingerprintDecorator> fingerprintDecorators = new LinkedList<>();

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

        //初始化相同路径上的相邻点
        fingerprintDecorators.parallelStream().forEach(fingerprintDecorator -> {
            String key = fingerprintDecorator.getPath()+"-"+(fingerprintDecorator.getPosition()-1);
            FingerprintDecorator  f =fingerprintDecoratorMap.get(key);
            if( f != null){//判读是否有上一个点
                fingerprintDecorator.addNeighbors(f);
            }
            key = fingerprintDecorator.getPath()+"-"+(fingerprintDecorator.getPosition()+1);
            f =fingerprintDecoratorMap.get(key);
            if( f != null){//判读是否有下一个点
                fingerprintDecorator.addNeighbors(f);
            }
        });

        //合并相同路径上的相似点
        Set<String> pathIds = grupedFingerprints.keySet();
        pathIds.forEach(pathId->{
            grupedFingerprints.get(pathId).parallelStream()
            .sorted(Comparator.comparing(FingerprintDecorator::getPosition))
            .forEach(f->{
                if(f.getPosition() == 0){

                }
            });
        });
        return null;
    }

}
