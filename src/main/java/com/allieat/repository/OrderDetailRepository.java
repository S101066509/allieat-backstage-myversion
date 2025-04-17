package com.allieat.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allieat.entity.OrderDetailId;
import com.allieat.entity.OrderDetailVO;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailVO, OrderDetailId> {

}