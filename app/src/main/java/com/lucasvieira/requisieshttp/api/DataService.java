package com.lucasvieira.requisieshttp.api;

import com.lucasvieira.requisieshttp.model.Foto;
import com.lucasvieira.requisieshttp.model.Postagem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @GET("/photos")
    Call <List<Foto>> recuperarFotos();

    @GET("/posts")
    Call <List<Postagem>> recuperarPostagens();

    //formato JSON
    @POST("/posts")
    Call <Postagem> salvarPostagem(@Body Postagem postagem);

    //formato xml
    //userId=1234&title=Título postagem&body=Corpo postagem
    @FormUrlEncoded
    @POST("/posts")
    Call <Postagem> salvarPostagem(
            @Field("userId") String userId,
            @Field("title") String title,
            @Field("body") String body
    );

}
