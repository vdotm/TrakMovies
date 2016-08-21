package com.rainbow.trakmovies.API;

import com.rainbow.trakmovies.model.DataModel;


import java.util.Collection;

import retrofit.Call;
import retrofit.http.*;
import java.util.List;

/**
 * Created by Meera on 29/09/15.
 */

/* Retrofit 2.0 */

public interface TrakApiService {

       @GET("cpLylIjqWa?indent=2")
        Call <List<DataModel>> getMovie();

}