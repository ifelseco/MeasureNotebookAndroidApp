package com.javaman.olcudefteri.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.javaman.olcudefteri.model.NotificationDetailModel;
import com.javaman.olcudefteri.model.NotificationSummaryModel;
import com.javaman.olcudefteri.ui.tailor.TailorHomeActivity;
import com.javaman.olcudefteri.utill.MyUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeNotificationFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.linear_layout_zero_not)
    LinearLayout linearLayoutZeroNot;

    @BindView(R.id.recycler_notification)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_notification)
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView.Adapter adapter;

    boolean isTailor=false;
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
            }else if(bundle.containsKey(TailorHomeActivity.ARG_NOTIFICATIONS)){
                isTailor=true;
                notificationSummaryModel = bundle.getParcelable(TailorHomeActivity.ARG_NOTIFICATIONS);
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
        swipeRefreshLayout.setOnRefreshListener(this);
        if(notificationCount>0){
            linearLayoutZeroNot.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        }else{
            recyclerView.setVisibility(View.GONE);
            linearLayoutZeroNot.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_action_delete, menu);
        MenuItem itemDelete = menu.findItem(R.id.item_delete);
        if (itemDelete != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), itemDelete, android.R.color.white);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_delete:
                if(notificationSummaryModel.getNotificationDetailModelList().size()>0){
                    showConfirmDialog();
                }else{
                    StyleableToast.makeText(getActivity(),"Hiç bildirim yok",R.style.info_toast_style).show();
                }
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
                        if(isTailor){
                            ((TailorHomeActivity)getActivity()).deleteAllNotification();
                        }else{
                            ((HomeActivity)getActivity()).deleteAllNotification();
                        }

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
        recyclerView.setVisibility(View.GONE);
        linearLayoutZeroNot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        if(isTailor){
            ((TailorHomeActivity)getActivity()).getNotificationFragment();
        }else{
            ((HomeActivity)getActivity()).getNotificationFragment();
        }
    }
}
