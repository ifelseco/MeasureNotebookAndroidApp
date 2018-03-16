package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.LocationProduct;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 14.12.2017.
 */

public class AddOrderFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    public static final String MyPREFERENCES = "MyPrefs";
    private AddCustomerResponse addCustomerResponse;
    private BottomSheetBehavior bottomSheetBehavior;
    private int productCount;
    private boolean spinnerTouched = false;
    private LocationProduct locationProduct=new LocationProduct();
    private String[] productNames;

    @BindView(R.id.tv_custmer_namesurname)
    TextView tvCustomerName;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;

    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.tv_location_name)
    TextView tvLocationName;
    @BindView(R.id.tv_location_type)
    TextView tvLocationType;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.linear_layout_location)
    LinearLayout linearLayoutLocation;

    @BindView(R.id.cb_briz)
    CheckBox checkBoxBriz;
    @BindView(R.id.cb_door)
    CheckBox checkBoxDoor;
    @BindView(R.id.cb_farb)
    CheckBox checkBoxFarb;
    @BindView(R.id.cb_fon)
    CheckBox checkBoxFon;
    @BindView(R.id.cb_jaluzi)
    CheckBox checkBoxJaluzi;
    @BindView(R.id.cb_kruvaze)
    CheckBox checkBoxKruvaze;
    @BindView(R.id.cb_net_curt)
    CheckBox checkBoxNetCurt;
    @BindView(R.id.cb_net_stor)
    CheckBox checkBoxNetStor;
    @BindView(R.id.cb_stor)
    CheckBox checkBoxStor;
    @BindView(R.id.cb_zebra)
    CheckBox checkBoxZebra;
    @BindView(R.id.cb_sun_blind)
    CheckBox checkBoxSunBLind;
    @BindView(R.id.cb_vertical)
    CheckBox checkBoxVertical;
    @BindView(R.id.cb_window)
    CheckBox checkBoxWindow;
    @BindView(R.id.spinner_location)
    Spinner spinnerLocation;
    @BindView(R.id.et_location_type_number)
    EditText etLocationTypeNumber;
    @BindView(R.id.et_location_other)
    EditText etLocationOther;
    @BindView(R.id.image_button_close)
    ImageButton imageButtonClose;


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
        View view = inflater.inflate(R.layout.add_order_layout, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Sipariş Oluştur");
        initView();
        setView();
        return view;
    }

    private void initView() {

        imageButtonClose.setOnClickListener(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        spinnerLocation.setOnTouchListener(this);
        checkBoxDoor.setOnClickListener(this);
        checkBoxWindow.setOnClickListener(this);
        checkBoxBriz.setOnClickListener(this);
        checkBoxFarb.setOnClickListener(this);
        checkBoxFon.setOnClickListener(this);
        checkBoxJaluzi.setOnClickListener(this);
        checkBoxNetCurt.setOnClickListener(this);
        checkBoxNetStor.setOnClickListener(this);
        checkBoxStor.setOnClickListener(this);
        checkBoxZebra.setOnClickListener(this);
        checkBoxSunBLind.setOnClickListener(this);
        checkBoxVertical.setOnClickListener(this);
        checkBoxKruvaze.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.rooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        spinnerLocation.setOnItemSelectedListener(this);

    }


    private void setView() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        String orderDate = simpleDateFormat.format(addCustomerResponse.getOrderDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(addCustomerResponse.getOrderDate());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        tvCustomerName.setText(addCustomerResponse.getCustomerNameSurname());
        tvOrderDate.setText(orderDate);
        tvOrderNumber.setText("" + addCustomerResponse.getId());
        tvOrderTime.setText("" + hours + ":" + minutes);

        productNames = getActivity().getResources().getStringArray(R.array.curtains);
    }

    @Override
    public void onClick(View view) {
        /*AddOrderLineFragment addOrderLineFragment =new AddOrderLineFragment();
        addOrderLineFragment.show(getFragmentManager(),"take measurement fragment");
*/
        int id = view.getId();

        if (id == R.id.image_button_close) {
            spinnerLocation.setVisibility(View.VISIBLE);
            etLocationOther.setVisibility(View.GONE);
            imageButtonClose.setVisibility(View.GONE);
            spinnerLocation.setSelection(0);

        } else if (id == R.id.cb_door) {
            if(checkBoxDoor.isChecked()){
                checkBoxWindow.setChecked(false);
                String number = etLocationTypeNumber.getText().toString();
                if (!number.isEmpty()) {
                    locationProduct.setLocationType("Kapı " + number);
                    tvLocationType.setText(locationProduct.getLocationType());

                } else {
                    locationProduct.setLocationType("Kapı");
                    tvLocationType.setText(locationProduct.getLocationType());

                }
            }else{
                tvLocationType.setText("");
            }

        } else if (id == R.id.cb_window) {
            if(checkBoxWindow.isChecked()){
                checkBoxDoor.setChecked(false);
                String number = etLocationTypeNumber.getText().toString();
                if (!number.isEmpty()) {
                    locationProduct.setLocationType("Cam " + number);
                    tvLocationType.setText(locationProduct.getLocationType());

                } else {
                    locationProduct.setLocationType("Cam");
                    tvLocationType.setText(locationProduct.getLocationType());
                }
            }else{
                tvLocationType.setText("");
            }


        } else if (id == R.id.cb_net_curt) {
            if (checkBoxNetCurt.isChecked()) {
                locationProduct.getProductValue().add(productNames[0]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[0]);
                updateLocationProduct(locationProduct.getProductValue());
            }

        } else if (id == R.id.cb_sun_blind) {
            if (checkBoxSunBLind.isChecked()) {
                locationProduct.getProductValue().add(productNames[1]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[1]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_stor) {
            if (checkBoxStor.isChecked()) {
                locationProduct.getProductValue().add(productNames[2]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[2]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_zebra) {
            if (checkBoxZebra.isChecked()) {
                locationProduct.getProductValue().add(productNames[3]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[3]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_jaluzi) {
            if (checkBoxJaluzi.isChecked()) {
                locationProduct.getProductValue().add(productNames[4]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[4]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_vertical) {
            if (checkBoxVertical.isChecked()) {
                locationProduct.getProductValue().add(productNames[5]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[5]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_kruvaze) {
            if (checkBoxKruvaze.isChecked()) {
                locationProduct.getProductValue().add(productNames[6]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[6]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_briz) {
            if (checkBoxBriz.isChecked()) {
                locationProduct.getProductValue().add(productNames[7]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[7]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_farb) {
            if (checkBoxFarb.isChecked()) {
                locationProduct.getProductValue().add(productNames[8]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[8]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_fon) {
            if (checkBoxFon.isChecked()) {
                locationProduct.getProductValue().add(productNames[9]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[9]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        } else if (id == R.id.cb_net_stor) {
            if (checkBoxNetStor.isChecked()) {
                locationProduct.getProductValue().add(productNames[10]);
                updateLocationProduct(locationProduct.getProductValue());
            } else {
                locationProduct.getProductValue().remove(productNames[10]);
                updateLocationProduct(locationProduct.getProductValue());
            }
        }

    }

    private void updateLocationProduct(List<String> productValues) {
        linearLayoutLocation.removeAllViews();
        int count=productValues.size();
        if(count>0){
            tvCount.setText("" + count);
        }else{
            tvCount.setText("");
        }

        for (String productValue:productValues) {
            View view = getLayoutInflater().inflate(R.layout.product_value_layout, linearLayoutLocation, false);
            ImageButton btnAdd = view.findViewById(R.id.image_button_product_add);
            ImageButton btnRemove = view.findViewById(R.id.image_button_product_remove);
            ImageButton btnEdit = view.findViewById(R.id.image_button_product_edit);
            TextView tvProductValue = view.findViewById(R.id.tv_product_value);
            TextView tvProductCount = view.findViewById(R.id.tv_product_count);
            TextView tvCount2 = view.findViewById(R.id.tv_count);
            tvProductValue.setText(productValue);
            tvCount2.setText("" + count);
            linearLayoutLocation.addView(view);

        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerTouched) {
            if (position == 9) {
                spinnerLocation.setVisibility(View.GONE);
                etLocationOther.setVisibility(View.VISIBLE);
                imageButtonClose.setVisibility(View.VISIBLE);
                if (!etLocationOther.getText().toString().isEmpty()) {
                    String location = parent.getItemAtPosition(position).toString();
                    locationProduct.setLocationName(location);
                    tvLocationName.setText(location);
                }

            } else {
                if(position>0){
                    String location = parent.getItemAtPosition(position).toString();
                    locationProduct.setLocationName(location);
                    tvLocationName.setText(location);
                }
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        spinnerTouched = true;
        return false;
    }


}
