package fit.se.servicefood.controller;


import fit.se.servicefood.model.Food;
import fit.se.servicefood.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000") // Cho phép React gọi API
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    // GET: Lấy danh sách tất cả món ăn
    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        List<Food> foods = foodService.getAllFoods();
        return ResponseEntity.ok(foods);
    }

    // GET: Lấy món ăn theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        Optional<Food> food = foodService.getFoodById(id);
        return food.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Thêm món ăn mới
    @PostMapping
    public ResponseEntity<?> createFood(@RequestBody Food food) {
        try {
            Food savedFood = foodService.addFood(food);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFood);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Thêm món ăn thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT: Cập nhật món ăn theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody Food food) {
        try {
            Food updatedFood = foodService.updateFood(id, food);
            return ResponseEntity.ok(updatedFood);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cập nhật thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DELETE: Xóa món ăn theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xóa món ăn thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Xóa món ăn thất bại!");
            return ResponseEntity.badRequest().body(error);
        }
    }

    // GET: Tìm kiếm món ăn theo tên
    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFoods(@RequestParam(required = false) String keyword) {
        List<Food> foods;
        if (keyword != null && !keyword.isEmpty()) {
            foods = foodService.searchFoodsByName(keyword);
        } else {
            foods = foodService.getAllFoods();
        }
        return ResponseEntity.ok(foods);
    }

    // GET: Lọc món ăn theo danh mục
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Food>> getFoodsByCategory(@PathVariable String category) {
        List<Food> foods = foodService.getFoodsByCategory(category);
        return ResponseEntity.ok(foods);
    }

    // GET: Lấy danh sách món ăn còn hàng
    @GetMapping("/available")
    public ResponseEntity<List<Food>> getAvailableFoods() {
        List<Food> foods = foodService.getAvailableFoods();
        return ResponseEntity.ok(foods);
    }
}