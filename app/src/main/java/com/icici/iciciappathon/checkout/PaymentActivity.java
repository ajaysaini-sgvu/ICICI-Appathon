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
package com.icici.iciciappathon.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.icici.iciciappathon.AppApplication;
import com.icici.iciciappathon.R;
import com.icici.iciciappathon.dagger.collaborator.RestApi;
import com.icici.iciciappathon.dashboard.DashboardActivity;
import com.icici.iciciappathon.databinding.ActivityPaymentBinding;
import com.icici.iciciappathon.login.Token;
import com.icici.iciciappathon.ui.BaseActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.icici.iciciappathon.utils.LogUtils.LOGD;
import static com.icici.iciciappathon.utils.LogUtils.makeLogTag;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    @Named("paymentRetrofit")
    Retrofit retrofit;

    private String TAG = makeLogTag(PaymentActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPaymentBinding activityPaymentBinding = setContentView(this, R.layout.activity_payment);

        // setting toolbar title
        setToolbarText(getString(R.string.payment_screen_name));

        ((AppApplication) getApplication()).getmNetComponent().inject(this);

        activityPaymentBinding.contentPayment.placeOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (isOnline()) {

            Call<List<Bill>> accessTokenCall = retrofit.create(RestApi.class).billPay(Token.TOKEN.getmClientId(),
                    Token.TOKEN.getmAccessToken(), "33335001", "e3", "1000");

            showProgress(getString(R.string.hang_on_payment));

            accessTokenCall.enqueue(new Callback<List<Bill>>() {
                @Override
                public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                    try {
                        dismissProgress();
                        Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
                        showAlertAndStartActivity(PaymentActivity.this, response.body().get(1).getSuccess(), intent);
                    } catch (Exception e) {
                        LOGD(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<List<Bill>> call, Throwable t) {
                    dismissProgress();
                    LOGD(TAG, t.getMessage());
                }
            });
        }
    }
}
