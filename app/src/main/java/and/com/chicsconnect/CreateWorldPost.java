package and.com.chicsconnect;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import and.com.chicsconnect.adapter.ImageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateWorldPost extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.user_world_caption_edit)
    EditText userWorldCaptionEdit;
    @BindView(R.id.user_world_post_image_edit)
    FloatingActionButton userWorldPostImageEdit;
    @BindView(R.id.create_button)
    Button createButton;

    @BindView(R.id.user_post_list)
    RecyclerView recyclerView;
    private List<Uri> mArrayUri;
    private ImageAdapter mAdapter;
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private String LIST_STATE_KEY = "list_state";

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceProfilePic;

    private static final int REQUEST_WRITE_PERMISSION = 786;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_world_post);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("World_activities");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("World_activities");
        databaseReferenceProfilePic = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mArrayUri = new ArrayList<Uri>();
        mAdapter = new ImageAdapter(mArrayUri, CreateWorldPost.this);
        mLayoutManager = new LinearLayoutManager(CreateWorldPost.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        userWorldPostImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            showFileChooser();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK) {

            if (data.getData() != null) {

                Uri mImageUri = data.getData();
                userWorldPostImageEdit.setImageURI(mImageUri);

                mArrayUri.add(mImageUri);
                mAdapter = new ImageAdapter(mArrayUri,this);
                recyclerView.setAdapter(mAdapter);
            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        if(i>5){
                            Toast.makeText(CreateWorldPost.this, "More than 5 images not allow !!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                    }
                    mAdapter = new ImageAdapter(mArrayUri,this);
                    recyclerView.setAdapter(mAdapter);
                }

            }
        }
    }

    private void startPosting() {

        final String world_post_caption = userWorldCaptionEdit.getText().toString().trim();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String country = sharedPreferences.getString("country", "India");
        final String state = sharedPreferences.getString("state", "Rajasthan");
        final String city = sharedPreferences.getString("city", "Udaipur");

        if (!TextUtils.isEmpty(world_post_caption) && mArrayUri != null) {

            progressDialog.setMessage("Posting");
            progressDialog.show();

            final DatabaseReference[] databaseReference1 = {databaseReference.push()};

            for(int i = 0; i<mArrayUri.size(); i++){

                StorageReference filePath = mStorageRef.child(country).child(state).child(city).
                        child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(mArrayUri.get(i).getLastPathSegment());
                final int finalI = i;
                filePath.putFile(mArrayUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        databaseReference1[0] = databaseReference.push();
                        databaseReference1[0].child("image"+ finalI).setValue(downloadUrl);
                        databaseReference1[0].child("caption").setValue(world_post_caption);
                        databaseReference1[0].child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference1[0].child("Username").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        databaseReference1[0].child("TimeStamp").setValue(ServerValue.TIMESTAMP);
                        databaseReferenceProfilePic.child(country).child(state).child(city).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                databaseReference1[0].child("Userpic").setValue(dataSnapshot.child("UserImage").getValue().toString());
                                if(finalI==mArrayUri.size()-1){
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateWorldPost.this, "Successfully posted!!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
        } else {
            Toast.makeText(CreateWorldPost.this, "All the fields are required!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}