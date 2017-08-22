package com.wsn.datacollector.service;

import com.wsn.datacollector.dao.FingerprintDao;
import com.wsn.datacollector.dao.FootStepDao;
import com.wsn.datacollector.model.Fingerprint;
import com.wsn.datacollector.model.FingerprintDecorator;
import com.wsn.datacollector.model.Path;
import com.wsn.datacollector.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lufen on 2017/8/6.
 */
@Service
public class FingerprintImplService {

    private static final int THRESHOLD = 30;
    private static volatile int numOfWeights = 0;
    private static BufferedWriter writer = null;

    {
        File file = new File("matrixOutput.txt");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter out = new OutputStreamWriter(fos);
        writer = new BufferedWriter(out);
    }

    @Autowired
    FingerprintDao fingerprintDao;

    public double[][] getMatrix() throws IOException {

        out("1 从数据库中读取数据：=============================================");
        //从数据库读取指纹数据
        List<Fingerprint> fingerprints = fingerprintDao.findAll();
        out("      共读取数据："+fingerprints.size());
        //建立两个指纹对象的管理容器，一个Map方便查找，一个List方便遍历
        Map<String,FingerprintDecorator> fingerprintDecoratorMap = new HashMap<>(fingerprints.size());
        List<FingerprintDecorator> fingerprintDecorators = new LinkedList<>();

        //用于存储两两点之间的权值
        Map<String,Double> footstepMap = new HashMap<>();

        out("2 通过所属路径分组所有fingerprint，同时将所有的fingerprint放入HashMap 和 List中");
        //通过所属路径分组所有fingerprint，同时将所有的fingerprint放入HashMap 和 List中
        Map<String,List<FingerprintDecorator>> grupedFingerprints = fingerprints.stream()
            .map(fingerprint -> {
                FingerprintDecorator fingerprintDecorator = new FingerprintDecorator(fingerprint);
                String key = fingerprintDecorator.getKey();
                fingerprintDecoratorMap.put(key,fingerprintDecorator);
                fingerprintDecorators.add(fingerprintDecorator);
                return fingerprintDecorator;
            })
            .collect(Collectors.groupingBy(fingerprintDecorator->fingerprintDecorator.getPath().toString()));

        grupedFingerprints.keySet().forEach(key->out(key+"  size:"+grupedFingerprints.get(key).size()));

        out("3 初始化相同路径上的相邻点=========");
        //初始化相同路径上的相邻点
        fingerprintDecorators.stream().forEach(fingerprintDecorator -> {
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

        fingerprintDecorators.forEach(fingerprintDecorator -> out(fingerprintDecorator.getId()+" neighbors:"+fingerprintDecorator.getNeighbors()));

        out("4 初始化带权边管理的Map ：key：key1_key2 , value: weight，目前默认为1");
        //初始化带权边管理的Map ：key：key1_key2 , value: weight，目前默认为1
        fingerprintDecorators.stream().forEach(fingerprintDecorator -> {
            String key1 = fingerprintDecorator.getKey();
            footstepMap.put(key1+"_"+key1,new Double(0L));
            numOfWeights+=2;
            fingerprintDecorator.getNeighbors().forEach(neighbor -> {
                String key2 = neighbor.getKey();
                footstepMap.put(key1+"_"+key2,new Double(1L));
                footstepMap.put(key2+"_"+key1,new Double(1L));
                numOfWeights+=2;
            });
        });

        footstepMap.keySet().forEach(FingerprintImplService::out);

        out("5 合并相同路径上的相似点");
        //合并相同路径上的相似点
        Set<String> pathIds = grupedFingerprints.keySet();
        Iterator<String> iterator = pathIds.iterator();
        while (iterator.hasNext()){
            String pathId = iterator.next();
            List<FingerprintDecorator> fingerprintsOfOnePath = grupedFingerprints.get(pathId);
            System.out.println("合并前");
            printPath(fingerprintsOfOnePath);
            fingerprintsOfOnePath.stream().sorted(Comparator.comparing(FingerprintDecorator::getPosition));
            for(int i=0; i<fingerprintsOfOnePath.size(); ++i){
                FingerprintDecorator f1 = fingerprintsOfOnePath.get(i);
                for(int j=i+1; j<fingerprintsOfOnePath.size(); ++j){
                    FingerprintDecorator f2 = fingerprintsOfOnePath.get(j);
                    if(f1 == f2) continue;//如果是同一个点，跳过
                    int norm1Value = Utils.calcNorm1(f1,f2); //计算第一范式
                    if(norm1Value>THRESHOLD) continue;  //如果范式值大于阈值则认为他们不需要合并
                    //合并相似的点
                    out("合并相似点: f1: "+f1+"   f2: "+f2+" 相似度(1范):" +norm1Value +"<"+THRESHOLD);
                    f1.removeNeighbors(f2); //将f1 邻居列表中的f2删除
                    //将f2从容器中删除
                    fingerprintDecorators.remove(f2);
                    fingerprintDecoratorMap.remove(f2.getKey());
                    //删除f1 f2 之间对应的边
                    footstepMap.remove(f1.getKey()+"_"+f2.getKey());
                    footstepMap.remove(f2.getKey()+"_"+f1.getKey());
                    numOfWeights-=2;
                    //修改邻居列表
                    Iterator<FingerprintDecorator> iterator1 =f2.getNeighbors().iterator();
                    while (iterator1.hasNext()){
                        FingerprintDecorator neighbor = iterator1.next();
                        if(neighbor != f1){
                            neighbor.removeNeighbors(f2);//将f2的邻居列表中的f1邻居全部删除
//                            f2.removeNeighbors(neighbor); //这一步可有可没有，f2 已经不会再用了
                            iterator1.remove();
                            footstepMap.remove(f2.getKey()+"_"+neighbor.getKey());
                            footstepMap.remove(neighbor.getKey()+"_"+f2.getKey());
                            numOfWeights-=2;
                            neighbor.addNeighbors(f1); //将f1添加到f2的邻居的邻居列表中
                            f1.addNeighbors(neighbor);  //将f2的邻居点加到f1的邻居列表中
                            footstepMap.put(f1.getKey()+"_"+neighbor.getKey(),new Double(1));
                            footstepMap.put(neighbor.getKey()+"_"+f1.getKey(),new Double(1));
                            numOfWeights+=2;
                        }
                    }
                    f1.updateFingerprintValue(f2); //更新指纹值
                    out("更新后的f1: "+f1);
                    grupedFingerprints.get(pathId).remove(f2); //将这个点从该条路径中删除
                }
            }
            System.out.println("合并后");
            printPath(fingerprintsOfOnePath);
        }

        out("6 合并不同路径上的点");
        //合并不同路径上的点
        Iterator<String> iterator1 = pathIds.iterator();
        Iterator<String> iterator2 = pathIds.iterator();
        while (iterator1.hasNext()){

            String pathId1 = iterator1.next();
            while (iterator2.hasNext()){

                String pathId2 = iterator2.next();
                if(pathId1.equals(pathId2)) continue;//跳过相同的路径,上一步已经处理过相同路径上的点了

                List<FingerprintDecorator> fingerprintsOfOnePath = grupedFingerprints.get(pathId1);
                fingerprintsOfOnePath.stream().sorted(Comparator.comparing(FingerprintDecorator::getPosition));
                List<FingerprintDecorator> fingerprintsOfOnePath2 = grupedFingerprints.get(pathId1);
                fingerprintsOfOnePath2.stream().sorted(Comparator.comparing(FingerprintDecorator::getPosition));
                for(int i=0; i<fingerprintsOfOnePath.size(); ++i) {
                    FingerprintDecorator f1 = fingerprintsOfOnePath.get(i);
                    for (int j = 0; j < fingerprintsOfOnePath2.size(); ++j) {
                        FingerprintDecorator f2 = fingerprintsOfOnePath2.get(j);
                        if (f1 == f2) continue;//如果是同一个点
                        int norm1Value = Utils.calcNorm1(f1, f2); //计算第一范式
                        if (norm1Value > THRESHOLD) continue;  //如果范式值大于阈值则认为他们不需要合并
                        //合并相似的点
                        out("合并相似点: f1: "+f1+"   f2: "+f2+" 相似度(1范):" +norm1Value +"<"+THRESHOLD);
                        f1.removeNeighbors(f2); //将f1 邻居列表中的f2删除
                        //将f2从容器中删除
                        fingerprintDecorators.remove(f2);
                        fingerprintDecoratorMap.remove(f2.getKey());
                        //删除f1 f2 之间对应的边
                        footstepMap.remove(f1.getKey()+"_"+f2.getKey());
                        footstepMap.remove(f2.getKey()+"_"+f1.getKey());
                        numOfWeights-=2;
                        //修改邻居列表
                        Iterator<FingerprintDecorator> iterator3 =f2.getNeighbors().iterator();
                        while (iterator3.hasNext()){
                            FingerprintDecorator neighbor = iterator3.next();
                            if(neighbor != f1){
                                neighbor.removeNeighbors(f2);//将f2的邻居列表中的f1邻居全部删除
//                                f2.removeNeighbors(neighbor); //这一步可有可没有，f2 已经不会再用了
                                iterator3.remove();
                                footstepMap.remove(f2.getKey()+"_"+neighbor.getKey());
                                footstepMap.remove(neighbor.getKey()+"_"+f2.getKey());
                                numOfWeights -= 2;

                                neighbor.addNeighbors(f1); //将f1添加到f2的邻居的邻居列表中
                                f1.addNeighbors(neighbor);  //将f2的邻居点加到f1的邻居列表中
                                footstepMap.put(f1.getKey()+"_"+neighbor.getKey(),new Double(1));
                                footstepMap.put(neighbor.getKey()+"_"+f1.getKey(),new Double(1));
                                numOfWeights += 2;
                            }
                        }
                        f1.updateFingerprintValue(f2); //更新指纹值
                        out("更新后的f1: "+f1);
                        grupedFingerprints.get(pathId2).remove(f2); //将这个点从该条路径中删除
                    }
                }
            }
        }

        out("7 构建二维矩阵");
        //构建二维矩阵
        int length = fingerprintDecorators.size();
        double[][] biMatrix = new double[length][length];
        int num = 0;
        for(int i=0; i<length; i++) {
            FingerprintDecorator fI = fingerprintDecorators.get(i);
            for(int j=0; j<length; j++) {
                FingerprintDecorator fJ = fingerprintDecorators.get(j);
                String key = fI.getKey() + "_" + fJ.getKey();
                Double value = footstepMap.get(key);
                System.out.println("( "+i+" ,"+j+" )"+"获取边：" + key + ""+ (value!=null?"有":"无"));
                double val = 2.2;
                if(value != null){
                    num += 1;
                    val = value;
                }
                biMatrix[i][j] = val;
//                biMatrix[j][i] = biMatrix[i][j];
            }
        }

        printMatrix(biMatrix);
        System.out.println("总边数："+ num+ " ,  应有边数："+ numOfWeights);
        writer.close();
        return null;
//        return biMatrix;
    }

    public static void main(String[] args){
        Integer integer = new Integer("");
        System.out.println(integer);
    }

    public static void printMatrix(double[][] matrix) {

        int length = matrix.length;
        for(int i=0; i<length; ++i) {
            for(int j=0; j<length; ++j)
                System.out.print(matrix[i][j] + " ,");
            System.out.println();
        }
    }

    public static void out(String message) {

        try {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printPath(List<FingerprintDecorator> fingerprintDecorators){
        System.out.println("打印路径：");
        fingerprintDecorators.forEach(System.out::println);
    }
}
