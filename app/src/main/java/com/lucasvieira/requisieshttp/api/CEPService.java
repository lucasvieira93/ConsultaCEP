package com.lucasvieira.requisieshttp.api;

import com.lucasvieira.requisieshttp.model.CEP;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CEPService {

    @GET("11390010/json/")
    Call<CEP> recuperarCEP();

}
