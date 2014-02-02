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
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memetro.android.R;
import com.memetro.android.models.Alert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DefineListViewAlerts  extends LinearLayout {

    private TextView description, hour;
    private String infService;
    private LayoutInflater li;

    public DefineListViewAlerts (Context context, Alert alert, Long itemId){

        super(context);

        // TODO Cambiar el icono del transporte

        infService = Context.LAYOUT_INFLATER_SERVICE;
        li = (LayoutInflater) getContext().getSystemService(infService);
        li.inflate(R.layout.item_alert_thermometer, this, true);

        description = (TextView) findViewById(R.id.alert_text);
        description.setText(alert.station);



        hour = (TextView) findViewById(R.id.hour_text);
        hour.setText(getHourFromDate(alert.date));
        hour.setBackgroundResource(getBgFromHour(alert.date));

    }

    private int getBgFromHour(String str_date) {
        Calendar calendar = getCalendarFromString(str_date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Calendar currentCalendar = GregorianCalendar.getInstance();
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

        if ((currentHour - hour) < 2) {
            return R.drawable.thermometer_red;
        }

        if ((currentHour - hour) < 5) {
            return R.drawable.thermometer_orange;
        }

        return R.drawable.thermometer_yellow;
    }

    private String getHourFromDate(String str_date) {
        Calendar calendar = getCalendarFromString(str_date);
        String hours = ((calendar.get(Calendar.HOUR_OF_DAY) < 10) ? "0" : "") + calendar.get(Calendar.HOUR_OF_DAY);
        String minutes = ((calendar.get(Calendar.MINUTE) < 10) ? "0" : "") + calendar.get(Calendar.MINUTE);
        return hours+":"+minutes;
    }

    private Calendar getCalendarFromString(String str_date) {
        try {
            java.text.DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = formatter.parse(str_date);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
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
