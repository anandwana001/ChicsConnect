package and.com.cswithandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import and.com.cswithandroid.R;

/**
 * Created by dell on 08-07-2017.
 */

public class WorldFragment extends Fragment {

    public static WorldFragment newInstance() {
        WorldFragment fragment = new WorldFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_world,container,false);
        return rootView;
    }
}
