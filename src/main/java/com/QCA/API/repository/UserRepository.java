package com.QCA.API.repository;
import org.springframework.data.repository.CrudRepository;

import com.QCA.API.model.MyUser;
public interface UserRepository extends CrudRepository<MyUser, Integer> {
    MyUser findByUsername(String username);
}