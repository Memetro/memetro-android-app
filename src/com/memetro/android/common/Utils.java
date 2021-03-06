/*
 * Copyright 2013 Nytyr [me at nytyr dot me]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.memetro.android.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.memetro.android.R;
import com.memetro.android.alerts.AddFragment;

public class Utils {

    public static void openFragment(FragmentManager fm, Fragment fragment){
        fm.beginTransaction().replace(R.id.fragmentData, fragment).addToBackStack(null).commit();
    }

}
