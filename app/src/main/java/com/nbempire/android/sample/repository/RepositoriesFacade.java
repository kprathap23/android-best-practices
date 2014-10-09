package com.nbempire.android.sample.repository;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nbempire.android.sample.Application;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by nbarrios on 09/10/14.
 */
public abstract class RepositoriesFacade {

    public static ItemRepository getItemRepository(Context context) {
        return ItemRepositoryImpl.getInstance(context);
    }

    public static ItemRemoteRepository getItemRemoteRepository() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Application.Keys.MELI_API_HOST)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(ItemRemoteRepository.class);
    }

}
