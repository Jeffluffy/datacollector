package com.wsn.datacollector.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wsn.datacollector.dao.FingerprintDao;
import com.wsn.datacollector.dao.UserDao;
import com.wsn.datacollector.dao.WifiLocationDao;
import com.wsn.datacollector.model.Fingerprint;
import com.wsn.datacollector.model.WifiLocation;
import com.wsn.datacollector.model.wifi;
import com.wsn.datacollector.model.wifiHot;
import com.wsn.datacollector.utils.GetPathNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by daaisailan on 2017/7/27.
 */
@RestController
public class TestController {



    @Autowired
    UserDao userDao;
    @Autowired
    FingerprintDao fingerprintDao;
    @Autowired
    WifiLocationDao wifiLocationDao;

    @RequestMapping("/test")
    public String test(){
/*        User user = userDao.getOne(Long.parseLong("1"));
        System.out.println(user.toString());*/
        return "success join ";
    }
    @RequestMapping("/collect")
    public String handleRequest (HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = (String) request.getParameter("wifi_fingerprint");
        Gson gson = new Gson();
        List<wifiHot> fingerprint =  gson.fromJson(s, new TypeToken<List<wifiHot>>(){}.getType());
        StringBuilder stringBuilder = new StringBuilder();
        for (wifiHot wifi: fingerprint){
            stringBuilder.append(wifi.getSsid()+" "+wifi.getBssid()+" "+wifi.getLevel()+" "+wifi.getCounter()+"\r\n");
        }
        System.out.print(stringBuilder);
        return "success get";
    }
    //只能运行一次
    @RequestMapping("/init")
    public String initLocation(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String s = (String) request.getParameter("wifi_fingerprint");
        Gson gson = new Gson();
        List<List<wifi>>  tempfingerprint =  gson.fromJson(s, new TypeToken<List<List<wifi>>>(){}.getType());
        List<wifi> l= tempfingerprint.get(0);
        for(wifi m : l){
            WifiLocation wifiLocation = new WifiLocation();
            wifiLocation.setSsid(m.getSsid());
            wifiLocation.setBssid(m.getBssid());
            wifiLocationDao.save(wifiLocation);
        }

        return "success return";
    }



    @RequestMapping("/wifi")
    public String handleRequestWifi (HttpServletRequest request, HttpServletResponse response) throws IOException {
        String s = (String) request.getParameter("wifi_fingerprint");
        String s2 = (String) request.getParameter("time_sequence");
        Gson gson = new Gson();
        List<List<wifi>>  tempfingerprint =  gson.fromJson(s, new TypeToken<List<List<wifi>>>(){}.getType());
        List<Long> timeSequence = gson.fromJson(s2, new TypeToken<List<Long>>(){}.getType());
        Long pathid =  GetPathNumber.getPathNumber();
        for(int i=0;i<tempfingerprint.size();i++){
            List<wifi> w = tempfingerprint.get(i);
            System.out.println(w.get(0).getBssid()+" ,"+w.get(0).getLevel()+" ,"+w.get(2).getBssid()+" ,"+w.get(3).getLevel());
            Fingerprint fingerprint = new Fingerprint();
            fingerprint.setTime( timeSequence.get(i));
            fingerprint.setPath(pathid);
            fingerprint.setPosition(i+1);
            fingerprint.setL1(w.get(0).getLevel());
            fingerprint.setL2(w.get(1).getLevel());
            fingerprint.setL3(w.get(2).getLevel());
            fingerprint.setL4(w.get(3).getLevel());
            fingerprint.setL5(w.get(4).getLevel());
            fingerprint.setL6(w.get(5).getLevel());
            fingerprint.setL7(w.get(6).getLevel());
            fingerprint.setL8(w.get(7).getLevel());
            fingerprint.setL9(w.get(8).getLevel());
            fingerprint.setL10(w.get(9).getLevel());
            fingerprint.setL11(w.get(10).getLevel());
            fingerprint.setL12(w.get(11).getLevel());
            fingerprint.setL13(w.get(12).getLevel());
            fingerprint.setL14(w.get(13).getLevel());
            fingerprint.setL15(w.get(14).getLevel());
            fingerprint.setL16(w.get(15).getLevel());
            fingerprint.setL17(w.get(16).getLevel());
            fingerprint.setL18(w.get(17).getLevel());
            fingerprint.setL19(w.get(18).getLevel());
            fingerprint.setL20(w.get(19).getLevel());
            fingerprint.setL21(w.get(20).getLevel());
            fingerprint.setL22(w.get(21).getLevel());
            fingerprint.setL23(w.get(22).getLevel());
            fingerprint.setL24(w.get(23).getLevel());
            fingerprint.setL25(w.get(24).getLevel());
            fingerprint.setL26(w.get(25).getLevel());

            fingerprint.setM1(w.get(0).getBssid());
            fingerprint.setM2(w.get(1).getBssid());
            fingerprint.setM3(w.get(2).getBssid());
            fingerprint.setM4(w.get(3).getBssid());
            fingerprint.setM5(w.get(4).getBssid());
            fingerprint.setM6(w.get(5).getBssid());
            fingerprint.setM7(w.get(6).getBssid());
            fingerprint.setM8(w.get(7).getBssid());
            fingerprint.setM9(w.get(8).getBssid());
            fingerprint.setM10(w.get(9).getBssid());
            fingerprint.setM11(w.get(10).getBssid());
            fingerprint.setM12(w.get(11).getBssid());
            fingerprint.setM13(w.get(12).getBssid());
            fingerprint.setM14(w.get(13).getBssid());
            fingerprint.setM15(w.get(14).getBssid());
            fingerprint.setM16(w.get(15).getBssid());
            fingerprint.setM17(w.get(16).getBssid());
            fingerprint.setM18(w.get(17).getBssid());
            fingerprint.setM19(w.get(18).getBssid());
            fingerprint.setM20(w.get(19).getBssid());
            fingerprint.setM21(w.get(20).getBssid());
            fingerprint.setM22(w.get(21).getBssid());
            fingerprint.setM23(w.get(22).getBssid());
            fingerprint.setM24(w.get(23).getBssid());
            fingerprint.setM25(w.get(24).getBssid());
            fingerprint.setM26(w.get(25).getBssid());

            fingerprintDao.save(fingerprint);
        }

      /*  List<wifi> init = fingerprint.get(0);
        List<WifiLocation> temp = new ArrayList<>();
        for (wifi w:init){
            WifiLocation wifiLocation = new WifiLocation();
            wifiLocation.setBssid(w.getBssid());
            wifiLocation.setSsid(w.getSsid());
            temp.add(wifiLocation);
        }
        pretreatment.insertWifiLocation(temp);*/
        /*for (int i=0;i<fingerprint.size();i++){
            System.out.println("指纹样例:"+i);
            List<wifi> tempwifi = fingerprint.get(i);
            StringBuilder stringBuilder = new StringBuilder();

            for(wifi w: tempwifi){
                stringBuilder.append(w.getSsid()+" "+w.getLevel()+"\r\n");
            }
            stringBuilder.append("时间序列"+ timeSequence.get(i));            System.out.print(stringBuilder);
        }*/

        return "success got wifi and time";
    }

    public void calculateFanshu1() throws IOException {

        File file;
        file = new File("output4.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fOutStream = new FileOutputStream(file);
        OutputStreamWriter outWriter = new OutputStreamWriter(fOutStream);
        BufferedWriter writer = new BufferedWriter(outWriter);

        List<Fingerprint> fingerprints = fingerprintDao.findAll();
        int length = fingerprints.size();

        writer.write("计算点与点的相似度\r\n");
        System.out.println("計算相似度");
        for(int i=0;i<length;++i){
            Fingerprint fingerprintI = fingerprints.get(i);
            for(int j=0;j<length;++j){
                Fingerprint fingerprintJ = fingerprints.get(j);
                int f1 = calcF1(fingerprintI,fingerprintJ);
                writer.write("( " + i + " , " + j + " )" + f1 +"\r\n");
                System.out.println("( " + i + " , " + j + " )" + f1);
            }
        }
        writer.write("计算完毕!");
        writer.close();
        System.out.println("計算完畢");
    }

    public int calcF1(Fingerprint fingerprint1,Fingerprint fingerprint2){

        initFingerPrint(fingerprint1);
        initFingerPrint(fingerprint2);
        int result = Math.abs(fingerprint1.getL1() - fingerprint2.getL1()) +
                Math.abs(fingerprint1.getL2() - fingerprint2.getL2()) +
                Math.abs(fingerprint1.getL3() - fingerprint2.getL3()) +
                Math.abs(fingerprint1.getL4() - fingerprint2.getL4()) +
                Math.abs(fingerprint1.getL5() - fingerprint2.getL5()) +
                Math.abs(fingerprint1.getL6() - fingerprint2.getL6()) +
                Math.abs(fingerprint1.getL7() - fingerprint2.getL7()) +
                Math.abs(fingerprint1.getL8() - fingerprint2.getL8()) +
                Math.abs(fingerprint1.getL9() - fingerprint2.getL9()) +
                Math.abs(fingerprint1.getL10() - fingerprint2.getL10()) +
                Math.abs(fingerprint1.getL11() - fingerprint2.getL11()) +
                Math.abs(fingerprint1.getL12() - fingerprint2.getL12()) +
                Math.abs(fingerprint1.getL13() - fingerprint2.getL13()) +
                Math.abs(fingerprint1.getL14() - fingerprint2.getL14()) +
                Math.abs(fingerprint1.getL15() - fingerprint2.getL15()) +
                Math.abs(fingerprint1.getL16() - fingerprint2.getL16()) +
                Math.abs(fingerprint1.getL17() - fingerprint2.getL17()) +
                Math.abs(fingerprint1.getL18() - fingerprint2.getL18()) +
                Math.abs(fingerprint1.getL19() - fingerprint2.getL19()) +
                Math.abs(fingerprint1.getL20() - fingerprint2.getL20()) +
                Math.abs(fingerprint1.getL21() - fingerprint2.getL21()) +
                Math.abs(fingerprint1.getL22() - fingerprint2.getL22()) +
                Math.abs(fingerprint1.getL23() - fingerprint2.getL23()) +
                Math.abs(fingerprint1.getL24() - fingerprint2.getL24()) +
                Math.abs(fingerprint1.getL25() - fingerprint2.getL25()) +
                Math.abs(fingerprint1.getL26() - fingerprint2.getL26());

        return result;
    }

    private void initFingerPrint(Fingerprint fingerprint){
        Integer l1 = fingerprint.getL1();
        if(l1 == null){
            fingerprint.setL1(0);
        }

        Integer l2 = fingerprint.getL2();
        if(l2 == null){
            fingerprint.setL2(0);
        }

        Integer l3 = fingerprint.getL3();
        if(l3 == null){
            fingerprint.setL3(0);
        }

        Integer l4 = fingerprint.getL4();
        if(l4 == null){
            fingerprint.setL4(0);
        }

        Integer l5 = fingerprint.getL5();
        if(l5 == null){
            fingerprint.setL5(0);
        }

        Integer l6 = fingerprint.getL6();
        if(l6 == null){
            fingerprint.setL6(0);
        }

        Integer l7 = fingerprint.getL7();
        if(l7 == null){
            fingerprint.setL7(0);
        }

        Integer l8 = fingerprint.getL8();
        if(l8 == null){
            fingerprint.setL8(0);
        }

        Integer l9 = fingerprint.getL9();
        if(l9 == null){
            fingerprint.setL9(0);
        }

        Integer l10 = fingerprint.getL10();
        if(l10 == null){
            fingerprint.setL10(0);
        }

        Integer l11 = fingerprint.getL11();
        if(l11 == null){
            fingerprint.setL11(0);
        }

        Integer l12 = fingerprint.getL12();
        if(l12 == null){
            fingerprint.setL12(0);
        }

        Integer l13 = fingerprint.getL13();
        if(l13 == null){
            fingerprint.setL13(0);
        }

        Integer l14 = fingerprint.getL14();
        if(l14 == null){
            fingerprint.setL14(0);
        }

        Integer l15 = fingerprint.getL15();
        if(l15 == null){
            fingerprint.setL15(0);
        }

        Integer l16 = fingerprint.getL16();
        if(l16 == null){
            fingerprint.setL16(0);
        }

        Integer l17 = fingerprint.getL17();
        if(l17 == null){
            fingerprint.setL17(0);
        }

        Integer l18 = fingerprint.getL18();
        if(l18 == null){
            fingerprint.setL18(0);
        }

        Integer l19 = fingerprint.getL19();
        if(l19 == null){
            fingerprint.setL19(0);
        }

        Integer l20 = fingerprint.getL20();
        if(l20 == null){
            fingerprint.setL20(0);
        }

        Integer l21 = fingerprint.getL21();
        if(l21 == null){
            fingerprint.setL21(0);
        }

        Integer l22 = fingerprint.getL22();
        if(l22 == null){
            fingerprint.setL22(0);
        }

        Integer l23 = fingerprint.getL23();
        if(l23 == null){
            fingerprint.setL23(0);
        }

        Integer l24 = fingerprint.getL24();
        if(l24 == null){
            fingerprint.setL24(0);
        }

        Integer l25 = fingerprint.getL25();
        if(l25 == null){
            fingerprint.setL25(0);
        }

        Integer l26 = fingerprint.getL26();
        if(l26 == null){
            fingerprint.setL26(0);
        }
    }

}
