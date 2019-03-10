package mymobileapp.code.mbasuony.weatheremb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import mymobileapp.code.mbasuony.weatheremb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment
{

  static TodayWeatherFragment instance;

  public static TodayWeatherFragment getInstance()
  {
      if (instance==null)
                instance=new TodayWeatherFragment();

      return instance;
  }


    public TodayWeatherFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

}