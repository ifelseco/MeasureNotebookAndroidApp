package com.javaman.olcudefteri.orders.curtain_type_dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.ProductDetailModel;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.view.CalculateView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Stor perde dialog
 */

public class ZebraCurtain extends DialogFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener,CalculateView {


    int parcaCount,mechanisStatus;
    String pattern ,variant ,alias ,desc,beadNo,skirtNo;
    @BindView(R.id.radioGroupType) RadioGroup radioGroupType;
    @BindView(R.id.radioGroupZincir) RadioGroup radioGroupDirection;
    @BindView(R.id.radioButtonParcali) RadioButton radioButtonParcali;
    @BindView(R.id.radioButtonCokluMekanizma) RadioButton radioButtonCokluMekanizma;
    @BindView(R.id.tableMeasureParcali) TableLayout tableLayoutParcali;
    @BindView(R.id.editTextParcaCount) EditText etParcaCount;
    @BindView(R.id.editTextStorUnitPrice) EditText etUnitPrice;
    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.textViewStorM2) TextView tvStorM2;
    @BindView(R.id.textViewProductValue) TextView tvProductValue;
    @BindView(R.id.textViewStorTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.progress_bar_calc) ProgressBar progressBarCalc;
    @BindView(R.id.progress_bar_save) ProgressBar progressBarSave;
    @BindView(R.id.linear_layout_normal) LinearLayout linearLayoutNormalWidthHeight;
    @BindView(R.id.linear_layout_normal_direction) LinearLayout linearLayoutNormalDirection;
    @BindView(R.id.linear_layout_pices_count) LinearLayout linearLayoutPiecesCount;
    @BindView(R.id.editTextVariant) EditText etVariant;
    @BindView(R.id.editTextPattern) EditText etPattern;
    @BindView(R.id.editTextAlias) EditText etAlias;
    @BindView(R.id.editTextStorDesc) EditText etDesc;
    @BindView(R.id.editTextBoncuk) EditText etBead;
    @BindView(R.id.editTextEtekDilimiNo) EditText etSkirt;


    public static final int ARG_PRODUCT_VALUE = 3;
    private AddOrderLinePresenter mAddOrderLinePresenter;
    private TextWatcher textWatcherParcaCount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            tableLayoutParcali.removeAllViews();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (charSequence.length() != 0) {
                tableLayoutParcali.setVisibility(View.VISIBLE);
                parcaCount = Integer.parseInt(etParcaCount.getText().toString());

                if (parcaCount <= 10) {
                    for (int j = 0; j < parcaCount; j++) {
                        View row = (TableRow) getLayoutInflater().inflate(R.layout.parcali_stor_row, null, false);
                        TextView textView = row.findViewById(R.id.labelParca);
                        textView.setText("Parça " + (j + 1));

                        tableLayoutParcali.addView(row);
                    }
                } else {

                    Toast.makeText(getActivity(), "En fazla 10 parça olabilir.", Toast.LENGTH_SHORT).show();
                }


            } else {
                tableLayoutParcali.removeAllViews();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    SharedPreferenceHelper sharedPreferenceHelper;


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
        View view = inflater.inflate(R.layout.roller_curtain, null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        mAddOrderLinePresenter=new AddOrderLinePresenterImpl(this);
        ButterKnife.bind(this,view);
        intiView();
        return view;
    }

    private void intiView() {
        tvProductValue.setText("Zebra");
        radioGroupType.clearCheck();
        radioGroupType.setOnCheckedChangeListener(this);
        etParcaCount.addTextChangedListener(textWatcherParcaCount);
        tableLayoutParcali.setVisibility(View.GONE);
        etParcaCount.setVisibility(View.GONE);
        linearLayoutNormalWidthHeight.setVisibility(View.GONE);
        linearLayoutNormalDirection.setVisibility(View.GONE);
        linearLayoutPiecesCount.setVisibility(View.GONE);

        setCancelable(false);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton selectedRadioButton = radioGroup.findViewById(i);

        int id = selectedRadioButton.getId();

        if (id == R.id.radioButtonCokluMekanizma) {
            mechanisStatus=3;
            linearLayoutPiecesCount.setVisibility(View.VISIBLE);
            etParcaCount.setVisibility(View.VISIBLE);
            linearLayoutNormalWidthHeight.setVisibility(View.GONE);
            linearLayoutNormalDirection.setVisibility(View.GONE);

        }else if(id == R.id.radioButtonParcali){
            mechanisStatus=2;
            linearLayoutPiecesCount.setVisibility(View.VISIBLE);
            etParcaCount.setVisibility(View.VISIBLE);
            linearLayoutNormalWidthHeight.setVisibility(View.GONE);
            linearLayoutNormalDirection.setVisibility(View.GONE);
        }
        else if(id==R.id.radioButtonTekKasa){
            parcaCount=0;
            mechanisStatus=1;
            etParcaCount.setText("");
            tableLayoutParcali.removeAllViews();
            tableLayoutParcali.setVisibility(View.GONE);
            etParcaCount.setVisibility(View.GONE);
            linearLayoutPiecesCount.setVisibility(View.GONE);
            linearLayoutNormalWidthHeight.setVisibility(View.VISIBLE);
            linearLayoutNormalDirection.setVisibility(View.VISIBLE);

        }else{
            mechanisStatus=0;
        }
    }


    @Override
    @OnClick({R.id.btnCancel,R.id.btnSave,R.id.btnCalculate})
    public void onClick(View view) {

        if (view.getId() == R.id.btnSave) {

            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            List<OrderLineDetailModel> orderLines = new ArrayList<>();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);

            if (!etPattern.getText().toString().isEmpty()) {
                pattern = etPattern.getText().toString();

            }


            if (!etVariant.getText().toString().isEmpty()) {
                variant = etVariant.getText().toString();

            }

            if (!etAlias.getText().toString().isEmpty()) {
                alias = etAlias.getText().toString();

            }

            if (!etDesc.getText().toString().isEmpty()) {
                desc = etDesc.getText().toString();
            }

            if (!etBead.getText().toString().isEmpty()) {
                beadNo = etBead.getText().toString();

            }

            if (!etSkirt.getText().toString().isEmpty()) {
                skirtNo = etSkirt.getText().toString();

            }



            if (parcaCount > 0) {

                for (int count = 0; count < parcaCount; count++) {
                    TableRow row = (TableRow) tableLayoutParcali.getChildAt(count);
                    EditText etWidthP=row.findViewById(R.id.editTextWidthP);
                    EditText etHeightP=row.findViewById(R.id.editTextHeightP);
                    RadioGroup radioGroupDirectionP=row.findViewById(R.id.radioGroupDirectionP);
                    OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                    orderLineDetailModel.setProduct(productDetailModel);

                    if (!TextUtils.isEmpty(etWidthP.getText().toString())){
                        double width=Double.parseDouble(etWidthP.getText().toString());
                        orderLineDetailModel.setPropertyWidth(width);
                    }

                    if(!TextUtils.isEmpty(etHeightP.getText().toString())){
                        double height=Double.parseDouble(etWidthP.getText().toString());
                        orderLineDetailModel.setPropertyHeight(height);
                    }

                    if(!TextUtils.isEmpty(etUnitPrice.getText().toString())){
                        double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                        orderLineDetailModel.setUnitPrice(unitPrice);
                    }

                    if (radioGroupDirectionP.getCheckedRadioButtonId() != -1) {
                        int checkedId = radioGroupDirectionP.getCheckedRadioButtonId();
                        if (checkedId == R.id.radioButtonStorLeftP) {
                            int direction=1;
                            orderLineDetailModel.setDirection(direction);
                        } else if (checkedId == R.id.radioButtonStorRightP) {
                            int direction=2;
                            orderLineDetailModel.setDirection(direction);

                        } else {
                            int direction=2;
                            orderLineDetailModel.setDirection(direction);

                        }
                    }



                    productDetailModel.setPatternCode(pattern);
                    productDetailModel.setVariantCode(variant);
                    productDetailModel.setAliasName(alias);
                    orderLineDetailModel.setProduct(productDetailModel);
                    orderLineDetailModel.setLineDescription(desc);
                    orderLineDetailModel.setBeadNo(beadNo);
                    orderLineDetailModel.setSkirtNo(skirtNo);


                    orderLineDetailModel.setPiecesCount(parcaCount);
                    orderLineDetailModel.setMechanismStatus(mechanisStatus);
                    orderLines.add(orderLineDetailModel);

                }

                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                EventBus.getDefault().post(addOrderLineDetailListModel);

            } else {

                OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                orderLineDetailModel.setProduct(productDetailModel);

                if (!TextUtils.isEmpty(etWidth.getText().toString())){
                    double width=Double.parseDouble(etWidth.getText().toString());
                    orderLineDetailModel.setPropertyWidth(width);
                }

                if(!TextUtils.isEmpty(etHeight.getText().toString())){
                    double height=Double.parseDouble(etHeight.getText().toString());
                    orderLineDetailModel.setPropertyHeight(height);
                }

                if(!TextUtils.isEmpty(etUnitPrice.getText().toString())){
                    double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                    orderLineDetailModel.setUnitPrice(unitPrice);
                }

                if (radioGroupDirection.getCheckedRadioButtonId() != -1) {
                    int checkedId = radioGroupDirection.getCheckedRadioButtonId();
                    if (checkedId == R.id.radioButtonStorLeft) {
                        int direction=1;
                        orderLineDetailModel.setDirection(direction);
                    } else if (checkedId == R.id.radioButtonStorRight) {
                        int direction=2;
                        orderLineDetailModel.setDirection(direction);

                    } else {
                        int direction=2;
                        orderLineDetailModel.setDirection(direction);

                    }
                }

                productDetailModel.setPatternCode(pattern);
                productDetailModel.setVariantCode(variant);
                productDetailModel.setAliasName(alias);
                orderLineDetailModel.setProduct(productDetailModel);
                orderLineDetailModel.setLineDescription(desc);
                orderLineDetailModel.setBeadNo(beadNo);
                orderLineDetailModel.setSkirtNo(skirtNo);

                orderLineDetailModel.setPiecesCount(parcaCount);
                orderLineDetailModel.setMechanismStatus(mechanisStatus);
                EventBus.getDefault().post(orderLineDetailModel);

            }







            dismiss();
        } else if (view.getId() == R.id.btnCancel) {
            dismiss();
        } else {

            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            List<OrderLineDetailModel> orderLines = new ArrayList<>();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);

            if (parcaCount > 0) {


                if(TextUtils.isEmpty(etUnitPrice.getText().toString())){
                    etUnitPrice.setError("Birim fiyat giriniz!");
                }else{
                    for (int count = 0; count < parcaCount; count++) {
                        TableRow row = (TableRow) tableLayoutParcali.getChildAt(count);
                        EditText etWidthP=row.findViewById(R.id.editTextWidthP);
                        EditText etHeightP=row.findViewById(R.id.editTextHeightP);
                        RadioGroup radioGroupDirectionP=row.findViewById(R.id.radioGroupDirectionP);

                        if (TextUtils.isEmpty(etWidthP.getText().toString())){
                            etWidthP.setError("Parça en giriniz!");
                        }else if(TextUtils.isEmpty(etHeightP.getText().toString())){
                            etHeightP.setError("Parça boy giriniz!");
                        }else {
                            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                            orderLineDetailModel.setProduct(productDetailModel);
                            double width=Double.parseDouble(etWidthP.getText().toString());
                            double height=Double.parseDouble(etWidthP.getText().toString());
                            double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                            orderLineDetailModel.setPropertyWidth(width);
                            orderLineDetailModel.setPropertyHeight(height);
                            orderLineDetailModel.setUnitPrice(unitPrice);
                            orderLines.add(orderLineDetailModel);
                        }
                    }

                    if(parcaCount==orderLines.size()){
                        addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                        calculateOrderLine(addOrderLineDetailListModel);
                    }else{
                        Toast.makeText(getActivity(), "Parçalardan biri eksik bilgi içeriyor", Toast.LENGTH_SHORT).show();
                    }


                }


            } else {

                if (TextUtils.isEmpty(etWidth.getText().toString())){
                    etWidth.setError("Parça en giriniz!");
                }else if(TextUtils.isEmpty(etHeight.getText().toString())){
                    etHeight.setError("Parça boy giriniz!");
                }if(TextUtils.isEmpty(etUnitPrice.getText().toString())){
                    etUnitPrice.setError("Birim fiyat giriniz!");
                }else{
                    OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                    orderLineDetailModel.setProduct(productDetailModel);
                    double width=Double.parseDouble(etWidth.getText().toString());
                    double height=Double.parseDouble(etHeight.getText().toString());
                    double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                    orderLineDetailModel.setPropertyWidth(width);
                    orderLineDetailModel.setPropertyHeight(height);
                    orderLineDetailModel.setUnitPrice(unitPrice);
                    orderLines.add(orderLineDetailModel);
                    addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                    calculateOrderLine(addOrderLineDetailListModel);

                }

            }

        }
    }



    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId=getSessionIdFromPref();
        mAddOrderLinePresenter.calculateOrderLine(orderLineDetailListModel,sessionId);
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBarCalc.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarCalc.setVisibility(View.GONE);
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void updateAmount(CalculationResponse calculationResponse) {
        double totalM2=calculationResponse.getUsedMaterial();
        double totalPrice=calculationResponse.getTotalAmount();

        tvStorM2.setText(String.format("%.2f",totalM2)+" m2");
        tvTotalPrice.setText(String.format("%.2f",totalPrice)+" TL");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }

}