public class FoodTruckTester {

    public static void main(String[] args) {
        // constructors
        FoodTruck defaultTruck = new FoodTruck();
        check("no-arg constructor uses default name", defaultTruck.getName().equals(FoodTruck.DEFAULT_NAME));
        check("no-arg constructor uses default money", defaultTruck.getMoney() == FoodTruck.DEFAULT_MONEY);

        FoodTruck namedTruck = new FoodTruck("Burger Bus");
        check("name constructor keeps the name", namedTruck.getName().equals("Burger Bus"));

        FoodTruck fullTruck = new FoodTruck("Hard Mode", 60.0);
        check("full constructor sets money", fullTruck.getMoney() == 60.0);
        check("fridge starts empty", fullTruck.getIngredients() == 0);

        // buying
        FoodTruck truck = new FoodTruck("Test Truck", 10.0);
        check("buying 4 kits with $10 works", truck.buyIngredients(4));
        check("money is now $0", truck.getMoney() == 0.0);
        check("buying with no money fails", !truck.buyIngredients(1));
        check("buying negative kits fails", !truck.buyIngredients(-3));

        // selling
        int sold = truck.sellMeals(10);
        check("can't sell more meals than we have kits", sold == 4);
        check("fridge is empty, not negative", truck.getIngredients() == 0);

        // setters
        truck.setReputation(9.0);
        check("reputation can't go above 5", truck.getReputation() == 5.0);
        truck.setReputation(-2.0);
        check("reputation can't go below 1", truck.getReputation() == 1.0);

        // spoilage
        FoodTruck spoilTruck = new FoodTruck("Spoil", 100.0);
        spoilTruck.buyIngredients(7);
        check("25% of 7 kits spoils 1", spoilTruck.spoilIngredients(0.25) == 1);
    }

    public static void check(String description, boolean passed) {
        if (passed) {
            System.out.println("  PASS: " + description);
        } else {
            System.out.println("  FAIL: " + description);
        }
    }
}
