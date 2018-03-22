package com.javaman.olcudefteri.orders.curtain_type_dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Zebra perde dialog
 */

public class ZebraCurtain extends DialogFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    int parcaCount;
    double totalPrice = 0;
    double unitPrice = 0;
    double totalM2=0;

    @BindView(R.id.radioGroupType) RadioGroup radioGroup;
    @BindView(R.id.radioButtonParcali) RadioButton radioButtonParcali;
    @BindView(R.id.radioButtonCokluMekanizma) RadioButton radioButtonCokluMekanizma;
    @BindView(R.id.tableMeasureParcali) TableLayout tableLayoutParcali;
    @BindView(R.id.editTextParcaCount) EditText etParcaCount;
    @BindView(R.id.editTextStorUnitPrice) EditText etUnitPrice;
    @BindView(R.id.editTextStorTotalPrice) EditText etTotalPrice;
    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.textViewStorM2) TextView tvZebraM2;
    @BindView(R.id.progress_bar_save) ProgressBar progressBarSave;
    @BindView(R.id.progress_bar_calc) ProgressBar progressBarCalc;

    private TextWatcher textWatcherParcaCount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            tableLayoutParcali.removeAllViews();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (charSequence.length() != 0) {

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



    public double calculateTotalPrice(double width, double height, double unitPrice) {
        double widthLast = calculateWidth(width);
        double heightLast = calculateHeight(height);
        return widthLast * heightLast * unitPrice;
    }

    public double calculateWidth(double width) {
        if (width <= 100) {
            width = 100;

        } else {
            if (width % 10 != 0) {
                width = Math.round((width + 5) / 10) * 10;
            }
        }
        return width/100;
    }

    public double calculateHeight(double height) {

        if (height <= 200) {
            height = 200;
        } else if (height <= 260 && height > 200) {
            height = 260;
        } else if (height <= 300 && height > 260) {
            height = 300;
        }

        return height/100;
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
        View view = inflater.inflate(R.layout.zebra_curtain, null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(this);
        etParcaCount.addTextChangedListener(textWatcherParcaCount);
        tableLayoutParcali.setVisibility(View.GONE);
        etParcaCount.setVisibility(View.GONE);
        setCancelable(false);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton selectedRadioButton = radioGroup.findViewById(i);

        int id = selectedRadioButton.getId();

        if (id == R.id.radioButtonCokluMekanizma || id == R.id.radioButtonParcali) {

            tableLayoutParcali.setVisibility(View.VISIBLE);
            etParcaCount.setVisibility(View.VISIBLE);


        } else {
            tableLayoutParcali.removeAllViews();
            tableLayoutParcali.setVisibility(View.GONE);
            etParcaCount.setVisibility(View.GONE);

        }
    }


    @Override
    @OnClick({R.id.btnCancel,R.id.btnSave,R.id.btnCalculate})
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            dismiss();
        } else if (view.getId() == R.id.btnCancel) {
            dismiss();
        } else {



            if(!etUnitPrice.getText().toString().equals("")){
                unitPrice = Double.parseDouble(etUnitPrice.getText().toString());

                if (parcaCount > 0) {

                    totalPrice=0;
                    totalM2=0;

                    for (int count = 0; count < parcaCount; count++) {
                        TableRow row = (TableRow) tableLayoutParcali.getChildAt(count);
                        EditText editTextWidthP1 = row.findViewById(R.id.editTextWidthP1);
                        EditText editTextHeightP1 = row.findViewById(R.id.editTextHeightP1);

                        if (!editTextWidthP1.getText().toString().equals("") &&
                                !editTextHeightP1.getText().toString().equals("")) {

                            double width=Double.parseDouble(editTextWidthP1.getText().toString());
                            double height=Double.parseDouble(editTextHeightP1.getText().toString());
                            double widthLast = calculateWidth(width);
                            double heightLast = calculateHeight(height);


                            totalPrice += calculateTotalPrice(width,height,unitPrice);
                            totalM2+=widthLast*heightLast;

                            tvZebraM2.setText(String.format("%.2f",totalM2));
                            etTotalPrice.setText(String.format("%.2f", totalPrice));

                        }

                    }
                } else {
                    if (!etWidth.getText().toString().equals("") &&
                            !etHeight.getText().toString().equals("")) {




                        double width=Double.parseDouble(etWidth.getText().toString());
                        double height=Double.parseDouble(etHeight.getText().toString());
                        double widthLast = calculateWidth(width);
                        double heightLast = calculateHeight(height);


                        totalPrice += calculateTotalPrice(width,height , unitPrice);
                        totalM2+=widthLast*heightLast;


                        tvZebraM2.setText(""+String.format("%.2f",totalM2));
                        etTotalPrice.setText(String.format("%.2f", totalPrice));
                    }


                }
            }

        }
    }
}
