package br.com.barrsoft.darkskyclient.services;

import br.com.barrsoft.darkskyclient.models.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherService {

    @GET("{latitude},{longitude}")
    //chamada para a classe model Weather
    Call<Weather> getWeather(@Path("latitude") double latitude, @Path("longitude") double longitude);

}
