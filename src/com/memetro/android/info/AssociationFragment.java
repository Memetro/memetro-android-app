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

package com.memetro.android.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.memetro.android.DashboardActivity;
import com.memetro.android.R;
import com.memetro.android.web.WebViewer;

public class AssociationFragment extends Fragment {

    private DashboardActivity mActivity;
    private Button transtorno, asociarse, asesoramiento, memetroles, sintomas, contacto;

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        this.mActivity = (DashboardActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_association, container, false);

        transtorno = (Button) inflated.findViewById(R.id.transtorno_button);
        asociarse = (Button) inflated.findViewById(R.id.asociarse_button);
        asesoramiento = (Button) inflated.findViewById(R.id.asesoramiento_button);
        memetroles = (Button) inflated.findViewById(R.id.memetroles_button);
        sintomas = (Button) inflated.findViewById(R.id.sintomas_button);
        contacto = (Button) inflated.findViewById(R.id.contacto_button);

        transtorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl("http://www.memetro.net/trastorno-memetro/");
            }
        });

        asociarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl("http://www.memetro.net/asociarse/");
            }
        });

        asesoramiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl("http://www.memetro.net/asesoramiento-legal/");
            }
        });

        memetroles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl("http://www.memetro.net/memetroles/");
            }
        });

        sintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl("http://www.memetro.net/vivir-con-el-trastono/");
            }
        });

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl("http://www.memetro.net/contacto/");
            }
        });

        return inflated;
    }

    private void loadUrl(String url) {
        WebViewer webViewer = new WebViewer();
        Bundle arguments = new Bundle();
        arguments.putString("url", url);
        webViewer.setArguments(arguments);
        mActivity.changeMainFragment(webViewer);
    }
}
