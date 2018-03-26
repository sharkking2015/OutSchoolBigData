package com.syq.outschoolbigdata.vo.orm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yfb on 2018/3/12.
 */

@Entity
public class TiInfo {
    @Id
    private long id;
    private String tiId;
    private String tiName;
    private String tiVersion;
    public String getTiVersion() {
        return this.tiVersion;
    }
    public void setTiVersion(String tiVersion) {
        this.tiVersion = tiVersion;
    }
    public String getTiName() {
        return this.tiName;
    }
    public void setTiName(String tiName) {
        this.tiName = tiName;
    }
    public String getTiId() {
        return this.tiId;
    }
    public void setTiId(String tiId) {
        this.tiId = tiId;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 1537899301)
    public TiInfo(long id, String tiId, String tiName, String tiVersion) {
        this.id = id;
        this.tiId = tiId;
        this.tiName = tiName;
        this.tiVersion = tiVersion;
    }
    @Generated(hash = 1181101095)
    public TiInfo() {
    }
}
