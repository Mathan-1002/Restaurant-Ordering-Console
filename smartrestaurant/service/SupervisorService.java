package smartrestaurant.service;

import smartrestaurant.manager.OrderManager;
import smartrestaurant.model.Order;
import smartrestaurant.util.Bill;

import java.util.*;
import java.io.*;

public class SupervisorService {

    Scanner sc = new Scanner(System.in);
    OrderManager manager;

    public SupervisorService(OrderManager manager) {
        this.manager = manager;
    }

    // 🔷 Load supervisor credentials from file
    public Map<String, String> loadSupervisors() {

        Map<String, String> map = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("data/supervisors.txt"));
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length == 2) {
                    map.put(data[0], data[1]);
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error reading supervisor file");
        }

        return map;
    }

    public void supervisorLogin() {

        Map<String, String> supervisors = loadSupervisors();

        System.out.print("Enter User ID: ");
        String uid = sc.next();

        System.out.print("Enter Password: ");
        String pwd = sc.next();

        // 🔷 Validate login
        if (!supervisors.containsKey(uid) || !supervisors.get(uid).equals(pwd)) {
            System.out.println("Invalid Login");
            return;
        }

        int start = 1, end = 3;
        String supervisorName = "";

        // 🔷 Assign tables
        if (uid.equals("sup1")) {
            start = 1; end = 3;
            supervisorName = "Supervisor 1";
        } else if (uid.equals("sup2")) {
            start = 4; end = 6;
            supervisorName = "Supervisor 2";
        } else if (uid.equals("sup3")) {
            start = 7; end = 10;
            supervisorName = "Supervisor 3";
        }

        while (true) {

            System.out.println("\n========= SUPERVISOR DASHBOARD =========");

            for (int t = start; t <= end; t++) {

                boolean occupied = false;

                for (Order o : manager.orders) {
                    if (o.tableNo == t) {
                        occupied = true;
                        break;
                    }
                }

                if (occupied)
                    System.out.println("Table " + t + " - Occupied");
                else
                    System.out.println("Table " + t + " - Free");
            }

            System.out.println("0. Logout");

            int selectedTable;

            while (true) {

                System.out.print("Select Table (" + start + "-" + end + ", 0 Logout): ");
                selectedTable = sc.nextInt();

                if (selectedTable == 0)
                    return;

                if (selectedTable >= start && selectedTable <= end)
                    break;

                System.out.println("Invalid Table.");
            }

            while (true) {

                boolean pendingFound = false;
                int idCount = 1;

                System.out.println("\n------ Orders in Table " + selectedTable + " ------");

                for (Order o : manager.orders) {

                    if (o.tableNo == selectedTable &&
                        o.status.equals("Pending")) {

                        System.out.println("Order ID : " + idCount);
                        System.out.println("Dish     : " + o.dish);
                        System.out.println("Qty      : " + o.quantity);
                        System.out.println("--------------------------------");

                        idCount++;
                        pendingFound = true;
                    }
                }

                if (!pendingFound)
                    System.out.println("No Pending Orders");

                System.out.println("\n1. Mark Dish Supplied");
                System.out.println("2. Generate Bill");
                System.out.println("0. Back");

                int ch = sc.nextInt();

                if (ch == 0)
                    break;

                // 🔷 Mark supplied
                if (ch == 1) {

                    System.out.print("Enter Order ID: ");
                    int oid = sc.nextInt();

                    int count = 1;

                    for (Order o : manager.orders) {

                        if (o.tableNo == selectedTable &&
                            o.status.equals("Pending")) {

                            if (count == oid) {

                                o.status = "Supplied";
                                manager.saveOrders();

                                System.out.println("Dish Marked Supplied");
                                break;
                            }
                            count++;
                        }
                    }
                }

                // 🔷 Generate Bill
                if (ch == 2) {

                    boolean allSupplied = true;

                    for (Order o : manager.orders) {

                        if (o.tableNo == selectedTable &&
                            o.status.equals("Pending")) {

                            allSupplied = false;
                            break;
                        }
                    }

                    if (!allSupplied) {

                        System.out.println("All dishes are not supplied yet.");

                    } else {

                        String supName = "";

                        for (Order o : manager.orders) {
                            if (o.tableNo == selectedTable) {
                                supName = o.supervisor;
                                break;
                            }
                        }

                        Bill.generateBill(selectedTable, supName, manager.orders);

                        clearTableOrders(selectedTable);
                        manager.saveOrders();

                        System.out.println("Bill Generated Successfully");
                        break;
                    }
                }
            }
        }
    }

    public void clearTableOrders(int tableNo) {

        for (int i = manager.orders.size() - 1; i >= 0; i--) {

            if (manager.orders.get(i).tableNo == tableNo) {
                manager.orders.remove(i);
            }
        }
    }
}