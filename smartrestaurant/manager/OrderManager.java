package smartrestaurant.manager;

import smartrestaurant.model.Order;
import java.util.*;
import java.io.*;

public class OrderManager {

    public ArrayList<Order> orders = new ArrayList<>();
    String fileName = "data/orders.txt";

    public void loadOrders() {

        try {
            File file = new File(fileName);
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {

                String d[] = line.split(",");

                orders.add(new Order(
                        Integer.parseInt(d[0]), d[1], d[2], d[3],
                        Integer.parseInt(d[4]),
                        Integer.parseInt(d[5]),
                        d[6],
                        d[7] // supervisor
                ));
            }
            br.close();

        } catch (Exception e) {}
    }

    public void saveOrders() {

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(fileName));

            for (Order o : orders) {

                pw.println(o.tableNo + "," +
                           o.customerName + "," +
                           o.phone + "," +
                           o.dish + "," +
                           o.price + "," +
                           o.quantity + "," +
                           o.status + "," +
                           o.supervisor);
            }

            pw.close();

        } catch (Exception e) {}
    }
}