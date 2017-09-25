package and.com.chicsconnect.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import and.com.chicsconnect.R;

/**
 * Created by dell on 10-07-2017.
 */

public class UserEventJoinedFragment extends Fragment {

    public UserEventJoinedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_joined_event,container,false);
        return rootView;
    }
}
