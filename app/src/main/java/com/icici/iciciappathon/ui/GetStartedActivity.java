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

package com.icici.iciciappathon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.icici.iciciappathon.R;
import com.icici.iciciappathon.databinding.ActivityGetStartedBinding;
import com.icici.iciciappathon.shopping.ScanBarcodeActivity;

public class GetStartedActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGetStartedBinding activityGetStartedBinding = setContentView(this, R.layout.activity_get_started);

        // setting the toolbar title
        setToolbarText(getString(R.string.get_started_screen_name));

        activityGetStartedBinding.contentGetStarted.getStartedButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(GetStartedActivity.this, ScanBarcodeActivity.class);
        startActivity(intent);
        finish();
    }
}
