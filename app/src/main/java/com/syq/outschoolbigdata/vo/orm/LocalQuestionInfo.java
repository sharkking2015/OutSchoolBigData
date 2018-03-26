package com.syq.outschoolbigdata.vo.orm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yfb on 2018/3/14.
 * 本地记录的小题信息
 */

@Entity
public class LocalQuestionInfo {
    @Id
    private long id;
    private String guid; //考试记录的主键，用来关联对应记录
    private String questionNo; //题号
    private int result; //答题结果，对，错等
    private String totalScore; //题目总分
    private String userScore;//学生得分
    private String userAnswer;//学生答案
    private String type;//题目类型
    private String createTime;//创建时间
    private boolean isUploaded;//是否上传
    private String uploadTime;//上传时间
    public String getUploadTime() {
        return this.uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    public boolean getIsUploaded() {
        return this.isUploaded;
    }
    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUserAnswer() {
        return this.userAnswer;
    }
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
    public String getUserScore() {
        return this.userScore;
    }
    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }
    public String getTotalScore() {
        return this.totalScore;
    }
    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }
    public int getResult() {
        return this.result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public String getQuestionNo() {
        return this.questionNo;
    }
    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }
    public String getGuid() {
        return this.guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 94898950)
    public LocalQuestionInfo(long id, String guid, String questionNo, int result,
            String totalScore, String userScore, String userAnswer, String type,
            String createTime, boolean isUploaded, String uploadTime) {
        this.id = id;
        this.guid = guid;
        this.questionNo = questionNo;
        this.result = result;
        this.totalScore = totalScore;
        this.userScore = userScore;
        this.userAnswer = userAnswer;
        this.type = type;
        this.createTime = createTime;
        this.isUploaded = isUploaded;
        this.uploadTime = uploadTime;
    }
    @Generated(hash = 1238874961)
    public LocalQuestionInfo() {
    }
}
