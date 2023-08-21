package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

//    @Query(value ="select * from user_history where user_id=:user_id", nativeQuery = true)
//    List <UserHistory> findAllByUser_id2(@Param("user_id") Long user_id);

    List <UserHistory> findAllByUserId(Long userId);


}
