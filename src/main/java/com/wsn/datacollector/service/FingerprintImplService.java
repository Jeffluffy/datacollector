package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FingerprintDao;
import com.wsn.datacollector.dao.FootStepDao;
import com.wsn.datacollector.model.Fingerprint;
import com.wsn.datacollector.model.FingerprintDecorator;
import com.wsn.datacollector.model.Path;
import com.wsn.datacollector.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public double[][] getMatrix(){

        List<Fingerprint> fingerprints = fingerprintDao.findAll();

        Map<String,FingerprintDecorator> fingerprintDecoratorMap = new HashMap<>(fingerprints.size());
        List<FingerprintDecorator> fingerprintDecorators = new LinkedList<>();

        //用于存储两两点之间的权值
        Map<String,Double> footstepMap = new HashMap<>();

        //通过所属路径分组所有fingerprint，同时将所有的fingerprint放入HashMap 和 List中
        Map<String,List<FingerprintDecorator>> grupedFingerprints = fingerprints.parallelStream()
                .map(fingerprint -> {
                    FingerprintDecorator fingerprintDecorator = new FingerprintDecorator(fingerprint);
                    String key = fingerprintDecorator.getKey();
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

            String key1 = fingerprintDecorator.getKey();
            footstepMap.put( key1,new Double(1L));

            fingerprintDecorator.getNeighbors().forEach(neighbor -> {
                String key2 = neighbor.getKey();
                footstepMap.put(key1+"_"+key2,new Double(1L));
                footstepMap.put(key2+"_"+key1,new Double(1L));
            });
        });

        //合并相同路径上的相似点
        Set<String> pathIds = grupedFingerprints.keySet();
        pathIds.forEach(pathId->{
            FingerprintDecorator[] fingerprintsOfOnePath = (FingerprintDecorator[]) grupedFingerprints.get(pathId).parallelStream()
            .sorted(Comparator.comparing(FingerprintDecorator::getPosition)).toArray();

            for(int i=0; i<fingerprintsOfOnePath.length; ++i){
                FingerprintDecorator f1 = fingerprintsOfOnePath[i];
                for(int j=i+1; j<fingerprintsOfOnePath.length; ++j){
                    FingerprintDecorator f2 = fingerprintsOfOnePath[j];
                    if(f1 == f2) continue;//如果是同一个点
                    int norm1Value = Utils.calcNorm1(f1,f2); //计算第一范式
                    if(norm1Value>THRESHOLD) continue;  //如果范式值大于阈值则认为他们不需要合并
                    //合并相似的点
                    f1.removeNeighbors(f2); //将f1 邻居列表中的f2删除
                    footstepMap.remove(f1.getKey()+"_"+f2.getKey());
                    footstepMap.remove(f2.getKey()+"_"+f1.getKey());

                    f2.getNeighbors().forEach(neighbor->{
                        if(neighbor != f1){
                            f1.addNeighbors(neighbor);  //将f2的邻居点加到f1的邻居列表中
                            neighbor.addNeighbors(f1); //将f1添加到f2的邻居的邻居列表中
                            footstepMap.put(f1.getKey()+"_"+neighbor.getKey(),new Double(1));
                            footstepMap.put(neighbor.getKey()+"_"+f1.getKey(),new Double(1));

                            neighbor.removeNeighbors(f2);//将f2的邻居列表中的f1邻居全部删除
                            footstepMap.remove(f2.getKey()+"_"+neighbor.getKey());
                            footstepMap.remove(neighbor.getKey()+"_"+f2.getKey());
                        }
                    });
                    f1.updateFingerprintValue(f2); //更新指纹值
                    grupedFingerprints.get(pathId).remove(f2); //将这个点从该条路径中删除
                }
            }
        });

        //合并不同路径上的点
        pathIds.forEach(pathId1->{
            pathIds.forEach(pathId2->{

                if(pathId1 == pathId2) return;//跳过相同的路径,上一步已经处理过相同路径上的点了
                FingerprintDecorator[] fingerprintsOfOnePath1 = (FingerprintDecorator[]) grupedFingerprints.get(pathId1).parallelStream()
                        .sorted(Comparator.comparing(FingerprintDecorator::getPosition)).toArray();
                FingerprintDecorator[] fingerprintsOfOnePath2 = (FingerprintDecorator[]) grupedFingerprints.get(pathId2).parallelStream()
                        .sorted(Comparator.comparing(FingerprintDecorator::getPosition)).toArray();
                for(int i=0; i<fingerprintsOfOnePath1.length; ++i) {
                    FingerprintDecorator f1 = fingerprintsOfOnePath1[i];
                    for (int j = 0; j < fingerprintsOfOnePath2.length; ++j) {
                        FingerprintDecorator f2 = fingerprintsOfOnePath2[j];
                        if (f1 == f2) continue;//如果是同一个点
                        int norm1Value = Utils.calcNorm1(f1, f2); //计算第一范式
                        if (norm1Value > THRESHOLD) continue;  //如果范式值大于阈值则认为他们不需要合并
                        //合并相似的点
                        f1.removeNeighbors(f2); //将f1 邻居列表中的f2删除
                        f2.getNeighbors().forEach(neighbor -> {
                            if (neighbor != f1) {
                                f1.addNeighbors(neighbor);  //将f2的邻居点加到f1的邻居列表中
                                neighbor.addNeighbors(f1); //将f1添加到f2的邻居的邻居列表中
                                footstepMap.put(f1.getKey()+"_"+neighbor.getKey(),new Double(1));
                                footstepMap.put(neighbor.getKey()+"_"+f1.getKey(),new Double(1));

                                neighbor.removeNeighbors(f2);//将f2的邻居列表中的f1邻居全部删除
                                footstepMap.remove(f2.getKey()+"_"+neighbor.getKey());
                                footstepMap.remove(neighbor.getKey()+"_"+f2.getKey());
                            }
                        });
                        f1.updateFingerprintValue(f2); //更新指纹值
                        grupedFingerprints.get(pathId2).remove(f2); //将这个点从该条路径中删除
                    }
                }
            });
        });

        //构建二维矩阵
        int length = fingerprintDecorators.size();
        double[][] biMatrix = new double[length][length];

        for(int i=0; i<length; i++) {
            FingerprintDecorator fI = fingerprintDecorators.get(i);
            for(int j=i; j<length; j++) {
                FingerprintDecorator fj = fingerprintDecorators.get(i);
                biMatrix[i][j] = footstepMap.get(fI.getKey()+"_"+ fj.getKey());
                biMatrix[j][i] = biMatrix[i][j];
            }
        }

        return biMatrix;
    }

    public static void main(String[] args){
        Integer integer = new Integer("");
        System.out.println(integer);
    }

}
