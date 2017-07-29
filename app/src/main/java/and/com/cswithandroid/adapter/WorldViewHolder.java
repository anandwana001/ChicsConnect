package and.com.cswithandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import and.com.cswithandroid.R;

/**
 * Created by dell on 28-07-2017.
 */

public class WorldViewHolder extends RecyclerView.ViewHolder {

    public View view;

    public WorldViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public void setUserName(String userName){
        TextView  username = (TextView) view.findViewById(R.id.user_world_name);
        username.setText(userName);
    }
    public void setProfilePic(Context context, String profilePic){
        ImageView user_world_profile_pic = (ImageView) view.findViewById(R.id.user_world_image);
        Glide.with(context).load(profilePic).into(user_world_profile_pic);
    }
    public void setCaption(String caption){
        TextView  userCaption = (TextView) view.findViewById(R.id.user_world_caption);
        userCaption.setText(caption);
    }
    public void setPostImage(Context context, String postImage){
        ImageView user_world_post_image = (ImageView) view.findViewById(R.id.user_world_post_image);
        Glide.with(context).load(postImage).into(user_world_post_image);
    }
    public void setTimestamp(String timestamp){
        TextView  userTimestamp = (TextView) view.findViewById(R.id.user_world_timestamp);
        userTimestamp.setText(timestamp);
    }

}
