package and.com.chicsconnect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import and.com.chicsconnect.model.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailEventActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.event_image_dialog)
    ImageView eventImageDialog;
    @BindView(R.id.event_fees_dialog)
    TextView eventFeesDialog;
    @BindView(R.id.event_place)
    TextView eventPlace;
    @BindView(R.id.event_time)
    TextView eventTime;
    @BindView(R.id.event_des_dialog)
    TextView eventDesDialog;
    @BindView(R.id.event_creater_name)
    TextView eventCreaterName;

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.call_creator)
    FloatingActionButton callCreator;

    private Event event;

    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;

    private int mMutedColor = 0xFF333333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event = getIntent().getParcelableExtra("model");

        toolbarLayout.setTitle(event.getEvent_name());
        eventFeesDialog.setText(event.getFees());
        eventPlace.setText(event.getAddress() + ", " + event.getEvent_city() + ",\n" + event.getEvent_state() + ", " + event.getEvent_country());
        eventTime.setText(event.getEvent_date() + " " + event.getEvent_time()+ "-" + event.getEvent_end_time());
        eventDesDialog.setText(event.getEvent_des());
        eventCreaterName.setText(event.getCreater_name());

        Glide.with(this).load(event.getImage()).into(eventImageDialog);

        Glide.with(this)
                .load(event.getImage())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        if (resource != null) {
                            Palette p = Palette.generate(resource, 12);
                            mMutedColor = p.getDarkMutedColor(0xFF333333);
                            toolbarLayout.setContentScrimColor(mMutedColor);
                            toolbarLayout.setStatusBarScrimColor(mMutedColor);
                        }
                    }});

        callCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
    }

    private void call() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + event.getCreator_phone_number().toString().trim()));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            } else {
                Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventFeesDialog.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        eventPlace.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        eventTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        eventDesDialog.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        eventCreaterName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));

        textView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf"));
        textView2.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf"));
        textView3.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf"));
        textView4.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf"));
        textView5.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf"));
    }
}