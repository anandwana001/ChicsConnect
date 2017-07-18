package and.com.cswithandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import and.com.cswithandroid.model.Users;
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

    private Users model;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        sendMessage.setVisibility(View.GONE);

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
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPersonalChatIntent = new Intent(ProfileActivity.this, PersonalChatActivity.class);
                startPersonalChatIntent.putExtra("reciever_uid",model.getUserUid());
                startActivity(startPersonalChatIntent);
            }
        });
    }
}