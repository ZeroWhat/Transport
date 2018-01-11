package com.whycools.transport.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.whycools.transport.R;

/**
 * Created by Zero on 2017-01-10.
 */

public class CustomDialog extends Dialog {
    private EditText editText;
    private Button positiveButton, negativeButton;
    private RadioButton custom_dialog_in;
    private RadioButton custom_dialog_out;
    private int isin=1;

    public CustomDialog(Context context) {
        super(context);
        setCustomDialog();
    }


    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, null);
        editText = mView.findViewById(R.id.number);
        positiveButton = mView.findViewById(R.id.positiveButton);
        negativeButton = mView.findViewById(R.id.negativeButton);
        custom_dialog_in = mView.findViewById(R.id.custom_dialog_in);
        custom_dialog_out = mView.findViewById(R.id.custom_dialog_out);
        custom_dialog_in.setChecked(true);
        super.setTitle("条形码");
        super.setContentView(mView);

    }

    public View getEditText(){
        return editText;
    }
    public View getrbin() {
        return custom_dialog_in;
    }
    public View getrbout() {
        return custom_dialog_out;
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        positiveButton.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        negativeButton.setOnClickListener(listener);
    }
}