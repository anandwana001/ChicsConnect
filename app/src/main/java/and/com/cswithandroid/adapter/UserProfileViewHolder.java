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

public class UserProfileViewHolder extends RecyclerView.ViewHolder{

    View Userview;

    public UserProfileViewHolder(View itemView) {
        super(itemView);
        Userview = itemView;
    }
    public void setUserName(String name){
        TextView user_name = (TextView) Userview.findViewById(R.id.user_name);
        user_name.setText(name);
    }
    public void setUserBio(String bio){
        TextView user_bio = (TextView) Userview.findViewById(R.id.user_bio);
        user_bio.setText(bio);
    }
    public void setUserImage(Context context, String image){
        ImageView user_image = (ImageView) Userview.findViewById(R.id.user_image);
        Glide.with(context).load(image).into(user_image);
    }
}
