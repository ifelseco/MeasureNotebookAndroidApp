package com.javaman.olcudefteri.orders.curtain_type_dialog;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.view.CalculateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Fon perde dialog
 */

public class FonCurtain extends DialogFragment implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,
        View.OnFocusChangeListener,
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener,CalculateView {


    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.editTextVariant) EditText etVariant;
    @BindView(R.id.editTextPattern) EditText etPattern;
    @BindView(R.id.editTextAlias) EditText etAlias;
    @BindView(R.id.editTextOtherPile) EditText etOtherPile;
    @BindView(R.id.editTextFonUnitPrice) EditText etUnitprice;
    @BindView(R.id.editTextFonTotalPrice) EditText etTotalPrice;
    @BindView(R.id.editTextFonDesc) EditText etDesc;
    @BindView(R.id.textViewFonM) TextView tvTotalMeter;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.radiGroupPile) RadioGroup radioGroupPile;
    @BindView(R.id.radiGroupPileType) RadioGroup radioGroupPileType;
    @BindView(R.id.radio_group_fon_direction) RadioGroup radioGroupDirection;
    @BindView(R.id.radiGroupFonType) RadioGroup radioGroupFonType;

    double totalPrice,netWidthTek,unitPrice,totalM,pile;
    String fonType,pileType;
    int fonTypeId,pileTypeId;
    int numberOfKanat = 0;
    private AddOrderLinePresenter mAddOrderLinePresenter;

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
        View view = inflater.inflate(R.layout.fon_curtain, null);
        mAddOrderLinePresenter=new AddOrderLinePresenterImpl(this);
        ButterKnife.bind(this,view);
        initView();

        return view;
    }

    private void initView() {

        radioGroupPile.setOnCheckedChangeListener(this);
        etOtherPile.setOnFocusChangeListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setCancelable(false);
    }

    public void fillSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                arrayResource, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        int id = radioGroup.getId();

        if (id == R.id.radiGroupPile) {
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
        } else {

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
    @OnClick({R.id.btnCalculate,R.id.btnSave,R.id.btnCancel})
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            dismiss();
        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
        } else {

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public double calculateKruvazeFon(double width, double pile, double unitPrice) {
        double widthLast = width / 100;
        return widthLast * pile * unitPrice;
    }

    public double calculateFonKanat(double width, double pile, double unitPrice) {
        double widthLast = width / 100;
        return ((widthLast * pile)+ 0.25) * unitPrice;
    }

    public double calculateJapanPanel(double width, double unitPrice) {
        double widthLast = width / 100;
        return (widthLast * 2.5) * unitPrice;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id=buttonView.getId();


    }



    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {

    }

    @Override
    public void showAlert(String message) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public String getSessionIdFromPref() {
        return null;
    }

    @Override
    public void updateAmount(CalculationResponse calculationResponse) {

    }
}
