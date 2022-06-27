package com.example.navigationview.ui.viewpaper;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationview.R;
import com.example.navigationview.ui.gallery.GalleryFragment;
import com.example.navigationview.ui.recyclerview.RecyclerViewFragment;
import com.example.navigationview.ui.login.registerFragment;
import com.example.navigationview.ui.user.userFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewPaperFragment extends Fragment {

    private ViewPaperViewModel mViewModel;
    private TabLayout tabLayout;
    private List<String> titles;
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.selector_tab1,
            R.drawable.selector_tab2,
            R.drawable.selector_tab3 };


    public static ViewPaperFragment newInstance() {
        return new ViewPaperFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_viewpaper, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs2);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        fragments = new ArrayList<>();
        GalleryFragment galleryFragment=new GalleryFragment();
        fragments.add(galleryFragment);
        userFragment userFragment =new userFragment();
        fragments.add(userFragment);
        RecyclerViewFragment recyclerViewFragment=new RecyclerViewFragment();
        fragments.add(recyclerViewFragment);
        titles = new ArrayList<>();
        titles.add("One");
        titles.add("Two");
        titles.add("Three");
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //
        setupTabIcons();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewPaperViewModel.class);
        // TODO: Use the ViewModel
    }
   class FragmentViewPagerAdapter extends FragmentPagerAdapter
   {

       private List<Fragment> fragments;
       private List<String> titles;

       public FragmentViewPagerAdapter(FragmentManager manager, List<Fragment> fragments, List<String> titles) {
           super(manager);
           this.fragments = fragments;
           this.titles = titles;
       }

       @Override
       public Fragment getItem(int position) {
           return fragments.get(position);
       }

       @Override
       public int getCount() {
           return fragments.size();
       }

       @Override
       public CharSequence getPageTitle(int position) {
           return titles.get(position);
       }
   }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setCustomView(getTabView(0));
        tabLayout.getTabAt(1).setCustomView(getTabView(1));
        tabLayout.getTabAt(2).setCustomView(getTabView(2));
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(titles.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(tabIcons[position]);
        return view;
    }

}
