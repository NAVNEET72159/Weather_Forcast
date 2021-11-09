package com.example.weatherforcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private ImageView search, weather_condition;
    private RelativeLayout relativeLayout;
    private TextView city_name, weather_condition_name, Temp;
    private TextInputEditText edit_city;
    private RecyclerView recyclerView;
    private ArrayList<Weather_Model>  weatherModel;
    private Weather_Adapter adapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String CityName;
    private custm_loading_dialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        search = findViewById(R.id.search);
        weather_condition = findViewById(R.id.weather_condition);
        relativeLayout = findViewById(R.id.relativeLayout);
        city_name = findViewById(R.id.city_name);
        weather_condition_name = findViewById(R.id.weather_condition_name);
        Temp = findViewById(R.id.Temp);
        edit_city = findViewById(R.id.edit_city);
        recyclerView = findViewById(R.id.recyclerView);
        weatherModel = new ArrayList<>();
        adapter = new Weather_Adapter(this,weatherModel);
        recyclerView.setAdapter(adapter);

        locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);

        loading_dialog = new custm_loading_dialog(HomeActivity.this);

        if (ActivityCompat.checkSelfPermission(this< Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this< Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        CityName = getCityName(location.getLongitude(), location.getLatitude());

        getWeatherInfo(CityName);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edit_city.getText().toString();
                if (city.isEmpty())
                {
                    Toast.makeText(this, "PLEASE ENTER A CITY NAME", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    city_name.setText(edit_city);
                    getWeatherInfo(city);
                    loading_dialog.startLoading();
                    getWeatherInfo(city_name);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please Provide Location Permission...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String city_name = "Not Found";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses){
                if (adr!=null){
                    String city = adr.getLocality();
                    if (city!=null && !city.equals("")){
                        city_name = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "USER CITY NOT FOUND", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return city_name;
    }
    public void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/current.json?key=1a9ce2218a634aee825175318212109&q="+cityName+"&aqi=yes";
        city_name.setText(edit_city);
        RequestQueue requestqueue = Volley.newRequestQueue(HomeActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading_dialog.dismissLoading();
                weatherModel.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    Temp.setText(temperature+"°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(weather_condition);
                    weather_condition_name.setText(condition);
                    if (isDay == 1)
                    {
                        Picasso.get().load("https://images.unsplash.com/photo-1471644806490-77c53366b18b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxhbGx8fHx8fHx8fHwxNjIzNDM1NDA4&ixlib=rb-1.2.1&q=80&w=1080&utm_source=unsplash_source&utm_medium=referral&utm_campaign=api-credit").into(relativeLayout);
                    }
                    else{
                        Picasso.get().load("https://images.unsplash.com/photo-1507502707541-f369a3b18502?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MXwxfDB8MXxhbGx8fHx8fHx8fA&ixlib=rb-1.2.1&q=80&w=1080&utm_source=unsplash_source&utm_medium=referral&utm_campaign=api-credit").into(relativeLayout);
                    }
                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecaste = forecast.getJSONArray("forecast").getJSONObject(0);
                    JSONArray hourArray = forecaste.getJSONArray("hour");
                    for (int i = 0; i<hourArray.length();i++)
                    {
                        JSONObject arrayObj = hourArray.getJSONObject(i);
                        String time = arrayObj.getString("time");
                        String temp = arrayObj.getString("time");
                        String img = arrayObj.getString("icon");
                        String wind = arrayObj.getString("wind_kph");
                        weatherModel.add(new Weather_Model(time,temp,img,wind));
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(this, "Please Enter Valid City Name...", Toast.LENGTH_SHORT).show();
            }
        });
        requestqueue.add(jsonObjectRequest);
    }
}