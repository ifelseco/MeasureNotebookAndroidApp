package com.javaman.olcudefteri.ui.orders.dialogs;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.event.FonTypeEvent;
import com.javaman.olcudefteri.event.MechanismEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FonTypeDialog extends DialogFragment implements  View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.radiGroupFonType)
    RadioGroup radioGroupFonType;

    @BindView(R.id.radiGroupPileType)
    RadioGroup radioGroupPileType;

    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.linear_layout_pile_type)
    LinearLayout linearLayoutPileType;

    int fonType;
    String pileName;

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
        View view = inflater.inflate(R.layout.fon_type_dialog_layout, null);
        ButterKnife.bind(this, view);
        initView();
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    private void initView() {
        linearLayoutPileType.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        radioGroupFonType.clearCheck();
        radioGroupFonType.setOnCheckedChangeListener(this);
        radioGroupPileType.setOnCheckedChangeListener(this);
    }

    @Override
    @OnClick({R.id.btn_save,R.id.btn_cancel})
    public void onClick(View v) {
        FonTypeEvent fonTypeEvent=new FonTypeEvent();
        if(v.getId()==R.id.btn_save){
            fonTypeEvent.setFonType(fonType);
            fonTypeEvent.setPileName(pileName);
            EventBus.getDefault().post(fonTypeEvent);
            dismiss();
        }else if(v.getId()==R.id.btn_cancel){
            fonTypeEvent.setFonType(-1);
            EventBus.getDefault().post(fonTypeEvent);
            dismiss();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int id=group.getId();

        if(id==R.id.radiGroupFonType){
            btnSave.setVisibility(View.VISIBLE);
            linearLayoutPileType.setVisibility(View.VISIBLE);
            if(checkedId==R.id.radioButtonJaponPanel){
                radioGroupPileType.clearCheck();
                linearLayoutPileType.setVisibility(View.GONE);
                fonType=3;
            }else if(checkedId==R.id.radioButtonKruvazeKanat){
                radioGroupPileType.clearCheck();
                linearLayoutPileType.setVisibility(View.VISIBLE);
                fonType=1;
            }else if(checkedId==R.id.radioButtonFonKanat){
                radioGroupPileType.clearCheck();
                linearLayoutPileType.setVisibility(View.VISIBLE);
                fonType=2;
            }else{
                fonType=0;
            }
        }else if(id==R.id.radiGroupPileType){
            if (checkedId == R.id.radioButtonAmerican) {
                pileName = "AP";
            } else if (checkedId == R.id.radioButtonKanun) {
                pileName = "KP";
            } else if (checkedId == R.id.radioButtonYan) {
                pileName = "YP";
            } else {
                pileName = "O";
            }
        }

    }
}
