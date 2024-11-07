package inventoryrecording;

import java.util.Scanner;

public class inventoryRecords {

    public void manageInventoryRecords() {
        Scanner sc = new Scanner(System.in);
        String response;
        boolean exit = true;

        do {
            System.out.println("\nINVENTORY RECORDING MENU:");
            System.out.println("1. ADD");
            System.out.println("2. VIEW");
            System.out.println("3. UPDATE");
            System.out.println("4. DELETE");
            System.out.println("5. EXIT");

            System.out.print("Enter Action: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                sc.next();
                System.out.print("Enter Action: ");
            }
            int action = sc.nextInt();

            switch (action) {
                case 1:
                    addInventoryRecord();
                    break;
                case 2:
                    viewInventoryRecords();
                    break;
                case 3:
                    viewInventoryRecords();
                    updateInventoryRecord();
                    viewInventoryRecords();
                    break;
                case 4:
                    deleteInventoryRecord();
                    break;
                case 5:
                    System.out.print("Exiting... Are you sure? (yes/no): ");
                    String resp = sc.next();
                    if (resp.equalsIgnoreCase("yes")) {
                        exit = false;
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }

            if (action >= 1 && action <= 4) {
                System.out.print("Do you want to make another transaction? (yes/no): ");
                response = sc.next();
                if (!response.equalsIgnoreCase("yes")) {
                    exit = false;
                }
            }
        } while (exit);

        System.out.println("Thank you, come again!");
    }

    public void addInventoryRecord() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Product ID: ");
        int productId = sc.nextInt();

        // Validate if Product ID exists
        while (!productExists(productId)) {
            System.out.println("Product ID does not exist. Please enter a valid Product ID.");
            System.out.print("Enter Product ID: ");
            productId = sc.nextInt();
        }

        System.out.print("Enter Quantity Added: ");
        int quantityAdded = sc.nextInt();

        System.out.print("Enter Sale Price: ");
        double salePrice = sc.nextDouble();

        System.out.print("Enter Purchase Price: ");
        double purchasePrice = sc.nextDouble();

        System.out.print("Enter Transaction Type (Sale/Purchase): ");
        String transactionType = sc.next();  // Changed 'Restock' to 'Purchase'

        System.out.print("Enter Transaction Date (yyyy/MM/dd): ");
        String transactionDate = sc.next();

        System.out.print("Enter Supplier Name: ");
        String supplierName = sc.next();

        System.out.print("Enter Stock Status (Active/Discontinued/Hold): ");
        String stockStatus = sc.next();

        double totalSales = 0;
        if (transactionType.equalsIgnoreCase("Sale")) {
            totalSales = quantityAdded * salePrice;
        }

        // Updated SQL query with correct SQLite date function
        String sql = "INSERT INTO tbl_inventory_recording (p_id, quantity_added, sale_price, purchase_price, transaction_type, transaction_date, supplier_name, stock_status, total_sales, last_updated) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now'))";
        conf.addRecord(sql, productId, quantityAdded, salePrice, purchasePrice, transactionType, transactionDate, supplierName, stockStatus, totalSales);

        System.out.println("Inventory record added successfully.");
    }

    private boolean productExists(int productId) {
        config conf = new config();
        String productQuery = "SELECT COUNT(*) FROM tbl_product WHERE p_id = ?";
        return conf.getSingleValue(productQuery, productId) > 0;
    }

    public void viewInventoryRecords() {
        config conf = new config();

        String query = "SELECT i.record_id, p.p_id, p.product_name, p.brand, p.category, i.quantity_added, i.sale_price, i.purchase_price, i.transaction_type, i.transaction_date, i.supplier_name, i.stock_status, i.total_sales " +
                       "FROM tbl_inventory_recording i " +
                       "JOIN tbl_product p ON i.p_id = p.p_id";

        // Column headers
        String[] headers = {"Record ID", "Product ID", "Product Name", "Product Brand", "Product Category", "Quantity Added", "Sale Price", "Purchase Price", "Transaction Type", "Transaction Date", "Supplier Name", "Stock Status", "Total Sales"};
        
        // Corresponding column names in the database
        String[] columns = {"record_id", "p_id", "product_name", "brand", "category", "quantity_added", "sale_price", "purchase_price", "transaction_type", "transaction_date", "supplier_name", "stock_status", "total_sales"};

        conf.viewRecords(query, headers, columns);
    }

    public void updateInventoryRecord() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Record ID to update: ");
        int recordId = sc.nextInt();

        String recordQuery = "SELECT record_id FROM tbl_inventory_recording WHERE record_id=?";
        while (conf.getSingleValue(recordQuery, recordId) == 0) {
            System.out.print("Record ID does not exist. Try again: ");
            recordId = sc.nextInt();
        }

        System.out.print("Enter new Quantity Added: ");
        int quantityAdded = sc.nextInt();

        System.out.print("Enter new Sale Price: ");
        double salePrice = sc.nextDouble();

        System.out.print("Enter new Purchase Price: ");
        double purchasePrice = sc.nextDouble();

        System.out.print("Enter new Transaction Type (Sale/Purchase): ");
        String transactionType = sc.next();  // Changed 'Restock' to 'Purchase'

        System.out.print("Enter new Transaction Date (yyyy/MM/dd): ");
        String transactionDate = sc.next();

        System.out.print("Enter new Supplier Name: ");
        String supplierName = sc.next();

        System.out.print("Enter new Stock Status (Active/Discontinued/Hold): ");
        String stockStatus = sc.next();

        double totalSales = 0;
        if (transactionType.equalsIgnoreCase("Sale")) {
            totalSales = quantityAdded * salePrice;
        }

        String sql = "UPDATE tbl_inventory SET quantity_added = ?, sale_price = ?, purchase_price = ?, transaction_type = ?, transaction_date = ?, supplier_name = ?, stock_status = ?, total_sales = ?, last_updated = NOW() WHERE record_id = ?";
        conf.updateRecord(sql, quantityAdded, salePrice, purchasePrice, transactionType, transactionDate, supplierName, stockStatus, totalSales, recordId);

        System.out.println("Inventory record updated successfully.");
    }

    public void deleteInventoryRecord() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Record ID to delete: ");
        int recordId = sc.nextInt();

        String recordQuery = "SELECT record_id FROM tbl_inventory_recording WHERE record_id=?";
        while (conf.getSingleValue(recordQuery, recordId) == 0) {
            System.out.print("Record ID does not exist. Try again: ");
            recordId = sc.nextInt();
        }

        String sql = "DELETE FROM tbl_inventory WHERE record_id = ?";
        conf.deleteRecord(sql, recordId);

        System.out.println("Inventory record deleted successfully.");
    }
}
