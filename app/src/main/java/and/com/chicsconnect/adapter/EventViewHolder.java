package and.com.chicsconnect.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import and.com.chicsconnect.R;

/**
 * Created by dell on 29-06-2017.
 */

public class EventViewHolder extends RecyclerView.ViewHolder{

    public View view;
    private int mMutedColor = 0xFF333333;

    public EventViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setName(String name){
        TextView event_name = (TextView) view.findViewById(R.id.event_name);
        event_name.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "OpenSans-Regular.ttf"));
        event_name.setText(name);
    }
    public void setDate(String date){
        TextView event_date = (TextView) view.findViewById(R.id.event_date);
        event_date.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "OpenSans-Light.ttf"));
        event_date.setText(date);
    }
    public void setFees(String fees){
        TextView event_fees = (TextView) view.findViewById(R.id.event_fees);
        event_fees.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "OpenSans-Light.ttf"));
        event_fees.setText(fees);
    }
    public void setType(String type){
        TextView event_type = (TextView) view.findViewById(R.id.event_type);
        event_type.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "OpenSans-Light.ttf"));
        event_type.setText(type);
    }
    public void setCity(String city){
        TextView event_city = (TextView) view.findViewById(R.id.event_city);
        event_city.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "OpenSans-Light.ttf"));
        event_city.setText(city);
    }
    public void setImage(Context cOntext, String image){
        ImageView event_type = (ImageView) view.findViewById(R.id.thumbnail);
        final TextView event_fees = (TextView) view.findViewById(R.id.event_fees);

        Glide.with(cOntext)
                .load(image)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        if (resource != null) {
                            Palette p = Palette.generate(resource, 12);
                            mMutedColor = p.getDarkMutedColor(0xFF333333);
                            event_fees.setBackgroundColor(mMutedColor);
                        }
                    }});

        Glide.with(cOntext).load(image).into(event_type);
    }

}