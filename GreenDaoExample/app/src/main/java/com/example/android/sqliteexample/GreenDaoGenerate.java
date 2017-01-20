package com.example.android.sqliteexample;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class GreenDaoGenerate {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.example.android.sqliteexample.dao");// 객체가 만들어질 위치

        Entity person = schema.addEntity("Memo"); // Memo 테이블추가
        person.addIdProperty();   // ID에대한 PK 값
        person.addStringProperty("title"); //title에대한 String 필드
        person.addStringProperty("content");//Content 에대한 String 필드

        new DaoGenerator().generateAll(schema, "../GreenDaoExample/app/src/main/java");
    }
}
