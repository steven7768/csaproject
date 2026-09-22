// Parker Comments: 
//  Lookin good brah, didnt find any errors
//  Inputs have failsafe
//  All functions correct

// looks good no errors - soha

import java.util.Scanner;

// food truck sim - main game
public class Main {

    // game settings - tweak these for balance
    public static final int MAX_DAYS = 7;
    public static final int HOURS_PER_SHIFT = 8;
    public static final int OPENING_HOUR = 11;// 11am (24hr)
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
                truck = new FoodTruck(); // constructor 1
            } else {
                truck = new FoodTruck(truckName);// constructor 2
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
            truck = new FoodTruck(truckName, startingMoney);// constructor 3
        }

        System.out.println();
        System.out.println("Welcome to the business, " + truck.getName() + "!");

        // main loop, 1 pass = 1 day
        int day = 1;
        boolean gameOver = false;

        while (!gameOver) {
            String weather = pickWeather();
            printStatus(truck, day, weather);

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
                    // can't open with nothing to sell :(
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
                runShift(truck, weather);
                boolean paidPermit = endOfDay(truck);

                // order matters! lose > win > out of days
                if (!paidPermit || truck.isBankrupt()) {
                    System.out.println(truck.getName() + " is out of business!");
                    gameOver = true;
                } else if (truck.getMoney() >= GOAL_MONEY) {
                    System.out.printf("YOU WIN! You hit $%.2f on day %d.%n", truck.getMoney(), day);
                    gameOver = true;
                } else if (day >= MAX_DAYS) {
                    System.out.println("The season is over!");
                    gameOver = true;
                } else {
                    day++;
                }
            }
        }

        printFinalReport(truck, day);
        input.close();
    }

    public static void printWelcome() {
        System.out.println("===== FOOD TRUCK SIMULATOR =====");
        System.out.println("Run your truck for " + MAX_DAYS + " days.");
        System.out.printf("Reach $%.2f to win. Run out of money and you lose.%n", GOAL_MONEY);
        System.out.println();
    }

    public static void printStatus(FoodTruck truck, int day, String weather) {
        System.out.println();
        System.out.printf("--- DAY %d of %d --- %s ---%n", day, MAX_DAYS, weather);
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

            if (truck.buyIngredients(amount)) {   // true = it worked
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

    public static void runShift(FoodTruck truck, String weather) {
        System.out.println("--- opening the window ---");

        int customersToday = 0;
        int servedToday = 0;
        double moneyBefore = truck.getMoney();

        // 8 hr shift
        for (int hour = 0; hour < HOURS_PER_SHIFT; hour++) {
            int clockHour = OPENING_HOUR + hour;   // 11..18

            // out of food -> close early
            if (truck.getIngredients() == 0) {
                System.out.println("  SOLD OUT, closing early at " + clockHour + ":00");
                break;
            }

            // downpour- skip this hour
            if (weather.equals("Rainy") && Math.random() < DOWNPOUR_CHANCE) {
                System.out.println("  " + clockHour + ":00   downpour, nobody came by");
                continue;
            }

            int customers = countCustomers(truck, weather, clockHour);
            int served = truck.sellMeals(customers);

            customersToday += customers;
            servedToday += served;

            System.out.printf("  %d:00   came: %2d   served: %2d%n", clockHour, customers, served);
        }

        double earned = truck.getMoney() - moneyBefore;
        System.out.printf("Sold %d meals, made $%.2f%n", servedToday, earned);

        if (customersToday > 0) {
            // need the (double) or 9/10 = 0
            double happy = (double) servedToday / customersToday * 100;
            System.out.printf("Happy customers: %.0f%%%n", happy);

            if (happy >= 80 && truck.getMealPrice() <= FAIR_PRICE) {
                System.out.println("Good day! +0.5 stars");
                truck.setReputation(truck.getReputation() + 0.5);
            } else if (happy < 50 || truck.getMealPrice() > FAIR_PRICE * 1.5) {
                System.out.println("People left hungry or thought you were pricey. -0.5 stars");
                truck.setReputation(truck.getReputation() - 0.5);
            }
        }
    }

    // base crowd, then multipliers
    public static int countCustomers(FoodTruck truck, String weather, int clockHour) {
        double customers = BASE_CUSTOMERS_PER_HOUR;

        // weather
        if (weather.equals("Sunny")) {
            customers *= 1.5;
        } else if (weather.equals("Rainy")) {
            customers *= 0.5;
        }
        // cloudy = no change

        // lunch rush
        if (clockHour == 12 || clockHour == 13) {
            customers *= 2.0;
        }

        // squared so high prices actually hurt
        // (plain ratio = same revenue at any price)
        double priceRatio = FAIR_PRICE / truck.getMealPrice();
        customers *= priceRatio * priceRatio;

        // 3 stars = normal crowd
        customers *= truck.getReputation() / 3.0;

        // random 80-120%
        customers *= 0.8 + Math.random() * 0.4;

        return (int) Math.round(customers);
    }

    // false = couldn't pay permit
    public static boolean endOfDay(FoodTruck truck) {
        int spoiled = truck.spoilIngredients(SPOIL_RATE);

        if (spoiled > 0) {
            System.out.println(spoiled + " leftover kit(s) spoiled overnight.");
        }

        if (truck.canAfford(DAILY_PERMIT_FEE)) {
            truck.payExpense(DAILY_PERMIT_FEE);
            System.out.printf("Paid the $%.2f permit. Cash: $%.2f%n", DAILY_PERMIT_FEE, truck.getMoney());
            return true;
        }

        System.out.printf("You can't afford the $%.2f permit!%n", DAILY_PERMIT_FEE);
        return false;
    }

    public static void printFinalReport(FoodTruck truck, int daysPlayed) {
        double profit = truck.getProfit();

        System.out.println();
        System.out.println("===== FINAL REPORT =====");
        System.out.printf("%-14s %s%n", "Truck:", truck.getName());
        System.out.printf("%-14s %d%n", "Days:", daysPlayed);
        System.out.printf("%-14s %d%n", "Meals sold:", truck.getTotalMealsSold());
        System.out.printf("%-14s $%.2f%n", "Cash:", truck.getMoney());
        System.out.printf("%-14s $%.2f%n", "Profit:", profit);

        // grade
        if (truck.getMoney() >= GOAL_MONEY) {
            System.out.println("Grade: A+   food truck legend");
        } else if (profit > 0) {
            System.out.println("Grade: B    you made money");
        } else {
            System.out.println("Grade: F    better luck next season");
        }
    }

    // 50% sunny, 30% cloudy, 20% rain
    public static String pickWeather() {
        double roll = Math.random();

        if (roll < 0.5) {
            return "Sunny";
        } else if (roll < 0.8) {
            return "Cloudy";
        } else {
            return "Rainy";
        }
    }

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
