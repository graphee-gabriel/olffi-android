package com.olffi.app.olffi.data.network.services;

import com.olffi.app.olffi.json.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gabrielmorin on 15/10/15.
 */
public interface GetService {

    @GET("api/country")
    Call<List<Country>> countryList();

}
