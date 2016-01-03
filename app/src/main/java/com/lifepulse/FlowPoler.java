package com.lifepulse;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class FlowPoler {
    Activity activity;

    public FlowPoler(Activity activity) {
        this.activity = activity;
    }

    public void startPollStream(){
        Request.Builder builder = new Request.Builder();
        builder.url("https://run-west.att.io/4095e2904b934/883c44cfa9e6/a649986eff9bc0d/in/flow/getWs");
        builder.method("POST", RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\n" +
                "    \"appKey\": \"DE_98D793C3062B5F83_1\",\n" +
                "    \"password\": \"NO-PASSWD\",\n" +
                "    \"userId\": \"553474442\",\n" +
                "    \"domain\": \"DL\"\n" +
                "}"));
        //552494308
        final Request request = builder.build();
        final OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}

            @Override
            public void onResponse(Response response) throws IOException {
                JsonElement json = new JsonParser().parse(response.body().string());
                String streamUrl = json.getAsJsonObject().get("url").getAsString();
                startStream(streamUrl);
            }
        });
    }

    public void startStream(String streamUrl) throws IOException {
        URL url = new URL(streamUrl);
        InputStream is = url.openStream();

        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(bis));
        String line;
        DigitalLifeParser dlParser = new DigitalLifeParser(activity);
        Log.d("WATCHING", "watching for events");
        while( (line = br.readLine()) != null) {
            line = line.replaceAll("\"\"\"", "");
            if(!line.startsWith("*")) {
                JsonObject event = new JsonParser().parse(line).getAsJsonObject();
                dlParser.parse(event);
            }
        }
    }
}
