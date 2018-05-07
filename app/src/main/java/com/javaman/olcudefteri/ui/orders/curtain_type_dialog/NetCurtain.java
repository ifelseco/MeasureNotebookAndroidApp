package com.javaman.olcudefteri.ui.orders.curtain_type_dialog;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.ProductDetailModel;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.view.CalculateView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Created by javaman on 18.12.2017.
 * Tül perde dialog
 */
public class NetCurtain extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener, CalculateView {


    @BindView(R.id.editTextWidth)
    EditText etWidth;
    @BindView(R.id.editTextHeight)
    EditText etHeight;
    @BindView(R.id.editTextVariant)
    EditText etVariant;
    @BindView(R.id.editTextPattern)
    EditText etPattern;
    @BindView(R.id.editTextAlias)
    EditText etAlias;
    @BindView(R.id.editTextOtherPile)
    EditText etOtherPile;
    @BindView(R.id.editTextNetUnitPrice)
    EditText etUnitprice;

    @BindView(R.id.editTextNetDesc)
    EditText etDesc;
    @BindView(R.id.textViewNetM)
    TextView tvTotalMeter;

    @BindView(R.id.textViewNetTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.btnSave)
    ImageButton btnSave;
    @BindView(R.id.btnCancel)
    ImageButton btnCancel;
    @BindView(R.id.btnCalculate)
    ImageButton btnCalculate;
    @BindView(R.id.radiGroupPile)
    RadioGroup radioGroupPile;

    @BindView(R.id.progress_bar_calc)
    ProgressBar progressBarCalc;
    double totalPrice, unitPrice, totalM, pile;
    private AddOrderLinePresenter mAddOrderLinePresenter;
    public static final int ARG_PRODUCT_VALUE = 0;
    SharedPreferenceHelper sharedPreferenceHelper;


    private void resetRadioButton() {

        radioGroupPile.clearCheck();

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
        View view = inflater.inflate(R.layout.net_curtain_layout, null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        mAddOrderLinePresenter = new AddOrderLinePresenterImpl(this);
        ButterKnife.bind(this, view);
        radioGroupPile.setOnCheckedChangeListener(this);
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    @OnClick({R.id.btnSave, R.id.btnCancel, R.id.btnCalculate})
    public void onClick(View view) {
        List<OrderLineDetailModel> orderLines = new ArrayList<>();

        if (view.getId() == R.id.btnSave) {
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel=new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if (!etWidth.getText().toString().isEmpty()) {
                double width = Double.parseDouble(etWidth.getText().toString());
                orderLineDetailModel.setPropertyWidth(width);
            }

            if (!etHeight.getText().toString().isEmpty()) {
                double height = Double.parseDouble(etHeight.getText().toString());
                orderLineDetailModel.setPropertyHeight(height);            }

            if (!etUnitprice.getText().toString().isEmpty()) {
                double unitPrice = Double.parseDouble(etUnitprice.getText().toString());
                orderLineDetailModel.setUnitPrice(unitPrice);
            }

            if (radioGroupPile.getCheckedRadioButtonId() != -1 || !etOtherPile.getText().toString().isEmpty()) {
                if (radioGroupPile.getCheckedRadioButtonId() != -1) {
                    int checkedId = radioGroupPile.getCheckedRadioButtonId();
                    if (checkedId == R.id.radioButton2) {
                        pile = 2;
                    } else if (checkedId == R.id.radioButton2_5) {
                        pile = 2.5;
                    } else {
                        pile = 3;
                    }
                } else {
                    pile = Double.parseDouble(etOtherPile.getText().toString());
                }

                orderLineDetailModel.setSizeOfPile(pile);
            }

            if (!etPattern.getText().toString().isEmpty()) {
                String pattern =etPattern.getText().toString();
                productDetailModel.setPatternCode(pattern);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etVariant.getText().toString().isEmpty()) {
                String variant=etVariant.getText().toString();
                productDetailModel.setVariantCode(variant);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etAlias.getText().toString().isEmpty()) {
                String alias=etAlias.getText().toString();
                productDetailModel.setAliasName(alias);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etDesc.getText().toString().isEmpty()) {
                String desc=etDesc.getText().toString();
                orderLineDetailModel.setLineDescription(desc);
            }


            EventBus.getDefault().post(orderLineDetailModel);

            dismiss();


        } else if (view.getId() == R.id.btnCancel) {
            dismiss();
        } else {

            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if(TextUtils.isEmpty(etWidth.getText().toString())){
                etWidth.setError("Genişlik giriniz!");
            }else if(TextUtils.isEmpty(etUnitprice.getText().toString())){
                etUnitprice.setError("Bİrim fiyat giriniz!");
            }else if(radioGroupPile.getCheckedRadioButtonId() == -1 && TextUtils.isEmpty(etOtherPile.getText().toString()) ){
                Toast.makeText(getActivity(), "Pile sıklığı giriniz!", Toast.LENGTH_SHORT).show();
            }else{
                unitPrice = Double.parseDouble(etUnitprice.getText().toString());
                if (radioGroupPile.getCheckedRadioButtonId() != -1) {
                    int checkedId = radioGroupPile.getCheckedRadioButtonId();
                    if (checkedId == R.id.radioButton2) {
                        pile = 2;
                    } else if (checkedId == R.id.radioButton2_5) {
                        pile = 2.5;
                    } else {
                        pile = 3;
                    }
                } else {
                    pile = Double.parseDouble(etOtherPile.getText().toString());
                }

                double netWidth = Double.parseDouble(etWidth.getText().toString());

                orderLineDetailModel.setUnitPrice(unitPrice);
                orderLineDetailModel.setPropertyWidth(netWidth);
                orderLineDetailModel.setSizeOfPile(pile);
                orderLines.add(orderLineDetailModel);
                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                calculateOrderLine(addOrderLineDetailListModel);
            }

        }

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioButton2:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;

            case R.id.radioButton2_5:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;

            case R.id.radioButton3:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;
        }
    }

    @Override
    @OnFocusChange(R.id.editTextOtherPile)
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.editTextOtherPile) {
            if (hasFocus) {
                resetRadioButton();
            }
        } else {

        }
    }


    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId = getSessionIdFromPref();
        mAddOrderLinePresenter.calculateOrderLine(orderLineDetailListModel, sessionId);
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
        totalPrice = calculationResponse.getTotalAmount();
        totalM = calculationResponse.getUsedMaterial();
        tvTotalMeter.setText(String.format("%.2f", totalM)+" m");
        tvTotalPrice.setText(String.format("%.2f",totalPrice)+" TL");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }
}
