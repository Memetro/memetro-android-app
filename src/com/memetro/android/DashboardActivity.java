package com.memetro.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;

import com.memetro.android.common.SlideHolder;
import com.memetro.android.settings.SettingsFragment;

public class DashboardActivity extends FragmentActivity {

    private SlideHolder mSlideHolder;
    private Boolean lastWindow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);

        mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);

        // Launch default fragment
        changeMainFragment(new DashboardFragment(), true);

        // Toggler listener
        View toggleView = findViewById(R.id.actionbarToggler);
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideHolder.toggle();
            }
        });

        // Menu buttons
        View config = findViewById(R.id.configMenu);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMainFragment(new SettingsFragment());
            }
        });
    }

    @Override
    public void onBackPressed(){
        if (mSlideHolder.isOpened()){
            mSlideHolder.close();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
            super.onBackPressed();
            return;
        }

        if (!lastWindow){
            changeMainFragment(new DashboardFragment(), true);
            return;
        }

        super.onBackPressed();
    }

    private void changeMainFragment(Fragment fragment){
        changeMainFragment(fragment, false);
    }

    private void changeMainFragment(Fragment fragment, Boolean isLastWindow) {
        lastWindow = isLastWindow;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentData, fragment).commit();

        // Toggle left menu
        if (mSlideHolder.isOpened()){
            mSlideHolder.close();
        }
    }
}
