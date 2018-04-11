package com.javaman.olcudefteri.orders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by javaman on 18.12.2017.
 * Güneşlik dialog
 */
public class OrderUpdateDialog extends DialogFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{


    SharedPreferenceHelper sharedPreferenceHelper;
    int orderStatus;
    Date deliveryDate;
    Date measureDate;

    OrderDetailResponseModel orderDetailResponseModel=new OrderDetailResponseModel();


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
    ImageButton imageButtonCancel;

    @BindView(R.id.btn_save)
    ImageButton imageButtonSave;

    @BindView(R.id.btn_edit_deposit)
    ImageButton imageButtonEditDepoist;

    @BindView(R.id.btn_edit_total)
    ImageButton imageButtonEditTotal;

    @BindView(R.id.container_measure)
    LinearLayout linearLayoutMeasureDate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().containsKey(AddOrderLineFragment.ARG_GOTO_ORDER_UPDATE)){
            orderDetailResponseModel=getArguments().getParcelable(AddOrderLineFragment.ARG_GOTO_ORDER_UPDATE);
            Toast.makeText(getActivity(), ""+orderDetailResponseModel.isMountExist(), Toast.LENGTH_SHORT).show();
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
        View view=inflater.inflate(R.layout.order_update_dialog,null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this,view);
        setCancelable(false);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        initView();

        return view;
    }

    private void initView() {
        setDateEdittext(editTextSelectDeliveryDate);
        disableEdittext(editTextDepositAmount);
        disableEdittext(editTextTotalAmount);
        editTextSelectDeliveryDate.setOnClickListener(this);
        initSpinner();

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        if(orderDetailResponseModel.getDepositeAmount()==0){
            editTextDepositAmount.setText("0");

        }else{
            editTextDepositAmount.setText(""+orderDetailResponseModel.getDepositeAmount());
        }


        if(orderDetailResponseModel.getTotalAmount()==0){
            editTextTotalAmount.setText("0");

        }else{
            editTextTotalAmount.setText(""+orderDetailResponseModel.getTotalAmount());
        }

        if(orderDetailResponseModel.getDeliveryDate()==null){
            editTextSelectDeliveryDate.setText("Tarih Seç");

        }else{
            String deliveryDate=sdf.format(orderDetailResponseModel.getDeliveryDate());
            editTextSelectDeliveryDate.setText(deliveryDate);
        }


        if(orderDetailResponseModel.getMeasureDate()==null){
            linearLayoutMeasureDate.setVisibility(View.GONE);
        }else{
            String measureDate=sdf.format(orderDetailResponseModel.getMeasureDate());
            editTextSelectMeasureDate.setText(measureDate);
        }

        if(orderDetailResponseModel.isMountExist()){
            switchMout.setChecked(true);
        }else{
            switchMout.setChecked(false);
        }

        spinnerOrderStatus.setSelection(orderDetailResponseModel.getOrderStatus());

    }

    private void setDateEdittext(EditText editText) {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
        editText.setText(""+mDay+"/"+mMonth+"/"+mYear);
    }

    private void disableEdittext(EditText editText) {
        editText.setEnabled(false);
    }

    private void enableEdittext(EditText editText){
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
    @OnClick({R.id.btn_edit_total, R.id.btn_cancel, R.id.btn_edit_deposit,R.id.btn_save})
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.btn_edit_total){
            enableEdittext(editTextTotalAmount);
            Toast.makeText(getActivity(), ""+spinnerOrderStatus.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
        }else if(id==R.id.btn_edit_deposit){
            enableEdittext(editTextDepositAmount);
        }else if(id==R.id.btn_save){
            /*orderDetailModel.setDeliveryDate();
            EventBus.getDefault().post();*/
            dismiss();
        }else if(id==R.id.btn_cancel){
            dismiss();
        }else if(id==R.id.et_select_delivery_date){
            Calendar mcurrentDate=Calendar.getInstance();
            int mYear=mcurrentDate.get(Calendar.YEAR);
            int mMonth=mcurrentDate.get(Calendar.MONTH);
            int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(selectedyear,selectedmonth,selectedday);
                    deliveryDate=calendar.getTime();
                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    editTextSelectDeliveryDate.setText(sdf.format(calendar.getTime()));
                }
            },mYear, mMonth, mDay);
            mDatePicker.setTitle("Tarih seç");
            mDatePicker.show();
        }else if(id==R.id.et_select_measure_date){
            Calendar mcurrentDate=Calendar.getInstance();
            int mYear=mcurrentDate.get(Calendar.YEAR);
            int mMonth=mcurrentDate.get(Calendar.MONTH);
            int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(selectedyear,selectedmonth,selectedday);
                    measureDate=calendar.getTime();
                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    editTextSelectMeasureDate.setText(sdf.format(calendar.getTime()));
                }
            },mYear, mMonth, mDay);
            mDatePicker.setTitle("Tarih seç");
            mDatePicker.show();
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    @OnItemSelected(R.id.spinner_order_status)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==1){
            linearLayoutMeasureDate.setVisibility(View.VISIBLE);
            editTextSelectMeasureDate.setBackgroundResource(R.drawable.edittext_border_bg_yellow);
            setDateEdittext(editTextSelectMeasureDate);

        }
    }

    @Override
    @OnItemSelected(R.id.spinner_order_status)
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

