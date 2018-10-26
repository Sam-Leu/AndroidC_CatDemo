package com.example.one.catdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button request = (Button)findViewById(R.id.btn);
        img = (ImageView)findViewById(R.id.img);
        //Picasso.get().load("https://27.media.tumblr.com/tumblr_m3u17mbcIM1r73wdao1_500.jpg").into(img);
        request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn){
            sendRequestWithOkHttp();
        }
    }

    //通过API获取json数据
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://api.thecatapi.com/v1/images/search")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //解释json数据
    private void parseJSONWithJSONObject(final String jsonData){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String url = jsonObject.getString("url");
                        Log.d("MainActivity", url);
                        //调用Picasso API
                        Picasso.get().load(url).into(img);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
