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
package com.memetro.android.alerts.listView.thermometer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memetro.android.R;
import com.memetro.android.models.Alert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DefineListViewAlerts  extends LinearLayout {

    private TextView description, info;
    private String infService;
    private LayoutInflater li;

    public DefineListViewAlerts (Context context, Alert alert, Long itemId){

        super(context);


        infService = Context.LAYOUT_INFLATER_SERVICE;
        li = (LayoutInflater) getContext().getSystemService(infService);
        li.inflate(R.layout.item_alert_thermometer, this, true);

        description = (TextView) findViewById(R.id.alertText);
        description.setText(alert.description);


        String infoMessage = context.getString(R.string.alert)+" "+getDateToShow(context, alert.date);

        info = (TextView) findViewById(R.id.alertInfo);
        info.setText(infoMessage);

    }

    private String getDateToShow(Context context, String str_date) {
        long time = (System.currentTimeMillis()/ 1000) - dateToTime(str_date);

        if (time < 0) time = 0;

        String type = context.getString(R.string.seconds);

        if (time > 86400) {
            type = context.getString(R.string.days);
            time = time / 86400;
            return time+" "+type;
        }else if (time > 3600) {
            type = context.getString(R.string.hours);
            time = time / 3600;
            return time+" "+type;
        }
        else if (time > 60) {
            type = context.getString(R.string.minutes);
            time = time / 60;
            return time+" "+type;
        }

        return time+" "+type;
    }

    private long dateToTime(String str_date) {
        try{

            java.text.DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = formatter.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate.getTime()/1000;

        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
