package com.allieat.repository;




import com.allieat.entity.PayDetailVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayDetailRepository extends JpaRepository<PayDetailVO, Integer> {
}
