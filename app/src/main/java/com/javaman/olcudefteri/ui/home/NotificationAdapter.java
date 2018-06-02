package com.javaman.olcudefteri.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.OrderDetailModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private List<NotificationDetailModel> mNotifications=new ArrayList<>();
    Context mContext;
    HomeNotificationFragment mHomeNotificationFragment;

    public NotificationAdapter(List<NotificationDetailModel> notificationDetailModelList, Context context, HomeNotificationFragment homeNotificationFragment) {
        mNotifications=notificationDetailModelList;
        mContext=context;
        mHomeNotificationFragment=homeNotificationFragment;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationDetailModel notificationDetailModel=mNotifications.get(position);
        holder.itemView.setTag(notificationDetailModel);
        holder.bind(notificationDetailModel,position);
    }

    @Override
    public int getItemCount() {
        if (mNotifications != null) {
            return mNotifications.size();
        } else {
            return 0;
        }
    }

    public void removeItemFromList(NotificationDetailModel notificationDetailModel){
        mNotifications.remove(notificationDetailModel);
        notifyDataSetChanged();

    }

    public void removeAllItemFromList() {
        mNotifications.clear();
        notifyDataSetChanged();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.notification_title)
        TextView tvNotfTitle;

        @BindView(R.id.notification_message)
        TextView tvNotfMessage;

        @BindView(R.id.notification_time)
        TextView tvNotfTime;

        @BindView(R.id.notification_item_fg)
        RelativeLayout relativeLayoutForeground;

        @BindView(R.id.notification_item_bg)
        RelativeLayout relativeLayoutBackground;

        @BindView(R.id.frame_layout_notf)
        FrameLayout frameLayoutNotf;





        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            frameLayoutNotf.setOnClickListener(this);
        }

        public void bind(NotificationDetailModel notificationDetailModel, int position) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
            Calendar calendar = Calendar.getInstance();

            if(notificationDetailModel.getTitle()!=null){
                if(!TextUtils.isEmpty(notificationDetailModel.getTitle())){
                    tvNotfTitle.setText(notificationDetailModel.getTitle());
                }
            }

            if(notificationDetailModel.getMessage()!=null){
                if(!TextUtils.isEmpty(notificationDetailModel.getMessage())){
                    String message=notificationDetailModel.getMessage();
                    tvNotfMessage.setText(message);
                }
            }

            if(notificationDetailModel.getCreatedDate()!=null){
                calendar.setTime(notificationDetailModel.getCreatedDate());
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                String hoursText,minutesText,timeText;

                String date=simpleDateFormat.format(notificationDetailModel.getCreatedDate());

                if(hours<10){
                    hoursText="0"+hours;
                }else{
                    hoursText=String.valueOf(hours);
                }

                if(minutes<10){
                    minutesText="0"+hours;
                }else{
                    minutesText=String.valueOf(minutes);
                }

                timeText=hoursText+":"+minutesText;

                String fullDate=date+"  "+timeText;

                tvNotfTime.setText(fullDate);

            }



        }

        @Override
        public void onClick(View v) {
            NotificationDetailModel notificationDetailModel=(NotificationDetailModel)itemView.getTag();
            if(v.getId()==R.id.frame_layout_notf){
                mHomeNotificationFragment.gotoOrder(notificationDetailModel.getData());
            }
        }
    }
}
