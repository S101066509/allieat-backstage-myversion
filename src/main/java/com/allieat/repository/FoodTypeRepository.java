package com.allieat.repository;



import com.allieat.entity.FoodTypeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodTypeVO, Integer> {
}
