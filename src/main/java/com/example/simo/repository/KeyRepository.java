package com.example.simo.repository;

import com.example.simo.model.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
    Key findByConsumerKey(String consumerKey);
    Key findByUser_UserName(String userName);
    Key findByUser_Token_Token(String token);
}
