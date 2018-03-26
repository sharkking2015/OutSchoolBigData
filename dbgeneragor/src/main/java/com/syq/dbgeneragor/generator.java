package com.syq.dbgeneragor;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by yfb on 2018/3/12.
 */

public class generator {

    static Schema schema = new Schema(1, "dbfile");

    public static void main(String args[]) throws Exception {

        addTiInfo();
        new DaoGenerator()
                .generateAll(schema, "F:\\demos\\OutSchoolBigData\\dbgeneragor\\");
    }

    private static void addTiInfo(){
        Entity entity = schema.addEntity("TI_INFO");
        entity.addIdProperty();
        entity.addStringProperty("tiId");
        entity.addStringProperty("tiName");
        entity.addStringProperty("tiVersion");
        entity.addDateProperty("date");
    }
}
