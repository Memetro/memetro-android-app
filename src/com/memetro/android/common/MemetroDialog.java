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

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.memetro.android.R;

public final class MemetroDialog {


    public static void showDialog(final Context context, String title, String message) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog);
        mDialog.setCancelable(true);

        TextView titleText = (TextView) mDialog.findViewById(R.id.title);
        TextView messageText = (TextView) mDialog.findViewById(R.id.message);
        Button closeButton = (Button) mDialog.findViewById(R.id.close);

        messageText.setMovementMethod(ScrollingMovementMethod.getInstance());
        messageText.setText(message);
        titleText.setText(title);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        mDialog.show();

    }
}
