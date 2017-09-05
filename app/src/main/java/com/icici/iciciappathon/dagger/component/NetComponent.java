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

package com.icici.iciciappathon.dagger.component;

import com.icici.iciciappathon.checkout.PaymentActivity;
import com.icici.iciciappathon.dagger.module.AppModule;
import com.icici.iciciappathon.dagger.module.NetModule;
import com.icici.iciciappathon.dagger.module.ValidatorModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(PaymentActivity paymentActivity);

    void hello(PaymentActivity paymentActivity);

    ValidationComponent validatorComponent(ValidatorModule validatorModule);
}
