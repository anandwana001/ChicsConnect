package and.com.cswithandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 29-06-2017.
 */

public class SetupAccountActivity extends AppCompatActivity {

    @BindView(R.id.user_profile_image)
    ImageView userProfileImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_emailid)
    TextView userEmailid;
    @BindView(R.id.user_bio)
    EditText userBio;
    @BindView(R.id.user_phone_number)
    EditText userPhoneNumber;
    @BindView(R.id.user_home_city)
    EditText userHomeCity;
    @BindView(R.id.user_home_state)
    EditText userHomeState;
    @BindView(R.id.user_home_country)
    EditText userHomeCountry;
    @BindView(R.id.create_button)
    Button createButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri = null;

    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference mStorageRef;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Profile_images");

        progressDialog = new ProgressDialog(this);

        userName.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
        userEmailid.setText(mFirebaseAuth.getCurrentUser().getEmail());

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });
    }

    private void startSetupAccount() {

        final String user_bio = userBio.getText().toString().trim();
        final String user_phone_number = userPhoneNumber.getText().toString().trim();
        final String user_home_city = userHomeCity.getText().toString().trim();
        final String user_home_state = userHomeState.getText().toString().trim();
        final String user_home_country = userHomeCountry.getText().toString().trim();

        final String user_id = mFirebaseAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(user_phone_number) && !TextUtils.isEmpty(user_home_city) && !TextUtils.isEmpty(user_home_state) && !TextUtils.isEmpty(user_home_country)
                && mImageUri != null && !TextUtils.isEmpty(user_bio)) {

            progressDialog.setMessage("Finishing Setup");
            progressDialog.show();

            StorageReference filePath = mStorageRef.child(mFirebaseAuth.getCurrentUser().getDisplayName());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                    databaseReference.child(user_id).child("UserName").setValue(mFirebaseAuth.getCurrentUser().getDisplayName());
                    databaseReference.child(user_id).child("emailid").setValue(mFirebaseAuth.getCurrentUser().getEmail());
                    databaseReference.child(user_id).child("UserImage").setValue(downloadUrl.toString());
                    databaseReference.child(user_id).child("UserBio").setValue(user_bio);
                    databaseReference.child(user_id).child("UserPhoneNumber").setValue(user_phone_number);
                    databaseReference.child(user_id).child("UserHomeCity").setValue(user_home_city);
                    databaseReference.child(user_id).child("UserHomeState").setValue(user_home_state);
                    databaseReference.child(user_id).child("UserHomeCountry").setValue(user_home_country);

                    progressDialog.dismiss();

                    Intent signinIntent = new Intent(SetupAccountActivity.this, SplashActivity.class);
                    signinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signinIntent);
                }
            });
        } else {
            Toast.makeText(this, "All Fields are compulsory", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK) {
            mImageUri = data.getData();
            userProfileImage.setImageURI(mImageUri);
        }
    }

}
