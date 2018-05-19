package br.com.barrsoft.darkskyclient.services;

import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import br.com.barrsoft.darkskyclient.events.ErrorEvent;
import br.com.barrsoft.darkskyclient.events.WeatherEvent;
import br.com.barrsoft.darkskyclient.models.Currently;
import br.com.barrsoft.darkskyclient.models.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherServiceProvider {

    private static final String BASE_URL = "https://api.darksky.net/forecast/e5652eb741363497aa5daa652d756dae/";
    public static final String TAG = WeatherServiceProvider.class.getName();
    private Retrofit retrofit;

    private Retrofit getRetrofit() {
        //verificar existencia de instancia retrofit
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return this.retrofit;
    }

    public void getWeather(double latitude, double longitude){

        //criar a interface de servico
        WeatherService weatherService = getRetrofit().create(WeatherService.class);
        Call<Weather> weatherData =  weatherService.getWeather(latitude, longitude);
        //consulta
        weatherData.enqueue(new Callback<Weather>() {

            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather weather = response.body();
                if(weather == null){
                    Log.i(TAG, "Verificar a chave secreta");
                    EventBus.getDefault().post(new ErrorEvent("Verificar a chave secreta"));
                }else {
                    Currently currently = weather.getCurrently();
                    Log.e(TAG,"Temperatura: " + currently.getTemperature());
                    EventBus.getDefault().post(new WeatherEvent(weather));
                }
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.i(TAG, "Não foi possivel recuperar os dados");
                EventBus.getDefault().post(new ErrorEvent("Não foi possivel conexão com o servidor."));
            }
        });
    }
}
