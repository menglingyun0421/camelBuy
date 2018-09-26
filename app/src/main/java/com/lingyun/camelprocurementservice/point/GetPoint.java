package com.lingyun.camelprocurementservice.point;

import android.util.Log;

import com.lingyun.camelprocurementservice.utils.Aes;
import com.lingyun.camelprocurementservice.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凌云 on 2018/8/14.
 */

public class GetPoint {

    public static boolean isLive(String point){
        boolean islive=false;
        String moneyPiont= FileUtils.getData("/Android/data/com.systemcamel.point","camelPoint.txt");
        String a[]=moneyPiont.split("##");

        List<String> list=new ArrayList<>();

        for (int i=0;i<a.length;i++){
            list.add(a[i]);
        }

        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<list.size();i++){
            if (point.equals(list.get(i))){
                list.remove(i);
                islive=true;
            }else {
                buffer.append(list.get(i));
            }
        }
        FileUtils.saveData(buffer.toString(),"/Android/data/com.systemcamel.point","camelPoint.txt");

        return islive;
    }
}
