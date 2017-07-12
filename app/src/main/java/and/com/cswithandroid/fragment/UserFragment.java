package and.com.cswithandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import and.com.cswithandroid.R;
import and.com.cswithandroid.adapter.UserProfileViewHolder;
import and.com.cswithandroid.model.Users;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dell on 28-05-2017.
 */

public class UserFragment extends Fragment {

    @BindView(R.id.users_list)
    RecyclerView usersList;
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

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    public UserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        FirebaseRecyclerAdapter<Users, UserProfileViewHolder> userProfileViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserProfileViewHolder>(
                Users.class,
                R.layout.content_user_list,
                UserProfileViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(UserProfileViewHolder viewHolder, Users model, int position) {

                viewHolder.setUserName(model.getUserName());
                viewHolder.setUserBio(model.getUserBio());
                viewHolder.setUserImage(getContext(), model.getUserImage());
            }
        };

        usersList.setAdapter(userProfileViewHolderFirebaseRecyclerAdapter);
        return rootView;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (usersList.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) usersList.getLayoutManager())
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

        usersList.setLayoutManager(mLayoutManager);
        usersList.scrollToPosition(scrollPosition);
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
