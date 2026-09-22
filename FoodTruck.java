public class FoodTruck {

    public static final String DEFAULT_NAME = "The Rolling Taco";
    public static final double DEFAULT_MONEY = 100.00;
    public static final double INGREDIENT_COST = 2.50;   // per kit
    public static final int FRIDGE_CAPACITY = 100;       // fridge size
    public static final double MIN_REPUTATION = 1.0;
    public static final double MAX_REPUTATION = 5.0;

    // fields
    private String name;
    private double money;
    private double startingMoney;
    private int ingredients;
    private double mealPrice;
    private double reputation;
    private int totalMealsSold;
    private double totalRevenue;

    // default everything
    public FoodTruck() {
        this(DEFAULT_NAME, DEFAULT_MONEY);
    }

    // name
    public FoodTruck(String name) {
        this(name, DEFAULT_MONEY);
    }

    // the main one, other 2 just call this
    public FoodTruck(String name, double startingMoney) {
        this.name = name;
        this.money = startingMoney;
        this.startingMoney = startingMoney;
        this.ingredients = 0;       // empty fridge
        this.mealPrice = 8.00;      // fair price to start
        this.reputation = 3.0;      // start at 3 stars
        this.totalMealsSold = 0;
        this.totalRevenue = 0.0;
    }

    // need to mak e buy, sell, spoil, pay, bankrupt
}
