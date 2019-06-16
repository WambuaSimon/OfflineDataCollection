package com.simon.datacollection.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "customers")
public class CustomerModel {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    String fName;
    String lName;
    String idNo;
    String qrCodeData;
    String coordinates;


    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] id_image;

    public CustomerModel(String fName, String lName, String idNo, String qrCodeData, byte[] id_image,String coordinates) {
        this.fName = fName;
        this.lName = lName;
        this.idNo = idNo;
        this.qrCodeData = qrCodeData;
        this.id_image = id_image;
        this.coordinates = coordinates;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getIdNo() {
        return idNo;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public byte[] getId_image() {
        return id_image;
    }
}
