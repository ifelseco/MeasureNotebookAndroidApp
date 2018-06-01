package com.javaman.olcudefteri.ui.orders.dialogs;

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
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.ui.orders.AddOrderLineFragment;
import com.javaman.olcudefteri.ui.orders.OrderDetailActivity;
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
public class OrderUpdateDialog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    SharedPreferenceHelper sharedPreferenceHelper;
    int orderStatus;
    Date deliveryDate;
    Date measureDate;

    OrderDetailResponseModel orderDetailResponseModel = new OrderDetailResponseModel();


    @BindView(R.id.spinner_order_status)
    Spinner spinnerOrderStatus;

    @BindView(R.id.et_select_delivery_date)
    EditText editTextSelectDeliveryDate;

    @BindView(R.id.et_select_measure_date)
    EditText editTextSelectMeasureDate;

    @BindView(R.id.switch_mount)
    Switch switchMout;

    @BindView(R.id.et_order_total)
    EditText editTextTotalAmount;

    @BindView(R.id.et_order_deposit)
    EditText editTextDepositAmount;

    @BindView(R.id.btn_cancel)
    Button imageButtonCancel;

    @BindView(R.id.btn_save)
    Button imageButtonSave;

    @BindView(R.id.btn_edit_deposit)
    ImageButton btnEditDepoist;

    @BindView(R.id.btn_edit_total)
    ImageButton btnEditTotal;

    @BindView(R.id.container_measure)
    LinearLayout linearLayoutMeasureDate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            if (arguments.containsKey(AddOrderLineFragment.ARG_GOTO_ORDER_UPDATE_FROM_ADD_ORDERLINE)) {
                orderDetailResponseModel = arguments.getParcelable(AddOrderLineFragment.ARG_GOTO_ORDER_UPDATE_FROM_ADD_ORDERLINE);
            } else if (arguments.containsKey(OrderDetailActivity.ARG_GOTO_UPDATE_ORDER_FROM_ORDER_DETAIL)) {
                orderDetailResponseModel = arguments.getParcelable(OrderDetailActivity.ARG_GOTO_UPDATE_ORDER_FROM_ORDER_DETAIL);
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
        View view = inflater.inflate(R.layout.order_update_dialog, null);
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
        disableEdittext(editTextDepositAmount);
        disableEdittext(editTextTotalAmount);
        editTextSelectDeliveryDate.setOnClickListener(this);
        editTextSelectMeasureDate.setOnClickListener(this);
        initSpinner();

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        if (orderDetailResponseModel.getDepositeAmount() == 0) {
            editTextDepositAmount.setText("0");

        } else {
            editTextDepositAmount.setText("" + orderDetailResponseModel.getDepositeAmount());
        }


        if (orderDetailResponseModel.getTotalAmount() == 0) {
            editTextTotalAmount.setText("0");

        } else {
            editTextTotalAmount.setText("" + orderDetailResponseModel.getTotalAmount());
        }

        if (orderDetailResponseModel.getDeliveryDate() == null) {
            editTextSelectDeliveryDate.setText("");

        } else {
            String deliveryDate = sdf.format(orderDetailResponseModel.getDeliveryDate());
            editTextSelectDeliveryDate.setText(deliveryDate);
        }


        if (orderDetailResponseModel.getMeasureDate() == null) {
            linearLayoutMeasureDate.setVisibility(View.GONE);
        } else {
            String measureDate = sdf.format(orderDetailResponseModel.getMeasureDate());
            editTextSelectMeasureDate.setText(measureDate);
        }

        if (orderDetailResponseModel.isMountExist()) {
            switchMout.setChecked(true);
        } else {
            switchMout.setChecked(false);
        }

        spinnerOrderStatus.setSelection(orderDetailResponseModel.getOrderStatus());

    }



    private void disableEdittext(EditText editText) {
        editText.setEnabled(false);
    }

    private void enableEdittext(EditText editText) {
        editText.setEnabled(true);
        editText.setBackgroundResource(R.drawable.edittext_border_bg_yellow);
    }


    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_status, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderStatus.setAdapter(adapter);
    }

    @Override
    @OnClick({R.id.btn_edit_total, R.id.btn_cancel, R.id.btn_edit_deposit, R.id.btn_save})
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_edit_total) {
            enableEdittext(editTextTotalAmount);
        } else if (id == R.id.btn_edit_deposit) {
            enableEdittext(editTextDepositAmount);
        } else if (id == R.id.btn_save) {
            OrderUpdateModel orderUpdateModel = null;
            try {
                orderUpdateModel = initOrderUpdateModel();
                if(orderUpdateModel.getDepositeAmount()>orderUpdateModel.getTotalAmount()){
                    editTextDepositAmount.setError("Kapora toplam fiyattan fazla olamaz.");
                }else{
                    showConfirmDialog(orderUpdateModel);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.et_select_delivery_date) {
            Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(selectedyear, selectedmonth, selectedday);
                    deliveryDate = calendar.getTime();
                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    editTextSelectDeliveryDate.setText(sdf.format(calendar.getTime()));
                }
            }, mYear, mMonth, mDay);
            mDatePicker.setTitle("Tarih seç");
            mDatePicker.show();
        } else if (id == R.id.et_select_measure_date) {
            Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(selectedyear, selectedmonth, selectedday);
                    measureDate = calendar.getTime();
                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    editTextSelectMeasureDate.setText(sdf.format(calendar.getTime()));
                }
            }, mYear, mMonth, mDay);
            mDatePicker.setTitle("Tarih seç");
            mDatePicker.show();
        }
    }

    private void showConfirmDialog(final OrderUpdateModel orderUpdateModel) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String mDeliveryDate, mMeasureDate;

        if (orderUpdateModel.getDeliveryDate() != null) {
            mDeliveryDate = sdf.format(orderUpdateModel.getDeliveryDate());

        } else {
            mDeliveryDate = "Seçilmedi.";
        }

        if (orderUpdateModel.getMeasureDate() != null) {
            mMeasureDate = sdf.format(orderUpdateModel.getMeasureDate());
        } else {
            mMeasureDate = "Seçilmedi";
        }

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText(
                                "Toplam fiyat :" + orderUpdateModel.getTotalAmount()+" TL \n" +
                                "Kapora :" + orderUpdateModel.getDepositeAmount()+" TL \n" +
                                "Teslim tarihi :" + mDeliveryDate + "\n" +
                                "Ölçü tarihi :" + mMeasureDate+"\n"+
                                "Montaj :" + (orderUpdateModel.isMountExist() == true ? "Var" : "Yok"))
                .setConfirmText("Evet")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        EventBus.getDefault().post(orderUpdateModel);
                        sDialog.dismissWithAnimation();
                        dismiss();
                    }
                })
                .showCancelButton(true)
                .setCancelText("Vazgeç!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private OrderUpdateModel initOrderUpdateModel() throws ParseException {
        OrderUpdateModel orderUpdateModel = new OrderUpdateModel();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        if (!TextUtils.isEmpty(editTextTotalAmount.getText().toString())) {
            Double totalAmount = Double.parseDouble(editTextTotalAmount.getText().toString());
            orderUpdateModel.setTotalAmount(totalAmount);
        }

        if (!TextUtils.isEmpty(editTextDepositAmount.getText().toString())) {
            Double depositAmount = Double.parseDouble(editTextDepositAmount.getText().toString());
            orderUpdateModel.setDepositeAmount(depositAmount);
        }

        if (switchMout.isChecked()) {
            orderUpdateModel.setMountExist(true);
        } else {
            orderUpdateModel.setMountExist(false);
        }

        orderUpdateModel.setOrderStatus(spinnerOrderStatus.getSelectedItemPosition());

        if (!TextUtils.isEmpty(editTextSelectDeliveryDate.getText().toString())) {

            if(deliveryDate!=null){
                orderUpdateModel.setDeliveryDate(deliveryDate);
            }else{
                Date date =sdf.parse(editTextSelectDeliveryDate.getText().toString());
                orderUpdateModel.setDeliveryDate(date);
            }


        }

        if (!TextUtils.isEmpty(editTextSelectMeasureDate.getText().toString())) {

            if(measureDate!=null){
                orderUpdateModel.setMeasureDate(measureDate);
            }else{
                Date date =sdf.parse(editTextSelectMeasureDate.getText().toString());
                orderUpdateModel.setMeasureDate(date);
            }


        }

        if (orderDetailResponseModel.getOrderDate() != null) {
            orderUpdateModel.setOrderDate(orderDetailResponseModel.getOrderDate());
        }



        return orderUpdateModel;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    @OnItemSelected(R.id.spinner_order_status)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            linearLayoutMeasureDate.setVisibility(View.VISIBLE);
            editTextSelectMeasureDate.setBackgroundResource(R.drawable.edittext_border_bg_yellow);

            editTextSelectMeasureDate.setText("");



        }
    }

    @Override
    @OnItemSelected(R.id.spinner_order_status)
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

