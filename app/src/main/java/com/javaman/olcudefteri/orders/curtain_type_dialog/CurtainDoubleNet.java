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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Tül kruvaze dialog
 */

public class CurtainDoubleNet extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

    @BindView(R.id.editTextWidth)
    EditText etWidth;

    @BindView(R.id.editTextHeight)
    EditText etHeight;

    @BindView(R.id.editTextRightWidth)
    EditText etRigthWidth;

    @BindView(R.id.editTextLeftWidth)
    EditText etLeftWidth;

    @BindView(R.id.editTextVariant)
    EditText etVariant;

    @BindView(R.id.editTextPattern)
    EditText etPattern;

    @BindView(R.id.editTextAlias)
    EditText etAlias;

    @BindView(R.id.editTextOtherPile)
    EditText etOtherPile;

    @BindView(R.id.editTextDoubleNetUnitPrice)
    EditText etUnitprice;

    @BindView(R.id.editTextDoubleNetTotalPrice)
    EditText etTotalPrice;

    @BindView(R.id.editTextDoubleNetDesc)
    EditText etDesc;

    @BindView(R.id.textViewDoubleNetM)
    TextView tvTotalMeter;

    @BindView(R.id.textViewDoubleNetWindowWidth)
    TextView tvDoubleNetWindowWidth;

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

    @BindView(R.id.progress_bar_save)
    ProgressBar progressBarSave;





    double totalPrice, unitPrice, totalM, pile;

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
        View view = inflater.inflate(R.layout.curtain_double_net_layout, null);
        ButterKnife.bind(this,view);
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
    }

    @Override
    @OnClick({R.id.btnSave,R.id.btnCancel,R.id.btnCalculate})
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            dismiss();
        } else if (view.getId() == R.id.btnCancel) {
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

                double doubleNetWidth = Double.parseDouble(etWidth.getText().toString());


                totalPrice = calculate(doubleNetWidth, pile, unitPrice);
                totalM = calculateTotalMeter(doubleNetWidth, pile);

                tvTotalMeter.setText(String.format("%.2f", totalM));
                etTotalPrice.setText(String.format("%.2f", totalPrice));

                if (!etRigthWidth.getText().toString().equals("") && !etLeftWidth.getText().toString().equals("")) {

                    double windowWidth = doubleNetWidth - (Double.parseDouble(etLeftWidth.getText().toString()) +
                            Double.parseDouble(etRigthWidth.getText().toString()));

                    tvDoubleNetWindowWidth.setText(String.format("Cam En : %.2f", windowWidth));
                }

            } else {
                Toast.makeText(getActivity(), "Gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show();
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
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.editTextOtherPile) {
            if (hasFocus) {
                resetRadioButton();
            }
        } else {

        }
    }

    public double calculate(double width, double pile, double unitPrice) {

        double widthLast = width / 100;

        if (widthLast > 0 && widthLast < 2.5) {
            return (widthLast * pile + 2) * unitPrice;
        } else if (widthLast >= 2.5 && widthLast <= 3.5) {
            return (widthLast * pile + 2.5) * unitPrice;
        } else if (widthLast > 3.5 && widthLast <= 5) {
            return (widthLast * pile + 3.5) * unitPrice;
        } else {
            return (widthLast * pile + 4) * unitPrice;
        }


    }

    public double calculateTotalMeter(double width, double pile) {

        double widthLast = width / 100;

        if (widthLast > 0 && widthLast < 2.5) {
            return widthLast * pile + 2;
        } else if (widthLast >= 2.5 && widthLast <= 3.5) {
            return widthLast * pile + 2.5;
        } else if (widthLast > 3.5 && widthLast <= 5) {
            return widthLast * pile + 3.5;
        } else {
            return widthLast * pile + 4;
        }


    }
}

