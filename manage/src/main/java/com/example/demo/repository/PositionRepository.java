package com.example.demo.repository;

import com.example.demo.model.Person;
import com.example.demo.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position,Integer> {

    //查找对应职位信息
    @Query(value = "select * from position where id=?", nativeQuery = true)
    Position myFindPo(Integer id);


}
