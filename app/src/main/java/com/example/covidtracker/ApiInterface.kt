package com.example.covidtracker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {

    @GET("countries")
    fun getCountryData() : Call<List<ModelClass>>

    companion object {
        private const val BASE_URL = "https://disease.sh/v3/covid-19/"

        fun getApiInterface(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }



}