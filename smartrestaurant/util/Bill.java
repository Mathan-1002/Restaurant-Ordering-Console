package smartrestaurant.util;

import smartrestaurant.model.Order;
import java.util.*;
import java.io.*;

public class Bill {

    public static void generateBill(int tableNo,
                                    String supervisor,
                                    ArrayList<Order> orders) {

        double total = 0;

        try {

            PrintWriter file =
                new PrintWriter(new FileWriter("data/bills.txt", true));

            System.out.println("\n=================================================");
            System.out.println("               SMART RESTAURANT");
            System.out.println("=================================================");

            file.println("\n=================================================");
            file.println("               SMART RESTAURANT");
            file.println("=================================================");

            // Customer Details
            for (Order o : orders) {

                if (o.tableNo == tableNo) {

                    System.out.printf("%-15s : %s\n", "Customer", o.customerName);
                    System.out.printf("%-15s : %s\n", "Phone", o.phone);

                    file.printf("%-15s : %s\n", "Customer", o.customerName);
                    file.printf("%-15s : %s\n", "Phone", o.phone);
                    break;
                }
            }

            System.out.printf("%-15s : %d\n", "Table No", tableNo);
            System.out.printf("%-15s : %s\n", "Supervisor", supervisor);

            file.printf("%-15s : %d\n", "Table No", tableNo);
            file.printf("%-15s : %s\n", "Supervisor", supervisor);

            // Header
            System.out.println("------------------------------------------------------------");
            System.out.printf("%-25s %5s %10s %10s\n", "Item", "Qty", "Price", "Amount");
            System.out.println("------------------------------------------------------------");

            file.println("------------------------------------------------------------");
            file.printf("%-25s %5s %10s %10s\n", "Item", "Qty", "Price", "Amount");
            file.println("------------------------------------------------------------");

            // Items
            for (Order o : orders) {

                if (o.tableNo == tableNo) {

                    double amount = o.price * o.quantity;

                    System.out.printf("%-25s %5d %10.2f %10.2f\n",
                            o.dish, o.quantity,
                            (double)o.price, amount);

                    file.printf("%-25s %5d %10.2f %10.2f\n",
                            o.dish, o.quantity,
                            (double)o.price, amount);

                    total += amount;
                }
            }

            System.out.println("------------------------------------------------------------");

            double gst = total * 0.05;
            double grand = total + gst;

            // ✅ PERFECT ALIGNMENT (KEY FIX)
            System.out.printf("%-40s %10.2f\n", "Subtotal", total);
            System.out.printf("%-40s %10.2f\n", "GST (5%)", gst);

            System.out.println("------------------------------------------------------------");

            System.out.printf("%-40s %10.2f\n", "Grand Total", grand);
            System.out.println("============================================================\n");

            file.println("------------------------------------------------------------");

            file.printf("%-40s %10.2f\n", "Subtotal", total);
            file.printf("%-40s %10.2f\n", "GST (5%)", gst);

            file.println("------------------------------------------------------------");

            file.printf("%-40s %10.2f\n", "Grand Total", grand);
            file.println("============================================================");

            file.println("************** BILL OVER **************\n");

            file.close();

        } catch (Exception e) {
        }
    }
}