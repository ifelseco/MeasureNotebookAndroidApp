package com.javaman.olcudefteri.orders;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.javaman.olcudefteri.R;

import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderLineUpdateDialog extends DialogFragment implements OnClickListener {

    SharedPreferenceHelper sharedPreferenceHelper;

    @BindView(R.id.textViewProductValue)
    TextView tvProductValue;

    @BindView(R.id.et_product_alias)
    EditText etAlias;

    @BindView(R.id.et_product_pattern)
    EditText etPattern;

    @BindView(R.id.et_product_variant)
    EditText etVariant;

    @BindView(R.id.et_width)
    EditText etWidth;

    @BindView(R.id.et_height)
    EditText etHeight;

    @BindView(R.id.linear_layout_pile)
    LinearLayout linearLayoutPile;

    @BindView(R.id.et_pile)
    EditText etPile;

    @BindView(R.id.linear_layout_direction)
    LinearLayout linearLayoutDirection;

    @BindView(R.id.radio_group_direction)
    RadioGroup radioGroupDirection;

    @BindView(R.id.rb_left)
    RadioButton rbLeft;

    @BindView(R.id.rb_right)
    RadioButton rbRight;

    @BindView(R.id.linear_layout_mechanism)
    LinearLayout linearLayoutMechanism;

    @BindView(R.id.radio_group_mechanism)
    RadioGroup radioGroupMechanism;

    @BindView(R.id.rb_one_piece)
    RadioButton rbOnePiece;

    @BindView(R.id.rb_pieces)
    RadioButton rbPieces;

    @BindView(R.id.rb_multi_mech)
    RadioButton rbMultiMech;

    @BindView(R.id.linear_layout_briz)
    LinearLayout linearLayoutBriz;

    @BindView(R.id.et_alt_width)
    EditText etAltWidth;

    @BindView(R.id.et_alt_height)
    EditText etAltHeight;

    @BindView(R.id.linear_layout_kruvaze)
    LinearLayout linearLayoutKruvaze;

    @BindView(R.id.et_left_width)
    EditText etLeftWidth;

    @BindView(R.id.et_right_width)
    EditText etRightWidth;

    @BindView(R.id.linear_layout_farbela)
    LinearLayout linearLayoutFarbela;

    @BindView(R.id.et_model_name)
    EditText etModelName;

    @BindView(R.id.linear_layout_fon)
    LinearLayout linearLayoutFon;

    @BindView(R.id.radio_group_fon_type)
    RadioGroup radioGroupFonType;

    @BindView(R.id.rb_kruvaze)
    RadioButton rbKruvaze;

    @BindView(R.id.rb_japon)
    RadioButton rbJapon;

    @BindView(R.id.rb_fon)
    RadioButton rbFon;


    @BindView(R.id.radio_group_pile_name)
    RadioGroup radioGroupPileName;

    @BindView(R.id.rb_american)
    RadioButton rbAmerikan;

    @BindView(R.id.rb_kanun)
    RadioButton rbKanun;

    @BindView(R.id.rb_yan)
    RadioButton rbYan;


    @BindView(R.id.rb_other)
    RadioButton rbOther;

    @BindView(R.id.linear_layout_unit_price)
    LinearLayout linearLayoutUnitPrice;

    @BindView(R.id.et_unit_rice)
    EditText etUnitPrice;

    @BindView(R.id.btn_calculate)
    ImageButton btnCalculate;

    @BindView(R.id.tv_line_amount)
    TextView tvLineAmount;

    @BindView(R.id.et_desc)
    EditText etDesc;

    @BindView(R.id.btn_cancel)
    ImageButton btnCancel;

    @BindView(R.id.btn_save)
    ImageButton btnSave;

    private OrderLineDetailModel orderLineDetailModel;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            if(bundle.containsKey(OrderLineFragment.ARG_UPDATE_ORDERLINE)){
                this.orderLineDetailModel=bundle.getParcelable(OrderLineFragment.ARG_UPDATE_ORDERLINE);
            }else{
                dismiss();
            }

        }else{
            dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.order_line_update_layout,null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        String[] productArray = getActivity().getResources().getStringArray(R.array.curtains);
        tvProductValue.setText(productArray[orderLineDetailModel.getProduct().getProductValue()]);
        switch (orderLineDetailModel.getProduct().getProductValue()){
            case 0:
                setNetCurtainLayout(orderLineDetailModel);
                break;
            case 1:
                setSunBlindLayout(orderLineDetailModel);
                break;
            case 2:
                setMechanismCurtainLayout(orderLineDetailModel);
                break;
            case 3:
                setMechanismCurtainLayout(orderLineDetailModel);
                break;
            case 4:
                setJaluziLayout(orderLineDetailModel);
                break;
            case 5:
                //dikey perde update
                break;
            case 6:
                setKruvazeCurtainLayout(orderLineDetailModel);
                break;
            case 7:
                setBrizLayout(orderLineDetailModel);
                break;
            case 8:
                setFarbelaLayout(orderLineDetailModel);
                break;
            case 9:
                setFonLayout(orderLineDetailModel);
                break;
            case 10:
                setMechanismCurtainLayout(orderLineDetailModel);
                break;
                default:
                    break;
        }

    }

    private void setFonLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutFon.setVisibility(View.VISIBLE);
        if(orderLineDetailModel.getFonType()!=3){
            linearLayoutPile.setVisibility(View.VISIBLE);
        }
    }

    private void setFarbelaLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutFarbela.setVisibility(View.VISIBLE);
    }

    private void setBrizLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutPile.setVisibility(View.VISIBLE);
        linearLayoutBriz.setVisibility(View.VISIBLE);
    }

    private void setSunBlindLayout(OrderLineDetailModel orderLineDetailModel) {
    }

    private void setJaluziLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutDirection.setVisibility(View.VISIBLE);
    }

    private void setNetCurtainLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutPile.setVisibility(View.VISIBLE);
    }

    private void setKruvazeCurtainLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutPile.setVisibility(View.VISIBLE);
        linearLayoutKruvaze.setVisibility(View.VISIBLE);
    }

    private void setMechanismCurtainLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutDirection.setVisibility(View.VISIBLE);
        linearLayoutMechanism.setVisibility(View.VISIBLE);
    }


    @Override
    @OnClick({R.id.btn_calculate,R.id.btn_save,R.id.btn_cancel})
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_calculate){

        }else if(id==R.id.btn_cancel){

        }else if(id==R.id.btn_save){

        }
    }
}
