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

package com.icici.iciciappathon.dagger.model;

import android.app.Activity;

import com.mobsandgeeks.saripaar.Validator;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class ValidatorModule {

    private final Activity activity;

    @Inject
    public ValidatorModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Validator providesValidator() {
        return new com.mobsandgeeks.saripaar.Validator(activity);
    }
}
