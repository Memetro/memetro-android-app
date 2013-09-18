/*
 * Copyright 2013 Josei
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

package com.memetro.android.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.memetro.android.R;

public class PersonalActivity extends Activity {

    private static String TAG = "Memetro Register Personal";
    private Button continueButton;
    private EditText name, mail, twitter, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_personal);

        continueButton = (Button) findViewById(R.id.register);

        name = (EditText) findViewById(R.id.name);
        mail = (EditText) findViewById(R.id.mail);
        twitter = (EditText) findViewById(R.id.twitter);
        about = (EditText) findViewById(R.id.about);
        areTextsEmpty();
        TextWatcher watcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (areTextsEmpty()){
                    continueButton.setText(getString(R.string.skip));
                }else{
                    continueButton.setText(getString(R.string.continue_text));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                // TODO Auto-generated method stub

            }
        };
        name.addTextChangedListener(watcher);

        mail.addTextChangedListener(watcher);

        twitter.addTextChangedListener(watcher);

        about.addTextChangedListener(watcher);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CredentialsActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("mail", mail.getText().toString());
                intent.putExtra("twitter", twitter.getText().toString());
                intent.putExtra("about", about.getText().toString());
                startActivity(intent);
            }
        });

    }

    private boolean areTextsEmpty(){
        return !(name.getText().toString().length() != 0
                || mail.getText().toString().length() != 0
                || twitter.getText().toString().length() != 0
                || about.getText().toString().length() != 0
            );
    }

}
