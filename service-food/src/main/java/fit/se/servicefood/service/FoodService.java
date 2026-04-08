package fit.se.servicefood.service;

import fit.se.servicefood.model.Food;
import fit.se.servicefood.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    // Get all foods
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    // Get food by ID
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }

    // Add new food
    public Food addFood(Food food) {
        return foodRepository.save(food);
    }

    // Update food
    public Food updateFood(Long id, Food foodDetails) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        food.setName(foodDetails.getName());
        food.setDescription(foodDetails.getDescription());
        food.setPrice(foodDetails.getPrice());
        food.setCategory(foodDetails.getCategory());
        food.setAvailable(foodDetails.getAvailable());

        return foodRepository.save(food);
    }

    // Delete food
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    // Search by category
    public List<Food> getFoodsByCategory(String category) {
        return foodRepository.findByCategory(category);
    }

    // Get available foods
    public List<Food> getAvailableFoods() {
        return foodRepository.findByAvailableTrue();
    }

    // Search by name
    public List<Food> searchFoodsByName(String name) {
        return foodRepository.findByNameContainingIgnoreCase(name);
    }
}