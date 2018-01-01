package wuweixiong.bwie.com.weekonemoni_exam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private MyBanner myBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myBanner = findViewById(R.id.myBanner);
        getDataFromNet();
    }

    private void getDataFromNet() {
        OkHttp3Util.doGet("http://120.27.23.105/ad/getAd", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    final DataDataBean detalBean = new Gson().fromJson(string,DataDataBean.class);
                    final List<String> list = new ArrayList<>();
                    final List<DataDataBean.DataBean> data = detalBean.getData();
                    for (DataDataBean.DataBean dataBean:data){
                        String icon = dataBean.getIcon();
                        list.add(icon);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置时间
                            myBanner.setTimeSecond(5);

                            //设置显示轮播
                            myBanner.setImageUrls(list);

                            //设置点击事件
                            myBanner.setClickListner(new MyBanner.OnClickLisner() {
                                @Override
                                public void onItemClick(int position) {
                                   if (detalBean.getData().get(position).getType() == 0){
                                       Toast.makeText(MainActivity.this,"点击了"+detalBean.getData().get(position).getAid(),Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(MainActivity.this,WebActivity.class);
                                       intent.putExtra("url",data.get(position).getUrl());
                                       startActivity(intent);
                                   }else {
                                       Toast.makeText(MainActivity.this,"我要跳转到商品详情页",Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

                        }
                    });
                }
            }
        });
    }
}
