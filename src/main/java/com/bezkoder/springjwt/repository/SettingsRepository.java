package com.bezkoder.springjwt.repository;


import com.bezkoder.springjwt.models.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
	@Query(value ="select * from setting", nativeQuery = true)
	Settings getApplicationSetting();
}
