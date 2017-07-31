package and.com.cswithandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateWorldPost extends AppCompatActivity {

    @BindView(R.id.user_world_caption_edit)
    EditText userWorldCaptionEdit;
    @BindView(R.id.user_world_post_image_edit)
    ImageView userWorldPostImageEdit;
    @BindView(R.id.create_button)
    Button createButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_world_post);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("World_activities");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("World_activities");
        databaseReferenceProfilePic = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);
        userWorldPostImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
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
            userWorldPostImageEdit.setImageURI(mImageUri);
        }
    }

    private void startPosting() {

        progressDialog.setMessage("Posting");
        progressDialog.show();

        final String world_post_caption = userWorldCaptionEdit.getText().toString().trim();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String country = sharedPreferences.getString("country","India");
        final String state = sharedPreferences.getString("state","Rajasthan");
        final String city = sharedPreferences.getString("city","Udaipur");

        if (!TextUtils.isEmpty(world_post_caption) && mImageUri != null ) {

            StorageReference filePath = mStorageRef.child(country).child(state).child(city).
                    child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    final DatabaseReference databaseReference1 = databaseReference.push();
                    databaseReference1.child("caption").setValue(world_post_caption);
                    databaseReference1.child("image").setValue(downloadUrl.toString());
                    databaseReference1.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference1.child("Username").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    databaseReferenceProfilePic.child(country).child(state).child(city).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            databaseReference1.child("Userpic").setValue(dataSnapshot.child("UserImage").getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progressDialog.dismiss();
                }
            });
        }
    }
}
