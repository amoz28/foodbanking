package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {

    @Query(value ="SELECT i.id, i.name, i.credit_price, i.item_image, i.created_at, i.created_by, i.updated_at, i.updated_by FROM package_item p INNER JOIN item i on i.id = p.item_id WHERE p.package_id=:packageId", nativeQuery = true)
    List<Items> getItemsByPackage(@Param("packageId") Long packageId);


    Optional <Items> deleteAllById(Long id);
}
