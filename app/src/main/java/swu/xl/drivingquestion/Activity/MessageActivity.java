package swu.xl.drivingquestion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import swu.xl.drivingquestion.Bean.QuestionBean;
import swu.xl.drivingquestion.DataCenter.DataUtil;
import swu.xl.drivingquestion.R;
import swu.xl.xltoolbar.XLToolBar;

public class MessageActivity extends AppCompatActivity {

    //科目类型
    private int subject;

    //驾照类型
    private String model;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //获取数据
        Intent intent = getIntent();
        subject = intent.getIntExtra("subject", 0);
        model = intent.getStringExtra("model");

        //设置题目类型
        XLToolBar toolBar = findViewById(R.id.tool_bar);
        String kind = getQuestionKind(subject,model);
        toolBar.getLogo_btn().setText(kind);

        //刷新控件
        refresh = findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.parseColor("#6C9AD7"));
        refresh.setRefreshing(true);

        //初始化操作
        initView();
    }

    /**
     * 获取题目类型
     * @param subject
     * @param model
     * @return
     */
    private String getQuestionKind(int subject, String model) {
        String sub;
        String type;

        switch (subject) {
            case 1:
                sub = "科目一";
                break;
            case 4:
                sub = "科目四";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + subject);
        }

        switch (model) {
            case "a1":
                type = "A1驾照";
                break;
            case "a2":
                type = "A2驾照";
                break;
            case "b1":
                type = "B1驾照";
                break;
            case "b2":
                type = "B2驾照";
                break;
            case "c1":
                type = "C1驾照";
                break;
            case "c2":
                type = "C2驾照";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + model);
        }

        return sub+" | "+type;
    }

    /**
     * 初始化操作
     */
    private void initView() {
        //获取数据
        DataUtil.loadDataByServer(subject,model);

        //数据的回调
        DataUtil.callback = new DataUtil.CallBack() {
            @Override
            public void getData(final QuestionBean bean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //绑定视图
                        ViewPager viewPager = findViewById(R.id.view_pager);

                        //获取具体的数据
                        final List<QuestionBean.ResultBean> beans = bean.getResult();

                        //设置适配器
                        viewPager.setAdapter(new PagerAdapter() {
                            @Override
                            public int getCount() {
                                return beans.size();
                            }

                            @Override
                            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                                return view == object;
                            }

                            @Override
                            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                                container.removeView((View) object);
                            }

                            @NonNull
                            @Override
                            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                                //加载布局以及视图
                                View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_question_layout, null);
                                ImageView url = inflate.findViewById(R.id.url);
                                TextView question = inflate.findViewById(R.id.question);
                                TextView item1 = inflate.findViewById(R.id.item1);
                                TextView item2 = inflate.findViewById(R.id.item2);
                                TextView item3 = inflate.findViewById(R.id.item3);
                                TextView item4 = inflate.findViewById(R.id.item4);
                                TextView answer = inflate.findViewById(R.id.answer);
                                TextView explain = inflate.findViewById(R.id.explain);

                                //加载数据
                                QuestionBean.ResultBean resultBean = beans.get(position);
                                if (!TextUtils.isEmpty(resultBean.getUrl())){
                                    Glide.with(getApplicationContext()).load(resultBean.getUrl()).into(url);
                                }
                                question.setText((position+1)+"/"+beans.size()+"、"+resultBean.getQuestion());
                                item1.setText("A、"+resultBean.getItem1());
                                item2.setText("B、"+resultBean.getItem2());
                                if (!TextUtils.isEmpty(resultBean.getItem3())){
                                    item3.setText("C、"+resultBean.getItem3());
                                }
                                if (!TextUtils.isEmpty(resultBean.getItem4())){
                                    item4.setText("D、"+resultBean.getItem4());
                                }
                                String result;
                                switch (resultBean.getAnswer()){
                                    case "1":
                                        result = "A";
                                        break;
                                    case "2":
                                        result = "B";
                                        break;
                                    case "3":
                                        result = "C";
                                        break;
                                    case "4":
                                        result = "D";
                                        break;
                                    case "7":
                                        result = "AB";
                                        break;
                                    case "8":
                                        result = "AC";
                                        break;
                                    case "9":
                                        result = "AD";
                                        break;
                                    case "10":
                                        result = "BC";
                                        break;
                                    case "11":
                                        result = "BD";
                                        break;
                                    case "12":
                                        result = "CD";
                                        break;
                                    case "13":
                                        result = "ABC";
                                        break;
                                    case "14":
                                        result = "ABD";
                                        break;
                                    case "15":
                                        result = "ACD";
                                        break;
                                    case "16":
                                        result = "BCD";
                                        break;
                                    case "17":
                                        result = "ABCD";
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + resultBean.getAnswer());
                                }
                                answer.setText("正确答案："+result);
                                explain.setText("解析："+resultBean.getExplains());

                                //添加视图
                                container.addView(inflate);

                                return inflate;
                            }
                        });

                        //刷新结束
                        refresh.setRefreshing(false);
                        refresh.setEnabled(false);
                    }
                });
            }
        };
    }
}