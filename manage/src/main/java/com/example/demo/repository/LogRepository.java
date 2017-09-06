package com.example.demo.repository;

import com.example.demo.model.Logrecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Logrecord,Integer> {
}
