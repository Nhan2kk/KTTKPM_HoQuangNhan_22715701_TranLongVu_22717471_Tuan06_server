package fit.se.servicefood.config;

import fit.se.servicefood.model.Food;
import fit.se.servicefood.repository.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(FoodRepository foodRepository) {
        return args -> {
            // Only seed data if the database is empty
            if (foodRepository.count() == 0) {
                // Breakfast foods
                foodRepository.save(new Food(null, "Pancakes", "Fluffy pancakes with butter and maple syrup", 8.99, "https://via.placeholder.com/200?text=Pancakes", "Breakfast", true));
                foodRepository.save(new Food(null, "Omelette", "Three egg omelette with cheese and vegetables", 10.99, "https://via.placeholder.com/200?text=Omelette", "Breakfast", true));
                foodRepository.save(new Food(null, "Toast & Jam", "Toasted bread with jam and butter", 5.99, "https://via.placeholder.com/200?text=Toast", "Breakfast", true));

                // Main courses
                foodRepository.save(new Food(null, "Burger", "Juicy beef burger with lettuce, tomato, and special sauce", 12.99, "https://via.placeholder.com/200?text=Burger", "Main", true));
                foodRepository.save(new Food(null, "Grilled Chicken", "Grilled chicken breast with herb seasoning", 14.99, "https://via.placeholder.com/200?text=Chicken", "Main", true));
                foodRepository.save(new Food(null, "Pasta Carbonara", "Creamy pasta with bacon and parmesan cheese", 13.99, "https://via.placeholder.com/200?text=Pasta", "Main", true));
                foodRepository.save(new Food(null, "Steak", "Premium 8oz beef steak with vegetables", 24.99, "https://via.placeholder.com/200?text=Steak", "Main", true));

                // Sides
                foodRepository.save(new Food(null, "French Fries", "Crispy golden french fries with seasoning", 4.99, "https://via.placeholder.com/200?text=Fries", "Side", true));
                foodRepository.save(new Food(null, "Caesar Salad", "Fresh romaine lettuce with caesar dressing and croutons", 8.99, "https://via.placeholder.com/200?text=Salad", "Side", true));
                foodRepository.save(new Food(null, "Soup of the Day", "Chef's special soup - changes daily", 6.99, "https://via.placeholder.com/200?text=Soup", "Side", true));

                // Desserts
                foodRepository.save(new Food(null, "Chocolate Cake", "Rich chocolate cake with chocolate frosting", 7.99, "https://via.placeholder.com/200?text=Cake", "Dessert", true));
                foodRepository.save(new Food(null, "Ice Cream", "Vanilla ice cream with chocolate sauce", 5.99, "https://via.placeholder.com/200?text=IceCream", "Dessert", true));
                foodRepository.save(new Food(null, "Apple Pie", "Homemade apple pie with whipped cream", 6.99, "https://via.placeholder.com/200?text=Pie", "Dessert", true));

                // Beverages
                foodRepository.save(new Food(null, "Soft Drink", "Coca Cola, Sprite, or Fanta (your choice)", 2.99, "https://via.placeholder.com/200?text=Drink", "Beverage", true));
                foodRepository.save(new Food(null, "Iced Tea", "Refreshing iced tea with lemon", 3.99, "https://via.placeholder.com/200?text=Tea", "Beverage", true));
                foodRepository.save(new Food(null, "Coffee", "Fresh brewed coffee - hot or iced", 3.99, "https://via.placeholder.com/200?text=Coffee", "Beverage", true));

                System.out.println("✓ Food database initialized with sample data!");
            }
        };
    }
}
