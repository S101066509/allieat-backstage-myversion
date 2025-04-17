package com.allieat.repository;




import com.allieat.entity.OrderFoodVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFoodRepository extends JpaRepository<OrderFoodVO, Integer> {
    @Query(value = """
    SELECT DATE_FORMAT(pickTime, '%Y-%m') AS labels, 
               COUNT(*) AS data
    FROM orderList
    WHERE pickStat = 1 
      AND pickTime >= DATE_SUB(CURDATE(), INTERVAL 11 MONTH)
    GROUP BY labels
    ORDER BY labels
    """, nativeQuery = true)
    List<Object[]> findMonthlyPickedOrders();



}
