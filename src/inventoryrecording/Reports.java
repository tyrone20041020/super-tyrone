package inventoryrecording;

import java.util.Scanner;

public class Reports {

    public void generateReports() {
        Scanner sc = new Scanner(System.in);

        boolean exit = false;

        do {
            System.out.println("\nREPORTS MENU:");
            System.out.println("1. INDIVIDUAL REPORT");
            System.out.println("2. GENERAL REPORT");
            System.out.println("3. EXIT");

            System.out.print("Enter Action: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                sc.next();
                System.out.print("Enter Action: ");
            }
            int action = sc.nextInt();

            // Switch case to handle user selection
            switch (action) {
                case 1:
                    individualReport();
                    break;
                case 2:
                    generalReport();
                    break;
                case 3:
                    System.out.println("Exiting Reports...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (!exit);

        System.out.println("Thank you, come again!");
    }

    // Method to generate the Individual Report
    public void individualReport() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Product ID: ");
        int productId = sc.nextInt();

        config conf = new config();
        String productQuery = "SELECT COUNT(*) FROM tbl_product WHERE p_id = ?";
        while (conf.getSingleValue(productQuery, productId) == 0) {
            System.out.println("Product ID does not exist. Please enter a valid Product ID.");
            System.out.print("Enter Product ID: ");
            productId = sc.nextInt();
        }

        // Query to get all details for the individual product, including the transaction_type directly from tbl_inventory_recording
        String query = "SELECT i.record_id, p.product_name, p.brand, p.category, i.purchase_price, i.sale_price, " +
                       "i.transaction_date, i.quantity_added, i.transaction_type, i.total_sales, i.transaction_date AS record_date, " +
                       "i.supplier_name " + 
                       "FROM tbl_inventory_recording i " +
                       "JOIN tbl_product p ON i.p_id = p.p_id " +
                       "WHERE i.p_id = ?";

        String[] headers = {"Record ID", "Product Name", "Product Brand", "Product Category", "Purchase Price", 
                            "Sale Price", "Transaction Date ", "Quantity Added", "Transaction Type", 
                            "Total Sales", "Record Date", "Supplier Name"};
        String[] columns = {"record_id", "product_name", "brand", "category", "purchase_price", "sale_price", "transaction_date", 
                            "quantity_added", "transaction_type", "total_sales", "record_date", "supplier_name"};

        conf.viewRecords(query, headers, columns, productId); 
    }


    public void generalReport() {
        String query = "SELECT i.record_id, p.p_id, p.product_name, p.brand, p.category, " +
                       "SUM(i.quantity_added) AS total_quantity, SUM(i.total_sales) AS total_sales, p.price, p.quantity_in_stock " +
                       "FROM tbl_inventory_recording i " +
                       "JOIN tbl_product p ON i.p_id = p.p_id " +
                       "GROUP BY p.p_id, p.product_name, p.brand, p.category, p.price, p.quantity_in_stock, i.record_id";

        String[] headers = {"Record ID", "Product ID", "Product Name", "Product Brand", "Product Category", "Product Price", 
                            "Stock Quantity", "Total Quantity Sold", "Total Sales"};
        String[] columns = {"record_id", "p_id", "product_name", "brand", "category", "price", "quantity_in_stock", 
                            "total_quantity", "total_sales"};

        config conf = new config();
        conf.viewRecords(query, headers, columns);  
    }
}
