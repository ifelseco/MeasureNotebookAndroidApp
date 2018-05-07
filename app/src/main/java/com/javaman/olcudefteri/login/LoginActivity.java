package com.javaman.olcudefteri.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.javaman.olcudefteri.ui.tailor.TailorHomeActivity;
import com.javaman.olcudefteri.ui.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.presenter.LoginPresenter;
import com.javaman.olcudefteri.presenter.impl.LoginPresenterImpl;
import com.javaman.olcudefteri.view.LoginView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.et_username)
    EditText editTextUsername;

    @BindView(R.id.et_password)
    EditText editTextPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.pb_login)
    ProgressBar progressBarLogin;

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
    }

    @OnClick(R.id.btn_login)
    public void onClick(View v){
        mLoginPresenter.validateCredential(editTextUsername.getText().toString().trim() ,
                editTextPassword.getText().toString().trim());
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
    public void navigatetoHome(boolean isTailor) {

        if(isTailor){
            startActivity(new Intent(LoginActivity.this , TailorHomeActivity.class));
        }else{
            startActivity(new Intent(LoginActivity.this , HomeActivity.class));
        }

    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void openSession(String sessionId) {
        sharedPreferenceHelper.setStringPreference("sessionId", sessionId);
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
