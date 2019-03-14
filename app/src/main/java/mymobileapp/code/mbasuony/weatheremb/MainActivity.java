package mymobileapp.code.mbasuony.weatheremb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import mymobileapp.code.mbasuony.weatheremb.adapter.ViewPageAdapter;
import mymobileapp.code.mbasuony.weatheremb.common.Common;
import mymobileapp.code.mbasuony.weatheremb.fragment.CityFragment;
import mymobileapp.code.mbasuony.weatheremb.fragment.ForecastFragment;
import mymobileapp.code.mbasuony.weatheremb.fragment.TodayWeatherFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private String foreCastFive;
    private String today;
    private String cityNam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialView();
        requestPermission();


    }


    private void initialView()
    {
        coordinatorLayout = findViewById(R.id.root_layout);
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void requestPermission() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted())
                        {
                            buildLocationRequest();
                            buildLocationCallBack();

                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            {
                                return;
                            }
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                    {
                        Snackbar.make(coordinatorLayout,"Permission Denied ",Snackbar.LENGTH_LONG).show();
                    }
                }).check();




    }

    private void buildLocationRequest()
    {
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }
    private void buildLocationCallBack()
    {
       locationCallback=new LocationCallback()
       {
           @Override
           public void onLocationResult(LocationResult locationResult)
           {
               super.onLocationResult(locationResult);

               Common.current_location=locationResult.getLastLocation();
               viewPager=(ViewPager)findViewById(R.id.view_pager);
               viewPager.setBackgroundColor(Color.parseColor("#fbfad3"));
               setUpViewPager(viewPager);
                tabLayout = findViewById(R.id.tabs);
               tabLayout.setupWithViewPager(viewPager);
               //log
               Log.d("Location",locationResult.getLastLocation().getLatitude()+","+locationResult.getLastLocation().getLongitude());

           }
       };
    }

    private void setUpViewPager(ViewPager viewPager)
    {
        foreCastFive=getResources().getString(R.string.forecastfive);
        today=getResources().getString(R.string.today_fragment);
        cityNam=getResources().getString(R.string.city_name);

        ViewPageAdapter adabter=new ViewPageAdapter(getSupportFragmentManager());
                        adabter.addFragment(TodayWeatherFragment.getInstance(),today);
                        adabter.addFragment(ForecastFragment.getInstance(),foreCastFive);
                        adabter.addFragment(CityFragment.getInstance(),cityNam);
                        viewPager.setAdapter(adabter);
    }

    @Override
    public void onBackPressed()
    {
        if (viewPager.getCurrentItem() != 0)
        {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1,true);
        }else

            {
              finish();
            }
    }

}
