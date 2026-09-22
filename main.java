import java.util.Scanner;

public class Main {

    public static final int MAX_DAYS = 7;
    public static final int HOURS_PER_SHIFT = 8;
    public static final int OPENING_HOUR = 11; //military time
    public static final double GOAL_MONEY = 1000.00;
    public static final double DAILY_PERMIT_FEE = 15.00;
    public static final double FAIR_PRICE = 8.00;
    public static final double SPOIL_RATE = 0.25;
    public static final double BASE_CUSTOMERS_PER_HOUR = 3.0;
    public static final double CRITIC_CHANCE = 0.04;
    public static final double DOWNPOUR_CHANCE = 0.30;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        printWelcome();

        // name
        System.out.print("Name your food truck (or press Enter for a default name): ");
        String truckName = input.nextLine().trim();

        //capitalize first letter might be not working
        truckName = truckName.substring(0, 1).toUpperCase() + truckName.substring(1);

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
            truck = new FoodTruck(truckName, startingMoney);  // constructor 3
        }

        System.out.println();
        System.out.println("Welcome to the business, " + truck.getName() + "!");

        // todo game loop

        input.close();
    }

    public static void printWelcome() {
        System.out.println("");
        System.out.println("          FOOD TRUCK SIMULATOR");
        System.out.println("");
        System.out.println("Run your own food truck for " + MAX_DAYS + " days.");
        System.out.printf("Reach $%.2f to WIN. Run out of money and you LOSE.%n", GOAL_MONEY);
        System.out.println();
    }

    // keeps asking till it gets a valid int
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
                    System.out.println("  Please enter a number from " + min + " to " + max + ".");
                }
            } else {
                String badInput = input.nextLine();
                System.out.println("  \"" + badInput.trim() + "\" is not a whole number. Try again.");
            }
        }
        return value;
    }
}
