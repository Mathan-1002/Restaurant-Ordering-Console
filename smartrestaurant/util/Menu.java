package smartrestaurant.util;

import java.util.*;
import java.io.*;
import java.time.LocalTime;

class MenuItem {
    int slot, variety, price, qty;
    String varietyName, name;

    MenuItem(int slot, int variety, String varietyName,
             String name, int qty, int price) {

        this.slot = slot;
        this.variety = variety;
        this.varietyName = varietyName;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }
}

public class Menu {

    Scanner sc = new Scanner(System.in);
    ArrayList<MenuItem> menuList = new ArrayList<>();

    public Menu() {
        loadMenu();
    }

    public void loadMenu() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data/menu.txt"));
            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] d = line.split(",");

                menuList.add(new MenuItem(
                        Integer.parseInt(d[0]),
                        Integer.parseInt(d[1]),
                        d[2],
                        d[3],
                        Integer.parseInt(d[4]),
                        Integer.parseInt(d[5])
                ));
            }
            br.close();

        } catch (Exception e) {
            System.out.println("Error loading menu file");
        }
    }

    public int getTimeSlot() {

        LocalTime time = LocalTime.now();
        double current = time.getHour() + (time.getMinute() / 60.0);

        if (current >= 6.0 && current < 11.5) return 1;
        if (current >= 11.5 && current < 15.5) return 2;
        if (current >= 15.5 && current < 18.5) return 3;
        if (current >= 18.5 && current < 23.0) return 4;

        return 0;
    }

    public int showVariety() {

        int slot = getTimeSlot();

        if (slot == 0) {
            System.out.println("\nHotel is CLOSED now (Opens at 6:00 AM)");
            return 0;
        }

        System.out.println("\n----------- VARIETY -----------");

        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        int index = 1;

        for (MenuItem m : menuList) {
            if (m.slot == slot && !map.containsValue(m.varietyName)) {
                map.put(index, m.varietyName);
                System.out.println(index + ". " + m.varietyName);
                index++;
            }
        }

        System.out.println("0. Back");

        int choice = sc.nextInt();

        for (MenuItem m : menuList) {
            if (m.slot == slot &&
                map.get(choice) != null &&
                m.varietyName.equals(map.get(choice))) {
                return m.variety;
            }
        }

        return 0;
    }

    public int showDishes(int slot, int variety) {

        System.out.println("\n----------- DISHES -----------");

        ArrayList<MenuItem> list = new ArrayList<>();

        for (MenuItem m : menuList) {
            if (m.slot == slot && m.variety == variety) {
                list.add(m);
            }
        }

        System.out.printf("%-3s %-20s %5s %10s\n", "No", "Dish", "Qty", "Price");
        System.out.println("-------------------------------------------");

        for (int i = 0; i < list.size(); i++) {

            MenuItem m = list.get(i);

            System.out.printf("%-3d %-20s %5d %10.2f\n",
                    (i + 1),
                    m.name,
                    m.qty,
                    (double) m.price);
        }

        System.out.println("0. Back");

        return sc.nextInt();
    }

    public String getDishName(int slot, int variety, int dish) {

        int count = 1;

        for (MenuItem m : menuList) {
            if (m.slot == slot && m.variety == variety) {
                if (count == dish) return m.name;
                count++;
            }
        }
        return "";
    }

    public int getPrice(int slot, int variety, int dish) {

        int count = 1;

        for (MenuItem m : menuList) {
            if (m.slot == slot && m.variety == variety) {
                if (count == dish) return m.price;
                count++;
            }
        }
        return 0;
    }

    public int getDefaultQuantity(int slot, int variety, int dish) {

        int count = 1;

        for (MenuItem m : menuList) {
            if (m.slot == slot && m.variety == variety) {
                if (count == dish) return m.qty;
                count++;
            }
        }
        return 1;
    }
}