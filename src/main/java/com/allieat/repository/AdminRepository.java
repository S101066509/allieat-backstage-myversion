package com.allieat.repository;

import com.allieat.entity.AdminVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminVO, Integer> {
    Optional<AdminVO> findByAccount(String account);
}
