package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    TextView textViewCityWeather,textViewTempWeather,textViewWeatherCondWeather
            ,textViewHumidityWeather,textViewMaxTempWeather
            ,textViewMinTempWeather,textViewPressureWeather,textViewWindWeather;
    ImageView imageViewWeather;
    Button search;
    EditText cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        textViewCityWeather= findViewById(R.id.textviewCityWeather);
        textViewTempWeather = findViewById(R.id.textviewTempWeather);
        textViewWeatherCondWeather = findViewById(R.id.textviewWeatherCondWeather);
        textViewHumidityWeather = findViewById(R.id.humidityWeather);
        textViewMaxTempWeather = findViewById(R.id.maxtempWeather);
        textViewMinTempWeather = findViewById(R.id.mintempWeather);
        textViewPressureWeather = findViewById(R.id.pressureWeather);
        textViewWindWeather = findViewById(R.id.windWeather);
        imageViewWeather = findViewById(R.id.imageviewWeather);

        search = findViewById(R.id.search);
        cityName = findViewById(R.id.editTextCityName);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityName.getText().toString();

                cityName.setText("");

                getWeatherData(city);
            }
        });
    }


    public void getWeatherData(String s)
    {
        WeatherApi weatherApi = RetrofitWeather.getclinet().create(WeatherApi.class);
        Call<OpenWeatherMap> call = weatherApi.getWeatherWithName(s);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful())
                {
                    textViewCityWeather.setText(response.body().getName());
                    textViewTempWeather.setText("" +response.body().getMain().getTemp());
                    textViewWeatherCondWeather.setText(response.body().getWeather().get(0).getDescription());
                    textViewHumidityWeather.setText(" : "+response.body().getMain().getHumidity()+"%");
                    textViewMaxTempWeather.setText(" : "+response.body().getMain().getTempMax()+"°C");
                    textViewMinTempWeather.setText(" : "+response.body().getMain().getTempMin()+"°C");
                    textViewPressureWeather.setText(" : "+response.body().getMain().getPressure());
                    textViewWindWeather.setText(" : "+response.body().getWind().getSpeed());

                    String imgIcon = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/"+imgIcon+"@2x.png")
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(imageViewWeather);
                }
                else
                {
                    Toast.makeText(WeatherActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Toast.makeText(WeatherActivity.this, "There is some error ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}