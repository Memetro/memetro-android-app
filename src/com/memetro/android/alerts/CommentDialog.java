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

package com.memetro.android.alerts;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.memetro.android.R;
import com.memetro.android.common.MemetroDialog;
import com.memetro.android.common.MemetroProgress;
import com.memetro.android.oauth.OAuth;
import com.memetro.android.oauth.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class CommentDialog {


    public static void showDialog(
            final Context context,
            String comment,
            String creator,
            boolean isMine,
            final Long id
    ) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.comment_dialog);
        mDialog.setCancelable(true);

        TextView titleText = (TextView) mDialog.findViewById(R.id.title);
        TextView messageText = (TextView) mDialog.findViewById(R.id.message);
        Button closeButton = (Button) mDialog.findViewById(R.id.close);
        Button deleteButton = (Button) mDialog.findViewById(R.id.delete);

        titleText.setText(titleText.getText().toString()+" "+creator);
        messageText.setMovementMethod(ScrollingMovementMethod.getInstance());
        messageText.setText(comment);

        if (isMine) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DeleteInBg(context, String.valueOf(id)).execute();
                    mDialog.dismiss();
                }
            });
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();

    }

    static class DeleteInBg extends AsyncTask<Void, Void, JSONObject> {

        private Context context;
        private String id;
        private MemetroProgress pdialog;

        public DeleteInBg(Context context, String id) {
            this.context = context;
            this.id = id;
            pdialog = new MemetroProgress(context);
        }

        @Override
        protected void onPreExecute() {
            pdialog.show();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {

            OAuth OAuth = new OAuth(context);
            Utils Utils = new Utils();

            Map<String, String> postParams = new HashMap<String, String>(2);
            postParams.put("access_token", Utils.getToken(context));
            postParams.put("id", id);

            return OAuth.call("alerts", "deleteAlert", postParams);

        }

        @Override
        protected void onPostExecute(JSONObject response){
            try {
                if (!response.getBoolean("success")) {
                    Log.d("Error deleting", "Data > "+response);
                    MemetroDialog.showDialog(context, null, context.getString(R.string.error_deleting_alert));
                } else {
                    Toast.makeText(
                            context.getApplicationContext(),
                            context.getString(R.string.alert_deleted),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                MemetroDialog.showDialog(context, null, context.getString(R.string.json_error));
            }
            pdialog.dismiss();
        }
    }
}
