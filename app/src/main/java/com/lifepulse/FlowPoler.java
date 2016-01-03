package com.lifepulse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class FlowPoler {
    private boolean isActive = false;

    public void startPoll(AuthToken authToken){
        isActive = true;
        Request.Builder builder = new Request.Builder();
        builder.get();
        builder.url("https://run-west.att.io/4095e2904b934/883c44cfa9e6/a649986eff9bc0d/in/flow/alerts");
        final Request request = builder.build();
        final OkHttpClient client = new OkHttpClient();
        new Thread(){
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void startStream(List<DigitalLifeDevice> devices) throws IOException {
        String hostname = "run-west.att.io";
        int port = 64948;

        Socket socket = new Socket(hostname, port);
        InputStream is = socket.getInputStream();

        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(bis));
        String line = "";
        DigitalLifeParser dlParser = new DigitalLifeParser(devices);
        while( (line = br.readLine()) != null) {
            line = line.replaceAll("\"\"\"", "");
            if(!line.startsWith("*")) {
                JsonObject event = new JsonParser().parse(line).getAsJsonObject();
                dlParser.parse(event);
            }
        }
    }
}
