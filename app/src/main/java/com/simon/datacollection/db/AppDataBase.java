package com.simon.datacollection.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.simon.datacollection.dao.CustomerDao;
import com.simon.datacollection.models.CustomerModel;

@Database(entities = {CustomerModel.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CustomerDao customerDao();
}
