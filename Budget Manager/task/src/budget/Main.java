package budget;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static float balance = 0f;
    private static final Map<String, Float> totalPurchases = new HashMap<>();
    private static final Map<String, List<String>> purchases = new HashMap<>();

    public static void main(String[] args) {

        var action = 0;
        do {
            action = printMenuAndGetAction();
            System.out.println();
            switch (action) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addPurchase();
                    break;
                case 3:
                    showListOfPurchases();
                    break;
                case 4:
                    printBalance();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    load();
                    break;
                case 7:
                    analyze();
                case 0:
                    System.out.println("Bye!");
                    break;
                default:
                    break;
            }
        } while (action != 0);
    }

    private static int printMenuAndGetAction() {
        System.out.println(
                "Choose your action:\n" +
                        "1) Add income\n" +
                        "2) Add purchase\n" +
                        "3) Show list of purchases\n" +
                        "4) Balance\n" +
                        "5) Save\n" +
                        "6) Load\n" +
                        "7) Analyze (Sort)\n" +
                        "0) Exit");

        return Integer.parseInt(scanner.nextLine());
    }

    private static void addIncome() {
        System.out.println("Enter income:");
        balance += Float.parseFloat(scanner.nextLine());
        System.out.println("Income was added!\n");
    }

    private static void addPurchase() {
        String option = printPurchaseTypesAndGetOption();
        while (option != null) {
            var purchaseBuilder = new StringBuilder();
            System.out.println("Enter purchase name:");
            purchaseBuilder.append(scanner.nextLine()).append(" $");

            System.out.println("Enter its price:");
            final var price = Float.parseFloat(scanner.nextLine());
            purchaseBuilder.append(price);

            countPurchase(option, purchaseBuilder.toString(), price);

            System.out.println("Purchase was added!\n");

            option = printPurchaseTypesAndGetOption();
        }
    }

    private static void countPurchase(String option, String purchase, Float price) {
        if (purchase.substring(purchase.lastIndexOf('.')).length() == 2) {
            purchase = purchase + "0";
        }

        var list = purchases.getOrDefault(option, new ArrayList<>());
        list.add(purchase);
        purchases.put(option, list);

        var all = purchases.getOrDefault("All", new ArrayList<>());
        all.add(purchase);
        purchases.put("All", all);

        balance -= price;
        totalPurchases.put(option, totalPurchases.getOrDefault(option, 0f) + price);
        totalPurchases.put("All", totalPurchases.getOrDefault("All", 0f) + price);
    }

    private static String printPurchaseTypesAndGetOption() {
        System.out.println("Choose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) Back");

        var option = Integer.parseInt(scanner.nextLine());
        System.out.println();

        return getTypeByOption(option);
    }

    private static String getTypeByOption(int option) {
        switch (option) {
            case 1:
                return "Food";
            case 2:
                return "Clothes";
            case 3:
                return "Entertainment";
            case 4:
                return "Other";
            default:
                break;
        }
        return null;
    }

    private static int getOptionByType(String type) {
        switch (type) {
            case "Food":
                return 1;
            case "Clothes":
                return 2;
            case "Entertainment":
                return 3;
            case "Other":
                return 4;
            default:
                break;
        }
        return 0;
    }

    private static void showListOfPurchases() {
        if (purchases.isEmpty()) {
            System.out.println("Purchase list is empty!\n");
        } else {
            var option = printPurchaseTypesForListAndGetOption();
            while (option != null) {
                System.out.println(option + ":");
                if (purchases.getOrDefault(option, new ArrayList<>()).isEmpty()) {
                    System.out.println("Purchase list is empty!\n");
                } else {
                    for (var purchase : purchases.get(option)) {
                        System.out.println(purchase);
                    }
                    System.out.printf("Total sum: $%.2f\n\n", totalPurchases.get(option));
                }

                option = printPurchaseTypesForListAndGetOption();
            }
        }
    }

    private static String printPurchaseTypesForListAndGetOption() {
        System.out.println("Choose the type of purchases\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) All\n" +
                "6) Back");

        var option = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (option) {
            case 1:
                return "Food";
            case 2:
                return "Clothes";
            case 3:
                return "Entertainment";
            case 4:
                return "Other";
            case 5:
                return "All";
            default:
                break;
        }
        return null;
    }

    private static void printBalance() {
        if (balance < 0) {
            balance = 0;
        }
        System.out.printf("Balance: $%.2f\n\n", balance);
    }

    private static void save() {
        var file = new File("purchases.txt");

        try {
            var isCreated = file.createNewFile();

            if (isCreated) {
                var writer = new FileWriter(file);

                for (var purchaseList : purchases.entrySet()) {
                    if (!purchaseList.getKey().equals("All")) {
                        writer.write(getOptionByType(purchaseList.getKey()) + "\n");
                        for (var purchase : purchaseList.getValue()) {
                            writer.write(purchase + "\n");
                        }
                    }
                }
                writer.close();

                System.out.println("Purchases were saved!\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    private static void load() {
        var file = new File("purchases.txt");

        try {
            if (file.exists()) {
                var reader = new Scanner(file);

                purchases.clear();
                totalPurchases.clear();
                balance = 1000;

                var option = 0;
                String type = null;
                while (reader.hasNextLine()) {
                    var line = reader.nextLine();
                    try {
                        option = Integer.parseInt(line);
                        type = getTypeByOption(option);
                        line = reader.nextLine();
                    } catch (NumberFormatException ignored) {
                    }

                    if (type == null) {
                        var loadedBalance = Float.parseFloat(line);
                        balance += loadedBalance;
                    } else {
                        var price = Float.parseFloat(line.substring(line.lastIndexOf('$') + 1));

                        countPurchase(type, line, price);
                    }
                }
                reader.close();

                System.out.println("Purchases were loaded!\n");
            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    private static void analyze() {
        var option = printSortsAndGetOption();
        while (option != 4) {
            switch (option) {
                case 1:
                    printSortAll();
                    break;
                case 2:
                    printSortByType();
                    break;
                case 3:
                    printSortCertainType();
                    break;
                default:
                    break;
            }
            option = printSortsAndGetOption();
        }
    }

    private static void printSortCertainType() {
        System.out.println("Choose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other");

        var option = Integer.parseInt(scanner.nextLine());
        System.out.println();

        var action = getTypeByOption(option);
        var list = purchases.getOrDefault(action, new ArrayList<>());

        if (list.isEmpty()) {
            System.out.println("Purchase list is empty!\n");
        } else {
            orderByBubbleSort(list);
            printList(list);
        }
    }

    private static void printSortByType() {
        System.out.println("Types:");
        var list = new ArrayList<String>();

        var df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        list.add("Food - $" + df.format(totalPurchases.getOrDefault("Food", 0F)));
        list.add("Entertainment - $" + df.format(totalPurchases.getOrDefault("Entertainment", 0F)));
        list.add("Clothes - $" + df.format(totalPurchases.getOrDefault("Clothes", 0F)));
        list.add("Other - $" + df.format(totalPurchases.getOrDefault("Other", 0F)));

        orderByBubbleSort(list);
        list.add("Total sum - $" + df.format(totalPurchases.getOrDefault("All", 0F)));
        printList(list);
    }

    private static void printSortAll() {
        var list = purchases.getOrDefault("All", new ArrayList<>());
        if (list.isEmpty()) {
            System.out.println("Purchase list is empty!\n");
        } else {
            orderByBubbleSort(list);
            printList(list);
        }
    }

    private static void orderByBubbleSort(List<String> list) {
        for (var i = 0; i < list.size() - 1; i++) {
            for (var j = 0; j < list.size() - i - 1; j++) {
                if (Float.parseFloat(list.get(j).substring(list.get(j).lastIndexOf('$') + 1)) <
                        Float.parseFloat(list.get(j + 1).substring(list.get(j + 1).lastIndexOf('$') + 1))) {
                    final var temp = list.get(j + 1);
                    list.set(j + 1, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }

    private static void printList(List<String> list) {
        for (var item : list) {
            System.out.println(item);
        }
        System.out.println();
    }

    private static int printSortsAndGetOption() {
        System.out.println("How do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back");

        var option = Integer.parseInt(scanner.nextLine());
        System.out.println();

        return option;
    }
}
