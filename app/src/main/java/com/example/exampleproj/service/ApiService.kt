package com.example.exampleproj.service

import com.example.exampleproj.responses.DrinksResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object BaseCaller{
    val caller: ApiService = Retrofit.Builder()
        .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiService::class.java)
}

interface ApiService{
    @GET("filter.php?a=Non_Alcoholic")
    suspend fun getAllNonAlcoholic() : DrinksResponse
    @GET("filter.php?a=Alcoholic")
    suspend fun getAllAlcoholic() : DrinksResponse
    @GET("lookup.php")
    suspend fun getDrinkById(@Query("i") drinkId: String) : DrinksResponse
}
