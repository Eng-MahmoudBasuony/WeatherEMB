package mymobileapp.code.mbasuony.weatheremb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mymobileapp.code.mbasuony.weatheremb.R;
import mymobileapp.code.mbasuony.weatheremb.adapter.WeatherForecastAdapter;
import mymobileapp.code.mbasuony.weatheremb.common.Common;
import mymobileapp.code.mbasuony.weatheremb.model.WeatherForecastResualt;
import mymobileapp.code.mbasuony.weatheremb.retrofit.IOpenWeatherMap;
import mymobileapp.code.mbasuony.weatheremb.retrofit.RetrofitClient;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment
{

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    WeatherForecastAdapter weatherForecastAdapter;

    private TextView textCityName;
    private TextView textGeoCoords;
    private RecyclerView recyclerViewForecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance()
    {
        if (instance==null)
            instance=new ForecastFragment();

        return instance;
    }


    public ForecastFragment()
    {
        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstanceRetrofit();
        mService=retrofit.create(IOpenWeatherMap.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_forecast, container, false);

        textCityName=view.findViewById(R.id.text_city_name_forecast);
        textGeoCoords=view.findViewById(R.id.text_geo_coords_forecast);

        recyclerViewForecast=view.findViewById(R.id.recycler_forecast);
        recyclerViewForecast.setHasFixedSize(true);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL,false));

        getWeatherForecastInformation();

        return view ;
    }

    private void getWeatherForecastInformation()
    {
         compositeDisposable.add(mService.getForecastWeatherByLatLon(String.valueOf(Common.current_location.getLatitude()),
                                                                     String.valueOf(Common.current_location.getLongitude()),
                                                                     Common.APP_ID,
                                                                 "metric")
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Consumer<WeatherForecastResualt>() {
                     @Override
                     public void accept(WeatherForecastResualt weatherForecastResualt) throws Exception
                     {
                         desiplayForecastWeather(weatherForecastResualt);
                     }
                 }, new Consumer<Throwable>()
                 {
                     @Override
                     public void accept(Throwable throwable) throws Exception
                     {
                         Log.e("Error",throwable.getMessage());
                     }
                 })
         );

    }

    private void desiplayForecastWeather(WeatherForecastResualt weatherForecastResualt)
    {
        textCityName.setText(new StringBuilder(weatherForecastResualt.city.name).toString());
        textGeoCoords.setText(new StringBuilder(weatherForecastResualt.city.coord.toString()));

        weatherForecastAdapter=new WeatherForecastAdapter(getContext(),weatherForecastResualt);
        recyclerViewForecast.setAdapter(weatherForecastAdapter);

    }

    @Override
    public void onStop()
    {
        compositeDisposable.clear();
        super.onStop();
    }
}
