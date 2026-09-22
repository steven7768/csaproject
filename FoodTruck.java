public class FoodTruck {

    // constants
    public static final String DEFAULT_NAME = "The Rolling Taco";
    public static final double DEFAULT_MONEY = 100.00;
    public static final double INGREDIENT_COST = 2.50;// per kit
    public static final int FRIDGE_CAPACITY = 100; // fridge size
    public static final double MIN_REPUTATION = 1.0;
    public static final double MAX_REPUTATION = 5.0;

    // fields
    private String name;
    private double money;
    private double startingMoney; // for profit at the end
    private int ingredients; // meal kits
    private double mealPrice;
    private double reputation; // 1-5
    private int totalMealsSold;

    // default everything
    public FoodTruck() {
        this(DEFAULT_NAME, DEFAULT_MONEY);
    }

    // just a name
    public FoodTruck(String name) {
        this(name, DEFAULT_MONEY);
    }

    // the main one, other 2 just call this
    public FoodTruck(String name, double startingMoney) {
        this.name = name;
        this.money = startingMoney;
        this.startingMoney = startingMoney;
        this.ingredients = 0; // empty fridge
        this.mealPrice = 8.00; // fair price to start
        this.reputation = 3.0; // start at 3 stars
        this.totalMealsSold = 0;
    }

    // getters
    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public int getIngredients() {
        return ingredients;
    }

    public double getMealPrice() {
        return mealPrice;
    }

    public double getReputation() {
        return reputation;
    }

    public int getTotalMealsSold() {
        return totalMealsSold;
    }

    public double getProfit() {
        return money - startingMoney;
    }

    // setters
    // no free/negative food (we are not a charity here)
    public void setMealPrice(double newPrice) {
        if (newPrice > 0) {
            mealPrice = newPrice;
        }
    }

    // clamp to 1-5
    public void setReputation(double newReputation) {
        if (newReputation > MAX_REPUTATION) {
            reputation = MAX_REPUTATION;
        } else if (newReputation < MIN_REPUTATION) {
            reputation = MIN_REPUTATION;
        } else {
            reputation = newReputation;
        }
    }

    // stuff the truck does
    public boolean canAfford(double cost) {
        return money >= cost;
    }

    public boolean buyIngredients(int amount) {
        double totalCost = amount * INGREDIENT_COST;
        boolean fitsInFridge = ingredients + amount <= FRIDGE_CAPACITY;

        // need all 3
        if (amount > 0 && canAfford(totalCost) && fitsInFridge) {
            money -= totalCost;
            ingredients += amount;
            return true;
        }
        return false;
    }

    // returns how many actually sold
    public int sellMeals(int customers) {
        // can't sell more than we have
        // fixed
        int mealsSold = Math.min(customers, ingredients);

        ingredients -= mealsSold;
        money += mealsSold * mealPrice;
        totalMealsSold += mealsSold;

        return mealsSold;
    }

    public int spoilIngredients(double spoilRate) {
        int spoiled = (int) (ingredients * spoilRate);
        ingredients -= spoiled;
        return spoiled;
    }

    public void payExpense(double amount) {
        money -= amount;
    }
    public boolean isBankrupt() {
        return ingredients == 0 && !canAfford(INGREDIENT_COST);
    }
}
