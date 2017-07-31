package and.com.cswithandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import and.com.cswithandroid.R;

/**
 * Created by dell on 29-06-2017.
 */

public class EventViewHolder extends RecyclerView.ViewHolder{

    public View view;

    public EventViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setName(String name){
        TextView event_name = (TextView) view.findViewById(R.id.event_name);
        event_name.setText(name);
    }
    public void setDate(String date){
        TextView event_date = (TextView) view.findViewById(R.id.event_date);
        event_date.setText(date);
    }
    public void setFees(String fees){
        TextView event_fees = (TextView) view.findViewById(R.id.event_fees);
        event_fees.setText(fees);
    }
    public void setType(String type){
        TextView event_type = (TextView) view.findViewById(R.id.event_type);
        event_type.setText(type);
    }
    public void setCity(String city){
        TextView event_city = (TextView) view.findViewById(R.id.event_city);
        event_city.setText(city);
    }
    public void setImage(Context cOntext, String image){
        ImageView event_type = (ImageView) view.findViewById(R.id.thumbnail);
        Glide.with(cOntext).load(image).into(event_type);
    }

}