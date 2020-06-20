package swu.xl.drivingquestion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import swu.xl.drivingquestion.Constant.Constant;
import swu.xl.drivingquestion.Dialog.TypeDialog;
import swu.xl.drivingquestion.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TypeDialog.CallBackListener {

    //记录选择的科目类型 1 4
    private int subject;

    //记录选择的驾照类型 a1 a2 b1 b2 c1 c2
    private String model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化操作
        initView();
    }

    /**
     * 初始化操作
     */
    private void initView() {
        TextView subject1 = findViewById(R.id.subject_1);
        subject1.setOnClickListener(this);
        TextView subject4 = findViewById(R.id.subject_4);
        subject4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subject_1:
                //设置科目类型
                subject = 1;

                break;

            case R.id.subject_4:
                //设置科目类型
                subject = 4;
                break;
        }

        //打开弹出框
        TypeDialog typeDialog = new TypeDialog();
        typeDialog.setCallBackListener(this);
        typeDialog.show(getSupportFragmentManager(),"type");
    }

    @Override
    public void chooseResult(String type) {
        this.model = type;

        //界面跳转
        Log.d(Constant.TAG,"subject:"+subject+" model:"+model);

        Intent intent = new Intent(MainActivity.this,MessageActivity.class);
        intent.putExtra("subject",subject);
        intent.putExtra("model",model);

        startActivity(intent);
    }
}