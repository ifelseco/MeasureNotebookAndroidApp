package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 08.03.2018.
 */

public class OrderLineFragment extends Fragment implements View.OnClickListener {

    public OrderLineFragment() {
    }

    @BindView(R.id.recycle_order_line)
    RecyclerView recyclerViewOrderLine;

    @BindView(R.id.swipe_refres_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    TextView tvLocationName, tvLocationType, tvProductValue,
            tvProductALias, tvProductPattern, tvProductVariant,
            tvProductWidth, tvProductHeight, tvUsedMaterial,
            tvUnitPrice, tvLineAmount, tvNetPile, tvStorDirection,
            tvStorSkirtNo, tvStorBeadNo, tvStorMechanism,
            tvJaluziDirection, tvVerticalDirection, tvKruvazePile,
            tvKruvazeLeftWidth, tvKruvazeRightWidth, tvBrizPile,
            tvBrizAltWidth, tvBrizAltHeight, tvFarbelaModel, tvFonPile,
            tvFonDirection, tvFonType, tvFonPileName, tvOrderLineDesc;

    View viewLine;
    LinearLayout linearLayoutDetail, linearLayoutDesc;
    ImageButton imageButtonClose;
    View detailView;

    private List<OrderLineDetailModel> orderLines = new ArrayList<>();

    RecyclerView.Adapter adapter;

    BottomSheetDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(OrderDetailActivity.ARG_ORDER_LINES)) {
            orderLines = getArguments().getParcelableArrayList(OrderDetailActivity.ARG_ORDER_LINES);
            adapter = new OrderLineAdapter(orderLines, getActivity(), OrderLineFragment.this);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_line_fragment, container, false);
        ButterKnife.bind(this, rootView);
        recyclerViewOrderLine.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOrderLine.setLayoutManager(layoutManager);
        init_modal_bottomsheet();

        return rootView;
    }


    public void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottom_sheet, null);
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        linearLayoutDesc = modalbottomsheet.findViewById(R.id.linear_layout_dec);
        tvOrderLineDesc = modalbottomsheet.findViewById(R.id.tv_line_description);
        initDialogView(modalbottomsheet);
    }

    public void openBottomSheet(OrderLineDetailModel orderLineDetailModel) {
        setDialogView(orderLineDetailModel);
        dialog.show();
    }

    private void initDialogView(View modalbottomsheet) {

        linearLayoutDetail = modalbottomsheet.findViewById(R.id.linear_layout_order_line_detail);
        viewLine = modalbottomsheet.findViewById(R.id.view_line_detail);
        tvLocationName = modalbottomsheet.findViewById(R.id.tv_location_name);
        tvLocationType = modalbottomsheet.findViewById(R.id.tv_location_type);
        tvProductValue = modalbottomsheet.findViewById(R.id.tv_product_value);
        tvProductALias = modalbottomsheet.findViewById(R.id.tv_product_alias);
        tvProductPattern = modalbottomsheet.findViewById(R.id.tv_product_pattern);
        tvProductVariant = modalbottomsheet.findViewById(R.id.tv_product_variant);
        tvProductWidth = modalbottomsheet.findViewById(R.id.tv_product_width);
        tvProductHeight = modalbottomsheet.findViewById(R.id.tv_product_height);
        tvUsedMaterial = modalbottomsheet.findViewById(R.id.tv_used_material);
        tvUnitPrice = modalbottomsheet.findViewById(R.id.tv_unit_price);
        tvLineAmount = modalbottomsheet.findViewById(R.id.tv_line_amount);
        imageButtonClose = modalbottomsheet.findViewById(R.id.image_button_close);
        imageButtonClose.setOnClickListener(this);

    }

    private void setDialogView(OrderLineDetailModel orderLineDetailModel) {
        linearLayoutDetail.setVisibility(View.VISIBLE);
        viewLine.setVisibility(View.VISIBLE);
        tvLocationName.setText(orderLineDetailModel.getLocationName());
        tvLocationType.setText(orderLineDetailModel.getLocationType());
        tvProductALias.setText("İsim :"+orderLineDetailModel.getProduct().getAliasName());
        tvProductPattern.setText("Desen :"+orderLineDetailModel.getProduct().getPatternCode());
        tvProductVariant.setText("Variant :"+orderLineDetailModel.getProduct().getVariantCode());
        tvProductWidth.setText("En :"+orderLineDetailModel.getPropertyWidth());
        tvProductHeight.setText("Boy :"+orderLineDetailModel.getPropertyHeight());
        tvOrderLineDesc.setText(orderLineDetailModel.getLineDescription());



        Currency currency=Currency.getInstance(new Locale("tr","TR"));


        tvUnitPrice.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderLineDetailModel.getUnitPrice()));
        tvLineAmount.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderLineDetailModel.getLineAmount()));


        String[] productArray = getActivity().getResources().getStringArray(R.array.curtains);
        switch (orderLineDetailModel.getProduct().getProductValue()) {
            case 0:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[0]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");
                detailView = getLayoutInflater().inflate(R.layout.net_curtain_detail_layout, linearLayoutDetail, false);
                tvNetPile = detailView.findViewById(R.id.tv_net_pile);
                tvNetPile.setText("" + orderLineDetailModel.getSizeOfPile());
                linearLayoutDetail.addView(detailView);
                break;
            case 1:
                linearLayoutDetail.removeAllViews();
                linearLayoutDetail.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                tvProductValue.setText(productArray[1]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");
                break;
            case 2:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[2]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m2");
                detailView = getLayoutInflater().inflate(R.layout.storzebra_detail_layout, linearLayoutDetail, false);
                tvStorDirection = detailView.findViewById(R.id.tv_store_direction);
                tvStorSkirtNo = detailView.findViewById(R.id.tv_store_skirt_no);
                tvStorBeadNo = detailView.findViewById(R.id.tv_store_bead_no);
                tvStorMechanism = detailView.findViewById(R.id.tv_store_mechanism);

                if (orderLineDetailModel.getMechanismStatus() == 1) {
                    tvStorMechanism.setText("Normal");
                } else if (orderLineDetailModel.getMechanismStatus() == 2) {
                    tvStorMechanism.setText("Parçalı");
                } else if (orderLineDetailModel.getMechanismStatus() == 3) {
                    tvStorMechanism.setText("Çoklu Mekanizmalı");
                }

                if (orderLineDetailModel.getDirection() == 1) {
                    tvStorDirection.setText("Zincir Solda");
                } else if (orderLineDetailModel.getDirection() == 2) {
                    tvStorDirection.setText("Zincir Sağda");
                }

                if (orderLineDetailModel.getBeadNo().isEmpty()) {
                    tvStorBeadNo.setText("Boncuk yok");
                } else {
                    tvStorBeadNo.setText(orderLineDetailModel.getBeadNo());
                }

                if (orderLineDetailModel.getSkirtNo().isEmpty()) {
                    tvStorSkirtNo.setText("Etek Dilimi yok");
                } else {
                    tvStorSkirtNo.setText(orderLineDetailModel.getBeadNo());
                }

                linearLayoutDetail.addView(detailView);

                break;

            case 3:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[3]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m2");
                detailView = getLayoutInflater().inflate(R.layout.storzebra_detail_layout, linearLayoutDetail, false);
                tvStorDirection = detailView.findViewById(R.id.tv_store_direction);
                tvStorSkirtNo = detailView.findViewById(R.id.tv_store_skirt_no);
                tvStorBeadNo = detailView.findViewById(R.id.tv_store_bead_no);
                tvStorMechanism = detailView.findViewById(R.id.tv_store_mechanism);

                if (orderLineDetailModel.getMechanismStatus() == 1) {
                    tvStorMechanism.setText("Normal");
                } else if (orderLineDetailModel.getMechanismStatus() == 2) {
                    tvStorMechanism.setText("Parçalı");
                } else if (orderLineDetailModel.getMechanismStatus() == 3) {
                    tvStorMechanism.setText("Çoklu Mekanizmalı");
                }

                if (orderLineDetailModel.getDirection() == 1) {
                    tvStorDirection.setText("Zincir Solda");
                } else if (orderLineDetailModel.getDirection() == 2) {
                    tvStorDirection.setText("Zincir Sağda");
                }

                if (orderLineDetailModel.getBeadNo().isEmpty()) {
                    tvStorBeadNo.setText("Boncuk yok");
                } else {
                    tvStorBeadNo.setText(orderLineDetailModel.getBeadNo());
                }

                if (orderLineDetailModel.getSkirtNo().isEmpty()) {
                    tvStorSkirtNo.setText("Etek Dilimi yok");
                } else {
                    tvStorSkirtNo.setText(orderLineDetailModel.getBeadNo());
                }
                linearLayoutDetail.addView(detailView);
                break;
            case 4:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[4]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m2");
                detailView = getLayoutInflater().inflate(R.layout.jaluzi_detail_layout, linearLayoutDetail, false);
                tvJaluziDirection = detailView.findViewById(R.id.tv_jaluzi_direction);


                if (orderLineDetailModel.getDirection() == 1) {
                    tvJaluziDirection.setText("Zincir Solda");
                } else if (orderLineDetailModel.getDirection() == 2) {
                    tvJaluziDirection.setText("Zincir Sağda");
                }
                linearLayoutDetail.addView(detailView);
                break;
            case 5:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[5]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m2");
                detailView = getLayoutInflater().inflate(R.layout.vertical_detail, linearLayoutDetail, false);
                tvVerticalDirection = detailView.findViewById(R.id.tv_vertical_direction);

                if (orderLineDetailModel.getDirection() == 1) {
                    tvVerticalDirection.setText("Zincir Solda");
                } else if (orderLineDetailModel.getDirection() == 2) {
                    tvVerticalDirection.setText("Zincir Sağda");
                }
                linearLayoutDetail.addView(detailView);
                break;
            case 6:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[6]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");
                detailView = getLayoutInflater().inflate(R.layout.kruvaze_detail_layout,linearLayoutDetail,false);
                tvKruvazePile = detailView.findViewById(R.id.tv_kruvaze_pile);
                tvKruvazeLeftWidth = detailView.findViewById(R.id.tv_kruvaze_left_width);
                tvKruvazeRightWidth = detailView.findViewById(R.id.tv_kruvaze_right_width);

                tvKruvazePile.setText("" + orderLineDetailModel.getSizeOfPile());
                tvKruvazeLeftWidth.setText("" + orderLineDetailModel.getPropertyLeftWidth() + " m");
                tvKruvazeRightWidth.setText("" + orderLineDetailModel.getPropertyRightWidth() + " m");
                linearLayoutDetail.addView(detailView);
                break;
            case 7:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[7]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");
                detailView = getLayoutInflater().inflate(R.layout.briz_detail_layout,linearLayoutDetail,false);
                tvBrizPile = detailView.findViewById(R.id.tv_briz_pile);
                tvBrizAltWidth = detailView.findViewById(R.id.tv_briz_alternatif_width);
                tvBrizAltHeight = detailView.findViewById(R.id.tv_briz_alternatif_height);

                tvBrizPile.setText("" + orderLineDetailModel.getSizeOfPile());
                tvBrizAltHeight.setText("Farbela En:" + orderLineDetailModel.getPropertyAlternativeHeight() + " m");
                tvBrizAltWidth.setText("Farbela Boy" + orderLineDetailModel.getPropertyAlternativeWidth() + " m");
                linearLayoutDetail.addView(detailView);
                break;
            case 8:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[8]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");
                detailView = getLayoutInflater().inflate(R.layout.farbela_detail_layout,linearLayoutDetail,false);
                tvFarbelaModel = detailView.findViewById(R.id.tv_farbela_model_name);
                tvFarbelaModel.setText(orderLineDetailModel.getPropertyModelName());
                linearLayoutDetail.addView(detailView);
                break;
            case 9:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[9]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");
                detailView = getLayoutInflater().inflate(R.layout.fon_detail_layout,linearLayoutDetail,false);

                tvFonPile = detailView.findViewById(R.id.tv_fon_pile);
                tvFonPileName = detailView.findViewById(R.id.tv_fon_pile_name);
                tvFonType = detailView.findViewById(R.id.tv_fon_type);
                tvFonDirection = detailView.findViewById(R.id.tv_fon_direction);

                if (orderLineDetailModel.getFonType() == 1) {
                    tvFonType.setText("Kruvaze Kanat");
                    tvFonPileName.setText(orderLineDetailModel.getPileName());
                    tvFonPile.setText("" + orderLineDetailModel.getSizeOfPile());

                    if (orderLineDetailModel.getDirection() == 1) {
                        tvVerticalDirection.setText("Yön : Sol");
                    } else if (orderLineDetailModel.getDirection() == 2) {
                        tvVerticalDirection.setText("Yön : Sağ");
                    }


                } else if (orderLineDetailModel.getFonType() == 2) {
                    tvFonType.setText("Fon Kanat");
                    tvFonPileName.setText(orderLineDetailModel.getPileName());
                    tvFonPile.setText("" + orderLineDetailModel.getSizeOfPile());

                    if (orderLineDetailModel.getDirection() == 1) {
                        tvVerticalDirection.setText("Yön : Sol");
                    } else if (orderLineDetailModel.getDirection() == 2) {
                        tvVerticalDirection.setText("Yön : Sağ");
                    }
                } else if (orderLineDetailModel.getFonType() == 2) {
                    tvFonType.setText("Jaapon Panel");
                }
                linearLayoutDetail.addView(detailView);

                break;
            case 10:
                linearLayoutDetail.removeAllViews();
                tvProductValue.setText(productArray[10]);
                tvUsedMaterial.setText(orderLineDetailModel.getUsedMaterial() + " m");

                detailView = getLayoutInflater().inflate(R.layout.storzebra_detail_layout,linearLayoutDetail,false);
                tvStorDirection = detailView.findViewById(R.id.tv_store_direction);
                tvStorSkirtNo = detailView.findViewById(R.id.tv_store_skirt_no);
                tvStorBeadNo = detailView.findViewById(R.id.tv_store_bead_no);
                tvStorMechanism = detailView.findViewById(R.id.tv_store_mechanism);

                if (orderLineDetailModel.getMechanismStatus() == 1) {
                    tvStorMechanism.setText("Normal");
                } else if (orderLineDetailModel.getMechanismStatus() == 2) {
                    tvStorMechanism.setText("Parçalı");
                } else if (orderLineDetailModel.getMechanismStatus() == 3) {
                    tvStorMechanism.setText("Çoklu Mekanizmalı");
                }

                if (orderLineDetailModel.getDirection() == 1) {
                    tvStorDirection.setText("Zincir Solda");
                } else if (orderLineDetailModel.getDirection() == 2) {
                    tvStorDirection.setText("Zincir Sağda");
                }

                if (orderLineDetailModel.getBeadNo().isEmpty()) {
                    tvStorBeadNo.setText("Boncuk yok");
                } else {
                    tvStorBeadNo.setText(orderLineDetailModel.getBeadNo());
                }

                if (orderLineDetailModel.getSkirtNo().isEmpty()) {
                    tvStorSkirtNo.setText("Etek Dilimi yok");
                } else {
                    tvStorSkirtNo.setText(orderLineDetailModel.getBeadNo());
                }
                linearLayoutDetail.addView(detailView);
                break;

        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_button_close) {
            dialog.hide();
        }
    }
}
