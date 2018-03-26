package com.syq.outschoolbigdata.vo.orm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yfb on 2018/3/14.
 * 试卷本地批阅记录
 */
@Entity
public class LocalExamInfo implements Serializable{
    @Id
    private String guid;    //唯一标识
    private String paperid; //试卷编号（长号）
    private String shortPaperId; //试卷短号
    private String userid; //用户id
    private String xjNum; //学生学号
    private int batch; //批阅批次
    private String createTime; //创建时间
    private String uploadTime; //上传时间
    private String mark; //试卷得分
    private String score; //获得的积分
    private String paperType; //试卷类型
    private boolean isUploaded;//是否上传
    public boolean getIsUploaded() {
        return this.isUploaded;
    }
    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }
    public String getPaperType() {
        return this.paperType;
    }
    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getMark() {
        return this.mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public String getUploadTime() {
        return this.uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public int getBatch() {
        return this.batch;
    }
    public void setBatch(int batch) {
        this.batch = batch;
    }
    public String getXjNum() {
        return this.xjNum;
    }
    public void setXjNum(String xjNum) {
        this.xjNum = xjNum;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getShortPaperId() {
        return this.shortPaperId;
    }
    public void setShortPaperId(String shortPaperId) {
        this.shortPaperId = shortPaperId;
    }
    public String getPaperid() {
        return this.paperid;
    }
    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }
    public String getGuid() {
        return this.guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }
    @Generated(hash = 277038109)
    public LocalExamInfo(String guid, String paperid, String shortPaperId,
            String userid, String xjNum, int batch, String createTime,
            String uploadTime, String mark, String score, String paperType,
            boolean isUploaded) {
        this.guid = guid;
        this.paperid = paperid;
        this.shortPaperId = shortPaperId;
        this.userid = userid;
        this.xjNum = xjNum;
        this.batch = batch;
        this.createTime = createTime;
        this.uploadTime = uploadTime;
        this.mark = mark;
        this.score = score;
        this.paperType = paperType;
        this.isUploaded = isUploaded;
    }
    @Generated(hash = 1855492624)
    public LocalExamInfo() {
    }

}
