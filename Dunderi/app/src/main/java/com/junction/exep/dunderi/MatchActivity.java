package com.junction.exep.dunderi;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by exep on 11/25/17.
 */

public class MatchActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ApiManager api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);



        api = ((DunderiApplication)getApplication()).getApi();

        api.haeDuunit(new DuuniListener() {
            @Override
            public void done(ArrayList<Duuni> duunit) {
                Log.d("LOL", "valmista on " + duunit.size());
                try {
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), duunit);
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                } catch(Exception e){

                }
            }
        });
    }

    public class DuuniListener {
        public void done(ArrayList<Duuni> duunit){

        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String JSON_ARG = "JSON";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(Duuni duuni) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(JSON_ARG, duuni.getJson());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_match, container, false);
            TextView titleView = (TextView) rootView.findViewById(R.id.title_label);
            TextView companyView = (TextView) rootView.findViewById(R.id.company_label);
            TextView descView = (TextView) rootView.findViewById(R.id.desc_label);
            ImageView imgV = (ImageView) rootView.findViewById(R.id.match_image);
            try {
                Duuni duuni = new Duuni(getArguments().getString(JSON_ARG));
                titleView.setText(duuni.getTitle());
                companyView.setText(duuni.getCompany());
                descView.setText(duuni.getDesc());
                Glide.with(this).load(duuni.getImage()).apply(new RequestOptions().transform(new RoundedCorners(64))).into(imgV);
            }catch(Exception e) {
                return null;
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Duuni> arr;
        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Duuni> arr) {
            super(fm);
            this.arr = arr;
        }

        public Duuni getDuuni(int pos) {
            try {
                return arr.get(pos);
            } catch(Exception e){
                return null;
            }
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(getDuuni(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return arr.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                return getDuuni(position).getTitle();
            } catch(Exception e) {
                return "JSON fail";
            }
        }
    }

}