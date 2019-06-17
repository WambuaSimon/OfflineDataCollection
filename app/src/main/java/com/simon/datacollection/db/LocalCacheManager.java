package com.simon.datacollection.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.simon.datacollection.models.CustomerModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocalCacheManager {
    private static final String DB_NAME = "customer_db";
    private Context context;
    private static LocalCacheManager _instance;
    AppDataBase db;

    public static LocalCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new LocalCacheManager(context);
        }
        return _instance;
    }

    public LocalCacheManager(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context, AppDataBase.class, DB_NAME).build();
    }

    public void getUsers(final DatabaseCallback databaseCallback) {
        db.customerDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CustomerModel>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<CustomerModel> users) throws Exception {
                databaseCallback.onUsersLoaded(users);

            }
        });
    }

    public void addUser(final DatabaseCallback databaseCallback, final String firstName, final String lastName, final String idNo, final String qrData, final byte[] id_image, final String latitide,final String longitude) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                CustomerModel user = new CustomerModel(firstName, lastName, idNo, qrData, id_image, latitide,longitude);
                db.customerDao().insertAll(user);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseCallback.onUserAdded();
            }

            @Override
            public void onError(Throwable e) {
                databaseCallback.onDataNotAvailable();
            }
        });
    }


}