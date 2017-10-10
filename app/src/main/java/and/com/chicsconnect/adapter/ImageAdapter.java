package and.com.chicsconnect.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import and.com.chicsconnect.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 01-10-2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private List<Uri> imageList;
    private Context context;

    public ImageAdapter(List<Uri> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_image_world, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position)).into(holder.imageUploadShow);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.image_upload_show)
        ImageView imageUploadShow;
        @BindView(R.id.remove)
        ImageView remove;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.equals(remove)){
                removeAt(getAdapterPosition());
            }
        }
    }


    public void removeAt(int position) {
        imageList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, imageList.size());
    }
}
