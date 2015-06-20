package com.masum.android_materialui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static String PREF_FILE_NAME = "testPref";
    public static String KEY_USER_LEARED_DRAWER = "user_leared_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUsreLearnedDrawer;
    private boolean mFromSavedState;
    private View drawerFragmentInMain;



    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nevigaion_drawer, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsreLearnedDrawer = Boolean.valueOf(readFromPreference(getActivity(), KEY_USER_LEARED_DRAWER, "false"));

        if(savedInstanceState !=null){
            mFromSavedState = true;
        }

    }


    public void setup(int drawer_fragment_id, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        drawerFragmentInMain = getActivity().findViewById(drawer_fragment_id);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(!mUsreLearnedDrawer){
                    mUsreLearnedDrawer = true;
                    savePreference(getActivity(), KEY_USER_LEARED_DRAWER, mUsreLearnedDrawer + "");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset < 0.6){
                    toolbar.setAlpha(1 - slideOffset);
                }
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if(!mUsreLearnedDrawer && !mFromSavedState){
            Log.i(MainActivity.TAG, "hit for the very fast time.");
            mDrawerLayout.openDrawer(drawerFragmentInMain);
        }

        // this will shows the hamburger icon
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public static void savePreference(Context context, String preferenceName, String preferenceVal) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceVal);

        editor.apply();
    }

    public static String readFromPreference(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferenceName, defaultValue);
    }

}
