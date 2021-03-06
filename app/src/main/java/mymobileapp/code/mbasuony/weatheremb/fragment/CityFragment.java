package mymobileapp.code.mbasuony.weatheremb.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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
public class CityFragment extends Fragment
{

     List<String> listCities;
    private MaterialSearchBar searchBar;


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
    private LinearLayout weatherPanel;
    private ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    private View itemView;


     static CityFragment instance;

    public static CityFragment getInstance()
    {
        if (instance==null)
            instance=new CityFragment();

        return instance;
    }

    public CityFragment()
    {   //Rx
        compositeDisposable=new CompositeDisposable();

        Retrofit retrofit=RetrofitClient.getInstanceRetrofit();
        mService=retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

          // Inflate the layout for this fragment
           itemView= inflater.inflate(R.layout.fragment_city, container, false);

        initualView();
        //Load Cities
        new LoadCities().execute();





        return itemView;
    }





    private void initualView()
    {
        imageWeather= itemView.findViewById(R.id.image_weather_searchbar);
        textCityName= itemView.findViewById(R.id.text_city_name_searchbar);
        textPressure= itemView.findViewById(R.id.text_pressure_searchbar);
        textHumidity=itemView.findViewById(R.id.text_humidity_searchbar);
        textSunrise= itemView.findViewById(R.id.text_sunrise_searchbar);
        textSunset= itemView.findViewById(R.id.text_sunset_searchbar);
        textGeoCoords= itemView.findViewById(R.id.text_geo_coords_searchbar);
        textTempreature=itemView.findViewById(R.id.text_temperature_searchbar);
        textDescription=itemView.findViewById(R.id.text_description_searchbar);
        textDateTime=itemView.findViewById(R.id.text_date_time_searchbar);
        weatherPanel=itemView.findViewById(R.id.weather_panel_searchbar);
        loading=itemView.findViewById(R.id.progress_circular_searchbar);
        searchBar=itemView.findViewById(R.id.search_bar_city);
        searchBar.setEnabled(false);
    }

    //AsyncTask class to Load Cities List
    private class LoadCities extends SimpleAsyncTask<List<String>>
    {

        @Override
        protected List<String> doInBackgroundSimple()
        {

            listCities = new ArrayList<>();
            try
            {

             StringBuilder builder=new StringBuilder();


             InputStream inputStream=getResources().openRawResource(R.raw.city_list);
             // * This class implements a stream Filter for reading compressed data in
             // * the GZIP file format.
             GZIPInputStream gzipInputStream=new GZIPInputStream(inputStream);

             InputStreamReader streamReader=new InputStreamReader(gzipInputStream);
             BufferedReader in=new BufferedReader(streamReader);

             String readed;

             while ((readed=in.readLine()) != null)
             {
                 builder.append(readed);

                 listCities=new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());
             }

            }catch (IOException e)
            {
               e.printStackTrace();
            }

            return listCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity)
        {
            super.onSuccess(listCity);

            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                   List<String>suggest=new ArrayList<>();

                    for (String search:listCity)
                    {
                        if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        {
                            suggest.add(search);
                        }
                    }
                    searchBar.setLastSuggestions(suggest);
                }

                @Override
                public void afterTextChanged(Editable s)
                {


                }
            });

            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener()
            {
                @Override
                public void onSearchStateChanged(boolean enabled)
                {

                }

                @Override
                public void onSearchConfirmed(CharSequence text)
                {
                    getWeatherInformation(text.toString());
                    searchBar.setLastSuggestions(listCity);
                }

                @Override
                public void onButtonClicked(int buttonCode)
                {

                }
            });

            searchBar.setLastSuggestions(listCity);

            loading.setVisibility(View.GONE);
          //  weatherPanel.setVisibility(View.VISIBLE);

        }



    }

    private void getWeatherInformation(String cityName)
    {
        compositeDisposable.add(mService.getWeatherByCityName(cityName,Common.APP_ID,"metric")
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
                        textDescription.setText(new StringBuilder("Weather in ")
                                .append(weatherResulet.getName()).toString());
                        textTempreature.setText(new StringBuilder(String.valueOf(weatherResulet.getMain().getTemp()))
                                .append("°C").toString());
                        textDateTime.setText(Common.convertUnixToDate(weatherResulet.getDt()));
                        textPressure.setText(new StringBuilder(String.valueOf(weatherResulet.getMain().getPressure()))
                                .append(" hpa").toString());
                        textHumidity.setText(new StringBuilder(String.valueOf(weatherResulet.getMain().getHumidity())).append(" %").toString());
                        textSunrise.setText(Common.convertUnixToHour(weatherResulet.getSys().getSunrise()));
                        textSunset.setText(Common.convertUnixToHour(weatherResulet.getSys().getSunset()));
                        textGeoCoords.setText(new StringBuilder("[").append(weatherResulet.getCoord().toString())
                                .append("]").toString());

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
    public void onDestroy()
    {
       compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop()
    {
        compositeDisposable.clear();
        super.onStop();
    }





}
