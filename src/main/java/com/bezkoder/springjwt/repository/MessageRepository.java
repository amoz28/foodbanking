package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List <Message> findAllByRecipient(Long user_id);

//    Optional <Message> deleteAllById(Long id);
}
