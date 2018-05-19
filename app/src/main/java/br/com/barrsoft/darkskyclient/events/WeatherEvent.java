package br.com.barrsoft.darkskyclient.events;

import br.com.barrsoft.darkskyclient.models.Weather;

public class WeatherEvent {

    private final Weather weather;

    public WeatherEvent(Weather weather) {
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
