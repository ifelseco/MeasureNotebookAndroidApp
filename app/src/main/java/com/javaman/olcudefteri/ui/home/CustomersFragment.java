package com.javaman.olcudefteri.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.CustomerSummaryModel;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.AddOrderPresenter;
import com.javaman.olcudefteri.presenter.CustomerPresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderPresenterImpl;
import com.javaman.olcudefteri.presenter.impl.CustomerPresenterImpl;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.ui.orders.AddOrderActivity;
import com.javaman.olcudefteri.ui.orders.CustomerDetailFragment;
import com.javaman.olcudefteri.ui.orders.OrderLineAdapter;
import com.javaman.olcudefteri.ui.orders.dialogs.CustomerOrdersDialog;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.javaman.olcudefteri.view.CustomerView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomersFragment extends Fragment implements CustomerView, SearchView.OnQueryTextListener, View.OnClickListener {

    SharedPreferenceHelper sharedPreferenceHelper;

    @BindView(R.id.card_search)
    CardView cardSearch;

    @BindView(R.id.recycler_view_customers)
    RecyclerView recyclerViewCustomers;

    @BindView(R.id.searchViewCustomer)
    SearchView searchView;

    @BindView(R.id.linear_layout_not_found)
    LinearLayout linearLayoutNotFound;

    @BindView(R.id.pb_search)
    ProgressBar progressBarSearch;

    @BindView(R.id.tv_search_secreen_text)
    TextView tvSearchScreenMessage;

    RecyclerView.Adapter adapter;

    CustomerSummaryModel customerSummaryModel;
    CustomerPresenter customerPresenter;
    private boolean canRun;
    Handler handler;
    private AddOrderPresenter addOrderPresenter;
    public static final String ARG_CUSTOMER_ORDERS = "add-customer-orders";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        customerPresenter=new CustomerPresenterImpl(this);
        handler= new Handler();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.customers_fragment_layout,container,false);
        ButterKnife.bind(this,view);
        addOrderPresenter=new AddOrderPresenterImpl(this);
        intiView();
        return view;
    }

    private void intiView() {
        getActivity().setTitle("Müşteri Ara");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCustomers.setLayoutManager(layoutManager);
        recyclerViewCustomers.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        searchView.setQueryHint("İsim yada telefon ile ara");
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnCloseListener(() -> {

            return false;
        });
        searchView.setOnSearchClickListener(this);
        cardSearch.setOnClickListener(this);
        recyclerViewCustomers.setVisibility(View.GONE);
        linearLayoutNotFound.setVisibility(View.VISIBLE);
        tvSearchScreenMessage.setText("İsim yada telefon ile müşteri ara.");
    }

    public void showDialog(DialogFragment dialogFragment , String fragmentTag){
        dialogFragment.show(getFragmentManager(),fragmentTag);
    }

    @Override
    public void searchCustomer(String text) {
        String headerData=getSessionIdFromPref();
        customerPresenter.customerSearch(headerData,text);
    }

    @Override
    public void getSearchResult(CustomerSummaryModel customerSummaryModel) {
        if(customerSummaryModel.getCustomers().size()>0){
            recyclerViewCustomers.setVisibility(View.VISIBLE);
            linearLayoutNotFound.setVisibility(View.GONE);
            setAdapter(customerSummaryModel);
        }else{
            clearAdapter();
            recyclerViewCustomers.setVisibility(View.GONE);
            linearLayoutNotFound.setVisibility(View.VISIBLE);
            tvSearchScreenMessage.setText("Sonuç bulunamadı...");
        }
    }

    @Override
    public void navigateToOrder(AddCustomerResponse addCustomerResponse) {
        Intent intent=new Intent(getActivity(),AddOrderActivity.class);
        intent.putExtra(AddOrderActivity.ARG_ADD_ORDER,addCustomerResponse);
        startActivity(intent);
    }

    @Override
    public void getCustomerOrders(OrderSummaryModel orderSummaryModel) {
        CustomerOrdersDialog customerOrdersDialog=new CustomerOrdersDialog();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ARG_CUSTOMER_ORDERS,orderSummaryModel);
        customerOrdersDialog.setArguments(bundle);
        showDialog(customerOrdersDialog,"customer-orders-dialog");
    }

    private void clearAdapter() {
        if(adapter!=null){
            SearchAdapter searchAdapter= (SearchAdapter) adapter;
            searchAdapter.clearAdapter();
        }
    }

    private void setAdapter(CustomerSummaryModel customerSummaryModel) {
        adapter = new SearchAdapter(customerSummaryModel.getCustomers(), getActivity(), CustomersFragment.this);
        recyclerViewCustomers.setAdapter(adapter);
    }

    @Override
    public void logout() {

    }

    @Override
    public void checkSession() {

    }


    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void removeKeyFromPref(String key) {
        sharedPreferenceHelper.removeKey(key);

    }

    @Override
    public void showAlert(String message,boolean isToast) {
        StyleableToast.makeText(getActivity(),message,R.style.info_toast_style).show();

    }

    @Override
    public void showProgress(String message) {
        progressBarSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarSearch.setVisibility(View.GONE);
    }

    @Override
    public void setLogoutPref(boolean isLogout) {

    }


    @Override
    public void navigateToLogin() {
        startActivity(new Intent(getActivity(),LoginActivity.class));
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(newText.length()>3){
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> {
                searchCustomer(newText);
                searchView.clearFocus();
            },1000);

        }else{
            handler.removeCallbacksAndMessages(null);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.card_search){
            searchView.setIconified(false);
        }
    }

    public void addOrder(CustomerDetailModel customerDetailModel){
        showConfirmDialog(customerDetailModel);
    }

    public void showCustomerOrders(long customerId){
        String headerData=getSessionIdFromPref();
        customerPresenter.getCustomerOrders(headerData,customerId);
    }

    private void showConfirmDialog(CustomerDetailModel customerDetailModel) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText("Müşteri için yeni \n sipariş kaydı açılacak")
                .setConfirmText("Evet")
                .setConfirmClickListener(sDialog -> {
                    AddCustomerModel addCustomerModel=new AddCustomerModel();
                    addCustomerModel.setCustomerDetailModel(customerDetailModel);
                    addCustomerModel.setOrderStatus(0);
                    String sessionId=getSessionIdFromPref();
                    addOrderPresenter.addCustomer(addCustomerModel,sessionId);
                    sDialog.dismissWithAnimation();
                })
                .showCancelButton(true)
                .setCancelText("Vazgeç!")
                .setCancelClickListener(sDialog -> sDialog.cancel())
                .show();
    }
}
