package com.simon.datacollection.ui;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.simon.datacollection.R;
import com.simon.datacollection.adapter.CustomerAdapter;
import com.simon.datacollection.db.AppDataBase;
import com.simon.datacollection.db.DatabaseCallback;
import com.simon.datacollection.db.LocalCacheManager;
import com.simon.datacollection.models.CustomerModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Activity_CustomerList extends AppCompatActivity implements DatabaseCallback {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        ButterKnife.bind(this);

        fetch();
    }

    public void fetch() {
        LocalCacheManager.getInstance(this).getUsers(this);
    }


    @Override
    public void onUsersLoaded(List<CustomerModel> customerModelList) {
        customerAdapter = new CustomerAdapter(customerModelList, Activity_CustomerList.this);
        recyclerView.setAdapter(customerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_CustomerList.this));

    }

    @Override
    public void onUserAdded() {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
