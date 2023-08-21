package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Packages, Long> {

    @Query(value ="select * from package", nativeQuery = true)
    List<Packages> getAllPackages();

    void deleteById(Long aLong);


    @Override
    Optional<Packages> findById(Long aLong);
}
