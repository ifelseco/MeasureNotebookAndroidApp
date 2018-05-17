package com.javaman.olcudefteri.ui.orders;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.view.CalculateView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderLineUpdateDialog extends DialogFragment implements OnClickListener, CalculateView {

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

    @BindView(R.id.btnCalculate)
    Button btnCalculate;

    @BindView(R.id.tv_line_amount)
    TextView tvLineAmount;

    @BindView(R.id.tv_used_material)
    TextView tvUsedMaterial;

    @BindView(R.id.et_desc)
    EditText etDesc;

    @BindView(R.id.et_bead)
    EditText etBead;

    @BindView(R.id.et_skirt)
    EditText etSkirt;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.btn_save)
    Button btnSave;

    @BindView(R.id.progress_bar_calc)
    ProgressBar progressCalc;

    private OrderLineDetailModel orderLineDetailModel;
    private AddOrderLinePresenter mAddOrderLinePresenter;

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
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(OrderLineFragment.ARG_UPDATE_ORDERLINE)) {
                this.orderLineDetailModel = bundle.getParcelable(OrderLineFragment.ARG_UPDATE_ORDERLINE);
            } else {
                dismiss();
            }

        } else {
            dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_line_update_layout, null);
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this, view);
        mAddOrderLinePresenter = new AddOrderLinePresenterImpl(this);
        initView();
        return view;
    }

    private void initView() {
        String[] productArray = getActivity().getResources().getStringArray(R.array.curtains);
        tvProductValue.setText(productArray[orderLineDetailModel.getProduct().getProductValue()]);
        switch (orderLineDetailModel.getProduct().getProductValue()) {
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


    private void setSunBlindLayout(OrderLineDetailModel orderLineDetailModel) {
        setProductPropertyLayout(orderLineDetailModel);
    }

    private void setNetCurtainLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutPile.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        etPile.setText(String.valueOf(orderLineDetailModel.getSizeOfPile()));
    }

    private void setFarbelaLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutFarbela.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        if (!TextUtils.isEmpty(orderLineDetailModel.getPropertyModelName())) {
            etModelName.setText(orderLineDetailModel.getPropertyModelName());
        } else {
            etModelName.setText("");
        }
    }

    private void setJaluziLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutDirection.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        if (orderLineDetailModel.getDirection() == 1) {
            radioGroupDirection.check(R.id.rb_left);
        } else if (orderLineDetailModel.getDirection() == 2) {
            radioGroupDirection.check(R.id.rb_right);
        }
    }


    private void setFonLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutFon.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        if (orderLineDetailModel.getFonType() != 3) {
            linearLayoutPile.setVisibility(View.VISIBLE);
            etPile.setText(String.valueOf(orderLineDetailModel.getSizeOfPile()));
            if (orderLineDetailModel.getDirection() == 1) {
                radioGroupDirection.check(R.id.rb_left);
            } else if (orderLineDetailModel.getDirection() == 2) {
                radioGroupDirection.check(R.id.rb_right);
            }

            if (TextUtils.equals(orderLineDetailModel.getPileName(), "AP")) {
                radioGroupPileName.check(R.id.rb_american);
            } else if (TextUtils.equals(orderLineDetailModel.getPileName(), "KP")) {
                radioGroupPileName.check(R.id.rb_kanun);
            } else if (TextUtils.equals(orderLineDetailModel.getPileName(), "YP")) {
                radioGroupPileName.check(R.id.rb_yan);
            } else if(TextUtils.equals(orderLineDetailModel.getPileName(), "O")){
                radioGroupPileName.check(R.id.rb_other);
            }else{
                radioGroupPileName.check(R.id.rb_other);
            }


        }

        if (orderLineDetailModel.getFonType() == 1) {
            radioGroupFonType.check(R.id.rb_kruvaze);
        } else if (orderLineDetailModel.getFonType() == 2) {
            radioGroupFonType.check(R.id.rb_fon);
        } else if (orderLineDetailModel.getFonType() == 3) {
            radioGroupFonType.check(R.id.rb_japon);
        }


    }


    private void setBrizLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutPile.setVisibility(View.VISIBLE);
        linearLayoutBriz.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        etAltWidth.setText(String.valueOf(orderLineDetailModel.getPropertyAlternativeWidth()));
        etAltHeight.setText(String.valueOf(orderLineDetailModel.getPropertyAlternativeHeight()));
        etPile.setText(String.valueOf(orderLineDetailModel.getSizeOfPile()));

    }


    private void setKruvazeCurtainLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutPile.setVisibility(View.VISIBLE);
        linearLayoutKruvaze.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        etLeftWidth.setText(String.valueOf(orderLineDetailModel.getPropertyLeftWidth()));
        etRightWidth.setText(String.valueOf(orderLineDetailModel.getPropertyRightWidth()));
        etPile.setText(String.valueOf(orderLineDetailModel.getSizeOfPile()));
    }

    private void setMechanismCurtainLayout(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutDirection.setVisibility(View.VISIBLE);
        linearLayoutMechanism.setVisibility(View.VISIBLE);
        setProductPropertyLayout(orderLineDetailModel);
        if (orderLineDetailModel.getDirection() == 1) {
            radioGroupDirection.check(R.id.rb_left);
        } else if (orderLineDetailModel.getDirection() == 2) {
            radioGroupDirection.check(R.id.rb_right);
        }


        if (orderLineDetailModel.getMechanismStatus() == 1) {
            radioGroupMechanism.check(R.id.rb_one_piece);
        } else if (orderLineDetailModel.getMechanismStatus() == 2) {
            radioGroupMechanism.check(R.id.rb_pieces);
        } else if (orderLineDetailModel.getMechanismStatus() == 3) {
            radioGroupMechanism.check(R.id.rb_multi_mech);
        }


        if (!TextUtils.isEmpty(orderLineDetailModel.getSkirtNo())) {
            etSkirt.setText(orderLineDetailModel.getSkirtNo());
        } else {
            etSkirt.setText("");
        }


        if (!TextUtils.isEmpty(orderLineDetailModel.getBeadNo())) {
            etBead.setText(orderLineDetailModel.getBeadNo());
        } else {
            etBead.setText("");
        }

    }

    private void setProductPropertyLayout(OrderLineDetailModel orderLineDetailModel) {
        if (!TextUtils.isEmpty(orderLineDetailModel.getProduct().getVariantCode())) {
            etVariant.setText(orderLineDetailModel.getProduct().getVariantCode());
        } else {
            etVariant.setText("");
        }

        if (!TextUtils.isEmpty(orderLineDetailModel.getProduct().getAliasName())) {
            etAlias.setText(orderLineDetailModel.getProduct().getAliasName());
        } else {
            etAlias.setText("");
        }

        if (!TextUtils.isEmpty(orderLineDetailModel.getProduct().getPatternCode())) {
            etPattern.setText(orderLineDetailModel.getProduct().getPatternCode());
        } else {
            etPattern.setText("");
        }

        if (!TextUtils.isEmpty(orderLineDetailModel.getLineDescription())) {
            etDesc.setText(orderLineDetailModel.getLineDescription());
        } else {
            etDesc.setText("");
        }

        etWidth.setText(String.valueOf(orderLineDetailModel.getPropertyWidth()));
        etHeight.setText(String.valueOf(orderLineDetailModel.getPropertyHeight()));
        etUnitPrice.setText(String.valueOf(orderLineDetailModel.getUnitPrice()));
        tvLineAmount.setText(String.valueOf(orderLineDetailModel.getLineAmount())+" TL");
        List<Integer> mechanismValue = Arrays.asList(2, 3, 4, 5, 10);
        if (mechanismValue.contains(orderLineDetailModel.getProduct().getProductValue())) {
            tvUsedMaterial.setText(String.format("%.2f", orderLineDetailModel.getUsedMaterial()) + " m2");
        } else {
            tvUsedMaterial.setText(String.format("%.2f", orderLineDetailModel.getUsedMaterial()) + " m");
        }
    }


    @Override
    @OnClick({R.id.btnCalculate, R.id.btn_save, R.id.btn_cancel})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnCalculate) {
            orderLineDetailModel=setOrderLine(true);
            AddOrderLineDetailListModel addOrderLineDetailListModel=new AddOrderLineDetailListModel();
            addOrderLineDetailListModel.setOrderLineDetailModelList(new ArrayList<>());
            addOrderLineDetailListModel.getOrderLineDetailModelList().add(orderLineDetailModel);
            calculateOrderLine(addOrderLineDetailListModel);
        } else if (id == R.id.btn_save) {
            orderLineDetailModel=setOrderLine(false);
            EventBus.getDefault().post(orderLineDetailModel);
            dismiss();
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }

    private OrderLineDetailModel setOrderLine(boolean isCalculate) {




        switch (orderLineDetailModel.getProduct().getProductValue()) {
            case 0:
                setCommonProperty(orderLineDetailModel,isCalculate,false);
                setNetCurtProprty(orderLineDetailModel,isCalculate);
                break;
            case 1:
                setCommonProperty(orderLineDetailModel,isCalculate,false);
                break;
            case 2:
                setCommonProperty(orderLineDetailModel,isCalculate,true);
                setMaechanismProperty(orderLineDetailModel,isCalculate);

                break;
            case 3:
                setCommonProperty(orderLineDetailModel,isCalculate,true);
                setMaechanismProperty(orderLineDetailModel,isCalculate);
                break;
            case 4:
                setCommonProperty(orderLineDetailModel,isCalculate,true);
                setJaluziProperty(orderLineDetailModel,isCalculate);
                break;
            case 5:
                setCommonProperty(orderLineDetailModel,isCalculate,true);
                //dikey perde update
                break;
            case 6:
                setCommonProperty(orderLineDetailModel,isCalculate,false);
                setKruvazeProprty(orderLineDetailModel,isCalculate);
                break;
            case 7:
                setCommonProperty(orderLineDetailModel,isCalculate,false);
                setBrizProperty(orderLineDetailModel,isCalculate);
                break;
            case 8:
                setCommonProperty(orderLineDetailModel,isCalculate,false);
                setFarbelaProprty(orderLineDetailModel,isCalculate);
                break;
            case 9:
                setCommonProperty(orderLineDetailModel,isCalculate,false);
                setFonProperty(orderLineDetailModel,isCalculate);
                break;
            case 10:
                setCommonProperty(orderLineDetailModel,isCalculate,true);
                setMaechanismProperty(orderLineDetailModel,isCalculate);
                break;
        }

        return orderLineDetailModel;
    }

    private void setFonProperty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {

        int fonType = 0;

        if (radioGroupDirection.getCheckedRadioButtonId() != -1) {
            int checkedId = radioGroupDirection.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_left) {
                int direction=1;
                orderLineDetailModel.setDirection(direction);
            } else if (checkedId == R.id.rb_right) {
                int direction=2;
                orderLineDetailModel.setDirection(direction);

            } else {
                int direction=0;
                orderLineDetailModel.setDirection(direction);

            }

        }


        if (radioGroupFonType.getCheckedRadioButtonId() != -1) {
            int checkedId = radioGroupFonType.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_kruvaze) {
                fonType=1;
                orderLineDetailModel.setFonType(fonType);
            } else if (checkedId == R.id.rb_fon) {
                fonType=2;
                orderLineDetailModel.setFonType(fonType);

            } else if(checkedId==R.id.rb_japon){
                fonType=3;
                orderLineDetailModel.setFonType(fonType);

            }else{
                fonType=0;
                orderLineDetailModel.setFonType(fonType);

            }

        }


        if (radioGroupPileName.getCheckedRadioButtonId() != -1) {
            int checkedId = radioGroupPileName.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_american) {
                String pileName="AP";
                orderLineDetailModel.setPileName(pileName);
            } else if (checkedId == R.id.rb_kanun) {
                String pileName="KP";
                orderLineDetailModel.setPileName(pileName);

            } else if(checkedId==R.id.rb_yan){
                String pileName="YP";
                orderLineDetailModel.setPileName(pileName);

            }else if(checkedId==R.id.rb_other){
                String pileName="O";
                orderLineDetailModel.setPileName(pileName);

            }else{
                String pileName="O";
                orderLineDetailModel.setPileName(pileName);
            }

        }

        if(fonType==2 || fonType==1){
            setPileProprty(orderLineDetailModel,isCalculate);
        }


    }

    private void setNetCurtProprty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {
        setPileProprty(orderLineDetailModel,isCalculate);
    }

    private void setPileProprty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {
        if(!TextUtils.isEmpty(etPile.getText().toString())){
            double pile = Double.parseDouble(etPile.getText().toString());
            orderLineDetailModel.setSizeOfPile(pile);
        }else if(isCalculate){
            etPile.setError("Pile Sıklığı Giriniz");
        }
    }

    private void setFarbelaProprty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {
        if(!TextUtils.isEmpty(etModelName.getText().toString())){
            orderLineDetailModel.setPropertyModelName(etModelName.getText().toString());
        }
    }

    private void setKruvazeProprty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {
        setPileProprty(orderLineDetailModel,isCalculate);

        if(!TextUtils.isEmpty(etLeftWidth.getText().toString())){
            double leftWidth = Double.parseDouble(etLeftWidth.getText().toString());
            orderLineDetailModel.setPropertyLeftWidth(leftWidth);
        }

        if(!TextUtils.isEmpty(etRightWidth.getText().toString())){
            double rightWidth = Double.parseDouble(etRightWidth.getText().toString());
            orderLineDetailModel.setPropertyRightWidth(rightWidth);
        }
    }

    private void setBrizProperty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {
        setPileProprty(orderLineDetailModel,isCalculate);

        if(!TextUtils.isEmpty(etAltWidth.getText().toString())){
            double altWidth = Double.parseDouble(etAltWidth.getText().toString());
            orderLineDetailModel.setPropertyAlternativeWidth(altWidth);
        }

        if(!TextUtils.isEmpty(etAltHeight.getText().toString())){
            double altHeight = Double.parseDouble(etAltHeight.getText().toString());
            orderLineDetailModel.setPropertyAlternativeHeight(altHeight);
        }
    }

    private void setJaluziProperty(OrderLineDetailModel orderLineDetailModel, boolean isCalculate) {
        if (radioGroupDirection.getCheckedRadioButtonId() != -1) {
            int checkedId = radioGroupDirection.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_left) {
                int direction=1;
                orderLineDetailModel.setDirection(direction);
            } else if (checkedId == R.id.rb_right) {
                int direction=2;
                orderLineDetailModel.setDirection(direction);

            } else {
                int direction=0;
                orderLineDetailModel.setDirection(direction);

            }

        }


    }

    private void setMaechanismProperty(OrderLineDetailModel orderLineDetailModel , boolean isCalculate) {
        if (radioGroupDirection.getCheckedRadioButtonId() != -1) {
            int checkedId = radioGroupDirection.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_left) {
                int direction=1;
                orderLineDetailModel.setDirection(direction);
            } else if (checkedId == R.id.rb_right) {
                int direction=2;
                orderLineDetailModel.setDirection(direction);

            } else {
                int direction=0;
                orderLineDetailModel.setDirection(direction);

            }

        }

        if (radioGroupMechanism.getCheckedRadioButtonId() != -1) {
            int checkedId = radioGroupMechanism.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_one_piece) {
                int mechanismStatus=1;
                orderLineDetailModel.setMechanismStatus(mechanismStatus);
            } else if (checkedId == R.id.rb_pieces) {
                int mechanismStatus=2;
                orderLineDetailModel.setMechanismStatus(mechanismStatus);

            } else if(checkedId == R.id.rb_multi_mech){
                int mechanismStatus=3;
                orderLineDetailModel.setMechanismStatus(mechanismStatus);

            }else{
                int mechanismStatus=0;
                orderLineDetailModel.setMechanismStatus(mechanismStatus);
            }

        }


        if(!TextUtils.isEmpty(etBead.getText().toString())){
            orderLineDetailModel.setBeadNo(etBead.getText().toString());
        }


        if(!TextUtils.isEmpty(etSkirt.getText().toString())){
            orderLineDetailModel.setSkirtNo(etSkirt.getText().toString());
        }


        if(!TextUtils.isEmpty(etBead.getText().toString())){
            orderLineDetailModel.setBeadNo(etBead.getText().toString());
        }

    }

    private void setCommonProperty(OrderLineDetailModel orderLineDetailModel , boolean isCalculate,boolean isMechanism) {
        if(!TextUtils.isEmpty(etAlias.getText().toString())){
            orderLineDetailModel.getProduct().setAliasName(etAlias.getText().toString());
        }

        if(!TextUtils.isEmpty(etVariant.getText().toString())){
            orderLineDetailModel.getProduct().setVariantCode(etVariant.getText().toString());
        }

        if(!TextUtils.isEmpty(etPattern.getText().toString())){
            orderLineDetailModel.getProduct().setPatternCode(etPattern.getText().toString());
        }

        if(!TextUtils.isEmpty(etDesc.getText().toString())){
            orderLineDetailModel.setLineDescription(etDesc.getText().toString());
        }

        if(!TextUtils.isEmpty(etWidth.getText().toString())){
            double width = Double.parseDouble(etWidth.getText().toString());
            orderLineDetailModel.setPropertyWidth(width);
        }else if(isCalculate){
            etWidth.setError("En Giriniz");
        }

        if(!TextUtils.isEmpty(etHeight.getText().toString())){
            double height = Double.parseDouble(etHeight.getText().toString());
            orderLineDetailModel.setPropertyHeight(height);
        }else if(isCalculate && isMechanism){
            etHeight.setError("Boy Giriniz");
        }

        if(!TextUtils.isEmpty(etUnitPrice.getText().toString())){
            double unitPrice=Double.parseDouble(etUnitPrice.getText().toString());
            orderLineDetailModel.setUnitPrice(unitPrice);
        }else if(isCalculate){
            etUnitPrice.setError("Birim Fiyat Giriniz");
        }


    }


    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId = getSessionIdFromPref();
        mAddOrderLinePresenter.calculateOrderLine(orderLineDetailListModel, sessionId);
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressCalc.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressCalc.setVisibility(View.GONE);
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken = sharedPreferenceHelper.getStringPreference("sessionId", null);
        return xAuthToken;
    }

    @Override
    public void updateAmount(CalculationResponse calculationResponse) {
        double totalM = calculationResponse.getUsedMaterial();
        double totalPrice = calculationResponse.getTotalAmount();
        tvLineAmount.setText(String.format("%.2f", totalPrice) + " TL");
        List<Integer> mechanismValue = Arrays.asList(2, 3, 4, 5, 10);
        if (mechanismValue.contains(orderLineDetailModel.getProduct().getProductValue())) {
            tvUsedMaterial.setText(String.format("%.2f", totalM) + " m2");
        } else {
            tvUsedMaterial.setText(String.format("%.2f", totalM) + " m");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }


}
