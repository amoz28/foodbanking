package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Deposit;
import com.bezkoder.springjwt.models.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

}
