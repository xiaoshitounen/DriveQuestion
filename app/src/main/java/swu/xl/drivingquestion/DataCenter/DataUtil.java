package swu.xl.drivingquestion.DataCenter;

import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import swu.xl.drivingquestion.Bean.QuestionBean;
import swu.xl.drivingquestion.Constant.Constant;

public class DataUtil {

    //监听者
    public static CallBack callback;
    //回调的CallBack
    public interface CallBack {
        void getData(QuestionBean bean);
    }

    //OkHttpClient
    private final static OkHttpClient client = new OkHttpClient();

    public static void loadDataByServer(int subject, String model){
        //异步Get请求经城市所在天气数据
        //url
        //https://api.avatardata.cn/Jztk/Query?key=2dffbbfe11fe4d9ab88aef67f6225aca&subject=1&model=b1&testType=rand
        String url = "http://api.avatardata.cn/Jztk/Query?" +
                "key=2dffbbfe11fe4d9ab88aef67f6225aca&" +
                "subject=" + subject + "&" +
                "model=" + model + "&" +
                "testType=rand";

        //请求
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        Log.d(Constant.TAG,url);

        //回调
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(Constant.TAG,"响应失败:Failure");
                Log.d(Constant.TAG,e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //成功的情况下
                if (response.isSuccessful()) {
                    //读取响应头
                    Headers headers = response.headers();
                    for (int i = 0; i < headers.size(); i++) {
                        Log.d(Constant.TAG,headers.name(i)+":"+headers.value(i));
                    }

                    //读取响应体
                    ResponseBody body = response.body();
                    String jsonString = body.string();

                    //解析
                    Gson gson = new Gson();
                    QuestionBean questionBean = gson.fromJson(jsonString, QuestionBean.class);
                    Log.d(Constant.TAG,questionBean.toString());

                    //回调
                    if (callback != null){
                        callback.getData(questionBean);
                    }

                }else {

                    Log.d(Constant.TAG,"响应失败:Response Failure");
                }
            }
        });
    }
}
