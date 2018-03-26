package com.syq.outschoolbigdata.utils;

import com.syq.outschoolbigdata.MyApplication;
import com.syq.outschoolbigdata.vo.orm.TimeTags;

/**
 * Created by yfb on 2018/3/13.
 */

public class DBUtil {
    public static long getOneDayTime(){
        long time;
        TimeTags tags = MyApplication.getInstances().getDaoSession().getTimeTagsDao().queryBuilder().build().unique();
        if(tags == null){
            time = System.currentTimeMillis();
//            tags = new TimeTags(0,time,time);
            MyApplication.getInstances().getDaoSession().getTimeTagsDao().insertOrReplace(tags);
        }else{
            time = tags.getOneDayTime();
        }
        return time;
    }

    public static TimeTags getTimeTags(){
        TimeTags tags = MyApplication.getInstances().getDaoSession().getTimeTagsDao().queryBuilder().build().unique();
        if(tags == null){
            tags = new TimeTags(0,0,0,0,0);
            MyApplication.getInstances().getDaoSession().getTimeTagsDao().insertOrReplace(tags);
        }
        return tags;
    }
}
