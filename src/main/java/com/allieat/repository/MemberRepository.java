package com.allieat.repository;



import com.allieat.entity.MemberVO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {
    //撈照片，用organizationId找
    @Query(value = "SELECT kycImage FROM member WHERE organizationId = :organizationId AND reviewed = 3", nativeQuery = true)
    String findPendingPicByOrgId(@Param("organizationId") Integer organizationId);
}
