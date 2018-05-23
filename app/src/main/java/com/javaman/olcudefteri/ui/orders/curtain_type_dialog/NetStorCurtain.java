package com.javaman.olcudefteri.ui.orders.curtain_type_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.event.MechanismEvent;
import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.ProductDetailModel;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.view.CalculateView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Stor perde dialog
 */

public class NetStorCurtain extends DialogFragment implements View.OnClickListener, CalculateView {



    String pattern, variant, alias, desc, beadNo, skirtNo;
    int parcaCount,mechanisStatus;


    @BindView(R.id.radioGroupZincir)
    RadioGroup radioGroupDirection;

    @BindView(R.id.editTextStorUnitPrice)
    EditText etUnitPrice;
    @BindView(R.id.editTextWidth)
    EditText etWidth;
    @BindView(R.id.editTextHeight)
    EditText etHeight;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnCalculate)
    Button btnCalculate;
    @BindView(R.id.textViewStorM2)
    TextView tvStorM2;
    @BindView(R.id.textViewStorTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.textViewProductValue)
    TextView tvProductValue;
    @BindView(R.id.progress_bar_calc)
    ProgressBar progressBarCalc;

    @BindView(R.id.linear_layout_normal)
    LinearLayout linearLayoutNormalWidthHeight;
    @BindView(R.id.linear_layout_normal_direction)
    LinearLayout linearLayoutNormalDirection;

    @BindView(R.id.editTextVariant)
    EditText etVariant;
    @BindView(R.id.editTextPattern)
    EditText etPattern;
    @BindView(R.id.editTextAlias)
    EditText etAlias;
    @BindView(R.id.editTextStorDesc)
    EditText etDesc;
    @BindView(R.id.editTextBoncuk)
    EditText etBead;
    @BindView(R.id.editTextEtekDilimiNo)
    EditText etSkirt;

    @BindView(R.id.tableMeasureParcali)
    TableLayout tableLayoutParcali;

    public static final int ARG_PRODUCT_VALUE = 10;
    private AddOrderLinePresenter mAddOrderLinePresenter;

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
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity().getApplicationContext());
        mAddOrderLinePresenter = new AddOrderLinePresenterImpl(this);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setCancelable(false);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        tvProductValue.setText("Zebra");
        linearLayoutNormalWidthHeight.setVisibility(View.GONE);
        linearLayoutNormalDirection.setVisibility(View.GONE);
        showDialog(new MechanismDialog(),"mechanism-dialog");
    }

    @Subscribe
    public void getDialogData(MechanismEvent mechanismEvent) {
        if(mechanismEvent.getMechanismStatus()==-1){
            dismiss();
        }else{
            parcaCount=mechanismEvent.getPiecesCount();
            mechanisStatus=mechanismEvent.getMechanismStatus();
            setView();
        }
    }

    private void setView() {

        if (mechanisStatus == 3) {
            linearLayoutNormalWidthHeight.setVisibility(View.GONE);
            linearLayoutNormalDirection.setVisibility(View.GONE);
            createTableView(parcaCount);

        }else if(mechanisStatus == 2){
            linearLayoutNormalWidthHeight.setVisibility(View.GONE);
            linearLayoutNormalDirection.setVisibility(View.GONE);
            createTableView(parcaCount);
        }
        else if(mechanisStatus==1){
            tableLayoutParcali.removeAllViews();
            tableLayoutParcali.setVisibility(View.GONE);
            linearLayoutNormalWidthHeight.setVisibility(View.VISIBLE);
            linearLayoutNormalDirection.setVisibility(View.VISIBLE);

        }
    }

    private void createTableView(int parcaCount) {
        for (int j = 0; j < parcaCount; j++) {
            View row = getLayoutInflater().inflate(R.layout.parcali_stor_row, null, false);
            TextView textView = row.findViewById(R.id.labelParca);
            textView.setText("Parça " + (j + 1));

            tableLayoutParcali.addView(row);
        }
    }


    @Override
    @OnClick({R.id.btnCancel, R.id.btnSave, R.id.btnCalculate})
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
                    EditText etWidthP = row.findViewById(R.id.editTextWidthP);
                    EditText etHeightP = row.findViewById(R.id.editTextHeightP);
                    RadioGroup radioGroupDirectionP = row.findViewById(R.id.radioGroupDirectionP);
                    OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                    orderLineDetailModel.setProduct(productDetailModel);

                    if (!TextUtils.isEmpty(etWidthP.getText().toString())) {
                        double width = Double.parseDouble(etWidthP.getText().toString());
                        orderLineDetailModel.setPropertyWidth(width);
                    }

                    if (!TextUtils.isEmpty(etHeightP.getText().toString())) {
                        double height = Double.parseDouble(etWidthP.getText().toString());
                        orderLineDetailModel.setPropertyHeight(height);
                    }

                    if (!TextUtils.isEmpty(etUnitPrice.getText().toString())) {
                        double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                        orderLineDetailModel.setUnitPrice(unitPrice);
                    }

                    if (radioGroupDirectionP.getCheckedRadioButtonId() != -1) {
                        int checkedId = radioGroupDirectionP.getCheckedRadioButtonId();
                        if (checkedId == R.id.radioButtonStorLeftP) {
                            int direction = 1;
                            orderLineDetailModel.setDirection(direction);
                        } else if (checkedId == R.id.radioButtonStorRightP) {
                            int direction = 2;
                            orderLineDetailModel.setDirection(direction);

                        } else {
                            int direction = 2;
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

                if (!TextUtils.isEmpty(etWidth.getText().toString())) {
                    double width = Double.parseDouble(etWidth.getText().toString());
                    orderLineDetailModel.setPropertyWidth(width);
                }

                if (!TextUtils.isEmpty(etHeight.getText().toString())) {
                    double height = Double.parseDouble(etHeight.getText().toString());
                    orderLineDetailModel.setPropertyHeight(height);
                }

                if (!TextUtils.isEmpty(etUnitPrice.getText().toString())) {
                    double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                    orderLineDetailModel.setUnitPrice(unitPrice);
                }

                if (radioGroupDirection.getCheckedRadioButtonId() != -1) {
                    int checkedId = radioGroupDirection.getCheckedRadioButtonId();
                    if (checkedId == R.id.radioButtonStorLeft) {
                        int direction = 1;
                        orderLineDetailModel.setDirection(direction);
                    } else if (checkedId == R.id.radioButtonStorRight) {
                        int direction = 2;
                        orderLineDetailModel.setDirection(direction);

                    } else {
                        int direction = 2;
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


                if (TextUtils.isEmpty(etUnitPrice.getText().toString())) {
                    etUnitPrice.setError("Birim fiyat giriniz!");
                } else {
                    for (int count = 0; count < parcaCount; count++) {
                        TableRow row = (TableRow) tableLayoutParcali.getChildAt(count);
                        EditText etWidthP = row.findViewById(R.id.editTextWidthP);
                        EditText etHeightP = row.findViewById(R.id.editTextHeightP);
                        RadioGroup radioGroupDirectionP = row.findViewById(R.id.radioGroupDirectionP);

                        if (TextUtils.isEmpty(etWidthP.getText().toString())) {
                            etWidthP.setError("Parça en giriniz!");
                        } else if (TextUtils.isEmpty(etHeightP.getText().toString())) {
                            etHeightP.setError("Parça boy giriniz!");
                        } else {
                            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                            orderLineDetailModel.setProduct(productDetailModel);
                            double width = Double.parseDouble(etWidthP.getText().toString());
                            double height = Double.parseDouble(etWidthP.getText().toString());
                            double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                            orderLineDetailModel.setPropertyWidth(width);
                            orderLineDetailModel.setPropertyHeight(height);
                            orderLineDetailModel.setUnitPrice(unitPrice);
                            orderLines.add(orderLineDetailModel);
                        }
                    }

                    if (parcaCount == orderLines.size()) {
                        addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                        calculateOrderLine(addOrderLineDetailListModel);
                    } else {
                        StyleableToast.makeText(getActivity(),"Parçalardan biri eksik bilgi içeriyor",R.style.warn_toast_style).show();

                    }


                }


            } else {

                if (TextUtils.isEmpty(etWidth.getText().toString())) {
                    etWidth.setError("Parça en giriniz!");
                } else if (TextUtils.isEmpty(etHeight.getText().toString())) {
                    etHeight.setError("Parça boy giriniz!");
                }
                if (TextUtils.isEmpty(etUnitPrice.getText().toString())) {
                    etUnitPrice.setError("Birim fiyat giriniz!");
                } else {
                    OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                    orderLineDetailModel.setProduct(productDetailModel);
                    double width = Double.parseDouble(etWidth.getText().toString());
                    double height = Double.parseDouble(etHeight.getText().toString());
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

    public void showDialog(DialogFragment dialogFragment , String fragmentTag){
        dialogFragment.show(getFragmentManager(),fragmentTag);
    }

    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId = getSessionIdFromPref();
        mAddOrderLinePresenter.calculateOrderLine(orderLineDetailListModel, sessionId);
    }

    @Override
    public void showAlert(String message) {
        StyleableToast.makeText(getActivity(),message,R.style.info_toast_style).show();
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
        String xAuthToken = sharedPreferenceHelper.getStringPreference("sessionId", null);
        return xAuthToken;
    }

    @Override
    public void updateAmount(CalculationResponse calculationResponse) {
        double totalM2 = calculationResponse.getUsedMaterial();
        double totalPrice = calculationResponse.getTotalAmount();

        tvStorM2.setText(String.format("%.2f", totalM2) + " m2");
        tvTotalPrice.setText(String.format("%.2f", totalPrice) + " TL");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void navigateLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}