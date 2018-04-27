package com.javaman.olcudefteri.tailor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class TailorOrderLineAdapter extends RecyclerView.Adapter<TailorOrderLineAdapter.TailorOrderLineViewHolder> {

    List<OrderLineDetailModel> mOrderLines;
    TailorOrderDetailActivity tailorOrderDetailActivity;
    LayoutInflater inflater;

    public TailorOrderLineAdapter(Context context, List<OrderLineDetailModel> orderLineDetailList) {
        mOrderLines = orderLineDetailList;
        tailorOrderDetailActivity = (TailorOrderDetailActivity) context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TailorOrderLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TailorOrderLineViewHolder holder;
        View view = inflater.inflate(R.layout.tailor_order_line, parent, false);
        holder = new TailorOrderLineViewHolder(view, tailorOrderDetailActivity);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TailorOrderLineViewHolder holder, int position) {
        OrderLineDetailModel orderLine = mOrderLines.get(position);
        holder.itemView.setTag(orderLine);
        holder.bind(orderLine, position);
    }

    @Override
    public int getItemCount() {
        if (mOrderLines != null) {
            return mOrderLines.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class TailorOrderLineViewHolder extends RecyclerView.ViewHolder {

        TailorOrderDetailActivity tailorOrderDetailActivity;

        @BindView(R.id.linear_layout_detail)
        LinearLayout linearLayoutDetail;

        @BindView(R.id.linear_layout_briz)
        LinearLayout linearLayoutBriz;

        @BindView(R.id.linear_layout_pile)
        LinearLayout linearLayoutPile;

        @BindView(R.id.linear_layout_fon)
        LinearLayout linearLayoutFon;

        @BindView(R.id.linear_layout_kruvaze)
        LinearLayout linearLayoutKruvaze;

        @BindView(R.id.linear_layout_farbela)
        LinearLayout linearLayoutFarbela;

        @BindView(R.id.tv_location)
        TextView textViewLocation;

        @BindView(R.id.tv_product_value)
        TextView textViewProductValue;

        @BindView(R.id.tv_product_alias)
        TextView textViewProductAlias;

        @BindView(R.id.tv_product_pattern)
        TextView textViewProductPatter;

        @BindView(R.id.tv_product_variant)
        TextView textViewProductVariant;

        @BindView(R.id.tv_product_width)
        TextView textViewProductWidth;

        @BindView(R.id.tv_product_height)
        TextView textViewProductHeight;

        @BindView(R.id.tv_pile)
        TextView textViewPile;

        @BindView(R.id.tv_fon_type)
        TextView textViewFonType;

        @BindView(R.id.tv_fon_pile_name)
        TextView textViewFonPileName;

        @BindView(R.id.tv_fon_direction)
        TextView textViewFonDirection;

        @BindView(R.id.tv_kruvaze_left_width)
        TextView textViewKruvazeLeftWidth;

        @BindView(R.id.tv_kruvaze_right_width)
        TextView textViewKruvazeRightWidth;

        @BindView(R.id.tv_farbela_model_name)
        TextView textViewFarbelaModelName;

        @BindView(R.id.tv_briz_alternatif_width)
        TextView textViewAltWidth;


        @BindView(R.id.tv_briz_alternatif_height)
        TextView textViewAltHeight;

        @BindView(R.id.tv_used_material)
        TextView textViewUsed;

        @BindView(R.id.tv_desc)
        TextView textViewDesc;


        public TailorOrderLineViewHolder(View itemView, TailorOrderDetailActivity tailorOrderDetailActivity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.tailorOrderDetailActivity = tailorOrderDetailActivity;

        }

        public void bind(OrderLineDetailModel orderLine, int position) {
            setDetailLayout(orderLine);
        }

        public void setDetailLayout(OrderLineDetailModel orderLine) {
            int productValue = orderLine.getProduct().getProductValue();
            String[] productArray = tailorOrderDetailActivity.getResources().getStringArray(R.array.curtains);
            String location = "";
            if (!TextUtils.isEmpty(orderLine.getLocationName())) {
                location = orderLine.getLocationName();
            }

            if (!TextUtils.isEmpty(orderLine.getLocationType())) {
                location += " " + orderLine.getLocationType();
            }

            if (!TextUtils.isEmpty(location)) {
                textViewLocation.setText(location);
            } else {
                textViewLocation.setText("Mekan seçilmedi");
            }

            if (!TextUtils.isEmpty(orderLine.getProduct().getAliasName())) {
                textViewProductAlias.setText("İsim: " + orderLine.getProduct().getAliasName());
            } else {
                textViewProductAlias.setText("İsim: seçilmedi");
            }

            if (!TextUtils.isEmpty(orderLine.getProduct().getPatternCode())) {
                textViewProductPatter.setText("Desen: " + orderLine.getProduct().getPatternCode());
            } else {
                textViewProductPatter.setText("Desen: seçilmedi");
            }

            if (!TextUtils.isEmpty(orderLine.getProduct().getVariantCode())) {
                textViewProductVariant.setText("Varyant: " + orderLine.getProduct().getVariantCode());
            } else {
                textViewProductVariant.setText("Varyant: seçilmedi");
            }

            if(!TextUtils.isEmpty(orderLine.getLineDescription())){
                textViewDesc.setText(orderLine.getLineDescription());
            }

            textViewProductWidth.setText("En: " + orderLine.getPropertyWidth());
            textViewProductHeight.setText("Boy: " + orderLine.getPropertyHeight());
            textViewUsed.setText(""+orderLine.getUsedMaterial()+" metre");

            switch (productValue) {
                case 0:
                    textViewProductValue.setText(productArray[0]);
                    linearLayoutDetail.setVisibility(View.VISIBLE);
                    linearLayoutPile.setVisibility(View.VISIBLE);
                    textViewPile.setText("" + orderLine.getSizeOfPile());
                    break;
                case 1:
                    textViewProductValue.setText(productArray[1]);
                    break;
                case 6:
                    linearLayoutDetail.setVisibility(View.VISIBLE);
                    textViewProductValue.setText(productArray[6]);
                    linearLayoutPile.setVisibility(View.VISIBLE);
                    linearLayoutKruvaze.setVisibility(View.VISIBLE);
                    textViewPile.setText("" + orderLine.getSizeOfPile());
                    textViewKruvazeLeftWidth.setText("Sol En: " + orderLine.getPropertyLeftWidth());
                    textViewKruvazeRightWidth.setText("Sağ En: " + orderLine.getPropertyRightWidth());
                    break;
                case 7:
                    linearLayoutDetail.setVisibility(View.VISIBLE);
                    textViewProductValue.setText(productArray[7]);
                    linearLayoutPile.setVisibility(View.VISIBLE);
                    linearLayoutBriz.setVisibility(View.VISIBLE);
                    textViewPile.setText("" + orderLine.getSizeOfPile());
                    textViewAltWidth.setText("Farbela En: " + orderLine.getPropertyAlternativeWidth());
                    textViewAltHeight.setText("Farbela Boy: " + orderLine.getPropertyAlternativeHeight());
                    break;
                case 8:
                    linearLayoutDetail.setVisibility(View.VISIBLE);
                    linearLayoutFarbela.setVisibility(View.VISIBLE);
                    textViewProductValue.setText(productArray[8]);
                    if (!TextUtils.isEmpty(orderLine.getPropertyModelName())) {
                        textViewFarbelaModelName.setText("Model Adı: " + orderLine.getPropertyModelName());
                    } else {
                        textViewFarbelaModelName.setText("Model Adı: Seçilmedi");
                    }
                    break;
                case 9:
                    linearLayoutDetail.setVisibility(View.VISIBLE);
                    textViewProductValue.setText(productArray[9]);
                    linearLayoutPile.setVisibility(View.VISIBLE);
                    linearLayoutFon.setVisibility(View.VISIBLE);

                    if (orderLine.getFonType() == 1) {
                        textViewFonType.setText("Kruvaze Kanat");
                        if (!TextUtils.isEmpty(orderLine.getPileName())) {
                            textViewFonPileName.setText("Pile Türü: " + orderLine.getPileName());
                        } else {
                            textViewFonPileName.setText("Pile Türü: Seçilmedi");
                        }

                        textViewPile.setText("" + orderLine.getSizeOfPile());


                        if (orderLine.getDirection() == 1) {
                            textViewFonDirection.setText("Yön: Sol");
                        } else if (orderLine.getDirection() == 2) {
                            textViewFonDirection.setText("Yön: Sağ");
                        }


                    } else if (orderLine.getFonType() == 2) {
                        textViewFonType.setText("Fon Kanat");
                        if (!TextUtils.isEmpty(orderLine.getPileName())) {
                            textViewFonPileName.setText("Pile Türü: " + orderLine.getPileName());
                        } else {
                            textViewFonPileName.setText("Pile Türü: Seçilmedi");
                        }
                        textViewPile.setText("" + orderLine.getSizeOfPile());

                        if (orderLine.getDirection() == 1) {
                            textViewFonDirection.setText("Yön: Sol");
                        } else if (orderLine.getDirection() == 2) {
                            textViewFonDirection.setText("Yön: Sağ");
                        }

                    } else if (orderLine.getFonType() == 2) {
                        textViewFonType.setText("Jaapon Panel");
                    }

                    break;
                default:
                    break;

            }

        }
    }
}
