package com.lucasvieira.requisieshttp.api;

import com.lucasvieira.requisieshttp.model.Foto;
import com.lucasvieira.requisieshttp.model.Postagem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {

    @GET("/photos")
    Call <List<Foto>>recuperarFotos();

    @GET("/posts")
    Call <List<Postagem>>recuperarPostagens();

}
