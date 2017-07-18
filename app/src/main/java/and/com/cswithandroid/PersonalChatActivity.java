package and.com.cswithandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import and.com.cswithandroid.model.Chat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 17-07-2017.
 */

public class PersonalChatActivity extends AppCompatActivity {

    @BindView(R.id.type_message)
    EditText typeMessage;
    @BindView(R.id.send_message)
    ImageView sendMessage;
    @BindView(R.id.chat_recyclerView)
    RecyclerView chatRecyclerView;

    private DatabaseReference userDatabaseReference;
    private DatabaseReference chatDatabaseReference;


    private String reciever_uid;
    private String mUsername;

    private String KEY_LAYOUT_MANAGER = "list_state";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    private final int RECIEVER_MESSAGE_CHAT=1;
    private final int SENDER_MESSAGE_CHAT=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);

        reciever_uid = getIntent().getStringExtra("reciever_uid");
        Log.v(PersonalChatActivity.class.getSimpleName(), "reciever_uid = "+reciever_uid);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(reciever_uid);
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername = dataSnapshot.child("UserName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

       FirebaseRecyclerAdapter<Chat, RecyclerView.ViewHolder> adapter = new FirebaseRecyclerAdapter<Chat, RecyclerView.ViewHolder>(
               Chat.class,
               R.layout.activity_send_message,
               RecyclerView.ViewHolder.class,
               chatDatabaseReference.orderByChild("reciever").equalTo(reciever_uid)) {

            @Override
            protected void populateViewHolder(final RecyclerView.ViewHolder viewHolder, final Chat chat, final int position) {

            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case SENDER_MESSAGE_CHAT:
                        View userType1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_message_layout, parent, false);
                        return new ViewHolder1(userType1);
                    case RECIEVER_MESSAGE_CHAT:
                        View userType2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_recieve_message, parent, false);
                        return new ViewHolder2(userType2);
                }
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public int getItemViewType(int position) {
                Chat chat = getItem(position);
                if(chat.getReciever()==FirebaseAuth.getInstance().getCurrentUser().getUid()){
                    return SENDER_MESSAGE_CHAT;
                }else{
                    return RECIEVER_MESSAGE_CHAT;
                }
            }
       };
        chatRecyclerView.setAdapter(adapter);
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{

        private TextView message;
        private ImageView profilePic;

        public ViewHolder1(View itemView) {
            super(itemView);
            message = (TextView) findViewById(R.id.sender_message);
            profilePic = (ImageView) findViewById(R.id.sender_profile_pic);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder{

        private TextView message;
        private ImageView profilePic;

        public ViewHolder2(View itemView) {
            super(itemView);
            message = (TextView) findViewById(R.id.reciepent_message);
            profilePic = (ImageView) findViewById(R.id.reciepent_profile_pic);
        }
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (chatRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) chatRecyclerView.getLayoutManager())
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
        chatRecyclerView.setLayoutManager(mLayoutManager);
        chatRecyclerView.scrollToPosition(scrollPosition);
    }

    @OnClick(R.id.send_message)
    public void onViewClicked() {
        Chat friendlyMessage = new Chat(typeMessage.getText().toString(), mUsername, reciever_uid);
        chatDatabaseReference.push().setValue(friendlyMessage);
        typeMessage.setText("");
    }
}
