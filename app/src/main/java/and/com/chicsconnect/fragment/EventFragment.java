package and.com.chicsconnect.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import and.com.chicsconnect.CreateEventActivity;
import and.com.chicsconnect.DetailEventActivity;
import and.com.chicsconnect.R;
import and.com.chicsconnect.adapter.EventViewHolder;
import and.com.chicsconnect.model.Event;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dell on 28-05-2017.
 */

public class EventFragment extends Fragment {

    @BindView(R.id.theory_list)
    RecyclerView recyclerList;
    Unbinder unbinder;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String KEY_LAYOUT_MANAGER = "list_state";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    public EventFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String country = sharedPreferences.getString("country", "India");
        String state = sharedPreferences.getString("state", "Rajasthan");
        String city = sharedPreferences.getString("city", "Udaipur");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Create_event").child(country).child(state).child(city);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        FirebaseRecyclerAdapter<Event, EventViewHolder> eventEventViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.content_event,
                EventViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, final Event model, final int position) {

                viewHolder.setName(model.getEvent_name());
                viewHolder.setFees(model.getFees());
                viewHolder.setDate(model.getEvent_date() + " " + model.getEvent_time()+ "-" + model.getEvent_end_time());
                viewHolder.setType(model.getEvent_type());
                viewHolder.setCity(model.getEvent_city());
                viewHolder.setImage(getContext(), model.getImage());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DetailEventActivity.class);
                        intent.putExtra("model", model);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerList.setAdapter(eventEventViewHolderFirebaseRecyclerAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_create_event_intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(user_create_event_intent);
            }
        });

        return rootView;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (recyclerList.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerList.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        recyclerList.setLayoutManager(mLayoutManager);
        recyclerList.scrollToPosition(scrollPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
}
