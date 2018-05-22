package com.javaman.olcudefteri.ui.orders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by javaman on 18.12.2017.
 * Güneşlik dialog
 */
public class OrderStatusUpdateDialog extends DialogFragment implements View.OnClickListener {


    SharedPreferenceHelper sharedPreferenceHelper;
    OrderDetailResponseModel orderDetailResponseModel = new OrderDetailResponseModel();

    @BindView(R.id.spinner_order_status)
    Spinner spinnerOrderStatus;

    String[] orderStatus;

    @BindView(R.id.btn_cancel)
    Button imageButtonCancel;

    @BindView(R.id.btn_save)
    Button imageButtonSave;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            if (arguments.containsKey(OrderDetailActivity.ARG_GOTO_UPDATE_ORDER_FROM_ORDER_DETAIL)) {
                orderDetailResponseModel = arguments.getParcelable(OrderDetailActivity.ARG_GOTO_UPDATE_ORDER_FROM_ORDER_DETAIL);
                orderStatus = getActivity().getResources().getStringArray(R.array.order_status);
            }else{
                dismiss();
            }
        } else {
            dismiss();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_status_update_dialog, null);
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this, view);
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        initView();

        return view;
    }

    private void initView() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_status, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(adapter);
        spinnerOrderStatus.setSelection(orderDetailResponseModel.getOrderStatus());

    }

    @Override
    @OnClick({R.id.btn_cancel,R.id.btn_save})
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_save) {
            OrderUpdateModel orderUpdateModel = null;
            orderUpdateModel = initOrderUpdateModel();
            showConfirmDialog(orderUpdateModel);
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }

    private void showConfirmDialog(final OrderUpdateModel orderUpdateModel) {


        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText("Sipariş Durumu:\n"+orderStatus[orderUpdateModel.getOrderStatus()])
                .setConfirmText("Evet")
                .setConfirmClickListener(sDialog -> {
                    EventBus.getDefault().post(orderUpdateModel);
                    sDialog.dismissWithAnimation();
                    dismiss();
                })
                .showCancelButton(true)
                .setCancelText("Vazgeç!")
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }

    private OrderUpdateModel initOrderUpdateModel(){
        OrderUpdateModel orderUpdateModel = new OrderUpdateModel();
        orderUpdateModel.setOrderStatus(spinnerOrderStatus.getSelectedItemPosition());
        orderUpdateModel.setTotalAmount(orderDetailResponseModel.getTotalAmount());
        orderUpdateModel.setDepositeAmount(orderDetailResponseModel.getDepositeAmount());
        orderUpdateModel.setDeliveryDate(orderDetailResponseModel.getDeliveryDate());
        orderUpdateModel.setMeasureDate(orderDetailResponseModel.getMeasureDate());
        orderUpdateModel.setOrderDate(orderDetailResponseModel.getOrderDate());
        orderUpdateModel.setMountExist(orderDetailResponseModel.isMountExist());
        return orderUpdateModel;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

