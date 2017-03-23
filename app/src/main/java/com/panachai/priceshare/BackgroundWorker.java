package com.panachai.priceshare;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;


public class BackgroundWorker extends AsyncTask<String, Void, String> { //ค่า parameter ที่ 3 คือค่า return

    private Context context;
    private AlertDialog alertDialog;
    private Gson gson = new Gson();

    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");

    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://10.0.2.2/Webservice/gen_json.php").build(); //http://consolesaleth.esy.es/json/gen_json.php

        try {
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                //response สำเร็จเข้า if นี้

                String result = response.body().string();
                //return result; //ถ้าไม่ทำไปใช้ต่อ return แค่นี้

                //นำค่ามาใช้ GSON
                Type collectionType = new TypeToken<Collection<MemberResponse>>() {
                }.getType();
                Collection<MemberResponse> enums = gson.fromJson(result, collectionType);
                MemberResponse[] memberResult = enums.toArray(new MemberResponse[enums.size()]);

                return memberResult[0].getUsername();
                //return response.body().string();
            } else {
                return "Not Success - code : " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error - " + e.getMessage();
        }

    }


    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
