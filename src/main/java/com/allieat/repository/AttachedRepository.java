package com.allieat.repository;

import com.allieat.entity.AttachedVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachedRepository extends JpaRepository<AttachedVO, Integer> {
}