package com.javaman.olcudefteri.orders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.javaman.olcudefteri.home.HomeActivity;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.reports.ReportsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String ARG_ADD_ORDER = "arg_add_order";
    private Bundle customerFormData = new Bundle();
    SharedPreferences sharedPref;
    private boolean isCustomerRegister = false;
    private AddCustomerResponse addCustomerResponse;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ActionBarDrawerToggle toggle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        navigationView.setCheckedItem(R.id.measure);
        navigationView.setNavigationItemSelectedListener(this);


        addCustomerResponse=getIntent().getParcelableExtra(ARG_ADD_ORDER);

        if(addCustomerResponse!=null){
            addAddOrderFragment(addCustomerResponse);
        }else{
            addFragmentRegisterCustomer();
        }

    }

    public void addFragmentRegisterCustomer() {


        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        RegisterCustomerFragment registerCustomerFragment = new RegisterCustomerFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.add(R.id.register_customer_area, registerCustomerFragment, "register-customer-fragment")
                .addToBackStack("register-customer-fragment");

        fragmentTransaction.commit();


    }

    public void addAddOrderFragment(AddCustomerResponse addCustomerResponse) {
        AddOrderFragment addOrderFragment = new AddOrderFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ARG_ADD_ORDER,addCustomerResponse);
        addOrderFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.register_customer_area, addOrderFragment, "add-order-fragment")
                .addToBackStack("add-order-fragment");
        fragmentTransaction.commit();
    }



    @Override
    public void onBackPressed() {
        Log.d("Count : ", "" + fragmentManager.getBackStackEntryCount());

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                Intent home = new Intent(AddOrderActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.orders:
                Intent orders = new Intent(AddOrderActivity.this, OrdersActivity.class);
                startActivity(orders);
                break;
            case R.id.measure:
                Intent measure = new Intent(AddOrderActivity.this, AddOrderActivity.class);
                startActivity(measure);
                break;
            case R.id.report:
                Intent report = new Intent(AddOrderActivity.this, ReportsActivity.class);
                startActivity(report);
                break;
            // this is done, now let us go and intialise the home page.
            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
        }


        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MesajXXXXXX : ", "CustomerMeasureActivity Pause metodu çalıştı....");

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();

    }


    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            //show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.syncState();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    drawer.openDrawer(GravityCompat.START);
                }
            });
        }
    }


}


