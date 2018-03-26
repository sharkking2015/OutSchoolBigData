package com.syq.outschoolbigdata.vo.orm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yfb on 2018/3/13.
 */

@Entity
public class TimeTags {
    @Id
    private long id;
    private long oneDayTime;
    private long oneHourTime;
    private int analysisVersion;
    private int answerVersion;
    public long getOneHourTime() {
        return this.oneHourTime;
    }
    public void setOneHourTime(long oneHourTime) {
        this.oneHourTime = oneHourTime;
    }
    public long getOneDayTime() {
        return this.oneDayTime;
    }
    public void setOneDayTime(long oneDayTime) {
        this.oneDayTime = oneDayTime;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getAnswerVersion() {
        return this.answerVersion;
    }
    public void setAnswerVersion(int answerVersion) {
        this.answerVersion = answerVersion;
    }
    public int getAnalysisVersion() {
        return this.analysisVersion;
    }
    public void setAnalysisVersion(int analysisVersion) {
        this.analysisVersion = analysisVersion;
    }
    @Generated(hash = 679393800)
    public TimeTags(long id, long oneDayTime, long oneHourTime,
            int analysisVersion, int answerVersion) {
        this.id = id;
        this.oneDayTime = oneDayTime;
        this.oneHourTime = oneHourTime;
        this.analysisVersion = analysisVersion;
        this.answerVersion = answerVersion;
    }
    @Generated(hash = 1016622291)
    public TimeTags() {
    }
}
