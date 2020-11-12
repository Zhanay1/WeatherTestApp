package com.example.weather;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<Weather> weathers;
    private City city;
    private List<Weather> weathersAll;
    private String q;

    public WeatherAdapter() {
        weathers = new ArrayList<>();
        city = new City();
    }

    public void setQ(String q) {
        this.q = q;
    }
    public List<Weather> getWeathers() {
        return weathers;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_items, parent, false);
        return new WeatherViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public synchronized void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weathers.get(position);
        String name = weather.getName();
        if (name.toLowerCase().contains(q.toLowerCase()) && q != null) {
            int start = name.toLowerCase().indexOf(q.toLowerCase());
            int end = start + q.length();
            Spannable span  = new SpannableString(name);
            span.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_INTERMEDIATE);
            holder.textViewName.setText(span);
        } else {
            holder.textViewName.setText(name);
        }
            holder.textViewTemp.setText(weather.getTemp() + "°C");
            holder.textViewDescription.setText(weather.getDescription());

    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private TextView textViewTemp;
        private TextView textViewDescription;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTemp = itemView.findViewById(R.id.textViewTemp);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
    public void addCity(List<Weather> weathers){
        this.weathers.addAll(weathers);
        notifyDataSetChanged();
    }
    public synchronized void setCity(List<Weather> weathers) {
        this.weathers = weathers;
        this.weathersAll = new ArrayList<>(this.weathers);
        notifyDataSetChanged();
    }
    public void clear(){
        this.weathers.clear();
        notifyDataSetChanged();
    }

}
