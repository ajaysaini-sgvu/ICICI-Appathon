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

package com.icici.iciciappathon.dagger.collaborator;


import com.icici.iciciappathon.checkout.Bill;
import com.icici.iciciappathon.login.AccessToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("authenticate_client")
    Call<List<AccessToken>> authUser(@Query("client_id") String clientId, @Query("password") String password);

    @GET("billpay")
    Call<List<Bill>> billPay(@Query("client_id") String client_id,
                             @Query("token") String token,
                             @Query("custid") String custid,
                             @Query("nickname") String nickname,
                             @Query("amount") String amount);

}
