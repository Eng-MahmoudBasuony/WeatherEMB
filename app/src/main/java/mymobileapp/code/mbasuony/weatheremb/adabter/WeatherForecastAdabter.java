package mymobileapp.code.mbasuony.weatheremb.adabter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mymobileapp.code.mbasuony.weatheremb.R;
import mymobileapp.code.mbasuony.weatheremb.common.Common;
import mymobileapp.code.mbasuony.weatheremb.model.WeatherForecastResualt;

public class WeatherForecastAdabter extends RecyclerView.Adapter<WeatherForecastAdabter.MyViewHolder>
{

    private Context context;
    private WeatherForecastResualt weatherForecastResualt;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
      View itemView= LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,viewGroup,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {

        //load Image
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResualt.getList().get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(myViewHolder.imageWeather);

        myViewHolder.textDateTime.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResualt.list.get(position).dt)).toString());
        myViewHolder.textDescription.setText(new StringBuilder(weatherForecastResualt.list.get(position).weather.get(0).getDescription()).toString());
        myViewHolder.textTempreature.setText(new StringBuilder(String.valueOf(weatherForecastResualt.list.get(position).main.getTemp())).append("°C"));

    }

    @Override
    public int getItemCount()
    {
        return weatherForecastResualt.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView imageWeather;
        private TextView  textTempreature;
        private TextView  textDescription;
        private TextView  textDateTime;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imageWeather=itemView.findViewById(R.id.image_weather_item);
            textTempreature=itemView.findViewById(R.id.text_temperature_item);
            textDescription=itemView.findViewById(R.id.text_description);
            textDateTime=itemView.findViewById(R.id.text_date_item);

        }
    }

}
