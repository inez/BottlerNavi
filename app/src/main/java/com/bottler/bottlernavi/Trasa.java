package com.bottler.bottlernavi;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Trasa extends RealmObject {

    @PrimaryKey
    private String id;
    private String nazwa;
    private long dataDodania;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public long getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(long dataDodania) {
        this.dataDodania = dataDodania;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
