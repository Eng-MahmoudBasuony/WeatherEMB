package mymobileapp.code.mbasuony.weatheremb.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{


    private static Retrofit instance;



    public Retrofit getInstanceRetrofit()
    {
        if (instance==null)
        {

            instance=new Retrofit.Builder()
                     .baseUrl("https://samples.openweathermap.org/data/2.5/")
                     .addConverterFactory(GsonConverterFactory.create())
                     .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                     .build();

        }

        return instance;
    }

}
