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


public enum Token {

    TOKEN("token");

    String mAccessToken;
    String mClientId;

    Token(String accessToken) {
        mAccessToken = accessToken;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public String getmClientId() {
        return mClientId;
    }

    public void setmClientId(String clientId) {
        this.mClientId = clientId;
    }

}
