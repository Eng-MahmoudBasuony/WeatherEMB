package mymobileapp.code.mbasuony.weatheremb.retrofit;

import io.reactivex.Observable;
import mymobileapp.code.mbasuony.weatheremb.model.WeatherForecastResualt;
import mymobileapp.code.mbasuony.weatheremb.model.WeatherResulet;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap
{

    @GET("weather")
    Observable<WeatherResulet> getWeatherByLatLon(@Query("lat") String lat,
                                                  @Query("lon")String lon,
                                                  @Query("appid")String appid,
                                                  @Query("units")String unit);


    //Forecast
    @GET("forecast")
    Observable<WeatherForecastResualt> getForecastWeatherByLatLon(@Query("lat") String lat,
                                                                  @Query("lon")String lon,
                                                                  @Query("appid")String appid,
                                                                  @Query("units")String unit);

    //City list
    @GET("weather")
    Observable<WeatherResulet> getWeatherByCityName(@Query("q") String cityName,
                                                    @Query("appid")String appid,
                                                    @Query("units")String unit);


}
