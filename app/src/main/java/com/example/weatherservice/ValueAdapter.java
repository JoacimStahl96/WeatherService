package com.example.weatherservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.ViewHolder> {

    ArrayList<Weatherbean> localWeatherBeans;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView valueDate, valueCity, valueTemp, valueDesc;


        public ViewHolder(View itemView) {
            super(itemView);

            valueDate = itemView.findViewById(R.id.valueDate);
            valueCity = itemView.findViewById(R.id.valueCity);
            valueTemp = itemView.findViewById(R.id.valueTemp);
            valueDesc = itemView.findViewById(R.id.valueDesc);
        }

        public TextView getValueDate() {
            return valueDate;
        }

        public TextView getValueCity() {
            return valueCity;
        }

        public TextView getValueTemp() {
            return valueTemp;
        }

        public TextView getValueDesc() {
            return valueDesc;
        }
    }


    public ValueAdapter(ArrayList<Weatherbean> valueBeans) {
        localWeatherBeans = valueBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dbvalue_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ValueAdapter.ViewHolder holder, int position) {
        // this is where you connect content and the list

        holder.getValueDate().setText(localWeatherBeans.get(position).getDate());
        holder.getValueCity().setText(localWeatherBeans.get(position).getCity());
        holder.getValueTemp().setText(localWeatherBeans.get(position).getTemperature());
        holder.getValueDesc().setText(localWeatherBeans.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return localWeatherBeans.size();
    }
}
