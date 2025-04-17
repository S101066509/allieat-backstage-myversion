package com.allieat.repository;




import com.allieat.entity.OrganizationVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationVO, Integer> {
    Optional<OrganizationVO> findByName(String name);
}
