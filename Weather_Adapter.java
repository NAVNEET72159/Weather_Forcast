package com.example.weatherforcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Weather_Adapter extends RecyclerView.Adapter<Weather_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Weather_Model> weather_Array_list;

    public Weather_Adapter(Context context, ArrayList<Weather_Model> weather_Array_list) {
        this.context = context;
        this.weather_Array_list = weather_Array_list;
    }

    @NonNull
    @Override
    public Weather_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Weather_Adapter.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Weather_Model model = weather_Array_list.get(position);
        holder.id_temp.setText(model.getTemperature()+"°C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.condition);
        holder.id_wind_speed.setText(model.getWindSpeed()+"Km/hr");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat outputdateFormat = new SimpleDateFormat("hh:mm aa");
        try {
            Date date = inputDateFormat.parse(model.getTime());
            holder.id_time.setText(outputdateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weather_Array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView id_time, id_temp, id_wind_speed;
        private ImageView condition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_time = itemView.findViewById(R.id.weather_time);
            id_temp = itemView.findViewById(R.id.temperature);
            id_wind_speed = itemView.findViewById(R.id.wind_speed);
        }
    }
}
