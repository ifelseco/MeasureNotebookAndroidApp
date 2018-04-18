package com.javaman.olcudefteri.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.home.model.NotificationDetailModel;
import com.javaman.olcudefteri.home.model.NotificationSummaryModel;
import com.javaman.olcudefteri.orders.OrderDetailActivity;
import com.javaman.olcudefteri.orders.OrderUpdateDialog;
import com.javaman.olcudefteri.utill.MyUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeNotificationFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.linear_layout_zero_not)
    LinearLayout linearLayoutZeroNot;

    @BindView(R.id.recycler_notification)
    RecyclerView recyclerView;

    RecyclerView.Adapter adapter;

    String text;
    int notificationCount = 0;
    NotificationSummaryModel notificationSummaryModel;

    public HomeNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(HomeActivity.ARG_NOTIFICATIONS)) {
                notificationSummaryModel = bundle.getParcelable(HomeActivity.ARG_NOTIFICATIONS);
                if(notificationSummaryModel!=null){
                    notificationCount=notificationSummaryModel.getNotificationDetailModelList().size();
                    adapter=new NotificationAdapter(notificationSummaryModel.getNotificationDetailModelList(),getActivity(),HomeNotificationFragment.this);
                }
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_notification, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        initView();
        return view;
    }

    private void initView() {
        if(notificationCount>0){
            linearLayoutZeroNot.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_action_delete, menu);
        MenuItem iteDelete = menu.findItem(R.id.item_delete);
        if (iteDelete != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), iteDelete, android.R.color.white);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_delete:
                showConfirmDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showConfirmDialog() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText("Tüm bildirimler silinecek.")
                .setConfirmText("Evet")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        ((HomeActivity)getActivity()).deleteAllNotification();
                        sDialog.dismissWithAnimation();
                    }
                })
                .showCancelButton(true)
                .setCancelText("Vazgeç!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.NotificationViewHolder) {


            final NotificationDetailModel notificationDetailModel=notificationSummaryModel.getNotificationDetailModelList().get(viewHolder.getAdapterPosition());
            final int deletedIndex=viewHolder.getAdapterPosition();
            EventBus.getDefault().post(notificationDetailModel);


        }
    }

    public void removeItemFromAdapter(NotificationDetailModel notificationDetailModel) {
        NotificationAdapter notificationAdapter= (NotificationAdapter) adapter;
        notificationAdapter.removeItemFromList(notificationDetailModel);
    }

    public void removeAllItemFromAdapter(){
        NotificationAdapter notificationAdapter= (NotificationAdapter) adapter;
        notificationAdapter.removeAllItemFromList();
    }

    public void setEmptyBackground(){
        linearLayoutZeroNot.setVisibility(View.VISIBLE);
    }
}
