package com.javaman.olcudefteri.add_order.curtain_type_dialog;

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
import android.widget.RadioGroup;

import com.javaman.olcudefteri.R;

/**
 * Created by javaman on 18.12.2017.
 * Farbela dialog
 */

public class FarbelaCurtain extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener, RadioGroup.OnCheckedChangeListener {

    Button btnCancel,btnSave;
    EditText etOtherPile;
    RadioGroup radioGroupPile;


    private void resetRadioButton() {

        radioGroupPile.clearCheck();

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.farbela_curtain,null);
        btnSave=view.findViewById(R.id.btnSave);
        btnCancel=view.findViewById(R.id.btnCancel);

        etOtherPile=view.findViewById(R.id.editTextOtherPile);
        radioGroupPile=view.findViewById(R.id.radiGroupPile);
        radioGroupPile.setOnCheckedChangeListener(this);

        etOtherPile.setOnFocusChangeListener(this);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        setCancelable(false);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnSave){
            dismiss();
        }else{
            dismiss();
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
        if(v.getId()==R.id.editTextOtherPile){
            if (hasFocus) {
                resetRadioButton();
            }
        }else{

        }
    }

}
