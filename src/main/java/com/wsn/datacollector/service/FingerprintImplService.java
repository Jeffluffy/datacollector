package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FingerprintDao;
import com.wsn.datacollector.dao.FootStepDao;
import com.wsn.datacollector.model.Fingerprint;
import com.wsn.datacollector.model.FingerprintDecorator;
import com.wsn.datacollector.model.Path;
import com.wsn.datacollector.utils.Utils;
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

    private static final int THRESHOLD = 30;

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
                    if(f1 == f2) continue;//如果是同一个点
                    int norm1Value = Utils.calcNorm1(f1,f2); //计算第一范式
                    if(norm1Value>THRESHOLD) continue;  //如果范式值大于阈值则认为他们不需要合并

                    //合并相似的点
                    f1.removeNeighbors(f2); //将f1 邻居列表中的f2删除

                    f2.getNeighbors().forEach(neighbor->{
                        if(neighbor != f1){
                            f1.addNeighbors(neighbor);  //将f2的邻居点加到f1的邻居列表中
                            neighbor.addNeighbors(f1); //将f1添加到f2的邻居的邻居列表中
                            neighbor.removeNeighbors(f2);//将f2的邻居列表中的f1邻居全部删除
                        }
                    });
                    grupedFingerprints.get(pathId).remove(f2); //将这个点从该条路径中删除
                }
            }
        });

        //合并不同路径上的点
        pathIds.forEach(pathId1->{
            pathIds.forEach(pathId2->{

                if(pathId1 == pathId2) return;//跳过相同的路径
                FingerprintDecorator[] fingerprintsOfOnePath1 = (FingerprintDecorator[]) grupedFingerprints.get(pathId1).parallelStream()
                        .sorted(Comparator.comparing(FingerprintDecorator::getPosition)).toArray();
                FingerprintDecorator[] fingerprintsOfOnePath2 = (FingerprintDecorator[]) grupedFingerprints.get(pathId2).parallelStream()
                        .sorted(Comparator.comparing(FingerprintDecorator::getPosition)).toArray();
                for(FingerprintDecorator f1 : fingerprintsOfOnePath1){
                    for(FingerprintDecorator f2 : fingerprintsOfOnePath2){
                        if(f1 == f2) continue;//如果是同一个点
                        int norm1Value = Utils.calcNorm1(f1,f2); //计算第一范式
                        if(norm1Value>THRESHOLD) continue;  //如果范式值大于阈值则认为他们不需要合并

                        //合并相似的点
                        f1.removeNeighbors(f2); //将f1 邻居列表中的f2删除
                        f2.getNeighbors().forEach(neighbor->{
                            if(neighbor != f1){
                                f1.addNeighbors(neighbor);  //将f2的邻居点加到f1的邻居列表中
                                neighbor.addNeighbors(f1); //将f1添加到f2的邻居的邻居列表中
                                neighbor.removeNeighbors(f2);//将f2的邻居列表中的f1邻居全部删除
                            }
                        });
                        grupedFingerprints.get(pathId2).remove(f2); //将这个点从该条路径中删除
                    }
                }
            });
        });

        return null;
    }

    public static void main(String[] args){
        Integer integer = new Integer("");
        System.out.println(integer);
    }

}
