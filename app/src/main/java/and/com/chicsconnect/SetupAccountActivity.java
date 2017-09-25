package and.com.chicsconnect;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 29-06-2017.
 */

public class SetupAccountActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.user_profile_image)
    ImageView userProfileImage;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.user_emailid)
    TextView userEmailid;
    @BindView(R.id.user_bio)
    EditText userBio;
    @BindView(R.id.user_phone_number)
    EditText userPhoneNumber;
    @BindView(R.id.user_referral_code)
    EditText userReferralCode;
    @BindView(R.id.user_home_city)
    Spinner userHomeCity;
    @BindView(R.id.user_home_state)
    Spinner userHomeState;
    @BindView(R.id.user_home_country)
    Spinner userHomeCountry;
    @BindView(R.id.create_button)
    Button createButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    private DatabaseReference databaseReference;

    private DatabaseReference countryDatabaseReference;
    private DatabaseReference stateDatabaseReference;
    private DatabaseReference cityDatabaseReference;

    private FirebaseAuth mFirebaseAuth;
    private StorageReference mStorageRef;

    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private String user_home_country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setup_account);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        countryDatabaseReference = FirebaseDatabase.getInstance().getReference();
        stateDatabaseReference = FirebaseDatabase.getInstance().getReference().child("State");
        cityDatabaseReference = FirebaseDatabase.getInstance().getReference().child("City");

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Profile_images");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userEmailid.setText(mFirebaseAuth.getCurrentUser().getEmail());
        userHomeCountry.setPrompt("Select Your Country");

        countryDatabaseReference.child("Country").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> country = new ArrayList<String>();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    String countryName = Snapshot.child("name").getValue(String.class);
                    country.add(countryName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SetupAccountActivity.this, android.R.layout.simple_spinner_item, country);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userHomeCountry.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userHomeCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_home_country = parent.getItemAtPosition(position).toString();
                stateDatabaseReference.child(user_home_country).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<String> state = new ArrayList<String>();

                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                            String stateName = Snapshot.child("name").getValue(String.class);
                            state.add(stateName);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SetupAccountActivity.this, android.R.layout.simple_spinner_item, state);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userHomeState.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userHomeState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String user_home_state = parent.getItemAtPosition(position).toString();

                cityDatabaseReference.child(user_home_country).child(user_home_state).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<String> city = new ArrayList<String>();

                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                            String cityName = Snapshot.child("name").getValue(String.class);
                            city.add(cityName);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SetupAccountActivity.this, android.R.layout.simple_spinner_item, city);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userHomeCity.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userProfileImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Creating Profile...");
                progressDialog.show();
                SetupAccountActivity.this.startSetupAccount();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setTypeface(Typeface.createFromAsset(this.getResources().getAssets(), "OpenSans-Light.ttf"));
        userEmailid.setTypeface(Typeface.createFromAsset(this.getResources().getAssets(), "OpenSans-Light.ttf"));
        userBio.setTypeface(Typeface.createFromAsset(this.getResources().getAssets(), "OpenSans-Light.ttf"));
        userPhoneNumber.setTypeface(Typeface.createFromAsset(this.getResources().getAssets(), "OpenSans-Light.ttf"));
    }

    private void startSetupAccount() {

        final String user_name = userName.getText().toString().trim();
        final String user_bio = userBio.getText().toString().trim();
        final String user_phone_number = userPhoneNumber.getText().toString().trim();
        final String user_referral_code = userReferralCode.getText().toString();

        final String user_home_city = userHomeCity.getSelectedItem().toString().trim();
        final String user_home_state = userHomeState.getSelectedItem().toString().trim();
        final String user_home_country = userHomeCountry.getSelectedItem().toString().trim();

        final String user_id = mFirebaseAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(user_referral_code) && !TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_phone_number) && !TextUtils.isEmpty(user_home_city) && !TextUtils.isEmpty(user_home_state) && !TextUtils.isEmpty(user_home_country)
                && mImageUri != null && !TextUtils.isEmpty(user_bio)) {


            if(user_referral_code.equals("C2700")) {

                StorageReference filePath = mStorageRef.child(user_home_country).child(user_home_state).child(user_home_city).child(mFirebaseAuth.getCurrentUser().getDisplayName());
                filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                    @Override
                    public void onSuccess(TaskSnapshot taskSnapshot) {

                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserName").setValue(user_name);
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("emailid").setValue(mFirebaseAuth.getCurrentUser().getEmail());
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserImage").setValue(downloadUrl.toString());
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserBio").setValue(user_bio);
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserPhoneNumber").setValue(user_phone_number);
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserHomeCity").setValue(user_home_city);
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserHomeState").setValue(user_home_state);
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserHomeCountry").setValue(user_home_country);
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserUid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference.child(user_home_country).child(user_home_state).child(user_home_city).child(user_id).child("UserDesignation").setValue("Public User");

                        Editor editor = sharedPreferences.edit();
                        editor.putString("country", user_home_country);
                        editor.putString("state", user_home_state);
                        editor.putString("city", user_home_city);
                        editor.apply();

                        Intent signinIntent = new Intent(SetupAccountActivity.this, and.com.chicsconnect.SplashActivity.class);
                        SetupAccountActivity.this.startActivity(signinIntent);
                        progressDialog.dismiss();
                        SetupAccountActivity.this.finish();
                    }
                });
            }else{
                progressDialog.dismiss();
                Toast.makeText(this, "Invalid Referral Code!!", Toast.LENGTH_LONG).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "All Fields are compulsory", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SetupAccountActivity.this.showFileChooser();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            SetupAccountActivity.this.showFileChooser();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(intent, SetupAccountActivity.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SetupAccountActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            userProfileImage.setImageURI(mImageUri);
        }
    }

}
