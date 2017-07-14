package and.com.cswithandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.text.TextUtils;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEventActivity extends AppCompatActivity {

    @BindView(R.id.create_event_imageView)
    ImageView createEventImageView;
    @BindView(R.id.create_event_name)
    EditText createEventName;
    @BindView(R.id.create_event_des)
    EditText createEventDes;
    @BindView(R.id.create_event_location)
    EditText createEventLocation;
    @BindView(R.id.create_event_city)
    EditText createEventCity;
    @BindView(R.id.create_event_state)
    EditText createEventState;
    @BindView(R.id.create_event_country)
    EditText createEventCountry;
    @BindView(R.id.create_event_fees)
    EditText createEventFees;
    @BindView(R.id.create_event_date)
    EditText createEventDate;
    @BindView(R.id.create_event_time)
    EditText createEventTime;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.create_button)
    Button createButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private DatabaseReference categoriesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Create Event");
        categoriesDatabaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        categoriesDatabaseReference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> categories = new ArrayList<String>();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    String areaName = Snapshot.child("name").getValue(String.class);
                    categories.add(areaName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, categories);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        createEventImageView.setOnClickListener(new View.OnClickListener() {
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

    private void startPosting() {

        progressDialog.setMessage("Creating Event");
        progressDialog.show();

        final String event_name = createEventName.getText().toString().trim();
        final String event_des = createEventDes.getText().toString().trim();
        final String address = createEventLocation.getText().toString().trim();
        final String fees = createEventFees.getText().toString().trim();
        final String event_type = spinner.getSelectedItem().toString().trim();
        final String event_city = createEventCity.getText().toString().trim();
        final String event_state = createEventState.getText().toString().trim();
        final String event_country = createEventCountry.getText().toString().trim();
        final String event_date = createEventDate.getText().toString().trim();
        final String event_time = createEventTime.getText().toString().trim();

        if (!TextUtils.isEmpty(event_name) && !TextUtils.isEmpty(event_des) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(fees)
                && !TextUtils.isEmpty(event_date) && !TextUtils.isEmpty(event_time)
                && !TextUtils.isEmpty(event_type) && mImageUri != null && !TextUtils.isEmpty(event_city) && !TextUtils.isEmpty(event_state) && !TextUtils.isEmpty(event_country)) {

            StorageReference filePath = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference databaseReference1 = databaseReference.push();

                    databaseReference1.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference1.child("event_name").setValue(event_name);
                    databaseReference1.child("event_des").setValue(event_des);
                    databaseReference1.child("address").setValue(address);
                    databaseReference1.child("fees").setValue(fees);
                    databaseReference1.child("event_date").setValue(event_date);
                    databaseReference1.child("event_time").setValue(event_time);
                    databaseReference1.child("event_type").setValue(event_type);
                    databaseReference1.child("image").setValue(downloadUrl.toString());
                    databaseReference1.child("event_city").setValue(event_city);
                    databaseReference1.child("event_state").setValue(event_state);
                    databaseReference1.child("event_country").setValue(event_country);

                    progressDialog.dismiss();
                }
            });
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
            createEventImageView.setImageURI(mImageUri);
        }
    }
}
