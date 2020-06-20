package swu.xl.drivingquestion.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import swu.xl.drivingquestion.R;

public class TypeDialog extends DialogFragment implements View.OnClickListener {

    //监听者
    private CallBackListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //创建构建者
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //加载布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_main_choose, null);

        //找到控件
        TextView A1 = inflate.findViewWithTag(getContext().getResources().getString(R.string.A1));
        TextView A2 = inflate.findViewWithTag(getContext().getResources().getString(R.string.A2));
        TextView B1 = inflate.findViewWithTag(getContext().getResources().getString(R.string.B1));
        TextView B2 = inflate.findViewWithTag(getContext().getResources().getString(R.string.B2));
        TextView C1 = inflate.findViewWithTag(getContext().getResources().getString(R.string.C1));
        TextView C2 = inflate.findViewWithTag(getContext().getResources().getString(R.string.C2));
        A1.setOnClickListener(this);
        A2.setOnClickListener(this);
        B1.setOnClickListener(this);
        B2.setOnClickListener(this);
        C1.setOnClickListener(this);
        C2.setOnClickListener(this);

        //设置给builder
        builder.setView(inflate);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.chooseResult((String) v.getTag());
        }

        dismiss();
    }

    /**
     * 回调数据的接口
     */
    public interface CallBackListener{
        void chooseResult(String type);
    }

    public void setCallBackListener(CallBackListener listener) {
        this.listener = listener;
    }
}
