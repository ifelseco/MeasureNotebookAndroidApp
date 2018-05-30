package com.javaman.olcudefteri.ui.orders.dialogs;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.event.MechanismEvent;
import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.ProductDetailModel;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.ui.orders.dialogs.BrizPiecesDialog;
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
 * Briz perde dialog
 */


public class BrizCurtain extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener, CalculateView {


    int parcaCount, mechanisStatus;
    @BindView(R.id.editTextOtherPile)
    EditText etOtherPile;
    @BindView(R.id.editTextBrizWidth)
    EditText etBrizWidth;
    @BindView(R.id.editTextBrizHeight)
    EditText etBrizHeight;
    @BindView(R.id.editTextFarbelaWidth)
    EditText etFarbelaWidth;
    @BindView(R.id.editTextFarbelaHeight)
    EditText etFarbelaHeight;
    @BindView(R.id.editTextBrizUnitPrice)
    EditText etUnitprice;
    @BindView(R.id.editTextPattern)
    EditText etPattern;
    @BindView(R.id.editTextVariant)
    EditText etVariant;
    @BindView(R.id.editTextAlias)
    EditText etAlias;
    @BindView(R.id.editTextBrizDesc)
    EditText etDesc;
    @BindView(R.id.radiGroupPile)
    RadioGroup radioGroupPile;
    @BindView(R.id.textViewBrizM)
    TextView tvTotalM;
    @BindView(R.id.textViewBrizTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.progress_bar_calc)
    ProgressBar progressCalc;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnCalculate)
    Button btnCalculate;
    @BindView(R.id.tableMeasureParcali)
    TableLayout tableLayoutParcali;
    @BindView(R.id.linear_layout_normal_size)
    LinearLayout linearLayoutNormalSize;


    double pile, unitPrice;
    private AddOrderLinePresenter mAddOrderLinePresenter;
    public static final int ARG_PRODUCT_VALUE = 7;
    SharedPreferenceHelper sharedPreferenceHelper;
    String pattern, variant, alias, desc;
    private double farbelaWidth, farbelaHeight;


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
        View view = inflater.inflate(R.layout.briz_layout, null);
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this, view);
        mAddOrderLinePresenter = new AddOrderLinePresenterImpl(this);
        initView();
        return view;
    }

    private void initView() {
        radioGroupPile.setOnCheckedChangeListener(this);
        etOtherPile.setOnFocusChangeListener(this);
        setCancelable(false);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        showDialog(new BrizPiecesDialog(), "briz-pieces-dialog");
    }

    @Override
    @OnClick({R.id.btnSave, R.id.btnCalculate, R.id.btnCancel})
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

            if (!etFarbelaWidth.getText().toString().isEmpty()) {
                farbelaWidth = Double.parseDouble(etFarbelaWidth.getText().toString());

            }

            if (!etFarbelaHeight.getText().toString().isEmpty()) {
                farbelaHeight = Double.parseDouble(etFarbelaHeight.getText().toString());

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

            }


            if (parcaCount > 0) {

                for (int count = 0; count < parcaCount; count++) {
                    TableRow row = (TableRow) tableLayoutParcali.getChildAt(count);
                    EditText etWidthP = row.findViewById(R.id.editTextWidthP);
                    EditText etHeightP = row.findViewById(R.id.editTextHeightP);
                    OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                    orderLineDetailModel.setProduct(productDetailModel);

                    if (!TextUtils.isEmpty(etWidthP.getText().toString())) {
                        double width = Double.parseDouble(etWidthP.getText().toString());
                        orderLineDetailModel.setPropertyWidth(width);
                    }

                    if (!TextUtils.isEmpty(etHeightP.getText().toString())) {
                        double height = Double.parseDouble(etHeightP.getText().toString());
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

                    }

                    productDetailModel.setPatternCode(pattern);
                    productDetailModel.setVariantCode(variant);
                    productDetailModel.setAliasName(alias);
                    orderLineDetailModel.setProduct(productDetailModel);
                    orderLineDetailModel.setLineDescription(desc);
                    orderLineDetailModel.setSizeOfPile(pile);
                    orderLineDetailModel.setPropertyAlternativeWidth(farbelaWidth);
                    orderLineDetailModel.setPropertyAlternativeHeight(farbelaHeight);
                    orderLineDetailModel.setPiecesCount(parcaCount);
                    orderLineDetailModel.setMechanismStatus(mechanisStatus);
                    orderLines.add(orderLineDetailModel);
                }

                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                EventBus.getDefault().post(addOrderLineDetailListModel);


            } else {
                OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                orderLineDetailModel.setProduct(productDetailModel);

                if (!TextUtils.isEmpty(etBrizWidth.getText().toString())) {
                    double width = Double.parseDouble(etBrizWidth.getText().toString());
                    orderLineDetailModel.setPropertyWidth(width);
                }

                if (!TextUtils.isEmpty(etBrizHeight.getText().toString())) {
                    double height = Double.parseDouble(etBrizHeight.getText().toString());
                    orderLineDetailModel.setPropertyHeight(height);
                }

                if (!TextUtils.isEmpty(etUnitprice.getText().toString())) {
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

                }

                productDetailModel.setPatternCode(pattern);
                productDetailModel.setVariantCode(variant);
                productDetailModel.setAliasName(alias);
                orderLineDetailModel.setProduct(productDetailModel);
                orderLineDetailModel.setLineDescription(desc);
                orderLineDetailModel.setPropertyAlternativeHeight(farbelaHeight);
                orderLineDetailModel.setPropertyAlternativeWidth(farbelaWidth);
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


                if (TextUtils.isEmpty(etUnitprice.getText().toString())) {
                    etUnitprice.setError("Birim fiyat giriniz!");
                } else {
                    for (int count = 0; count < parcaCount; count++) {
                        TableRow row = (TableRow) tableLayoutParcali.getChildAt(count);
                        EditText etWidthP = row.findViewById(R.id.editTextWidthP);
                        EditText etHeightP = row.findViewById(R.id.editTextHeightP);

                        if (TextUtils.isEmpty(etWidthP.getText().toString())) {
                            etWidthP.setError("Parça en giriniz!");
                        } else if (TextUtils.isEmpty(etHeightP.getText().toString())) {
                            etHeightP.setError("Parça boy giriniz!");
                        } else if (radioGroupPile.getCheckedRadioButtonId() == -1 && TextUtils.isEmpty(etOtherPile.getText().toString())) {
                            StyleableToast.makeText(getActivity(), "Pile sıklığı giriniz!", R.style.warn_toast_style).show();
                        } else {
                            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                            orderLineDetailModel.setProduct(productDetailModel);
                            double width = Double.parseDouble(etWidthP.getText().toString());
                            double height = Double.parseDouble(etWidthP.getText().toString());
                            double unitPrice = Double.parseDouble(etUnitprice.getText().toString());

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

                            }

                            orderLineDetailModel.setPropertyWidth(width);
                            orderLineDetailModel.setPropertyHeight(height);
                            orderLineDetailModel.setSizeOfPile(pile);
                            orderLineDetailModel.setUnitPrice(unitPrice);
                            orderLines.add(orderLineDetailModel);
                        }
                    }

                    if (parcaCount == orderLines.size()) {
                        addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                        calculateOrderLine(addOrderLineDetailListModel);
                    } else {
                        StyleableToast.makeText(getActivity(), "Parçalardan biri eksik bilgi içeriyor", R.style.warn_toast_style).show();

                    }


                }


            } else {

                if (TextUtils.isEmpty(etBrizWidth.getText().toString())) {
                    etBrizWidth.setError("Briz en giriniz!");
                } else if (TextUtils.isEmpty(etBrizHeight.getText().toString())) {
                    etBrizHeight.setError("Briz boy giriniz!");
                }
                if (TextUtils.isEmpty(etUnitprice.getText().toString())) {
                    etUnitprice.setError("Birim fiyat giriniz!");
                } else if (radioGroupPile.getCheckedRadioButtonId() == -1 && TextUtils.isEmpty(etOtherPile.getText().toString())) {
                    StyleableToast.makeText(getActivity(), "Pile sıklığı giriniz!", R.style.warn_toast_style).show();
                } else {
                    OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
                    orderLineDetailModel.setProduct(productDetailModel);
                    double width = Double.parseDouble(etBrizWidth.getText().toString());
                    double height = Double.parseDouble(etBrizHeight.getText().toString());
                    double unitPrice = Double.parseDouble(etUnitprice.getText().toString());

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

                    }
                    orderLineDetailModel.setPropertyWidth(width);
                    orderLineDetailModel.setPropertyHeight(height);
                    orderLineDetailModel.setUnitPrice(unitPrice);
                    orderLineDetailModel.setSizeOfPile(pile);
                    orderLines.add(orderLineDetailModel);
                    addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                    calculateOrderLine(addOrderLineDetailListModel);

                }

            }


        }


    }

    public void showDialog(DialogFragment dialogFragment, String fragmentTag) {
        dialogFragment.show(getFragmentManager(), fragmentTag);
    }

    @Subscribe
    public void getDialogData(MechanismEvent mechanismEvent) {
        if (mechanismEvent.getMechanismStatus() == -1) {
            dismiss();
        } else {
            parcaCount = mechanismEvent.getPiecesCount();
            mechanisStatus = mechanismEvent.getMechanismStatus();
            setView();
        }
    }

    private void setView() {

        if (mechanisStatus == 2) {
            linearLayoutNormalSize.setVisibility(View.GONE);
            createTableView(parcaCount);
        } else if (mechanisStatus == 1) {
            tableLayoutParcali.removeAllViews();
            tableLayoutParcali.setVisibility(View.GONE);
            linearLayoutNormalSize.setVisibility(View.VISIBLE);

        }
    }

    private void createTableView(int parcaCount) {
        for (int j = 0; j < parcaCount; j++) {
            View row = getLayoutInflater().inflate(R.layout.parcali_briz_row, null, false);
            TextView textView = row.findViewById(R.id.labelParca);
            textView.setText("Briz Parça " + (j + 1));

            tableLayoutParcali.addView(row);
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
        StyleableToast.makeText(getActivity(), message, R.style.info_toast_style).show();
    }

    @Override
    public void showProgress() {
        progressCalc.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressCalc.setVisibility(View.GONE);
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken = sharedPreferenceHelper.getStringPreference("sessionId", null);
        return xAuthToken;
    }

    @Override
    public void updateAmount(CalculationResponse calculationResponse) {
        double totalM = calculationResponse.getUsedMaterial();
        double totalPrice = calculationResponse.getTotalAmount();
        tvTotalM.setText(String.format("%.2f", totalM) + " m");
        tvTotalPrice.setText(String.format("%.2f", totalPrice) + " TL");
    }

    @Override
    public void navigateLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }
}

