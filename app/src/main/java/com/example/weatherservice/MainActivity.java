package com.example.weatherservice;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnGetWeather;
    private TextView tvWeather, tvResult;
    public EditText etCity, etCountry;
    private final static String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final static String APP_ID = "26e6323eb2dbbbc71df32ed601e303e5";
    private final DecimalFormat df = new DecimalFormat("#.##");
    private String oldCitySearch;
    private DBSetupHelper dbSetupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetWeather = findViewById(R.id.btnGet);
        tvWeather = findViewById(R.id.tvWeather);
        tvResult = findViewById(R.id.tvResult);
        etCity = findViewById(R.id.etCityWeather);
        etCountry = findViewById(R.id.etCountryWeather);

        btnGetWeather.setOnClickListener(this::onClickWeatherDetails);

        dbSetupHelper = new DBSetupHelper(MainActivity.this);

    }

    public void onClickWeatherDetails(View v) {
        String tempUrl = "";
        String city = etCity.getText().toString();
        String country = etCountry.getText().toString();


        if (city.equals("")) {
            String emptyCity = "City field cannot be empty";
            tvResult.setText(emptyCity);
        } else {
            if (!country.equals("")) {
                tempUrl = URL + "?q=" + city + "," + country + "&APPID=" + APP_ID;
            } else {
                tempUrl = URL + "?q=" + city + "&appid=" + APP_ID;
            }


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, tempUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("response", "onResponse: " + response.toString());

                            try {
                                String description = "x";
                                JSONArray weather = response.getJSONArray("weather");

                                Log.d("response", "onResponse: " + weather.toString());
                                // ville testa både loopar och hämta ur object som gjort nedanför loopen
                                for (int i = 0; i < weather.length(); i++) {
                                    JSONObject weatherJSONObject = weather.getJSONObject(i);
                                    description = weatherJSONObject.getString("description");
                                }
                                JSONObject jsonObjectMain = response.getJSONObject("main");
                                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;


                                JSONObject jsonObjectWind = response.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");

                                JSONObject jsonObjectSys = response.getJSONObject("sys");
                                String countryName = jsonObjectSys.getString("country");
                                String cityName = response.getString("name");

                                String output = "temp: " + df.format(temp)
                                        + "°C\nfeels like: " + df.format(feelsLike)
                                        + "°C\ndescription: " + description
                                        + "\nWind speed: " + wind + "m/s"
                                        + "\nCity: " + cityName + "\nCountry: " + countryName;

                                tvResult.setText(output);
                                String weatherInfo = df.format(temp) + "°C\n" + description;

                                if (!etCity.getText().toString().equals(oldCitySearch)) {
                                    String xx = etCity.getText().toString();
                                    Log.d("xx", "onResponse: " + xx);
                                    oldCitySearch = xx;
                                    startService(cityName, weatherInfo);
                                }
                                ////////////////////////////////////// det kraschar pga databasen, hitta varför!!!!!
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();
                                String dateToDb = format.format(date);
                                Log.d("DATEEEE", "onResponse: " + dateToDb);
                                dbSetupHelper.addValues(dateToDb, cityName, String.valueOf(temp), description);
                            } catch (JSONException e) {
                                Log.d("Errorrrr", "onResponse: " + e);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String didNotWork = "That search wasn't sufficient";
                    tvResult.setText(didNotWork);
                    Log.d("error", "onErrorResponse: " + error);
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonRequest);
            Log.d("btn", "onClickWeatherDetails: ");
        }
    }

    private void startService(String city, String weatherDescriptionInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        bundle.putString("weatherDescriptionInfo", weatherDescriptionInfo);

        Intent intent = new Intent(this, ServiceWeatherNotifications.class);
        intent.putExtra("weatherInfo", bundle);

        ContextCompat.startForegroundService(this, intent);
    }

    public void gotoView(View view){
        Intent nextIntent = new Intent(this, dbActivity.class);
        nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(nextIntent);

    }
}