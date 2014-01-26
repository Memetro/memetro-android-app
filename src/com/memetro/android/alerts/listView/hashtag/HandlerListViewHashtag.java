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
package com.memetro.android.alerts.listView.hashtag;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.memetro.android.models.Tweet;

import java.util.List;

public class HandlerListViewHashtag extends BaseAdapter {

    private Activity activity;
    private List<Tweet> list_alerts;

    public HandlerListViewHashtag(Activity activity, List<Tweet> list_alerts) {
        this.activity = activity;
        this.list_alerts = list_alerts;
    }

    public int getCount(){
        return list_alerts.size();
    }

    public Tweet getItem(int position){

        return list_alerts.get(position);
    }

    public long getItemId(int position){

        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        DefineListViewHashtag lstItem = new DefineListViewHashtag(activity, list_alerts.get(position), getItemId(position));

        return lstItem;
    }
}