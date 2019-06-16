package com.simon.datacollection.dao;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.simon.datacollection.models.CustomerModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface CustomerDao {

    @Insert
    void insertAll(CustomerModel... customerModels);

    @Query("SELECT * FROM customers")
    Maybe<List<CustomerModel>> getAll();

}
