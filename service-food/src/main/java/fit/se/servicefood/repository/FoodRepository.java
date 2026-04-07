package fit.se.servicefood.repository;


import fit.se.servicefood.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByCategory(String category);
    List<Food> findByAvailableTrue();
    List<Food> findByNameContainingIgnoreCase(String name);
}