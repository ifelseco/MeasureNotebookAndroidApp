package com.javaman.olcudefteri.ui.orders.dialogs;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.event.MechanismEvent;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrizPiecesDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {


    int parcaCount;

    @BindView(R.id.radioGroupType)
    RadioGroup radioGroupType;

    @BindView(R.id.editTextParcaCount)
    EditText etParcaCount;

    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.linear_layout_pices_count)
    LinearLayout linearLayoutPiecesCount;

    int mechanisStatus;

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
        View view = inflater.inflate(R.layout.briz_pieces_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    private void initView() {
        btnSave.setVisibility(View.GONE);
        linearLayoutPiecesCount.setVisibility(View.GONE);
        radioGroupType.clearCheck();
        radioGroupType.setOnCheckedChangeListener(this);

    }



    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton selectedRadioButton = radioGroup.findViewById(i);

        int id = selectedRadioButton.getId();

        if(id == R.id.radioButtonParcali){
            btnSave.setVisibility(View.VISIBLE);
            linearLayoutPiecesCount.setVisibility(View.VISIBLE);
            etParcaCount.setVisibility(View.VISIBLE);
            mechanisStatus=2;
        }
        else if(id==R.id.radioButtonNormal){
            btnSave.setVisibility(View.VISIBLE);
            parcaCount=0;
            etParcaCount.setText("");
            etParcaCount.setVisibility(View.GONE);
            linearLayoutPiecesCount.setVisibility(View.GONE);
            mechanisStatus=1;
        }else{
            btnSave.setVisibility(View.GONE);
            mechanisStatus=0;
        }
    }

    @Override
    @OnClick({R.id.btn_save,R.id.btn_cancel})
    public void onClick(View v) {
        MechanismEvent mechanismEvent=new MechanismEvent();
        if(v.getId()==R.id.btn_save){

            if(mechanisStatus!=1){
                if(!TextUtils.isEmpty(etParcaCount.getText().toString())){
                    int piecesCount=Integer.parseInt(etParcaCount.getText().toString());
                    mechanismEvent.setMechanismStatus(mechanisStatus);
                    mechanismEvent.setPiecesCount(piecesCount);
                    EventBus.getDefault().post(mechanismEvent);
                    dismiss();
                }else{
                    etParcaCount.setError("Parça sayısı giriniz!");
                }
            }else{
                mechanismEvent.setMechanismStatus(mechanisStatus);
                mechanismEvent.setPiecesCount(0);
                EventBus.getDefault().post(mechanismEvent);
                dismiss();
            }

        }else if(v.getId()==R.id.btn_cancel){
            mechanismEvent.setMechanismStatus(-1);
            EventBus.getDefault().post(mechanismEvent);
            dismiss();
        }
    }
}
