package com.example.demo.control;

import com.example.demo.model.Person;
import com.example.demo.model.Position;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.PositionRepository;
import com.example.demo.resp.CreditsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonControl {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;
    /*
    查询所有人员信息
     */
    @GetMapping(value = "/persons")
    public List<Person> person_list(){
        return personRepository.findAll();


    }


    /*
    新增人员信息
     */
    @Transactional
    @PostMapping(value = "/persons",produces = "application/json")
    public Person person_add(@Validated @RequestBody CreditsEntity requestObj){
        Person person = new Person();
        person.setName(requestObj.getPersonName());
        person.setAge(requestObj.getPersonAge());

        System.out.println(requestObj.toString());

        person.setPosition(requestObj.getPosition());
        return person = personRepository.save(person);
    }


    /*
    查询一个人员
     */
    @GetMapping(value = "/persons/{id}")
    public Person find_person(@PathVariable("id") Integer id){
        return personRepository.findOne(id);
    }


    /*
    更新一个人员
     */
    @PutMapping(value = "/persons",produces = "application/json")
    public Person person_update(@Validated @RequestBody CreditsEntity requestObj){
        ArrayList<Integer> num = new ArrayList<>();
        for(Position position:personRepository.findOne(requestObj.getPersonId()).getPosition()){
            num.add(position.getId());
        }
        Person person = new Person();
        person.setName(requestObj.getPersonName());
        person.setAge(requestObj.getPersonAge());
        person.setId(requestObj.getPersonId());
        person.setPosition(requestObj.getPosition());
        person=personRepository.save(person);
        for(int i = 0;i<num.size();i++){
            positionRepository.delete(num.get(i));
        }
        return person;

    }


    /*
    删除一个人员
     */
    @DeleteMapping(value = "/persons/{id}")
    public void delete_person(@PathVariable("id") Integer id){
        personRepository.delete(id);
    }

}
