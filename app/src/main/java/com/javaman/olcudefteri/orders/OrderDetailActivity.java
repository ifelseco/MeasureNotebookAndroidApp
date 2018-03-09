package com.javaman.olcudefteri.orders;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends AppCompatActivity implements FloatingActionMenu.OnMenuToggleListener, View.OnClickListener {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    @BindView(R.id.fab_order_edit)
    com.github.clans.fab.FloatingActionButton fabOrderEdit;

    @BindView(R.id.fab_order_delete)
    com.github.clans.fab.FloatingActionButton fabOrderDelete;

    @BindView(R.id.fab_order_status)
    com.github.clans.fab.FloatingActionButton fabOrderStatus;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private OrderDetailResponseModel orderDetailResponseModel;
    private CustomerDetailModel customerDetailModel;
    Bundle arguments;

    public static final String ARG_CURRENT_ORDER = "current_order";
    public static final String ARG_CURRENT_CUSTOMER = "current_customer_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initFabMenu();

        setArgumentForFragments();

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        /*if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Long orderId=getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID,-1);
            arguments.putLong(OrderDetailFragment.ARG_ITEM_ID,orderId);
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.order_detail_container, fragment)
                    .commit();
        }*/


    }

    public void initFabMenu(){
        fabMenu.setOnMenuToggleListener(this);
        fabOrderDelete.setOnClickListener(this);
        fabOrderEdit.setOnClickListener(this);
        fabOrderStatus.setOnClickListener(this);
    }

    public void setArgumentForFragments(){
        arguments = new Bundle();
        orderDetailResponseModel=getIntent().getExtras().getParcelable(OrderDetailActivity.ARG_CURRENT_ORDER);
        customerDetailModel=getIntent().getExtras().getParcelable(OrderDetailActivity.ARG_CURRENT_CUSTOMER);
        arguments.putParcelable(OrderDetailActivity.ARG_CURRENT_ORDER,orderDetailResponseModel);
        arguments.putParcelable(OrderDetailActivity.ARG_CURRENT_CUSTOMER,customerDetailModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_order_detail_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //NavUtils.navigateUpTo(this, new Intent(this, OrdersActivity.class));
            Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //NavUtils.navigateUpTo(this, new Intent(this, OrdersActivity.class));
        Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_order_edit) {
            showToast("Sipariş düzenle diyalogu");
        } else if (v.getId() == R.id.fab_order_delete) {
            showToast("Sipariş silme diyalogu");
        } else {
            showToast("Sipariş durumu değiştir");
        }
        fabMenu.close(true);
    }

    @Override
    public void onMenuToggle(boolean opened) {
        if (opened) {
            showToast("Menu is açıldı");
        } else {
            showToast("Menu is kapandı");
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        OrderDetailFragment orderDetailFragment=new OrderDetailFragment();
        orderDetailFragment.setArguments(arguments);

        CustomerDetailFragment customerDetailFragment=new CustomerDetailFragment();
        customerDetailFragment.setArguments(arguments);

        OrderLineFragment orderLineFragment=new OrderLineFragment();
        orderLineFragment.setArguments(arguments);

        adapter.addFragment(orderDetailFragment, "Sipariş Bilgileri");
        adapter.addFragment(customerDetailFragment, "Müşteri Bilgileri");
        adapter.addFragment(orderLineFragment, "Sipariş Ölçüleri");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
