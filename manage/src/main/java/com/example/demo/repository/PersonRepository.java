package com.example.demo.repository;

import com.example.demo.model.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person,Integer>{
    public List<Person> findByAge(Integer age);
    public List<Person> findByNameLike(String name,Sort sort);
}
