package com.javaman.olcudefteri.tailor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.notification.NotificationFragment;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TailorHomeActivity extends AppCompatActivity {

    SharedPreferenceHelper sharedPreferenceHelper;
    String text;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public void getNotificationFragment(String text) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        NotificationFragment notificationFragment= new NotificationFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        notificationFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_container, notificationFragment, "notification-fragment");
        fragmentTransaction.commit();
    }

    public void getOrderFragment(String text) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        TailorOrderFragment  tailorOrderFragment= new TailorOrderFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        tailorOrderFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_container, tailorOrderFragment, "tailor-order-fragment");
        fragmentTransaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor_home);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        setSupportActionBar(toolbar);
        initBottomNav();
        getOrderFragment("Devam eden işler");

    }

    private void initBottomNav() {
        AHBottomNavigationItem item_processing = new AHBottomNavigationItem(R.string.title_processing, R.drawable.ic_tailor, R.color.hintColor);
        AHBottomNavigationItem item_processed = new AHBottomNavigationItem(R.string.title_processed, R.drawable.ic_check_circle_black_24dp, R.color.hintColor);
        AHBottomNavigationItem item_notification = new AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_notifications_black_24dp, R.color.hintColor);
        ahBottomNavigation.addItem(item_processing);
        ahBottomNavigation.addItem(item_processed);
        ahBottomNavigation.addItem(item_notification);
        ahBottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.colorAccentText));
        ahBottomNavigation.setAccentColor(fetchColor(R.color.yello));
        ahBottomNavigation.setInactiveColor(fetchColor(R.color.hintColor));
        ahBottomNavigation.setCurrentItem(0);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setNotification("3",2);
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    text="Devam eden işler";
                    getOrderFragment(text);
                    return true;
                }else if(position==1){
                    text="Biten işler";
                    getOrderFragment(text);
                    return true;
                }else if(position==2){
                    text="Bildirimler";
                    getNotificationFragment(text);
                    return true;
                }
                return true;
            }
        });

    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferenceHelper.setStringPreference("lastActivity", getClass().getName());

    }

}
