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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.memetro.android.DashboardActivity;
import com.memetro.android.R;
import com.memetro.android.alerts.listView.hashtag.HandlerListViewHashtag;
import com.memetro.android.alerts.listView.thermometer.HandlerListViewAlerts;
import com.memetro.android.dataManager.dataUtils;
import com.memetro.android.oauth.oauthHandler;

import org.json.JSONArray;

public class HashtagFragment extends Fragment {

    private DashboardActivity mActivity;
    private AlertUtils alertUtils = new AlertUtils();
    private PullToRefreshListView alertListView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.mActivity = (DashboardActivity) getActivity();
        mActivity.fullActionBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.compressActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.fragment_thermometer, container, false);

        alertListView = (PullToRefreshListView) inflated.findViewById(R.id.alertListView);

        alertListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getTweets();
            }
        });

        getTweets();
        setList();

        return inflated;
    }

    private void setList() {
        HandlerListViewHashtag adapter = new HandlerListViewHashtag(mActivity, dataUtils.getTweets());
        alertListView.setAdapter(adapter);
    }

    private void getTweets() {
        alertUtils.getTweets(mActivity, new oauthHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(JSONArray tweetsData) {
                try {
                    dataUtils.saveTweets(tweetsData);
                    setList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                alertListView.onRefreshComplete();
            }
        });
    }
}
