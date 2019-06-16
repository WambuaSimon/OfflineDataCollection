package com.simon.datacollection.db;

import com.simon.datacollection.models.CustomerModel;

import java.util.List;

public interface DatabaseCallback {

    void onUsersLoaded(List<CustomerModel> customerModelList);

    void onUserAdded();

    void onDataNotAvailable();

}