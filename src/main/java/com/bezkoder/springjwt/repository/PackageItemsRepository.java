package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.PackageItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageItemsRepository extends JpaRepository<PackageItems, Long> {

    @Query(value ="select * from package_item where packageId=:packageId", nativeQuery = true)
    PackageItems findByPackageId(@Param("packageId") Long packageId);

    Optional <PackageItems> deleteByItemId(Long itemId);

    @Query(value ="select count(*) from package_item where package_id=:packageId and item_id=:itemId", nativeQuery = true)
    int checkIfItemExist(Long itemId, Long packageId);

    @Transactional
    void deleteByItemIdAndPackageId(@Param("itemId") Long itemId, @Param("packageId") Long packageId);
}
