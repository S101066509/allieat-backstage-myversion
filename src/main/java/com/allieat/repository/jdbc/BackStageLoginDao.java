package com.allieat.repository.jdbc;




import com.allieat.entity.AdminVO;

import java.util.List;


public interface BackStageLoginDao {
     List<AdminVO>  findByAccount(AdminVO admin);
}
