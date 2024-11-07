package inventoryrecording;

import java.util.Scanner;

public class Product {


   Scanner sc = new Scanner(System.in);

   public void manageProduct() {
    config conf = new config();

    String response;
    boolean exit = true;

    do {
        System.out.println("\nPRODUCT MENU:");
        System.out.println("1. ADD PRODUCT");
        System.out.println("2. VIEW PRODUCTS");
        System.out.println("3. UPDATE PRODUCT");
        System.out.println("4. DELETE PRODUCT");
        System.out.println("5. EXIT");

        System.out.print("Enter Action: ");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number between 1 and 5.");
            sc.next();  // Clear invalid input
            System.out.print("Enter Action: ");
        }
        int action = sc.nextInt();

        switch (action) {
            case 1:
                addProduct();
                break;
            case 2:
                viewProducts();
                break;
            case 3:
                viewProducts();
                updateProduct();
                viewProducts();
                break;
            case 4:
                viewProducts();
                deleteProduct();
                viewProducts();
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

        // After any transaction that modifies data, ask if they want to make another transaction
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


    public void addProduct() {
    System.out.print("Enter Product ID: ");
    int productId = sc.nextInt();

    while (productId < 101) {
        System.out.print("Invalid Product ID. Product ID must be 101 or greater. Please try again: ");
        productId = sc.nextInt();
    }

    String productQuery = "SELECT p_id FROM tbl_product WHERE p_id=?";
    config conf = new config();
    while (conf.getSingleValue(productQuery, productId) != 0) {
        System.out.print("Product ID already exists. Please enter a unique Product ID: ");
        productId = sc.nextInt();
    }

    System.out.print("Enter Product Name: ");
    String productName = sc.next();

    if (productName.isEmpty()) {
        System.out.println("Product Name cannot be empty.");
        return;
    }

    System.out.print("Enter Product Category (Haircare, Skincare, Makeup): ");
    String productCategory = sc.next().trim();

    if (!productCategory.equalsIgnoreCase("Haircare") &&
        !productCategory.equalsIgnoreCase("Skincare") &&
        !productCategory.equalsIgnoreCase("Makeup")) {
        System.out.println("Invalid category. Product must be in the 'Haircare', 'Skincare', or 'Makeup' category.");
        return;
    }

    System.out.print("Enter Product Brand: ");
    String productBrand = sc.next();

    // Handle invalid price input properly
    System.out.print("Enter Product Price: ");
    while (!sc.hasNextDouble()) {
        System.out.println("Invalid input. Please enter a valid price.");
        sc.next();  // Clear the invalid input
        System.out.print("Enter Product Price: ");
    }
    double productPrice = sc.nextDouble();

    while (productPrice <= 0) {
        System.out.print("Invalid price. Product price must be positive: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid price.");
            sc.next();  // Clear the invalid input
            System.out.print("Enter Product Price: ");
        }
        productPrice = sc.nextDouble();
    }

    System.out.print("Enter Product Quantity in Stock: ");
    int quantityInStock = sc.nextInt();

    while (quantityInStock < 0) {
        System.out.print("Quantity cannot be negative. Please enter a valid quantity: ");
        quantityInStock = sc.nextInt();
    }

    String sql = "INSERT INTO tbl_product (p_id, product_name, category, brand, price, quantity_in_stock) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
    conf.addRecord(sql, productId, productName, productCategory, productBrand, productPrice, quantityInStock);

    System.out.println("Product added successfully.");
}


    public void viewProducts() {
        String query = "SELECT p_id, product_name, category, brand, price, quantity_in_stock " +
                       "FROM tbl_product";

        String[] headers = {"Product ID", "Product Name", "Category", "Brand", "Price", "Quantity in Stock"};
        String[] columns = {"p_id", "product_name", "category", "brand", "price", "quantity_in_stock"};

        config conf = new config();
        conf.viewRecords(query, headers, columns); // Ensure this method fetches and displays results correctly
    }
public void updateProduct() {
    System.out.print("Enter Product ID to update: ");
    int productId = sc.nextInt();

    String productQuery = "SELECT p_id FROM tbl_product WHERE p_id=?";
    config conf = new config();
    while (conf.getSingleValue(productQuery, productId) == 0) {
        System.out.print("Product ID does not exist. Try again: ");
        productId = sc.nextInt();
    }

    System.out.print("Enter new Product Name: ");
    String productName = sc.next();

    if (productName.isEmpty()) {
        System.out.println("Product Name cannot be empty.");
        return;
    }

    System.out.print("Enter new Product Category (Haircare, Skincare, Makeup): ");
    String productCategory = sc.next().trim();

    if (!productCategory.equalsIgnoreCase("Haircare") &&
        !productCategory.equalsIgnoreCase("Skincare") &&
        !productCategory.equalsIgnoreCase("Makeup")) {
        System.out.println("Invalid category. Product must be in the 'Haircare', 'Skincare', or 'Makeup' category.");
        return;
    }

    System.out.print("Enter new Product Brand: ");
    String productBrand = sc.next();

    // Handle invalid price input properly
    System.out.print("Enter new Product Price: ");
    while (!sc.hasNextDouble()) {
        System.out.println("Invalid input. Please enter a valid price (numeric value).");
        sc.next();  // Clear the invalid input
        System.out.print("Enter new Product Price: ");
    }
    double productPrice = sc.nextDouble();

    while (productPrice <= 0) {
        System.out.print("Invalid price. Product price must be positive: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid price (numeric value).");
            sc.next();  // Clear the invalid input
            System.out.print("Enter new Product Price: ");
        }
        productPrice = sc.nextDouble();
    }

    System.out.print("Enter new Product Quantity in Stock: ");
    int quantityInStock = sc.nextInt();

    while (quantityInStock < 0) {
        System.out.print("Quantity cannot be negative. Please enter a valid quantity: ");
        quantityInStock = sc.nextInt();
    }

    String sql = "UPDATE tbl_product SET product_name = ?, category = ?, brand = ?, price = ?, quantity_in_stock = ? WHERE p_id = ?";
    conf.updateRecord(sql, productName, productCategory, productBrand, productPrice, quantityInStock, productId);

    System.out.println("Product updated successfully.");
}

    public void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int productId = sc.nextInt();

        String productQuery = "SELECT p_id FROM tbl_product WHERE p_id=?";
        config conf = new config();
        while (conf.getSingleValue(productQuery, productId) == 0) {
            System.out.print("Product ID does not exist. Try again: ");
            productId = sc.nextInt();
        }

        String sql = "DELETE FROM tbl_product WHERE p_id = ?";
        conf.deleteRecord(sql, productId);

        System.out.println("Product deleted successfully.");
    }


    public void closeScanner() {
        if (sc != null) {
            sc.close();
        }
    }
}

