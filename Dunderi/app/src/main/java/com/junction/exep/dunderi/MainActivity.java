package com.junction.exep.dunderi;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static DunderiApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (DunderiApplication) getApplication();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMatchActivity();
            }
        });

    }

    public void openMatchActivity() {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class InterestFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public InterestFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static InterestFragment newInstance(int sectionNumber) {
            InterestFragment fragment = new InterestFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private RecyclerView interestView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            interestView = (RecyclerView) rootView.findViewById(R.id.interest_view);
            interestView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
            ArrayList<String> nimikkeet = app.getFiles().readData();
            Collections.shuffle(nimikkeet);
            InterestAdapter adapter = new InterestAdapter(nimikkeet);
            interestView.setAdapter(adapter);

            return rootView;
        }

        public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestHolder> {
            private ArrayList<String> data;

            public InterestAdapter(ArrayList<String> data) {
                this.data = data;
            }

            @Override
            public InterestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item, parent, false);
                return new InterestHolder(view);
            }

            @Override
            public void onBindViewHolder(InterestHolder holder, int position) {
                holder.bind(data.get(position));
            }

            @Override
            public int getItemCount() {
                return data.size();
            }

            public void removeAt(int position) {
                app.getApi().addHakutermi(data.get(position));
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
            }

            public class InterestHolder extends RecyclerView.ViewHolder {

                private TextView titleView;
                private LinearLayout layout;

                public InterestHolder(View itemView) {
                    super(itemView);
                    titleView = (TextView) itemView.findViewById(R.id.info_text);
                    layout = (LinearLayout) itemView.findViewById(R.id.interest_layout);
                }

                public void bind(String title){
                    titleView.setText(title);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeAt(getPosition());
                        }
                    });
                }
            }
        }
    }

    public static class InfoFragment extends Fragment {

        public static InfoFragment newInstance() {
            InfoFragment fragment = new InfoFragment();
            return fragment;
        }

        private RecyclerView interestView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_info, container, false);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return InterestFragment.newInstance(position + 1);
            else
                return InfoFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Your interests";
                case 1:
                    return "Your info";
            }
            return null;
        }
    }
}
