package com.allieat.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allieat.entity.FoodVO;

@Repository
public interface FoodRepository extends JpaRepository<FoodVO, Integer> {
}