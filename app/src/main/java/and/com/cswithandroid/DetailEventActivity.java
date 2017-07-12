package and.com.cswithandroid;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import and.com.cswithandroid.model.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailEventActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
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
    @BindView(R.id.event_join_dialog)
    Button eventJoinDialog;
    @BindView(R.id.event_chat_dialog)
    Button eventChatDialog;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event = getIntent().getParcelableExtra("model");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Glide.with(this).load(event.getImage()).into(eventImageDialog);
        toolbarLayout.setTitle(event.getEvent_name());
        eventFeesDialog.setText(event.getFees());
        eventPlace.setText(event.getAddress()+", "+event.getEvent_city()+",\n"+event.getEvent_state()+", "+event.getEvent_country());
        eventTime.setText(event.getTime()+", "+event.getEvent_date());
        eventDesDialog.setText(event.getEvent_des());
    }
}
