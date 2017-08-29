package com.example.demo.control;

import com.example.demo.model.Person;
import com.example.demo.model.Position;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonControl {
    @Autowired
    private PersonRepository personRepository;


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
    @PostMapping(value = "/persons")
    public Person person_add(@RequestParam("person_name") String person_name,
                            @RequestParam("person_age") Integer person_age,
                             @RequestParam("position_name") String position_name){
        Person person = new Person();
        person.setAge(person_age);
        person.setName(person_name);

        Position position = new Position();
        position.setPosition_name(position_name);

        person.getPosition().add(position);
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
    @PutMapping(value = "/persons/{id}")
    public Person person_update(@RequestParam("name") String name,
                               @RequestParam("age") Integer age,
                               @PathVariable("id") Integer id){
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setId(id);
        return personRepository.save(person);
    }


    /*
    删除一个人员
     */
    @DeleteMapping(value = "/persons/{id}")
    public void delete_person(@PathVariable("id") Integer id){
        personRepository.delete(id);
    }

}
