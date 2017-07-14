package and.com.cswithandroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import and.com.cswithandroid.CreateEventActivity;
import and.com.cswithandroid.DetailEventActivity;
import and.com.cswithandroid.R;
import and.com.cswithandroid.UserEventsActivity;
import and.com.cswithandroid.adapter.EventViewHolder;
import and.com.cswithandroid.model.Event;
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
        setHasOptionsMenu(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Create Event");
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
                viewHolder.setDate(model.getEvent_date()+" " + model.getTime());
                viewHolder.setType(model.getEvent_type());
                viewHolder.setCity(model.getEvent_city());
                viewHolder.setImage(getContext(), model.getImage());

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), DetailEventActivity.class);
                        intent.putExtra("model", (Parcelable) model);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerList.setAdapter(eventEventViewHolderFirebaseRecyclerAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.user_events,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_events:
                Intent user_event_intent = new Intent(getContext(), UserEventsActivity.class);
                startActivity(user_event_intent);
                return false;
            case R.id.user_create_events:
                Intent user_create_event_intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(user_create_event_intent);
                return false;
        }
        return false;
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
