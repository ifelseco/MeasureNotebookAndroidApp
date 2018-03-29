package com.javaman.olcudefteri.orders.curtain_type_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import butterknife.OnFocusChange;

/**
 * Created by javaman on 18.12.2017.
 * Fon perde dialog
 */

public class FonCurtain extends DialogFragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,
        View.OnFocusChangeListener, CalculateView {


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
    @BindView(R.id.editTextFonUnitPrice)
    EditText etUnitprice;

    @BindView(R.id.editTextFonDesc)
    EditText etDesc;
    @BindView(R.id.textViewFonM)
    TextView tvTotalMeter;

    @BindView(R.id.tvFonTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.btnSave)
    ImageButton btnSave;
    @BindView(R.id.btnCancel)
    ImageButton btnCancel;
    @BindView(R.id.btnCalculate)
    ImageButton btnCalculate;
    @BindView(R.id.radiGroupPile)
    RadioGroup radioGroupPile;
    @BindView(R.id.radiGroupPileType)
    RadioGroup radioGroupPileType;
    @BindView(R.id.radio_group_fon_direction)
    RadioGroup radioGroupDirection;
    @BindView(R.id.radiGroupFonType)
    RadioGroup radioGroupFonType;
    @BindView(R.id.progress_bar_calc)
    ProgressBar progressBarCalc;

    public static final int ARG_PRODUCT_VALUE = 9;
    int fonType, direction, sashCount;
    String pileName;
    double pile;
    private AddOrderLinePresenter mAddOrderLinePresenter;
    SharedPreferenceHelper sharedPreferenceHelper;


    private void resetRadioButton(RadioGroup radioGroup) {

        radioGroup.clearCheck();

    }

    private void disableRadioButton(RadioGroup radioGroup){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRadioButton(RadioGroup radioGroup){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
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
        View view = inflater.inflate(R.layout.fon_curtain, null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        mAddOrderLinePresenter = new AddOrderLinePresenterImpl(this);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {

        radioGroupPile.setOnCheckedChangeListener(this);
        radioGroupFonType.setOnCheckedChangeListener(this);
        radioGroupPileType.setOnCheckedChangeListener(this);
        radioGroupDirection.setOnCheckedChangeListener(this);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setCancelable(false);
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        int id = radioGroup.getId();

        if (id == R.id.radiGroupPile) {
            switch (i) {
                case R.id.radioButton2:
                    etOtherPile.clearFocus();
                    etOtherPile.setText("");
                    pile = 2;
                    break;

                case R.id.radioButton2_5:
                    etOtherPile.clearFocus();
                    etOtherPile.setText("");
                    pile = 2.5;
                    break;

                case R.id.radioButton3:
                    etOtherPile.clearFocus();
                    etOtherPile.setText("");
                    pile = 3;
                    break;
            }
        } else if (id == R.id.radiGroupFonType) {

            if (i == R.id.radioButtonJaponPanel) {
                fonType = 3;
                sashCount=0;
                pile = 0;
                radioGroupDirection.setEnabled(false);
                radioGroupPile.setEnabled(false);
                radioGroupPileType.setEnabled(false);

                disableRadioButton(radioGroupDirection);
                disableRadioButton(radioGroupPile);
                disableRadioButton(radioGroupPileType);
                etOtherPile.setEnabled(false);

                resetRadioButton(radioGroupDirection);
                resetRadioButton(radioGroupPile);
                resetRadioButton(radioGroupPileType);
            } else if (i == R.id.radioButtonKruvazeKanat) {
                fonType = 1;
                enableRadioButton(radioGroupDirection);
                enableRadioButton(radioGroupPile);
                enableRadioButton(radioGroupPileType);
                etOtherPile.setEnabled(true);

            } else {
                fonType = 2;
                enableRadioButton(radioGroupDirection);
                enableRadioButton(radioGroupPile);
                enableRadioButton(radioGroupPileType);
                etOtherPile.setEnabled(true);

            }

        } else if (id == R.id.radio_group_fon_direction) {
            if (i == R.id.radi_button_left) {
                direction = 1;
                sashCount = 1;
            } else if (i == R.id.radi_button_right) {
                direction = 2;
                sashCount = 1;
            } else if (i == R.id.radi_button_left_right){
                sashCount = 2;
            }else{
                sashCount=0;
            }
        } else {
            if (i == R.id.radioButtonAmerican) {
                pileName = "American Pile";
            } else if (i == R.id.radioButtonKanun) {
                pileName = "Kanun Pile";
            } else if (i == R.id.radioButtonYan) {
                pileName = "Yan Pile";
            } else {
                pileName = "Diğer";
            }
        }

    }

    @Override
    @OnFocusChange(R.id.editTextOtherPile)
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.editTextOtherPile) {
            if (hasFocus) {
                resetRadioButton(radioGroupPile);
            }
        } else {

        }
    }

    @Override
    @OnClick({R.id.btnCalculate, R.id.btnSave, R.id.btnCancel})
    public void onClick(View v) {

        if (v.getId() == R.id.btnSave) {
            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            List<OrderLineDetailModel> orderLines = new ArrayList<>();
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if (!etWidth.getText().toString().isEmpty()) {
                double width = Double.parseDouble(etWidth.getText().toString());
                orderLineDetailModel.setPropertyWidth(width);
            }

            if (!etHeight.getText().toString().isEmpty()) {
                double height = Double.parseDouble(etHeight.getText().toString());
                orderLineDetailModel.setPropertyHeight(height);
            }

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
                String pattern = etPattern.getText().toString();
                productDetailModel.setPatternCode(pattern);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etVariant.getText().toString().isEmpty()) {
                String variant = etVariant.getText().toString();
                productDetailModel.setVariantCode(variant);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etAlias.getText().toString().isEmpty()) {
                String alias = etAlias.getText().toString();
                productDetailModel.setAliasName(alias);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etDesc.getText().toString().isEmpty()) {
                String desc = etDesc.getText().toString();
                orderLineDetailModel.setLineDescription(desc);
            }


            if (sashCount == 2) {
                orderLineDetailModel.setFonType(fonType);
                orderLineDetailModel.setPileName(pileName);
                OrderLineDetailModel otherOrderLine = orderLineDetailModel;
                otherOrderLine.setDirection(1);
                orderLines.add(otherOrderLine);
                orderLineDetailModel.setDirection(2);
                orderLines.add(orderLineDetailModel);
                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                EventBus.getDefault().post(addOrderLineDetailListModel);
            } else {
                orderLineDetailModel.setFonType(fonType);
                orderLineDetailModel.setPileName(pileName);
                orderLineDetailModel.setDirection(direction);
                EventBus.getDefault().post(orderLineDetailModel);
            }

            dismiss();
        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
        } else {
            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            List<OrderLineDetailModel> orderLines = new ArrayList<>();
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if (TextUtils.isEmpty(etWidth.getText().toString())) {
                etWidth.setError("En giriniz!");
            } else if (TextUtils.isEmpty(etUnitprice.getText().toString())) {
                etUnitprice.setError("Birim fiyat giriniz!");

            } else if (radioGroupFonType.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getActivity(), "Fon türü seçiniz!", Toast.LENGTH_SHORT).show();
            } else {

                if (fonType != 3) {
                    if (radioGroupPile.getCheckedRadioButtonId() == -1 && etOtherPile.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Pile sıklığı giriniz!", Toast.LENGTH_SHORT).show();
                    }else{
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

                        orderLineDetailModel.setFonType(fonType);
                        orderLineDetailModel.setSizeOfPile(pile);
                    }

                } else {
                    orderLineDetailModel.setFonType(fonType);
                }

                double unitPrice = Double.parseDouble(etUnitprice.getText().toString());
                orderLineDetailModel.setUnitPrice(unitPrice);
                double width = Double.parseDouble(etWidth.getText().toString());
                orderLineDetailModel.setPropertyWidth(width);

                if (sashCount == 2) {
                    OrderLineDetailModel otherOrderLine = orderLineDetailModel;
                    otherOrderLine.setDirection(1);
                    orderLineDetailModel.setDirection(2);
                    orderLines.add(otherOrderLine);
                    orderLines.add(orderLineDetailModel);
                    addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                    calculateOrderLine(addOrderLineDetailListModel);
                } else {
                    orderLines.add(orderLineDetailModel);
                    addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                    calculateOrderLine(addOrderLineDetailListModel);
                }

            }

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
        double totalM = calculationResponse.getUsedMaterial();
        double totalPrice = calculationResponse.getTotalAmount();
        tvTotalMeter.setText(String.format("%.2f", totalM)+" m");
        tvTotalPrice.setText(String.format("%.2f", totalPrice)+" TL");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }
}
