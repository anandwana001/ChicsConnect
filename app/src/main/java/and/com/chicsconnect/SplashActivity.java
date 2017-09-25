package and.com.chicsconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 29-06-2017.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.map)
    ImageView map;
    @BindView(R.id.user)
    ImageView user;
    @BindView(R.id.event)
    ImageView event;
    @BindView(R.id.world)
    ImageView world;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView5)
    TextView textView5;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient googleApiClient;

    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (mFirebaseUser == null) {
                    Intent loginIntent = new Intent(SplashActivity.this, and.com.chicsconnect.LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        checkUserExist();
    }

    private void checkUserExist() {

        if (mFirebaseAuth.getCurrentUser() != null) {
            final String user_id = mFirebaseAuth.getCurrentUser().getUid();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String country = sharedPreferences.getString("country", "India");
            String state = sharedPreferences.getString("state", "Rajasthan");
            String city = sharedPreferences.getString("city", "Udaipur");

            userDatabaseReference.child(country).child(state).child(city).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {
                        Intent setupAccountIntent = new Intent(SplashActivity.this, and.com.chicsconnect.SetupAccountActivity.class);
                        setupAccountIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupAccountIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView2.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        textView3.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        textView4.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        textView5.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Regular.ttf"));
        textView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        googleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.map)
    public void onViewClickedMap() {
        Intent intent = new Intent(this, and.com.chicsconnect.MainActivity.class);
        intent.putExtra("check", R.id.navigation_home);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.user)
    public void onViewClickedUser() {
        Intent intent = new Intent(this, and.com.chicsconnect.MainActivity.class);
        intent.putExtra("check", R.id.navigation_notifications);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.event)
    public void onViewClickedEvent() {
        Intent intent = new Intent(this, and.com.chicsconnect.MainActivity.class);
        intent.putExtra("check", R.id.navigation_dashboard);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.world)
    public void onViewClickedWorld() {
        Intent intent = new Intent(this, and.com.chicsconnect.MainActivity.class);
        intent.putExtra("check", R.id.navigation_world);
        startActivity(intent);
        finish();
    }
}
