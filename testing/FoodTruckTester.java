public class FoodTruckTester {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        // constructors
        FoodTruck defaultTruck = new FoodTruck();
        check("no-arg constructor uses default name", defaultTruck.getName().equals(FoodTruck.DEFAULT_NAME));
        check("no-arg constructor uses default money", defaultTruck.getMoney() == FoodTruck.DEFAULT_MONEY);

        FoodTruck namedTruck = new FoodTruck("Burger Bus");
        check("name constructor keeps the name", namedTruck.getName().equals("Burger Bus"));
        check("name constructor uses default money", namedTruck.getMoney() == FoodTruck.DEFAULT_MONEY);

        FoodTruck hardTruck = new FoodTruck("Hard Mode", 60.0);
        check("full constructor sets money", hardTruck.getMoney() == 60.0);
        check("fridge starts empty", hardTruck.getIngredients() == 0);
        check("starts at 3 stars", hardTruck.getReputation() == 3.0);

        // buying
        FoodTruck truck = new FoodTruck("Test Truck", 10.0);
        check("buying 4 kits with $10 works", truck.buyIngredients(4));
        check("money is now $0", truck.getMoney() == 0.0);
        check("fridge has 4 kits", truck.getIngredients() == 4);
        check("buying with no money fails", !truck.buyIngredients(1));
        check("buying negative kits fails", !truck.buyIngredients(-3));
        check("buying 0 kits fails", !truck.buyIngredients(0));

        FoodTruck richTruck = new FoodTruck("Rich", 1000.0);
        check("can't overfill the fridge", !richTruck.buyIngredients(FoodTruck.FRIDGE_CAPACITY + 1));

        // selling
        int sold = truck.sellMeals(10);
        check("can't sell more meals than we have kits", sold == 4);
        check("fridge is empty, not negative", truck.getIngredients() == 0);
        check("selling 4 at $8 earns $32", truck.getMoney() == 32.0);
        check("meals sold is tracked", truck.getTotalMealsSold() == 4);
        check("profit is cash minus what we started with", truck.getProfit() == 22.0);

        // setters
        truck.setReputation(9.0);
        check("stars can't go above 5", truck.getReputation() == 5.0);
        truck.setReputation(-2.0);
        check("stars can't go below 1", truck.getReputation() == 1.0);
        truck.setMealPrice(-5.0);
        check("negative price is ignored", truck.getMealPrice() == 8.0);

        // spoiling + going broke
        FoodTruck spoilTruck = new FoodTruck("Spoil", 100.0);
        spoilTruck.buyIngredients(7);
        check("25% of 7 kits spoils 1 (cast cuts 1.75)", spoilTruck.spoilIngredients(0.25) == 1);

        FoodTruck brokeTruck = new FoodTruck("Broke", 2.0);
        check("no kits and under $2.50 is broke", brokeTruck.isBankrupt());

        System.out.println();
        System.out.println("Passed: " + passed + "   Failed: " + failed);
    }

    public static void check(String description, boolean result) {
        if (result) {
            System.out.println("  PASS: " + description);
            passed++;
        } else {
            System.out.println("  FAIL: " + description);
            failed++;
        }
    }
}
