package com.syq.outschoolbigdata.vo.orm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yfb on 2018/3/9.
 */

@Entity
public class UserBean {
    @Id
    private Long id;
    private String name;
    private String score;
    private String avater;

    @Generated(hash = 785734949)
    public UserBean(Long id, String name, String score, String avater) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.avater = avater;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
