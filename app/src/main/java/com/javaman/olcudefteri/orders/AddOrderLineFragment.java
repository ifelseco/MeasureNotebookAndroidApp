package com.javaman.olcudefteri.orders;


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
import android.widget.Spinner;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.curtain_type_dialog.BrizCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.CurtainDoubleNet;
import com.javaman.olcudefteri.orders.curtain_type_dialog.FarbelaCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.FonCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.JalouiseCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.NetCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.RollerCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.SunBlindCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.VerticalCurtain;
import com.javaman.olcudefteri.orders.curtain_type_dialog.ZebraCurtain;


/**
 * Created by javaman on 18.12.2017.
 */

public class AddOrderLineFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinnerRooms,spinnerWindows,spinnerCurtains;
    Button btnCancel,btnSave;
    String room;
    String window;
    String curtain;
    String curtainCode;

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
        View view=inflater.inflate(R.layout.add_order_line_layout,null);


        spinnerRooms=view.findViewById(R.id.spinnerRooms);
        spinnerWindows=view.findViewById(R.id.spinnerWindows);
        spinnerCurtains=view.findViewById(R.id.spinnerCurtains);
        btnSave=view.findViewById(R.id.btnSave);
        btnCancel=view.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        fillSpinner(spinnerRooms,R.array.rooms);
        fillSpinner(spinnerWindows,R.array.windows);
        fillSpinner(spinnerCurtains,R.array.curtains);

        spinnerRooms.setOnItemSelectedListener(this);
        spinnerWindows.setOnItemSelectedListener(this);
        spinnerCurtains.setOnItemSelectedListener(this);


        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setCancelable(false);


        return view;
    }



    public void fillSpinner(Spinner spinner , int arrayResource){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                arrayResource, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.spinnerRooms:
                room=adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spinnerWindows:
                window=adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spinnerCurtains:
                curtain=adapterView.getItemAtPosition(i).toString();
                curtainCode=getResources().getStringArray(R.array.curtainsCode)[i];

                switch (curtainCode){
                    case "TP":
                        showCurtainDialog(new NetCurtain() , "Tül Perde");
                        break;
                    case "GP":
                        showCurtainDialog(new SunBlindCurtain() , "Güneşlik Perde");
                        break;
                    case "SP":
                        showCurtainDialog(new RollerCurtain() , "Stor Perde");
                        break;
                    case "ZP":
                        showCurtainDialog(new ZebraCurtain() , "Zebra Perde");
                        break;
                    case "JP":
                        showCurtainDialog(new JalouiseCurtain() , "Jaluzi Perde");
                        break;
                    case "DP":
                        showCurtainDialog(new VerticalCurtain() , "Dikey Perde");
                        break;
                    case "KTP":
                        showCurtainDialog(new CurtainDoubleNet() , "Kruvaze Tül Perde");
                        break;
                    case "BP":
                        showCurtainDialog(new BrizCurtain() , "Briz Perde");
                        break;
                    case "FARBP":
                        showCurtainDialog(new FarbelaCurtain() , "Farbela Perde");
                        break;
                    case "FP":
                        showCurtainDialog(new FonCurtain() , "Fon Perde");
                        break;
                }

                break;
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showCurtainDialog(DialogFragment dialogFragment , String fragmentTag){
        dialogFragment.show(getFragmentManager(),fragmentTag);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btnSave){
            dismiss();
        }else{
            dismiss();
        }


    }
}
