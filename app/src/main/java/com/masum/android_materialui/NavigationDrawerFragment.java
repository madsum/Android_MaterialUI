package com.masum.android_materialui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    /*
    STEPS TO HANDLE THE RECYCLER CLICK

    1 Create a class that EXTENDS RecylcerView.OnItemTouchListener

    2 Create an interface inside that class that supports click and long click and indicates the View that was clicked and the position where it was clicked

    3 Create a GestureDetector to detect ACTION_UP single tap and Long Press events

    4 Return true from the singleTap to indicate your GestureDetector has consumed the event.

    5 Find the childView containing the coordinates specified by the MotionEvent and if the childView is not null and the listener is not null either, fire a long click event

    6 Use the onInterceptTouchEvent of your RecyclerView to check if the childView is not null, the listener is not null and the gesture detector consumed the touch event

    7 if above condition holds true, fire the click event

    8 return false from the onInterceptedTouchEvent to give a chance to the childViews of the RecyclerView to process touch events if any.

    9 Add the onItemTouchListener object for our RecyclerView that uses our class created in step 1
     */

    public static String PREF_FILE_NAME = "testPref";
    public static String KEY_USER_LEARED_DRAWER = "user_leared_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUsreLearnedDrawer;
    private boolean mFromSavedState;
    private View drawerFragmentInMain;
    private RecyclerView recyclerView;
    private InformationAdapter informationAdapter;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_nevigaion_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerlist);
        informationAdapter = new InformationAdapter(getActivity(), getData());
        recyclerView.setAdapter(informationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "onClick "+position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), "onLongClick "+position, Toast.LENGTH_LONG).show();

            }
        }));

        return layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsreLearnedDrawer = Boolean.valueOf(readFromPreference(getActivity(), KEY_USER_LEARED_DRAWER, "false"));

        if (savedInstanceState != null) {
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

                if (!mUsreLearnedDrawer) {
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
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (!mUsreLearnedDrawer && !mFromSavedState) {
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

    private static List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] iconId = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four};
        String[] title = {"facebook", "linkedIn", "twetter", "youtube"};


        //for(int i=0; i<iconId.length && i<title.length; i++){
        for (int i = 0; i < 100; i++) {

            Information currentInfo = new Information();

            currentInfo.iconId = iconId[i % iconId.length];
            currentInfo.title = title[i % title.length];
            data.add(currentInfo);
        }
        return data;
    }


    class RecyclerTouchListener implements  RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private  MyClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MyClickListener clickListener){
            //Log.i(MainActivity.TAG, "# My RecyclerTouchListener ctor.");

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.i(MainActivity.TAG, "onSingleTapUp");

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(child != null && clickListener!= null){

                        clickListener.onClick(child, recyclerView.getChildPosition(child));
                    }

                    return true; //super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    Log.i(MainActivity.TAG, "onLongPress");

                  View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(child != null && clickListener!= null){

                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }

                    super.onLongPress(e);
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i(MainActivity.TAG, "# onInterceptTouchEvent "+gestureDetector.onTouchEvent(e)+ " ## " +e);
/*
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child !=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
              clickListener.onClick(child, rv.getChildPosition(child));
            }
*/
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i(MainActivity.TAG, "# onTouchEvent "+e);
        }
    }

    public static interface MyClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
}
