package and.com.chicsconnect;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import and.com.chicsconnect.fragment.EventFragment;
import and.com.chicsconnect.fragment.MapFragment;
import and.com.chicsconnect.fragment.UserFragment;
import and.com.chicsconnect.fragment.WorldFragment;
import and.com.chicsconnect.util.BottomNavigationViewHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int saveState;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = MapFragment.newInstance();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = EventFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = UserFragment.newInstance();
                    break;
                case R.id.navigation_world:
                    selectedFragment = WorldFragment.newInstance();
                    break;
            }
            BottomNavigationViewHelper.disableShiftMode(navigation);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        int check = getIntent().getIntExtra("check", R.id.navigation_home);

        if (savedInstanceState != null) {
            navigation.setSelectedItemId(saveState);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (check) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content, MapFragment.newInstance());
                    break;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.content, UserFragment.newInstance());
                    break;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.content, EventFragment.newInstance());
                    break;
                case R.id.navigation_world:
                    transaction.replace(R.id.content, WorldFragment.newInstance());
                    break;
            }
            transaction.commit();
            navigation.setSelectedItemId(check);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(saveState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveState = navigation.getSelectedItemId();
    }
}
