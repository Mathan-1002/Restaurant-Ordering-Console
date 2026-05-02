package smartrestaurant.model;

public class Order {

    public int tableNo;
    public String customerName;
    public String phone;
    public String dish;
    public int price;
    public int quantity;
    public String status;
    public String supervisor;

    public Order(int tableNo, String customerName, String phone,
                 String dish, int price, int quantity,
                 String status, String supervisor) {

        this.tableNo = tableNo;
        this.customerName = customerName;
        this.phone = phone;
        this.dish = dish;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.supervisor = supervisor;
    }
}