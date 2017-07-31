package and.com.cswithandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import and.com.cswithandroid.adapter.WorldViewHolder;
import and.com.cswithandroid.model.Users;
import and.com.cswithandroid.model.World;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_profile_image)
    ImageView userProfileImage;
    @BindView(R.id.user_profile_name)
    TextView userProfileName;
    @BindView(R.id.user_profile_email)
    TextView userProfileEmail;
    @BindView(R.id.send_message)
    Button sendMessage;

    @BindView(R.id.user_profile_bio)
    TextView userProfileBio;
    @BindView(R.id.user_post_list)
    RecyclerView userPostList;

    private Users model;
    private DatabaseReference databaseReference;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceWorldPost;

    private String KEY_LAYOUT_MANAGER = "list_state";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        sendMessage.setVisibility(View.GONE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceWorldPost = firebaseDatabase.getReference().child("World_activities");

        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String country = sharedPreferences.getString("country", "India");
        String state = sharedPreferences.getString("state", "Rajasthan");
        String city = sharedPreferences.getString("city", "Udaipur");

        if (getIntent().getParcelableExtra("model") != null) {
            model = getIntent().getParcelableExtra("model");
            Glide.with(ProfileActivity.this).load(model.getUserImage()).into(userProfileImage);
            userProfileName.setText(model.getUserName());
            userProfileEmail.setText(model.getEmailid());
            userProfileBio.setText(model.getUserBio());
            if (model.getEmailid() == FirebaseAuth.getInstance().getCurrentUser().getEmail())
                sendMessage.setVisibility(View.GONE);
            else
                sendMessage.setVisibility(View.VISIBLE);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(country).child(state).child(city).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Glide.with(ProfileActivity.this).load(dataSnapshot.child("UserImage").getValue().toString().replace("\\", "")).into(userProfileImage);
                    userProfileName.setText(dataSnapshot.child("UserName").getValue().toString());
                    userProfileEmail.setText(dataSnapshot.child("emailid").getValue().toString());
                    userProfileBio.setText(dataSnapshot.child("UserBio").getValue().toString());
                    sendMessage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

       /* sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPersonalChatIntent = new Intent(ProfileActivity.this, PersonalChatActivity.class);
                startPersonalChatIntent.putExtra("reciever_uid",model.getUserUid());
                startActivity(startPersonalChatIntent);
            }
        });*/
        FirebaseRecyclerAdapter<World, WorldViewHolder> eventEventViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<World, WorldViewHolder>(
                World.class,
                R.layout.content_world_post,
                WorldViewHolder.class,
                databaseReferenceWorldPost.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
        ) {
            @Override
            protected void populateViewHolder(WorldViewHolder viewHolder, final World model, final int position) {
                viewHolder.setUserName(model.getUsername());
                viewHolder.setCaption(model.getCaption());
                viewHolder.setPostImage(ProfileActivity.this,model.getImage());
                viewHolder.setProfilePic(ProfileActivity.this, model.getUserpic());
            }
        };
        userPostList.setAdapter(eventEventViewHolderFirebaseRecyclerAdapter);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (userPostList.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) userPostList.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        userPostList.setLayoutManager(mLayoutManager);
        userPostList.scrollToPosition(scrollPosition);
    }
}