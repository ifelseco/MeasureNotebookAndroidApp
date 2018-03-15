package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 14.12.2017.
 */

public class AddOrderFragment extends Fragment implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private AddCustomerResponse addCustomerResponse;
    private BottomSheetBehavior bottomSheetBehavior;
    private int productCount;

    @BindView(R.id.tv_custmer_namesurname) TextView tvCustomerName;
    @BindView(R.id.tv_order_date) TextView tvOrderDate;
    @BindView(R.id.tv_order_time) TextView tvOrderTime;
    @BindView(R.id.tv_order_number) TextView tvOrderNumber;
    @BindView(R.id.btn_take_measurement) Button btnTakeMeasurement;
    @BindView(R.id.bottom_sheet) View bottomSheet;
    @BindView(R.id.tv_location_name) TextView tvLocationName;
    @BindView(R.id.tv_location_type) TextView tvLocationType;
    @BindView(R.id.tv_count) TextView tvCount;
    @BindView(R.id.linear_layout_location) LinearLayout linearLayoutLocation;
    @BindView(R.id.btn_take_measurement2) Button btnRemove;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(AddOrderActivity.ARG_ADD_ORDER)) {
            addCustomerResponse = getArguments().getParcelable(AddOrderActivity.ARG_ADD_ORDER);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_order_layout,container,false);
        ButterKnife.bind(this,view);
        getActivity().setTitle("Sipariş Oluştur");
        setView();
        return view;
    }


    private void setView() {
        btnTakeMeasurement.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        String orderDate = simpleDateFormat.format(addCustomerResponse.getOrderDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(addCustomerResponse.getOrderDate());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        tvCustomerName.setText(addCustomerResponse.getCustomerNameSurname());
        tvOrderDate.setText(orderDate);
        tvOrderNumber.setText(""+addCustomerResponse.getId());
        tvOrderTime.setText(""+hours+":"+minutes);
    }

    @Override
    public void onClick(View view) {
        /*AddOrderLineFragment addOrderLineFragment =new AddOrderLineFragment();
        addOrderLineFragment.show(getFragmentManager(),"take measurement fragment");
*/
        if(view.getId()==R.id.btn_take_measurement){
            addLocation();
        }else{
            removeLocation();
        }

    }

    private void removeLocation() {
        if(productCount>0){
            linearLayoutLocation.removeView(linearLayoutLocation.findViewWithTag("product"+productCount));
            productCount--;

            if(productCount==0){
                tvCount.setText("");
            }else{
                tvCount.setText(""+productCount);
            }


        }


    }

    private void addLocation() {
        View view=getLayoutInflater().inflate(R.layout.product_value_layout,linearLayoutLocation,false);
        ImageButton btnAdd=view.findViewById(R.id.image_button_product_add);
        ImageButton btnRemove=view.findViewById(R.id.image_button_product_remove);
        ImageButton btnEdit=view.findViewById(R.id.image_button_product_edit);
        TextView tvProductValue=view.findViewById(R.id.tv_product_value);
        TextView tvProductCount=view.findViewById(R.id.tv_product_count);
        TextView tvCount2=view.findViewById(R.id.tv_count);
        productCount++;
        view.setTag("product"+productCount);
        tvCount2.setText(""+productCount);
        linearLayoutLocation.addView(view);
        tvCount.setText(""+productCount);

    }


}
