package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.orders.curtain_type_dialog.BrizCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.CurtainDoubleNet;
import com.javaman.olcudefteri.orders.curtain_type_dialog.FarbelaCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.FonCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.JalouiseCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.NetCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.NetStorCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.RollerCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.SunBlindCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.VerticalCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.ZebraCurtain;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.DeleteOrderLinesModel;
import com.javaman.olcudefteri.orders.model.LocationProduct;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.presenter.OrderPresenter;
import com.javaman.olcudefteri.orders.presenter.OrderPresenterImpl;
import com.javaman.olcudefteri.orders.view.AddOrderLineView;
import com.javaman.olcudefteri.utill.MyUtil;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by javaman on 14.12.2017.
 */

public class AddOrderLineFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener ,AddOrderLineView{

    SharedPreferenceHelper sharedPreferenceHelper;
    public static final String ARG_GOTO_ORDERLINE = "arg_goto_order_line";
    public static final String ARG_GOTO_ORDER_UPDATE_FROM_ADD_ORDERLINE = "current-order-goto-update";
    private OrderDetailResponseModel orderDetailResponseModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private int productCount;
    private boolean spinnerTouched = false;
    private LocationProduct locationProduct=new LocationProduct();
    private String[] productNames;
    private String[] productCodes;
    private String locationTypeCount="";
    int currentProductValue;
    double orderTotalAmount;
    private TextWatcher textWatcherTypeNumber=new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>=0){
                locationTypeCount=etLocationTypeNumber.getText().toString();
                if(checkBoxDoor.isChecked()){
                    locationProduct.setLocationType("Kapı " + locationTypeCount);
                    tvLocationType.setText(locationProduct.getLocationType());
                }else if(checkBoxWindow.isChecked()){
                    locationProduct.setLocationType("Cam " + locationTypeCount);
                    tvLocationType.setText(locationProduct.getLocationType());
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private TextWatcher textWatcherOtherLocation=new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>=0){
                locationProduct.setLocationName(etLocationOther.getText().toString());
                tvLocationName.setText(etLocationOther.getText().toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @BindView(R.id.tv_custmer_namesurname)
    TextView tvCustomerName;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;

    @BindView(R.id.tv_total_amount)
    TextView tvOrderTotalAmount;

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

    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;

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

    SweetAlertDialog pDialog;
    String[] orderStatus;

    private AddOrderLinePresenter mAddOrderLinePresenter;
    private OrderPresenter mOrderPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderStatus=getActivity().getResources().getStringArray(R.array.order_status);
        productNames = getActivity().getResources().getStringArray(R.array.curtains);
        productCodes=getActivity().getResources().getStringArray(R.array.curtainsCode);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        if(getArguments().containsKey(OrderDetailActivity.ARG_CURRENT_ORDER)){
            orderDetailResponseModel=getArguments().getParcelable(OrderDetailActivity.ARG_CURRENT_ORDER);
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_order_layout, container, false);
        mAddOrderLinePresenter=new AddOrderLinePresenterImpl(this);
        mOrderPresenter=new OrderPresenterImpl(this);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Sipariş Oluştur");
        initView();
        setView(orderDetailResponseModel);
        setHasOptionsMenu(true);
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
        etLocationTypeNumber.addTextChangedListener(textWatcherTypeNumber);
        etLocationOther.addTextChangedListener(textWatcherOtherLocation);
        pDialog=new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
    }


    private void setView(OrderDetailResponseModel orderDetailResponseModel) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        Calendar calendar = Calendar.getInstance();



        if(orderDetailResponseModel!=null){

            tvOrderStatus.setText(orderStatus[orderDetailResponseModel.getOrderStatus()]);

            String orderDate = simpleDateFormat.format(orderDetailResponseModel.getOrderDate());
            calendar.setTime(orderDetailResponseModel.getOrderDate());
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            tvCustomerName.setText(orderDetailResponseModel.getCustomer().getNameSurname());
            tvOrderDate.setText(orderDate);
            tvOrderNumber.setText("" + orderDetailResponseModel.getId());
            tvOrderTime.setText("" + hours + ":" + minutes);
            orderTotalAmount=orderDetailResponseModel.getTotalAmount();
            tvOrderTotalAmount.setText(String.format("%.2f",orderTotalAmount));        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add_order, menu);

        MenuItem menuItemLineList = menu.findItem(R.id.item_line_list);
        MenuItem menuItemUpdate = menu.findItem(R.id.item_update);

        if (menuItemLineList != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), menuItemLineList, android.R.color.white);
        }

        if (menuItemUpdate != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), menuItemUpdate, android.R.color.white);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_line_list:
                Intent orderDetail = new Intent(getActivity(), OrderDetailActivity.class);
                orderDetail.putExtra(ARG_GOTO_ORDERLINE,orderDetailResponseModel.getId());
                startActivity(orderDetail);
                return true;
            case R.id.item_update:
                Toast.makeText(getActivity(), "Update", Toast.LENGTH_SHORT).show();
                //show order update dilog
                OrderUpdateDialog orderUpdateDialog=new OrderUpdateDialog();
                Bundle bundle=new Bundle();
                bundle.putParcelable(ARG_GOTO_ORDER_UPDATE_FROM_ADD_ORDERLINE,orderDetailResponseModel);
                orderUpdateDialog.setArguments(bundle);
                showDialog(orderUpdateDialog,"order-update-dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.image_button_close) {
            spinnerLocation.setVisibility(View.VISIBLE);
            etLocationOther.setVisibility(View.GONE);
            imageButtonClose.setVisibility(View.GONE);
            spinnerLocation.setSelection(0);


        } else if (id == R.id.cb_door) {
            if(checkBoxDoor.isChecked()){
                checkBoxWindow.setChecked(false);
                if (!locationTypeCount.isEmpty()) {
                    locationProduct.setLocationType("Kapı " + locationTypeCount);
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

                if (!locationTypeCount.isEmpty()) {
                    locationProduct.setLocationType("Cam " + locationTypeCount);
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

    private void updateLocationProduct(final List<String> productValues) {
        linearLayoutLocation.removeAllViews();
        int productCount=productValues.size();
        int count=0;
        if(productCount>0){
            tvCount.setText("" + productCount);
        }else{
            tvCount.setText("");
        }

        for (final String productValue:productValues) {

            count++;
            View view = getLayoutInflater().inflate(R.layout.product_value_layout, linearLayoutLocation, false);
            ImageButton btnAdd = view.findViewById(R.id.image_button_product_add);
            ImageButton btnRemove = view.findViewById(R.id.image_button_product_remove);
            ImageButton btnEdit = view.findViewById(R.id.image_button_product_edit);
            TextView tvProductValue = view.findViewById(R.id.tv_product_value);
            TextView tvProductCount = view.findViewById(R.id.tv_product_count);
            TextView tvCount2 = view.findViewById(R.id.tv_count);
            tvProductValue.setText(productValue);
            int index= Arrays.asList(productNames).indexOf(productValue);
            final String productCode=productCodes[index];
            tvCount2.setText("" + count);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (productCode){
                        case "TP":
                            showDialog(new NetCurtain() , "Tül Perde");
                            break;
                        case "GP":
                            Toast.makeText(getActivity(), ""+locationProduct.getLocationName()+locationProduct.getLocationType(), Toast.LENGTH_SHORT).show();
                            showDialog(new SunBlindCurtain() , "Güneşlik Perde");
                            break;
                        case "SP":
                            showDialog(new RollerCurtain() , "Stor Perde");
                            break;
                        case "ZP":
                            showDialog(new ZebraCurtain() , "Zebra Perde");
                            break;
                        case "JP":
                            showDialog(new JalouiseCurtain() , "Jaluzi Perde");
                            break;
                        case "DP":
                            showDialog(new VerticalCurtain() , "Dikey Perde");
                            break;
                        case "KTP":
                            showDialog(new CurtainDoubleNet() , "Kruvaze Tül Perde");
                            break;
                        case "BP":
                            showDialog(new BrizCurtain() , "Briz Perde");
                            break;
                        case "FARBP":
                            showDialog(new FarbelaCurtain() , "Farbela Perde");
                            break;
                        case "FP":
                            showDialog(new FonCurtain() , "Fon Perde");
                            break;
                        case "TSP":
                            //Tül Stor dialog
                            showDialog(new NetStorCurtain() , "Tül Stor");
                            break;
                    }
                }
            });


            linearLayoutLocation.addView(view);

        }
    }

    public void showDialog(DialogFragment dialogFragment , String fragmentTag){
        dialogFragment.show(getFragmentManager(),fragmentTag);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerTouched) {
            if (position == 9) {
                spinnerLocation.setVisibility(View.GONE);
                etLocationOther.setVisibility(View.VISIBLE);
                imageButtonClose.setVisibility(View.VISIBLE);
                if (!etLocationOther.getText().toString().isEmpty()) {
                    String location = etLocationOther.getText().toString();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    @Subscribe
    public void addOrderLine(OrderLineDetailModel orderLineDetailModel) {
        String sessionId=getSessionIdFromPref();
        orderLineDetailModel.setLocationName(locationProduct.getLocationName());
        orderLineDetailModel.setLocationType(locationProduct.getLocationType());
        OrderDetailModel orderDetailModel=new OrderDetailModel();
        orderDetailModel.setId(orderDetailResponseModel.getId());
        orderLineDetailModel.setOrder(orderDetailModel);
        mAddOrderLinePresenter.addOrderLine(orderLineDetailModel,sessionId);
        currentProductValue=orderLineDetailModel.getProduct().getProductValue();

    }


    @Override
    @Subscribe
    public void addOrderLineList(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId=getSessionIdFromPref();

        for (OrderLineDetailModel orderLineDetailModel:orderLineDetailListModel.getOrderLineDetailModelList()) {
            orderLineDetailModel.setLocationName(locationProduct.getLocationName());
            orderLineDetailModel.setLocationType(locationProduct.getLocationType());
            OrderDetailModel orderDetailModel=new OrderDetailModel();
            orderDetailModel.setId(orderDetailResponseModel.getId());
            orderLineDetailModel.setOrder(orderDetailModel);
            currentProductValue=orderLineDetailModel.getProduct().getProductValue();
        }

        mAddOrderLinePresenter.addOrderLineList(orderLineDetailListModel,sessionId);
    }


    @Override
    @Subscribe
    public void updateOrder(OrderUpdateModel orderUpdateModel) {
        //set order id
        orderUpdateModel.setId(orderDetailResponseModel.getId());
        String headerData=getSessionIdFromPref();
        mOrderPresenter.orderUpdate(orderUpdateModel,headerData);

    }


    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(getActivity() , LoginActivity.class));
    }

    @Override
    public void checkSession() {

    }

    @Override
    public void showAlert(String message,boolean isError) {
        if(isError){

            SweetAlertDialog pDialog= new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Hata...");
            pDialog.setContentText(message);
            pDialog.setConfirmText("Kapat");
            pDialog.setCancelable(true);
            pDialog.show();

        }else{
            SweetAlertDialog pDialog=new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
            //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(message);
            pDialog.setConfirmText("Kapat");
            pDialog.setCancelable(true);
            pDialog.show();
        }
    }

    @Override
    public void showProgress() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Ölçü kaydediliyor...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyAddOrderLine();
        mOrderPresenter.onDestroy();
    }

    @Override
    public void updateCart(double orderTotalAmount) {

        this.orderTotalAmount=orderTotalAmount;
        tvOrderTotalAmount.setText(String.format("%.2f",orderTotalAmount));
        orderDetailResponseModel.setTotalAmount(orderTotalAmount);

       switch (currentProductValue){
           case 0:
               checkBoxNetCurt.setChecked(false);
               locationProduct.getProductValue().remove(productNames[0]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 1:
               checkBoxSunBLind.setChecked(false);
               locationProduct.getProductValue().remove(productNames[1]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 2:
               checkBoxStor.setChecked(false);
               locationProduct.getProductValue().remove(productNames[2]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 3:
               checkBoxZebra.setChecked(false);
               locationProduct.getProductValue().remove(productNames[3]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 4:
               checkBoxJaluzi.setChecked(false);
               locationProduct.getProductValue().remove(productNames[4]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 5:
               checkBoxVertical.setChecked(false);
               locationProduct.getProductValue().remove(productNames[5]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 6:
               checkBoxKruvaze.setChecked(false);
               locationProduct.getProductValue().remove(productNames[6]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 7:
               checkBoxBriz.setChecked(false);
               locationProduct.getProductValue().remove(productNames[7]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 8:
               checkBoxFarb.setChecked(false);
               locationProduct.getProductValue().remove(productNames[8]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 9:
               checkBoxFon.setChecked(false);
               locationProduct.getProductValue().remove(productNames[9]);
               updateLocationProduct(locationProduct.getProductValue());
               break;
           case 10:
               checkBoxNetStor.setChecked(false);
               locationProduct.getProductValue().remove(productNames[10]);
               updateLocationProduct(locationProduct.getProductValue());
               break;

       }
    }

    @Override
    public void updateView(OrderUpdateModel orderUpdateModel) {

        orderDetailResponseModel.setTotalAmount(orderUpdateModel.getTotalAmount());
        orderDetailResponseModel.setDepositeAmount(orderUpdateModel.getDepositeAmount());
        orderDetailResponseModel.setDeliveryDate(orderUpdateModel.getDeliveryDate());
        orderDetailResponseModel.setMeasureDate(orderUpdateModel.getMeasureDate());
        orderDetailResponseModel.setMountExist(orderUpdateModel.isMountExist());
        orderDetailResponseModel.setOrderStatus(orderUpdateModel.getOrderStatus());
        setView(orderDetailResponseModel);
    }




}
