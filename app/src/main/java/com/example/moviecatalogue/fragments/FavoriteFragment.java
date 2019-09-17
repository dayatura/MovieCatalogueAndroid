package com.example.moviecatalogue.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviecatalogue.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    public ViewPager viewPager;
    private TabLayout tabLayout;
    private FavMoviesFragment favMoviesFragment;
    private FavTvShowsFragment favTvShowsFragment;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceStates) {
        super.onViewCreated(view, savedInstanceStates);

        viewPager = view.findViewById(R.id.view_pager_fav);
        favMoviesFragment = new FavMoviesFragment();
        favTvShowsFragment = new FavTvShowsFragment();
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tab_fav);
        tabLayout.setupWithViewPager(viewPager);

    }


    public void setupViewPager(ViewPager viewPager) {
        FavPagerAdapter adapter = new FavPagerAdapter(getChildFragmentManager());
        adapter.addFragment(favMoviesFragment, getString(R.string.nav_movies));
        adapter.addFragment(favTvShowsFragment, getString(R.string.nav_tv_shows));
        viewPager.setAdapter(adapter);
    }

    class FavPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public FavPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
