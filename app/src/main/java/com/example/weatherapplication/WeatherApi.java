package com.example.weatherapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("weather?appid=7112fac2834ae7518ce0e354b8ce6710&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat,@Query("lon")double lot);

    @GET("weather?appid=7112fac2834ae7518ce0e354b8ce6710&units=metric")
    Call<OpenWeatherMap>getWeatherWithName(@Query("q") String name);

}
