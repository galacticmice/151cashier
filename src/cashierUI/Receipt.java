package cashierUI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Receipt {
    private String storeName;
    private String phone;
    private String city;
    private String state;
    private double taxPercentage;
    private String cashierName;
    private ArrayList<ReceiptItem> items;
    private double discountPercentage;

    public Receipt(String storeName, String phone, String city, String state, double taxPercentage, String cashierName) {
        this.storeName = storeName;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.taxPercentage = taxPercentage;
        this.cashierName = cashierName;
        this.items = new ArrayList<>();
        this.discountPercentage = 0.0;
    }

    public void addItem(String productName, String productCode, int quantity, double unitPrice) {
        items.add(new ReceiptItem(productName, productCode, quantity, unitPrice));
    }

    public void setDiscount(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void printReceipt() {
        double subtotal = 0;
        for (ReceiptItem item : items) {
            subtotal += item.getTotalPrice();
        }

        double tax = subtotal * (taxPercentage / 100);
        double discount = subtotal * (discountPercentage / 100);
        double total = subtotal + tax - discount;

        StringBuilder receipt = new StringBuilder();
        receipt.append("\n")
                .append("=".repeat(40)).append("\n")
                .append(String.format("%s\n", storeName))
                .append(String.format("City: %s, State: %s\n", city, state))
                .append(String.format("Phone: %s\n", phone))
                .append("=".repeat(40)).append("\n")
                .append("Date/Time: ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n")
                .append("=".repeat(40)).append("\n")
                .append(String.format("%-10s %-10s %-10s %-10s\n", "Code", "Product", "Qty", "Price"))
                .append("-".repeat(40)).append("\n");

        for (ReceiptItem item : items) {
            receipt.append(String.format("%-10s %-10s %-10d $%-10.2f\n",
                    item.getProductCode(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getTotalPrice()));
        }

        receipt.append("=".repeat(40)).append("\n")
                .append(String.format("Subtotal: $%.2f\n", subtotal))
                .append(String.format("Tax: $%.2f\n", tax))
                .append(String.format("Discount: -$%.2f\n", discount))
                .append("=".repeat(40)).append("\n")
                .append(String.format("Total: $%.2f\n", total))
                .append("=".repeat(40)).append("\n")
                .append(String.format("Your cashier: %s\n", cashierName))
                .append("Thank you for shopping with us!\n");

        System.out.println(receipt.toString());
    }
}
/*
public class ReceiptItem {
    private String productName;
    private String productCode;
    private int quantity;
    private double unitPrice;

    public ReceiptItem(String productName, String productCode, int quantity, double unitPrice) {
        this.productName = productName;
        this.productCode = productCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}
private void setupReceiptButton(Inventory inventory) {
    JButton printReceiptButton = new JButton("Print Receipt");
    printReceiptButton.addActionListener(e -> {
        Receipt receipt = new Receipt(
                inventory.getStoreName(),
                inventory.getPhone(),
                inventory.getCity(),
                inventory.getState(),
                inventory.getTaxPercentage(),
                firstNameField.getText() + " " + lastNameField.getText()
        );

        // Example items to add to the receipt
        receipt.addItem("Apples", "01145", 3, 2.5);
        receipt.addItem("Bananas", "01151", 2, 1.2);

        // Example: Setting a discount
        receipt.setDiscount(10.0);

        // Print the receipt
        receipt.printReceipt();
    });

    panel.add(printReceiptButton);
}*/