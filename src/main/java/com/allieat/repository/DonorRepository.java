package com.allieat.repository;


import com.allieat.entity.DonaVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.Timestamp;
import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<DonaVO, Integer> {
    @Query(value = "SELECT COUNT(*) FROM donationrecord WHERE createdTime BETWEEN DATE_SUB(NOW(), INTERVAL 1 MONTH) AND NOW()", nativeQuery = true)
    long countDonorLastMonth();
}
