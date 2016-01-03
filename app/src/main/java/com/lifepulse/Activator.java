package com.lifepulse;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class Activator {

    public void authenticate(){
        Request.Builder builder = new Request.Builder();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", "553474421");
        params.put("password", "NO-PASSWD");
        params.put("appKey", "BE_75FCFB017D1EEF50_1");

        builder.url("https://systest.digitallife.att.com/penguin/api/authtokens");
        builder.post(transformToRequestBody(params));
        final Request request = builder.build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}

            @Override
            public void onResponse(Response response) throws IOException {
                JsonElement json = new JsonParser().parse(response.body().string());
                String authToken = json.getAsJsonObject().get("content").getAsJsonObject().get("authToken").getAsString();
                String requestToken = json.getAsJsonObject().get("content").getAsJsonObject().get("requestToken").getAsString();
                String gateway = json.getAsJsonObject().get("content").getAsJsonObject().get("gateways").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                AuthToken token = new AuthToken(authToken, requestToken, gateway);
                EventBus.getDefault().post(new AuthenticatedEvent(token));
            }
        });
    }

    public void getDevices(AuthToken token){
        Request.Builder builder = new Request.Builder();
        builder.url("https://systest.digitallife.att.com/penguin/api/" + token.getGateway() +"/devices");
        builder.get();
        Request request = builder.build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}

            @Override
            public void onResponse(Response response) throws IOException {
                JsonElement json = new JsonParser().parse(response.body().string()).getAsJsonObject().get("content").getAsJsonArray();
                List<DigitalLifeDevice> devices = new Gson().fromJson(json, new TypeToken<List<DigitalLifeDevice>>(){}.getType());
                EventBus.getDefault().post(new DevicesReceivedEvent(devices));
            }
        });
    }

    @Nullable
    private RequestBody transformToRequestBody(HashMap<String, String> query) {
        if(query == null) return null;

        FormEncodingBuilder formBody = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            formBody.add(entry.getKey(), entry.getValue());
        }
        return formBody.build();
    }
}
