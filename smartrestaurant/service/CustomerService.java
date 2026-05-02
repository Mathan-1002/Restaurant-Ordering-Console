package smartrestaurant.service;

import smartrestaurant.manager.OrderManager;
import smartrestaurant.model.Order;
import smartrestaurant.util.Menu;

import java.util.Scanner;

public class CustomerService {

    Scanner sc = new Scanner(System.in);
    Menu menu = new Menu();
    OrderManager manager;

    public CustomerService(OrderManager manager) {
        this.manager = manager;
    }

    public void placeOrder() {

        int table;

        // 🔹 Table validation
        while (true) {
            System.out.print("Enter Table Number (1-10): ");
            table = sc.nextInt();

            if (table >= 1 && table <= 10)
                break;

            System.out.println("Invalid Table Number.");
        }

        sc.nextLine();

        String name = "", phone = "";
        boolean exist = false;

        // 🔹 Check if table already has customer
        for (Order o : manager.orders) {
            if (o.tableNo == table) {
                name = o.customerName;
                phone = o.phone;
                exist = true;
                break;
            }
        }

        // 🔹 Get customer details if new
        if (!exist) {
            System.out.print("Enter Name: ");
            name = sc.nextLine();
            System.out.print("Enter Phone: ");
            phone = sc.nextLine();
        }

        // 🔹 Assign supervisor based on table
        String supervisor = "";
        if (table >= 1 && table <= 3) supervisor = "Supervisor 1";
        else if (table >= 4 && table <= 6) supervisor = "Supervisor 2";
        else supervisor = "Supervisor 3";

        while (true) {

            System.out.println("\n1. Add Order");
            System.out.println("2. Cancel Order");
            System.out.println("0. Back");

            int op = sc.nextInt();

            if (op == 0)
                return;

            // 🔷 ADD ORDER
            if (op == 1) {

                while (true) {

                    int slot = menu.getTimeSlot();

                    if (slot == 0) {
                        System.out.println("\nHotel is CLOSED now.");
                        break;
                    }

                    int variety = menu.showVariety();

                    if (variety == 0) {
                        showOrderSummary(table);
                        break;
                    }

                    while (true) {

                        int d = menu.showDishes(slot, variety);

                        if (d == 0)
                            break;

                        String dish = menu.getDishName(slot, variety, d);
                        int price = menu.getPrice(slot, variety, d);

                        System.out.print("Enter Quantity: ");
                        int q = sc.nextInt();

                        // 🔹 Validation
                        if (q <= 0) {
                            System.out.println("Invalid quantity. Setting to 1.");
                            q = 1;
                        }

                        manager.orders.add(
                            new Order(table, name, phone, dish, price, q, "Pending", supervisor)
                        );

                        manager.saveOrders();

                        System.out.println("Dish Added Successfully");
                    }
                }
            }

            // 🔷 CANCEL ORDER
            if (op == 2)
                cancelOrder(table);
        }
    }

    // 🔷 Cancel Order
    public void cancelOrder(int table) {

        while (true) {

            int id = 1;
            boolean found = false;

            System.out.println("\n--- Your Orders ---");

            for (Order o : manager.orders) {

                if (o.tableNo == table &&
                    o.status.equals("Pending")) {

                    System.out.println("Order ID : " + id);
                    System.out.println("Dish     : " + o.dish);
                    System.out.println("Qty      : " + o.quantity);
                    System.out.println("-------------------");

                    id++;
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No order is placed.");
                return;
            }

            System.out.print("Enter Order ID (0 Back): ");
            int oid = sc.nextInt();

            if (oid == 0)
                return;

            int count = 1;

            for (int i = 0; i < manager.orders.size(); i++) {

                Order o = manager.orders.get(i);

                if (o.tableNo == table &&
                    o.status.equals("Pending")) {

                    if (count == oid) {

                        manager.orders.remove(i);
                        manager.saveOrders();

                        System.out.println("Order Cancelled Successfully");
                        break;
                    }
                    count++;
                }
            }
        }
    }

    // 🔷 Order Summary (FINAL FIX)
    public void showOrderSummary(int table) {

        boolean found = false;

        System.out.println("\n========== ORDER SUMMARY ==========");
        System.out.printf("%-20s %5s %10s\n", "Dish", "Qty", "Amount");
        System.out.println("----------------------------------------");

        for (Order o : manager.orders) {

            if (o.tableNo == table &&
                o.status.equals("Pending")) {

                double amount = o.price * o.quantity; // ✅ FIX

                System.out.printf("%-20s %5d %10.2f\n",
                        o.dish,
                        o.quantity,
                        amount);

                found = true;
            }
        }

        if (!found)
            System.out.println("No orders placed.");

        System.out.println("========================================");
    }
}