package com.example.demo.repository;
import com.example.demo.model.Person_Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Person_PositionRepository extends JpaRepository<Person_Position,Integer> {

    //查找对应人员ID的所有职业相关对应表信息
    @Query(value = "select * from person_position where person_id=?", nativeQuery = true)
    List<Person_Position> myFindPId(Integer id);

}
