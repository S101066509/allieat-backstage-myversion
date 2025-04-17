package com.allieat.repository;




import com.allieat.entity.PhotoVO;
import com.allieat.entity.StoreVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoVO, Integer> {

}
