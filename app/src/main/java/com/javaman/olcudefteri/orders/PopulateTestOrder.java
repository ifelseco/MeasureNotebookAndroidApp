package com.javaman.olcudefteri.orders;

import android.util.Log;

import com.javaman.olcudefteri.model.Customer;
import com.javaman.olcudefteri.model.CustomerMeasure;
import com.javaman.olcudefteri.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by javaman on 13.01.2018.
 *
 *
     private List<CustomerMeasure> measureList;

 */

public class PopulateTestOrder {

    public static List<Order> populateOrders()  {

        ArrayList<Order> mOrders=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");



        for(int i=0;i<=10;i++){
            Order order=new Order();
            order.setId(i);
            order.setTotalPrice(100+i*5);
            order.setDownPayment(25+i*5);

            try{
                Date date = sdf.parse("01/02/2018");
                order.setDeliveryDate(date);
            }catch(ParseException e){
                Log.e("Date Parse Error",e.getMessage());
            }

            order.setOrderDate(new Date());

            if(i<=3){
                order.setOrderStatus(1);
            }else if(i<=5 && i>3){
                order.setOrderStatus(2);
            }else{
                order.setOrderStatus(3);
            }

            Customer customer=new Customer();

            customer.setId(i);
            customer.setName(i<5?"Ali":"Veli");
            customer.setSurname(i<5?"Aydın":"Kaya");
            customer.setAddress("Adres "+i);
            customer.setMobilePhone("0555xxxxxx");
            customer.setFixedPhone("0212xxxxxx");
            customer.setGivePermission(i<5?true:false);

            order.setCustomer(customer);

            List<CustomerMeasure> mCustomerMeasureList=new ArrayList<>();

            for(int j=0; j<4; j++){
                CustomerMeasure customerMeasure=new CustomerMeasure();
                customerMeasure.setCurtainCode(j);
                customerMeasure.setCustomer(customer);
                customerMeasure.setId(j);
                customerMeasure.setMeasureId(j);
                customerMeasure.setPrice(100+j*5);

                mCustomerMeasureList.add(customerMeasure);
            }

            order.setMeasureList(mCustomerMeasureList);

            mOrders.add(order);

        }

        return mOrders;
    }
}
