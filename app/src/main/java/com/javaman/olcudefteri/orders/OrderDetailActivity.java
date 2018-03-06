package com.javaman.olcudefteri.orders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.javaman.olcudefteri.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Order detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items

 */
public class OrderDetailActivity extends AppCompatActivity implements FloatingActionMenu.OnMenuToggleListener, View.OnClickListener {

    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    @BindView(R.id.fab_order_edit)
    FloatingActionButton fabOrderEdit;

    @BindView(R.id.fab_order_delete)
    FloatingActionButton fabOrderDelete;

    @BindView(R.id.fab_order_status)
    FloatingActionButton fabOrderStatus;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initFabMenu();

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            Long orderId=getIntent().getLongExtra(OrderDetailFragment.ARG_ITEM_ID,-1);
            arguments.putLong(OrderDetailFragment.ARG_ITEM_ID,orderId);
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.order_detail_container, fragment)
                    .commit();
        }
    }

    public void initFabMenu(){
        fabMenu.setOnMenuToggleListener(this);
        fabOrderDelete.setOnClickListener(this);
        fabOrderEdit.setOnClickListener(this);
        fabOrderStatus.setOnClickListener(this);
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

    @Override
    public void onMenuToggle(boolean opened) {
        if (opened) {
            showToast("Menu is açıldı");
        } else {
            showToast("Menu is kapandı");
        }
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
}
