
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

package com.icici.iciciappathon.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.icici.iciciappathon.AppApplication;
import com.icici.iciciappathon.R;
import com.icici.iciciappathon.dagger.collaborator.RestApi;
import com.icici.iciciappathon.dagger.model.ValidatorModule;
import com.icici.iciciappathon.dashboard.DashboardActivity;
import com.icici.iciciappathon.databinding.ActivityAuthenticationBinding;
import com.icici.iciciappathon.ui.BaseActivity;
import com.icici.iciciappathon.ui.GetStartedActivity;
import com.icici.iciciappathon.utils.ICICIAppathon;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.icici.iciciappathon.utils.LogUtils.LOGD;
import static com.icici.iciciappathon.utils.LogUtils.makeLogTag;

/**
 * Wrapper activity is responsible for authentication.
 * <p>
 * <p>This class is responsible to get unique access token in response to access all
 * ICICI Bank & Group companies APIs.</p>
 */
public class AuthenticationActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    @Inject
    @Named("authRetrofit")
    Retrofit retrofit;

    @Inject
    Validator validator;

    @NotEmpty
    private EditText client_id_input_edittext;

    @NotEmpty
    private EditText access_code_input_edittext;

    @NonNull
    private CoordinatorLayout mCoordinatorLayout;

    private String TAG = makeLogTag(AuthenticationActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedPreferences.getString(ICICIAppathon.ACCESSTOKEN, null) != null) {
            Token.TOKEN.setmAccessToken(sharedPreferences.getString(ICICIAppathon.ACCESSTOKEN, ""));
            Token.TOKEN.setmClientId(sharedPreferences.getString(ICICIAppathon.CLIENTID, ""));
            finish();

            Intent intent = new Intent(AuthenticationActivity.this, DashboardActivity.class);
            startActivity(intent);
        }


        ActivityAuthenticationBinding activityAuthenticationBinding = setContentView(this, R.layout.activity_authentication);

        // setting toolbar title
        setToolbarText(getString(R.string.login_screen_name));

        // And call the implementation from AuthenticationActivity
        ((AppApplication) getApplication()).getmNetComponent()
                .validatorComponent(new ValidatorModule(this))
                .inject(AuthenticationActivity.this);

        this.client_id_input_edittext = activityAuthenticationBinding.contentAuthenticate.clientIdInputEdittext;
        this.access_code_input_edittext = activityAuthenticationBinding.contentAuthenticate.accessCodeInputEdittext;
        mCoordinatorLayout = activityAuthenticationBinding.coordinatorLayoutLogin;

        // apply click and validation listeners
        validator.setValidationListener(this);
        activityAuthenticationBinding.contentAuthenticate.authenticate.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (isOnline()) {
            validator.validate();
        } else
            Snackbar.make(mCoordinatorLayout, getString(R.string.network_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onValidationSucceeded() {
        final String clientId = client_id_input_edittext.getText().toString();
        final String accessCode = access_code_input_edittext.getText().toString();

        Call<List<AccessToken>> accessTokenCall = retrofit.create(RestApi.class).authUser(clientId, accessCode);
        showProgress(getString(R.string.hang_on_authenticate));

        accessTokenCall.enqueue(new Callback<List<AccessToken>>() {
            @Override
            public void onResponse(Call<List<AccessToken>> call, Response<List<AccessToken>> response) {
                dismissProgress();
                String accessToken = response.body().get(0).getToken();
                Token.TOKEN.setmAccessToken(accessToken);
                Token.TOKEN.setmClientId(clientId);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ICICIAppathon.ACCESSTOKEN, accessToken);
                editor.putString(ICICIAppathon.CLIENTID, clientId);
                editor.apply();

                Intent intent = new Intent(AuthenticationActivity.this, GetStartedActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<List<AccessToken>> call, Throwable t) {
                dismissProgress();
                LOGD(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        LOGD(TAG, "My Bad!!");
    }
}
