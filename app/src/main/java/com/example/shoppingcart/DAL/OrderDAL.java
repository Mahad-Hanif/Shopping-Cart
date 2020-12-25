package com.example.shoppingcart.DAL;

import com.example.shoppingcart.Order;

public class OrderDAL {
    public String write(Order order)
    {
        if(order!=null) {
            return (order.getfName() + "," +
                    order.getlName() + "," +
                    order.getEmail() + "," +
                    order.getNo() + "," +
                    order.getCity() + "," +
                    order.getZip() + "," +
                    order.getState() + "," +
                    order.getBoxes() + "," +
                    order.getcName() + "," +
                    order.getCategory() + "," +
                    order.getLocalDateTime() +
                    "\n");
        }
        return null;
    }

    public Order read(String strOrder)
    {
        Order orderList = null;
        if (strOrder.length() > 0)
        {
            String[] info = strOrder.split(",");
            Order data = new Order();
            data.setfName(info[0]);
            data.setlName(info[1]);
            data.setEmail(info[2]);
            data.setNo(info[3]);
            data.setCity(info[4]);
            data.setZip(info[5]);
            data.setState(info[6]);
            data.setBoxes(info[7]);
            data.setcName(info[8]);
            data.setCategory(info[9]);
            data.setLocalDateTime(info[10]);

            orderList = data;
        }
        return orderList;
    }
}
