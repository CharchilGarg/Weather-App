package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textViewCity,textViewTemp,textViewWeatherCond
            ,textViewHumidity,textViewMaxTemp,textViewMinTemp,textViewPressure,textViewWind;
    ImageView imageView;
    FloatingActionButton flo;
    LocationListener locationListener;
    LocationManager locationManager;
    Double lon,lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCity = findViewById(R.id.textviewCity);
        textViewTemp = findViewById(R.id.textviewTemp);
        textViewWeatherCond = findViewById(R.id.textviewWeatherCond);
        textViewHumidity = findViewById(R.id.humidity);
        textViewMaxTemp = findViewById(R.id.maxtemp);
        textViewMinTemp = findViewById(R.id.mintemp);
        textViewPressure = findViewById(R.id.pressure);
        textViewWind = findViewById(R.id.wind);
        imageView = findViewById(R.id.imageview);
        flo = findViewById(R.id.flot);

        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,WeatherActivity.class);
                startActivity(i);
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                lon = location.getLongitude();
                lat = location.getLatitude();

                Log.e("Lat : ",String.valueOf(lat));
                Log.e("Lon : ",String.valueOf(lon));

                getWeatherData(lat,lon);
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
                    ,1);
        }
        else
        {
            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission
                (this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    public void getWeatherData(Double lat,Double lon)
    {
        WeatherApi weatherApi = RetrofitWeather.getclinet().create(WeatherApi.class);
        Call<OpenWeatherMap> call = weatherApi.getWeatherWithLocation(lat,lon);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful())
                {
                    textViewCity.setText(response.body().getName());
                    textViewTemp.setText("" +response.body().getMain().getTemp());
                    textViewWeatherCond.setText(response.body().getWeather().get(0).getDescription());
                    textViewHumidity.setText(" : "+response.body().getMain().getHumidity()+"%");
                    textViewMaxTemp.setText(" : "+response.body().getMain().getTempMax()+"°C");
                    textViewMinTemp.setText(" : "+response.body().getMain().getTempMin()+"°C");
                    textViewPressure.setText(" : "+response.body().getMain().getPressure());
                    textViewWind.setText(" : "+response.body().getWind().getSpeed());

                    String imgIcon = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/"+imgIcon+"@2x.png")
                       .placeholder(R.drawable.ic_launcher_foreground)
                       .into(imageView);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Toast.makeText(MainActivity.this, "There is some error ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}