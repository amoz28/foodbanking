package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.MarkReadMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkReadMessageRepository extends JpaRepository<MarkReadMessage, Long> {

	@Query(value ="update message_read set isMessageRead=true where user_id=:user_id and message_id=:message_id", nativeQuery = true)
	MarkReadMessage messageReadStatus(@Param("user_id") Long user_id, @Param("message_id") Long message_id);

}
