package mymobileapp.code.mbasuony.weatheremb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mymobileapp.code.mbasuony.weatheremb.R;
import mymobileapp.code.mbasuony.weatheremb.common.Common;
import mymobileapp.code.mbasuony.weatheremb.model.WeatherResulet;
import mymobileapp.code.mbasuony.weatheremb.retrofit.IOpenWeatherMap;
import mymobileapp.code.mbasuony.weatheremb.retrofit.RetrofitClient;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment
{

  private ImageView imageWeather;
  private TextView  textCityName;
  private TextView  textGeoCoords;
  private TextView  textPressure;
  private TextView  textHumidity;
  private TextView  textSunrise;
  private TextView  textSunset;
  private TextView  textTempreature;
  private TextView  textDescription;
  private TextView  textDateTime;
  private TextView  textWinSpeed;
  private TextView  textWinDeg;
  private LinearLayout weatherPanel;
  private ProgressBar loading;

   CompositeDisposable compositeDisposable;
   IOpenWeatherMap mService;

  private View itemView;


  static TodayWeatherFragment instance;

  public static TodayWeatherFragment getInstance()
  {
      if (instance==null)
                instance=new TodayWeatherFragment();

      return instance;
  }

    public TodayWeatherFragment()
    {
        //Rx
        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstanceRetrofit();
        mService=retrofit.create(IOpenWeatherMap.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
         itemView= inflater.inflate(R.layout.fragment_today, container, false);
         initualView();

         getWeatherInformation();

        return itemView;
    }

    private void initualView()
    {
        imageWeather= itemView.findViewById(R.id.image_weather);
        textCityName= itemView.findViewById(R.id.text_city_name);
        textPressure= itemView.findViewById(R.id.text_pressure);
        textHumidity=itemView.findViewById(R.id.text_humidity);
        textSunrise= itemView.findViewById(R.id.text_sunrise);
        textSunset= itemView.findViewById(R.id.text_sunset);
        textGeoCoords= itemView.findViewById(R.id.text_geo_coords);
        textTempreature=itemView.findViewById(R.id.text_temperature);
        textDescription=itemView.findViewById(R.id.text_description);
        textDateTime=itemView.findViewById(R.id.text_date_time);
        textWinDeg=itemView.findViewById(R.id.text_wind_Deg);
        textWinSpeed=itemView.findViewById(R.id.text_wind_Speed);
        weatherPanel=itemView.findViewById(R.id.weather_panel);
        loading=itemView.findViewById(R.id.progress_circular);
    }

    private void getWeatherInformation()
    {

        final String weather_in=getResources().getString(R.string.Weather_in);
        final String winDeg=getResources().getString(R.string.Deg);
        final String winSpeed=getResources().getString(R.string.Speed);
        final String unitPreature=getResources().getString(R.string.unit_pre);

        compositeDisposable.add(mService.getWeatherByLatLon(String.valueOf(Common.current_location.getLatitude()),
                                                            String.valueOf(Common.current_location.getLongitude()),Common.APP_ID,"metric")
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(new Consumer<WeatherResulet>() {
                                   @Override
                                   public void accept(WeatherResulet weatherResulet) throws Exception
                                   {
                                        //load Image
                                       //load Image
                                      Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                               .append(weatherResulet.getWeather().get(0).getIcon())
                                               .append(".png").toString()).into(imageWeather);

                                       //---Load Information
                                       textCityName.setText(weatherResulet.getName());
                                       textDescription.setText(new StringBuilder(weather_in+" ")
                                                                             .append(weatherResulet.getName()).toString());
                                       textWinDeg.setText(new StringBuilder(winDeg)
                                                                              .append(weatherResulet.getWind().getDeg()).toString());
                                       textWinSpeed.setText(new StringBuilder(winSpeed)
                                                                            .append(weatherResulet.getWind().getSpeed()).toString());
                                       textTempreature.setText(new StringBuilder(String.valueOf(weatherResulet.getMain().getTemp()))
                                                                            .append("°C").toString());
                                       textPressure.setText(new StringBuilder(String.valueOf(weatherResulet.getMain().getPressure()))
                                                                              .append(unitPreature).toString());
                                       textGeoCoords.setText(new StringBuilder("[").append(weatherResulet.getCoord().toString())
                                                                             .append("]").toString());
                                       textHumidity.setText(new StringBuilder(String.valueOf(weatherResulet.getMain().getHumidity()))
                                                                             .append(" %").toString());
                                       textSunrise.setText(Common.convertUnixToHour(weatherResulet.getSys().getSunrise()));
                                       textSunset.setText(Common.convertUnixToHour(weatherResulet.getSys().getSunset()));
                                       textDateTime.setText(Common.convertUnixToDate(weatherResulet.getDt()));

                                       //Display Panel
                                        weatherPanel.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.GONE);

                                   }
                               }, new Consumer<Throwable>()
                               {
                                   @Override
                                   public void accept(Throwable throwable) throws Exception
                                   {
                                       Toast.makeText(getActivity(), "throwable " +throwable.getMessage(), Toast.LENGTH_LONG).show();
                                       Log.d("weBN",throwable.toString());
                                      // Toast.makeText(getActivity(), "Longtatud ="+Common.current_location.getLongitude(), Toast.LENGTH_LONG).show();

                                   }
                               })
               );
    }

    @Override
    public void onStop()
    {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
