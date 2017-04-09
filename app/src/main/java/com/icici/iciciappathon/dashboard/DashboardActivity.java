/*
 *
 *  Copyright 2017 Nagarro Agile. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package com.icici.iciciappathon.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;

import com.icici.iciciappathon.R;
import com.icici.iciciappathon.databinding.ActivityDashboardBinding;
import com.icici.iciciappathon.shopping.ScanBarcodeActivity;
import com.icici.iciciappathon.ui.BaseActivity;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDashboardBinding activityDashboardBinding = setContentView(this, R.layout.activity_dashboard);

        // setting the toolbar title
        setToolbarText(getString(R.string.home_screen_name));

        ((AppCompatTextView) getToolbar().getChildAt(0)).setGravity(Gravity.START);

        // initializing drawer layout and action bar toggle
        mDrawerLayout = activityDashboardBinding.drawer;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getToolbar(), R.string.home_screen_open_drawer, R.string.home_screen_close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();

        NavigationView navigationView = activityDashboardBinding.navigationView;

        activityDashboardBinding.contentGetStarted.getStartedButton.setOnClickListener(this);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DashboardActivity.this, ScanBarcodeActivity.class);
        startActivity(intent);
        finish();
    }
}