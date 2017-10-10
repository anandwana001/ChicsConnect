package and.com.chicsconnect;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateEventActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.create_event_imageView)
    ImageView createEventImageView;
    @BindView(R.id.create_event_name)
    EditText createEventName;
    @BindView(R.id.create_event_des)
    EditText createEventDes;
    @BindView(R.id.create_event_location)
    EditText createEventLocation;
    @BindView(R.id.create_event_city)
    Spinner createEventCity;
    @BindView(R.id.create_event_state)
    Spinner createEventState;
    @BindView(R.id.create_event_country)
    Spinner createEventCountry;
    @BindView(R.id.create_event_fees)
    EditText createEventFees;

    @BindView(R.id.create_event_date)
    TextView createEventDate;
    @BindView(R.id.create_event_time)
    TextView createEventTime;
    @BindView(R.id.create_event_endtime)
    TextView createEventEndtime;

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
    private DatabaseReference databaseReferenceNumber;

    private DatabaseReference countryDatabaseReference;
    private DatabaseReference stateDatabaseReference;
    private DatabaseReference cityDatabaseReference;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private String phone_number;
    private String user_home_country;
    private  Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String country = sharedPreferences.getString("country", "India");
        String state = sharedPreferences.getString("state", "Rajasthan");
        String city = sharedPreferences.getString("city", "Udaipur");

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Create_event");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Create_event");
        categoriesDatabaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceNumber = FirebaseDatabase.getInstance().getReference().child("Users").child(country).child(state).child(city).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        countryDatabaseReference = FirebaseDatabase.getInstance().getReference();
        stateDatabaseReference = FirebaseDatabase.getInstance().getReference().child("State");
        cityDatabaseReference = FirebaseDatabase.getInstance().getReference().child("City");

        createEventDate.setText("Select Activity Date");
        createEventTime.setText("Select Activity Starting Time");
        createEventEndtime.setText("Select Activity Ending Time");

        createEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        createEventTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Starting Time");
                mTimePicker.show();
            }
        });

        createEventEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        createEventEndtime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Ending Time");
                mTimePicker.show();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        createEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        databaseReferenceNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phone_number = dataSnapshot.child("UserPhoneNumber").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        countryDatabaseReference.child("Country").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> country = new ArrayList<String>();

                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    String countryName = Snapshot.child("name").getValue(String.class);
                    country.add(countryName);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, country);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                createEventCountry.setAdapter(arrayAdapter);
                createEventCountry.setPrompt(getResources().getString(R.string.country));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        createEventCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, state);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        createEventState.setAdapter(arrayAdapter);
                        createEventState.setPrompt(getResources().getString(R.string.state));
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

        createEventState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, city);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        createEventCity.setAdapter(arrayAdapter);

                        createEventCity.setPrompt(getResources().getString(R.string.city));
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

                spinner.setPrompt(getResources().getString(R.string.category));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        createEventImageView.setOnClickListener(new View.OnClickListener() {
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

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        createEventDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void startPosting() {

        final String event_name = createEventName.getText().toString().trim();
        final String event_des = createEventDes.getText().toString().trim();
        final String address = createEventLocation.getText().toString().trim();
        final String fees = createEventFees.getText().toString().trim();
        final String event_type = spinner.getSelectedItem().toString().trim();
        final String event_city = createEventCity.getSelectedItem().toString().trim();
        final String event_state = createEventState.getSelectedItem().toString().trim();
        final String event_country = createEventCountry.getSelectedItem().toString().trim();
        final String event_date = createEventDate.getText().toString().trim();
        final String event_time = createEventTime.getText().toString().trim();
        final String event_end_time = createEventEndtime.getText().toString().trim();

        if (!TextUtils.isEmpty(event_name) && !TextUtils.isEmpty(event_des) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(fees)
                && !TextUtils.isEmpty(event_date) && !TextUtils.isEmpty(event_time)
                && !TextUtils.isEmpty(event_type)  && !TextUtils.isEmpty(event_end_time) && mImageUri != null && !TextUtils.isEmpty(event_city) && !TextUtils.isEmpty(event_state) && !TextUtils.isEmpty(event_country)) {

            progressDialog.setMessage("Creating Event");
            progressDialog.show();

            StorageReference filePath = mStorageRef.child(event_country).child(event_state).child(event_city).child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference databaseReference1 = databaseReference.child(event_country).child(event_state).child(event_city).push();

                    databaseReference1.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference1.child("event_name").setValue(event_name);
                    databaseReference1.child("event_des").setValue(event_des);
                    databaseReference1.child("address").setValue(address);
                    databaseReference1.child("fees").setValue(fees);
                    databaseReference1.child("event_date").setValue(event_date);
                    databaseReference1.child("event_time").setValue(event_time);
                    databaseReference1.child("event_end_time").setValue(event_end_time);
                    databaseReference1.child("event_type").setValue(event_type);
                    databaseReference1.child("image").setValue(downloadUrl.toString());
                    databaseReference1.child("event_city").setValue(event_city);
                    databaseReference1.child("event_state").setValue(event_state);
                    databaseReference1.child("event_country").setValue(event_country);
                    databaseReference1.child("creater_name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    databaseReference1.child("creator_phone_number").setValue(phone_number);

                    progressDialog.dismiss();
                    Toast.makeText(CreateEventActivity.this, "Successfully Created!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(CreateEventActivity.this, "All the fields are required!!", Toast.LENGTH_LONG).show();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        createEventName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        createEventDes.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        createEventLocation.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        createEventFees.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        createEventDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        createEventTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
        createEventEndtime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "OpenSans-Light.ttf"));
    }
}
