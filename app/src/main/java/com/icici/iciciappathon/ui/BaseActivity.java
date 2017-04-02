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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.icici.iciciappathon.R;
import com.icici.iciciappathon.checkout.PaymentActivity;

/**
 * This base activity that handles common functionality in the app. This includes the
 * navigation drawer, login, toolbar, others etc.
 */
public class BaseActivity extends AppCompatActivity {

    private AppCompatTextView mAppCompatTextView;

    private ProgressDialog mProgressDialog;

    private Toolbar mToolbar;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // register device with firebase
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public <T extends ViewDataBinding> T setContentView(Activity mActivity, int layoutResID) {
        T viewDataBinding = DataBindingUtil.setContentView(mActivity, layoutResID);
        getToolbar();
        return viewDataBinding;
    }

    /**
     * Show a progress dialog.
     *
     * @param msg
     */
    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

    /**
     * Show progress dialog.
     */
    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Show a toast.
     *
     * @param msg the text to show
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Show a alert dialog.
     *
     * @param msg the text to show
     */
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog_Alert));
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    /**
     * Show a alert dialog to check out bought items.
     *
     * @param msg the text to show
     */
    protected void promptCheckOut(final Context context, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog_Alert));
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        // start check out activity for payment
                        Intent intent = new Intent(context, PaymentActivity.class);
                        startActivity(intent);
                        ((Activity) context).finish();

                    }
                }).create().show();
    }


    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    protected void setToolbarText(String title) {
        AppCompatTextView toolbarTitle = (AppCompatTextView) mToolbar.findViewById(R.id.titleToolbar);
        toolbarTitle.setText(title);
    }

    protected void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    protected String getAccessToken() {
        return accessToken;
    }

    protected void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
