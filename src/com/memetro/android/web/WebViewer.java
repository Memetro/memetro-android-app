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

package com.memetro.android.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.memetro.android.DashboardActivity;
import com.memetro.android.R;
import com.memetro.android.common.MemetroProgress;

public class WebViewer extends Fragment {

    private WebView mainWebView;
    private String loadUrl;
    private DashboardActivity mActivity;
    private MemetroProgress pdialog;

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        this.mActivity = (DashboardActivity) getActivity();
        this.pdialog = new MemetroProgress(mActivity);
        pdialog.setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_webview, container, false);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            loadUrl = arguments.getString("url");
        }

        mainWebView = (WebView) inflated.findViewById(R.id.main_web_view);

        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pdialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pdialog.dismiss();
            }
        });

        mainWebView.loadUrl(loadUrl);

        return inflated;
    }
}
