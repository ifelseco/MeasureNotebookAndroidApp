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
        AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {


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
    @BindView(R.id.checkBoxRight) CheckBox checkBoxRight;
    @BindView(R.id.checkBoxLeft) CheckBox checkBoxLeft;
    @BindView(R.id.checkBoxTek) CheckBox checkBoxTekKanat;
    @BindView(R.id.tableMeasureTek) TableLayout tableLayoutTek;
    @BindView(R.id.spinnerFonPileType) Spinner spPileType;
    @BindView(R.id.spinnerFonType) Spinner spFonType;

    double totalPrice,netWidthTek,unitPrice,totalM,pile;
    String fonType,pileType;
    int fonTypeId,pileTypeId;
    int numberOfKanat = 0;
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
        ButterKnife.bind(this,view);
        initView();

        return view;
    }

    private void initView() {
        checkBoxTekKanat.setOnCheckedChangeListener(this);
        fillSpinner(spFonType, R.array.fonType);
        fillSpinner(spPileType, R.array.fonPileType);
        spFonType.setOnItemSelectedListener(this);
        spPileType.setOnItemSelectedListener(this);
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


            if (!etWidth.getText().toString().equals("") &&
                    !etUnitprice.getText().toString().equals("") &&
                    (radioGroupPile.getCheckedRadioButtonId() != -1 || !etOtherPile.getText().toString().equals(""))) {

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

                if(checkBoxTekKanat.isChecked()){
                    TableRow row = (TableRow) tableLayoutTek.getChildAt(0);
                    EditText editTextWidthTek = row.findViewById(R.id.editTextWidthTekKanat);
                    EditText editTextHeightTek = row.findViewById(R.id.editTextHeightTekKanat);
                    RadioGroup radioGroupYonTek=row.findViewById(R.id.radioGroupYonTek);

                    if(!editTextWidthTek.getText().toString().equals("")){
                        netWidthTek = Double.parseDouble(editTextWidthTek.getText().toString());
                    }else{
                        Toast.makeText(getActivity(), "Tek kanat en giriniz", Toast.LENGTH_SHORT).show();
                    }
                   
                }else{
                    netWidthTek =0;
                }



                double netWidth = Double.parseDouble(etWidth.getText().toString());

                if (fonTypeId != -1) {
                    switch (fonTypeId) {
                        case 0:

                            if(checkBoxRight.isChecked() && checkBoxLeft.isChecked()){
                                totalPrice = 2*calculateKruvazeFon(netWidth, pile, unitPrice) + calculateKruvazeFon(netWidthTek,pile,unitPrice);
                                totalM = 2*(netWidth / 100) * pile + (netWidthTek/100)*pile;
                            }else if(checkBoxRight.isChecked() && !checkBoxLeft.isChecked()){
                                totalPrice = calculateKruvazeFon(netWidth, pile, unitPrice) + calculateKruvazeFon(netWidthTek,pile,unitPrice);
                                totalM = (netWidth / 100) * pile + (netWidthTek/100)*pile;
                            }else if(!checkBoxRight.isChecked() && checkBoxLeft.isChecked()){
                                totalPrice = calculateKruvazeFon(netWidth, pile, unitPrice) + calculateKruvazeFon(netWidthTek,pile,unitPrice);
                                totalM = (netWidth / 100) * pile + (netWidthTek/100)*pile;
                            }else{
                                Toast.makeText(getActivity(), "Kanat Yönü Seçiniz", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            if(checkBoxRight.isChecked() && checkBoxLeft.isChecked()){
                                totalPrice = 2*calculateFonKanat(netWidth, pile, unitPrice) + calculateFonKanat(netWidthTek,pile,unitPrice);
                                totalM = 2*(netWidth / 100) * pile+0.5 + (netWidthTek/100)*pile+0.25;
                            }else if(checkBoxRight.isChecked() && !checkBoxLeft.isChecked()){
                                totalPrice = calculateFonKanat(netWidth, pile, unitPrice) + calculateFonKanat(netWidthTek,pile,unitPrice);
                                totalM = (netWidth / 100 ) * pile+0.25 + (netWidthTek/100)*pile+0.25;
                            }else if(!checkBoxRight.isChecked() && checkBoxLeft.isChecked()){
                                totalPrice = calculateFonKanat(netWidth, pile, unitPrice) + calculateFonKanat(netWidthTek,pile,unitPrice);
                                totalM = (netWidth / 100) * pile+0.25 + (netWidthTek/100)*pile+0.25;
                            }else{
                                Toast.makeText(getActivity(), "Kanat Yönü Seçiniz", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            totalPrice = 2*calculateJapanPanel(netWidth, unitPrice)+calculateJapanPanel(netWidthTek,unitPrice);
                            totalM = (netWidth / 100) * 2.5 + (netWidthTek/100)*2.5;
                            break;

                    }
                }

                tvTotalMeter.setText(String.format("%.2f", totalM));
                etTotalPrice.setText(String.format("%.2f", totalPrice));

            } else {
                Toast.makeText(getActivity(), "Gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerFonType:
                fonType = parent.getItemAtPosition(position).toString();
                fonTypeId = position;
                break;
            case R.id.spinnerFonPileType:
                pileType = parent.getItemAtPosition(position).toString();
                pileTypeId = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {
            case R.id.spinnerFonType:
                fonType = parent.getItemAtPosition(0).toString();
                fonTypeId = 0;
                break;
            case R.id.spinnerFonPileType:
                pileType = parent.getItemAtPosition(0).toString();
                pileTypeId = 0;
                break;
        }
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

        if(id==R.id.checkBoxTek){
            if(isChecked){
                View row = (TableRow) getLayoutInflater().inflate(R.layout.tek_kanat_row, null, false);
                tableLayoutTek.addView(row);
            }else{
                tableLayoutTek.removeAllViews();
            }
        }
    }
}
