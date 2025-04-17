package com.allieat.repository;


import com.allieat.entity.DonationVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<DonationVO, Integer> {
    DonationVO findTopByOrderByRankIdDesc();
}
