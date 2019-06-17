package com.simon.datacollection.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "customers")
public class CustomerModel {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    public String fName;
    public String lName;
    public String idNo;
    public String qrCodeData;
    public String latitude;
    public String longitude;
    private boolean expanded;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] id_image;

    public CustomerModel(String fName, String lName, String idNo, String qrCodeData,  byte[] id_image,String latitude, String longitude) {
        this.fName = fName;
        this.lName = lName;
        this.idNo = idNo;
        this.qrCodeData = qrCodeData;
        this.id_image = id_image;
        this.latitude = latitude;
        this.longitude = longitude;

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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }



    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
