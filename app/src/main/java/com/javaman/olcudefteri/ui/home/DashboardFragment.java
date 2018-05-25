package com.javaman.olcudefteri.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.ui.orders.dialogs.DashboardOrderDialog;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    SharedPreferenceHelper sharedPreferenceHelper;

    @BindView(R.id.card_end_of_day)
    CardView cardEndDay;

    @BindView(R.id.card_next_delivery)
    CardView cardNextDelivery;

    @BindView(R.id.card_next_measure)
    CardView cardNextMeasure;

    public static final String ARG_END_OF_DAY="arg_end_of_day";
    public static final String ARG_NEXT_MEASURE="arg_next_measure";
    public static final String ARG_NEXT_DELIVERY="arg_next_delivery";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dashboard_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    @OnClick({R.id.card_end_of_day,R.id.card_next_delivery,R.id.card_next_measure})
    public void onClick(View v) {
        if(v.getId()==R.id.card_end_of_day){
            showDialog(createAndSetFragment(ARG_END_OF_DAY),ARG_END_OF_DAY);
        }else if(v.getId()==R.id.card_next_delivery){
            showDialog(createAndSetFragment(ARG_NEXT_DELIVERY),ARG_NEXT_DELIVERY);
        }else if(v.getId()==R.id.card_next_measure){
            showDialog(createAndSetFragment(ARG_NEXT_MEASURE),ARG_NEXT_MEASURE);
        }
    }

    private DashboardOrderDialog createAndSetFragment(String arg) {
        DashboardOrderDialog dashboardOrderDialog=new DashboardOrderDialog();
        Bundle bundle=new Bundle();
        bundle.putString(arg,arg);
        dashboardOrderDialog.setArguments(bundle);
        return dashboardOrderDialog;
    }

    public void showDialog(DialogFragment dialogFragment , String fragmentTag){
        dialogFragment.show(getFragmentManager(),fragmentTag);
    }
}
