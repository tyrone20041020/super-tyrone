package inventoryrecording;

import java.util.Scanner;

public class InventoryRecording {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        Product pr = new Product();
        inventoryRecords ir = new inventoryRecords();
        Reports reports = new Reports();

        do {
            System.out.println("\nMAIN MENU:");
            System.out.println("1. Product Management");
            System.out.println("2. Inventory Recording");
            System.out.println("3. Generate Reports");
            System.out.println("4. Exit");

            System.out.print("Enter Action: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                sc.next();  
                System.out.print("Enter Action: ");
            }
            int action = sc.nextInt();

            switch (action) {
                case 1:
                    pr.manageProduct();  // Updated method call
                    break;
                case 2:
                    ir.manageInventoryRecords(); 
                    break;
                case 3:
                    reports.generateReports(); 
                    break;
                case 4:
                    System.out.print("Exiting... Are you sure? (yes/no): ");
                    String resp = sc.next();
                    if (resp.equalsIgnoreCase("yes")) {
                        exit = true;
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }

        } while (!exit);

        System.out.println("Thank you, come again!");
    }
}
