package com.whycools.transport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.whycools.transport.MainActivity;
import com.whycools.transport.R;
import com.whycools.transport.base.BaseActivity;
import com.whycools.transport.utils.Error;
import com.whycools.transport.utils.RequestData;
import com.whycools.transport.utils.SharedPreferencesUtil;
import com.whycools.transport.utils.TransparentDialogUtil;
import com.whycools.transport.utils.Zip;

/**
 * \
 * 登录页面
 * Created by Administrator on 2017-12-29.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText login_ed_username;//用户名输入框
    private EditText login_ed_password;//密码输入框
    private ImageView login_lookimg;//显示密码
    private Checkable login_ck_password;//记住密码
    private TextView login_forgetpassword;//忘记密码
    private Button login_bt;//登录按钮
    private Button login_bt_register;//注册按钮

    private boolean showPassword = true;//是否显示密码

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        login_ed_username = findViewById(R.id.login_ed_username);
        login_ed_password = findViewById(R.id.login_ed_password);

        login_lookimg = findViewById(R.id.login_lookimg);

        login_ck_password = findViewById(R.id.login_ck_password);

        login_forgetpassword = findViewById(R.id.login_forgetpassword);

        login_bt = findViewById(R.id.login_bt);
        login_bt_register = findViewById(R.id.login_bt_register);

    }

    @Override
    public void initListeners() {
        login_lookimg.setOnClickListener(this);
        login_forgetpassword.setOnClickListener(this);
        login_bt.setOnClickListener(this);
        login_bt_register.setOnClickListener(this);

    }

    @Override
    public void initData() {
        String username=SharedPreferencesUtil.getData(mContext,"username","").toString();
        String password=SharedPreferencesUtil.getData(mContext,"password","").toString();
        login_ed_username.setText(username);
        login_ed_password.setText(password);
        Selection.setSelection(login_ed_username.getText(), login_ed_username.getText().length());//光标放在末尾
        Selection.setSelection(login_ed_password.getText(), login_ed_password.getText().length());//光标放在末尾

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_lookimg:
                isShowPassword();
                break;
            case R.id.login_bt:
                login();
                break;
            case R.id.login_forgetpassword:
                login_forgetpassword.setEnabled(false);
                getPassword();
                break;
        }

    }

    /**
     * 是都显示密码默认为不显示
     */
    private void isShowPassword() {
        if (showPassword) {
            login_ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
            Selection.setSelection(login_ed_password.getText(), login_ed_password.getText().length());//光标放在末尾
            showPassword = false;
        } else {
            //否则隐藏密码
            login_ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            Selection.setSelection(login_ed_password.getText(), login_ed_password.getText().length());//光标放在末尾
            showPassword = true;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!isNetworkAvailable(mContext)) {
            TransparentDialogUtil.showErrorMessage(LoginActivity.this, NET_NOUSE);
            return;
        }
        SharedPreferencesUtil.saveData(mContext, "username", login_ed_username.getText().toString());
        if (login_ck_password.isChecked()) {
            SharedPreferencesUtil.saveData(mContext, "password", login_ed_password.getText().toString());
        } else {
            SharedPreferencesUtil.saveData(mContext, "password", "");
        }

        if (!isNetworkAvailable(mContext)) {
            TransparentDialogUtil.showErrorMessage(mContext, NET_NOUSE);
            return;
        }
        String services = SharedPreferencesUtil.getData(mContext, "ServerAddress", SERVERDDRESS).toString();
        Log.i(TAG, "服务器地址: " + services);
        if (login_ed_username.getText().length() == 0) {
            TransparentDialogUtil.showErrorMessage(LoginActivity.this, "用户名不能为空");
            return;
        }
        if (login_ed_password.getText().length() == 0) {
            TransparentDialogUtil.showErrorMessage(LoginActivity.this, "密码不能为空");
            return;
        }
        TransparentDialogUtil.showLoadingMessage(LoginActivity.this, "正在登陆中...", false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("user", login_ed_username.getText().toString());
        params.addBodyParameter("pwd", login_ed_password.getText().toString());
        String url = SharedPreferencesUtil.getData(mContext, "ServerAddress", SERVERDDRESS).toString();
        String login_url = url + "whycools/getAccessRight.jsp?";
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                login_url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        TransparentDialogUtil.dismiss();
                        if (responseInfo.result != null && responseInfo.result.length() != 0) {
                            SharedPreferencesUtil.saveData(mContext, "userid", responseInfo.result);
                            Intent intent_menuencyclopedia = new Intent(LoginActivity.this, MenuEncyclopediaActivity.class);
                            startActivity(intent_menuencyclopedia);
                            finish();
                        } else {
                            TransparentDialogUtil.showErrorMessage(LoginActivity.this, "用户名或密码错误");
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        TransparentDialogUtil.showErrorMessage(LoginActivity.this, "登录失败");
                    }
                });

    }
    public void getPassword(){
        new Thread() {
            @Override
            public void run() {
                super.run();
                String username = SharedPreferencesUtil.getData(LoginActivity.this, "username", "").toString();
                String ServerAddress = SharedPreferencesUtil.getData(LoginActivity.this, "ServerAddress", SERVERDDRESS).toString();
                String url = ServerAddress + "whycools/changePassword.jsp?s=" + Zip.compress("ChangePassword '" + username + "','',''");
                String result = RequestData.getResult(url);
                handler.sendMessage(new Message());


            }
        }.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(LoginActivity.this, "请不要重复请求，密码将以短信的方式发送到你的手机，请查看", Toast.LENGTH_LONG).show();
        }
    };
      // 记录退出时间时候使用
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                        Toast.LENGTH_SHORT).show();
                // 记录当前时间，如果是在两秒内
                exitTime = System.currentTimeMillis();
            } else {
                // 退出代码
                finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
