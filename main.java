import java.util.Scanner;

// food truck sim - main game
public class Main {

    public static final int MAX_DAYS = 7;
    public static final int HOURS_PER_SHIFT = 8;
    public static final int OPENING_HOUR = 11;
    public static final double GOAL_MONEY = 1000.00;
    public static final double DAILY_PERMIT_FEE = 15.00;
    public static final double FAIR_PRICE = 8.00;
    public static final double SPOIL_RATE = 0.25;
    public static final double BASE_CUSTOMERS_PER_HOUR = 3.0;
    public static final double DOWNPOUR_CHANCE = 0.30;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        printWelcome();

        // name
        System.out.print("Name your food truck (or press Enter for a default name): ");
        String truckName = input.nextLine().trim();

        if (truckName.length() > 0) {
            // capitalize first letter
            truckName = truckName.substring(0, 1).toUpperCase() + truckName.substring(1);
        }

        // difficulty
        System.out.println();
        System.out.println("Choose a difficulty:");
        System.out.println("  1) Easy   - start with $150");
        System.out.println("  2) Normal - start with $100");
        System.out.println("  3) Hard   - start with $60");
        int difficulty = getIntInRange(input, "Enter 1, 2, or 3: ", 1, 3);

        // pick which constructor
        FoodTruck truck;

        if (difficulty == 2) {
            if (truckName.length() == 0) {
                truck = new FoodTruck();              // constructor 1
            } else {
                truck = new FoodTruck(truckName);     // constructor 2
            }
        } else {
            double startingMoney;
            if (difficulty == 1) {
                startingMoney = 150.00;
            } else {
                startingMoney = 60.00;
            }

            if (truckName.length() == 0) {
                truckName = FoodTruck.DEFAULT_NAME;
            }
            truck = new FoodTruck(truckName, startingMoney);   // constructor 3
        }

        System.out.println();
        System.out.println("Welcome to the business, " + truck.getName() + "!");

        // main loop, 1 pass = 1 day
        int day = 1;
        boolean gameOver = false;

        while (!gameOver) {
            printStatus(truck, day);

            // morning menu, loops till they open or quit
            boolean opened = false;

            while (!opened && !gameOver) {
                System.out.println();
                System.out.println("  1) Buy meal kits   2) Change price   3) Open up   4) Quit");
                int choice = getIntInRange(input, "Your choice: ", 1, 4);

                if (choice == 1) {
                    buyKits(input, truck);
                } else if (choice == 2) {
                    setPrice(input, truck);
                } else if (choice == 3) {
                    // can't open with nothing to sell
                    if (truck.getIngredients() == 0) {
                        System.out.println("  Your fridge is empty! Buy some kits first.");
                    } else {
                        opened = true;
                    }
                } else {
                    gameOver = true;
                }
            }

            if (opened) {
                System.out.println("  (shift goes here)");

                // TEMP so it ends, real endings + money stuff later
                if (day >= MAX_DAYS) {
                    gameOver = true;
                } else {
                    day++;
                }
            }
        }

        System.out.println("Game over.");   // temp, real report later
        input.close();
    }

    public static void printWelcome() {
        System.out.println("===== FOOD TRUCK SIMULATOR =====");
        System.out.println("Run your truck for " + MAX_DAYS + " days.");
        System.out.printf("Reach $%.2f to win. Run out of money and you lose.%n", GOAL_MONEY);
        System.out.println();
    }

    public static void printStatus(FoodTruck truck, int day) {
        System.out.println();
        System.out.printf("--- DAY %d of %d ---%n", day, MAX_DAYS);
        System.out.printf("Cash: $%-8.2f Kits: %-4d Price: $%-6.2f Stars: %.1f%n",
                truck.getMoney(), truck.getIngredients(), truck.getMealPrice(), truck.getReputation());
    }

    public static void buyKits(Scanner input, FoodTruck truck) {
        // $47.50 / 2.50 = 19.0 -> 19
        int maxKits = (int) (truck.getMoney() / FoodTruck.INGREDIENT_COST);
        int fridgeSpace = FoodTruck.FRIDGE_CAPACITY - truck.getIngredients();

        if (maxKits > fridgeSpace) {
            maxKits = fridgeSpace;
        }

        if (maxKits == 0) {
            System.out.println("  No money or no fridge space.");
        } else {
            System.out.printf("  $%.2f each, you can afford %d.%n", FoodTruck.INGREDIENT_COST, maxKits);
            int amount = getIntInRange(input, "  How many? (0 to cancel): ", 0, maxKits);

            if (truck.buyIngredients(amount)) { // true = it worked
                System.out.printf("  Bought %d. Kits: %d   Cash: $%.2f%n",
                        amount, truck.getIngredients(), truck.getMoney());
            } else {
                System.out.println("  No kits bought.");
            }
        }
    }

    public static void setPrice(Scanner input, FoodTruck truck) {
        System.out.printf("  Price is $%.2f, people think $%.2f is fair.%n",
                truck.getMealPrice(), FAIR_PRICE);
        int newPrice = getIntInRange(input, "  New price ($1 - $20): ", 1, 20);
        truck.setMealPrice(newPrice);
        System.out.printf("  Price is now $%.2f%n", truck.getMealPrice());
    }

    //keeps asking till it gets a valid int
    public static int getIntInRange(Scanner input, String prompt, int min, int max) {
        int value = 0;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);

            if (input.hasNextInt()) {
                value = input.nextInt();
                input.nextLine();

                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.println("  Enter a number from " + min + " to " + max + ".");
                }
            } else {
                String bad = input.nextLine();
                System.out.println("  \"" + bad.trim() + "\" is not a whole number.");
            }
        }
        return value;
    }
}
