package com.javaman.olcudefteri.home;

import com.javaman.olcudefteri.login.LoginIntractorImpl;
import com.javaman.olcudefteri.login.LoginView;
import com.javaman.olcudefteri.model.FirebaseRegIdModel;

/**
 * Created by javaman on 20.02.2018.
 */

public class HomePresenterImpl implements HomePresenter {

    HomeView mHomeViev;
    HomeIntractor mHomeIntractor;

    public HomePresenterImpl(HomeView mHomeViev ){
        this.mHomeViev=mHomeViev;
        mHomeIntractor=new HomeIntractorImpl();
    }

    @Override
    public void sendFirebaseRegIdToServer(String xAuthToken , FirebaseRegIdModel regIdModel) {
        if(mHomeViev!=null){
            mHomeIntractor.sendFirebaseRegIdToServer(xAuthToken,regIdModel);
        }
    }

    @Override
    public void onDestroy() {
        if(mHomeViev!=null){
            mHomeViev=null;
        }
    }
}
