package smartrestaurant.main;

import smartrestaurant.manager.OrderManager;
import smartrestaurant.service.CustomerService;
import smartrestaurant.service.SupervisorService;
import java.util.Scanner;

public class RestaurantMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        OrderManager manager = new OrderManager();
        manager.loadOrders();

        CustomerService customer = new CustomerService(manager);
        SupervisorService supervisor = new SupervisorService(manager);

        int choice;

        do {
            System.out.println("\n=====================================");
            System.out.println("      SMART RESTAURANT SYSTEM");
            System.out.println("=====================================");
            System.out.println("1. Place Orders");
            System.out.println("2. Supervisor Login");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            if (choice == 1)
                customer.placeOrder();

            if (choice == 2)
                supervisor.supervisorLogin();

        } while (choice != 0);

        System.out.println("System Closed");
    }
}