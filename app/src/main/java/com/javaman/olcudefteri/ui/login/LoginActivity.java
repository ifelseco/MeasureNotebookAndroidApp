package com.javaman.olcudefteri.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.javaman.olcudefteri.ui.tailor.TailorHomeActivity;
import com.javaman.olcudefteri.ui.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.presenter.LoginPresenter;
import com.javaman.olcudefteri.presenter.impl.LoginPresenterImpl;
import com.javaman.olcudefteri.utill.CipherHelper;
import com.javaman.olcudefteri.view.LoginView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String ARG_LOGIN_1="78s78ds78d45f";
    public static final String ARG_LOGIN_2="343434dfdfdfdf";
    private static final String ARG_ROLE_TAILOR="r3";


    @BindView(R.id.et_username)
    EditText editTextUsername;

    @BindView(R.id.et_password)
    EditText editTextPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.pb_login)
    ProgressBar progressBarLogin;

    @BindView(R.id.cb_remember)
    CheckBox cbRememberMe;

    private LoginPresenter mLoginPresenter;
    SharedPreferenceHelper sharedPreferenceHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());

        if(sharedPreferenceHelper.containKey("lastActivity")){
            sharedPreferenceHelper.removeKey("lastActivity");
        }


        Log.i(TAG, "onCreate()");

        checkGooglePlay();

        setContentView(R.layout.activity_login);
        mLoginPresenter=new LoginPresenterImpl(this);
        ButterKnife.bind(this);
        controlRememberMe(sharedPreferenceHelper);
    }

    private void controlRememberMe(SharedPreferenceHelper sharedPreferenceHelper) {
        if(sharedPreferenceHelper.containKey("rememberMe")){
            boolean rememberMeActive=sharedPreferenceHelper.getBooleanPreference("rememberMe",false);
            cbRememberMe.setChecked(rememberMeActive);
            if(rememberMeActive){
                if(sharedPreferenceHelper.containKey(ARG_LOGIN_1)){
                    if(!TextUtils.isEmpty(sharedPreferenceHelper.getStringPreference(ARG_LOGIN_1,""))){
                        try {
                            editTextUsername.setText(CipherHelper.decrypt(sharedPreferenceHelper.getStringPreference(ARG_LOGIN_1,"")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(sharedPreferenceHelper.containKey(ARG_LOGIN_2)){
                    if(!TextUtils.isEmpty(sharedPreferenceHelper.getStringPreference(ARG_LOGIN_2,""))){
                        try {
                            editTextPassword.setText(CipherHelper.decrypt(sharedPreferenceHelper.getStringPreference(ARG_LOGIN_2,"")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }
    }

    @OnClick(R.id.btn_login)
    public void onClick(View v){
        mLoginPresenter.validateCredential(editTextUsername.getText().toString().trim() ,
                editTextPassword.getText().toString().trim(),cbRememberMe.isChecked());
    }

    @Override
    public void showProgress() {
        progressBarLogin.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        progressBarLogin.setVisibility(View.GONE);
    }

    @Override
    public void setUserNameEmptyError() {
        editTextUsername.setError("Kullanıcı adı gerekli");
    }

    @Override
    public void setPasswordEmptyError() {
        editTextPassword.setError("Parola gerekli");
    }

    @Override
    public void navigatetoHome(String role) {
        sharedPreferenceHelper.setStringPreference("role",role);
        if(TextUtils.equals(role,ARG_ROLE_TAILOR)){
            startActivity(new Intent(LoginActivity.this , TailorHomeActivity.class));
        }else{
            startActivity(new Intent(LoginActivity.this , HomeActivity.class));
        }

    }

    @Override
    public void showAlert(String message, boolean isError) {
        if(isError){
            StyleableToast.makeText(this,message,R.style.error_toast_style).show();
        }else{
            StyleableToast.makeText(this,message,R.style.info_toast_style).show();
        }
    }

    @Override
    public void openSession(String sessionId) {
        sharedPreferenceHelper.setStringPreference("sessionId", sessionId);
    }

    @Override
    public void setRememberMe(String username, String password, boolean rememberMeActive) {
        if(rememberMeActive){
           sharedPreferenceHelper.setBooleanPreference("rememberMe",rememberMeActive);
            try {
                sharedPreferenceHelper.setStringPreference(ARG_LOGIN_1, CipherHelper.encrypt(username));
                sharedPreferenceHelper.setStringPreference(ARG_LOGIN_2,CipherHelper.encrypt(password));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            sharedPreferenceHelper.setBooleanPreference("rememberMe",rememberMeActive);
            sharedPreferenceHelper.removeKey(ARG_LOGIN_1);
            sharedPreferenceHelper.removeKey(ARG_LOGIN_2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroy();

    }

    public void checkGooglePlay(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGooglePlay();
    }


}
