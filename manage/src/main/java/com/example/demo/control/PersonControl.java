package com.example.demo.control;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.resp.CreditsEntity;
import com.example.demo.until.TimeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.util.*;

@RestController
public class PersonControl {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Person_PositionRepository ppRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public static Map<Object,Object> map = new HashMap<>();

    /*
    用户登入(账号密码)
     */
    @PostMapping(value = "/login")
    public CreditsEntity userlogin(@Validated @RequestBody UserLogin userLogin) {
        UserLogin login = loginRepository.findByUsername(userLogin.getUsername());
        if (login.getPassword().equals(userLogin.getPassword())) {
            System.out.println("登陆成功");
            TimeSet timeSet = new TimeSet();
            timeSet.timeStart(login.getId());

            return loginToken(login);
        } else {
            System.out.println("登陆失败");
        }
        return null;
    }

    //获取已登入Token用户信息
    @PostMapping(value = "/token")
    public Object loginByToken(@Validated @RequestBody CreditsEntity requestObj){
        if(map.get("token")==null){
            map.put("token","");
            map.put("user","");
        }
        if(map.get("token").equals(requestObj.getToken()) && requestObj.getToken()!=null){
            return map.get("user");
        }
        return "Token无效";
    }

    /*
    查询所有人员信息
     */
    @GetMapping(value = "/persons")
    public List<CreditsEntity> person_list() {
        //返回类数组初始化
        List<CreditsEntity> creditsEntities = new ArrayList<>();
        List<Person> persons = personRepository.findAll();

        for (Person person : persons) {
            //返回类cred填充数据
            CreditsEntity cred = new CreditsEntity();
            cred.setPersonId(person.getId());
            cred.setPersonName(person.getName());
            cred.setPersonAge(person.getAge());
            List<Person_Position> person_positions = ppRepository.myFindPId(person.getId());
            for (Person_Position p : person_positions) {
                Position position = positionRepository.myFindPo(p.getPositionId());
                cred.getPosition().add(position);
            }
            //加入返回类数组
            creditsEntities.add(cred);
        }
        return creditsEntities;
    }


    /*
    新增人员信息
     */
    @Transactional
    @PostMapping(value = "/persons", produces = "application/json")
    public CreditsEntity person_add(@Validated @RequestBody CreditsEntity creditsEntity) {
        //新增人员表
        Person person = new Person();
        person.setName(creditsEntity.getPersonName());
        person.setAge(creditsEntity.getPersonAge());
        person = personRepository.save(person);

        //新增人员-职位对照表
        for (Position position : creditsEntity.getPosition()) {
            int i = 0;
            Person_Position person_position = new Person_Position();
            person_position.setPerson(person);
            person_position.setPositionId(position.getId());
            ppRepository.save(person_position);
        }
        //返回人员信息
        creditsEntity.setPersonId(person.getId());
        return creditsEntity;
    }


    /*
    查询一个人员
     */
    @GetMapping(value = "/persons/{id}")
    public CreditsEntity find_person(@PathVariable("id") Integer id) {
        //初始化返回信息类
        CreditsEntity creditsEntity = new CreditsEntity();
        //查询人员基本信息（不包括职业）
        Person person = personRepository.findOne(id);
        creditsEntity.setPersonId(id);
        creditsEntity.setPersonAge(person.getAge());
        creditsEntity.setPersonName(person.getName());

        //查询人员职业信息存入返回类
        List<Person_Position> person_positions = ppRepository.myFindPId(id);
        for (Person_Position p : person_positions) {
            Position position = positionRepository.myFindPo(p.getPositionId());
            creditsEntity.getPosition().add(position);
        }

        return creditsEntity;
    }


    /*
    更新一个人员
     */
    @PutMapping(value = "/persons", produces = "application/json")
    public CreditsEntity person_update(@Validated @RequestBody CreditsEntity requestObj) {
        Person person = personRepository.findOne(requestObj.getPersonId());
        person.setName(requestObj.getPersonName());
        person.setAge(requestObj.getPersonAge());

        //删除原本中间表信息
        List<Person_Position> person_positions = ppRepository.myFindPId(requestObj.getPersonId());
        ppRepository.delete(person_positions);

        //保存新的中间表信息
        for (Position position : requestObj.getPosition()) {
            Person_Position person_position = new Person_Position();
            person_position.setPerson(person);
            person_position.setPositionId(position.getId());
            ppRepository.save(person_position);
        }

        return requestObj;
    }

    /*
    删除一个人员
     */
    @DeleteMapping(value = "/persons/{id}")
    public void delete_person(@PathVariable("id") Integer id) {
        List<Person_Position> person_positions = ppRepository.myFindPId(id);
        ppRepository.delete(person_positions);
    }



    //数据库Token操作类
    public CreditsEntity loginToken(UserLogin login){
        UUID uuid = UUID.randomUUID();
        Token token1 = new Token();
        if (tokenRepository.findOne(login.getPerson().getId()) != null) {
            token1 = tokenRepository.findOne(login.getPerson().getId());
            if (token1.getToken().equals(uuid.toString())) {

            } else {
                token1.setToken(uuid.toString());
                tokenRepository.save(token1);
            }
        }
        token1.setId(login.getPerson().getId());
        token1.setToken(uuid.toString());
        tokenRepository.save(token1);
        //填充返回数据
        CreditsEntity result = new CreditsEntity();
        result.setPersonId(login.getPerson().getId());
        result.setPersonName(login.getPerson().getName());
        result.setPersonAge(login.getPerson().getAge());
        List<Person_Position> list = ppRepository.myFindPId(login.getPerson().getId());
        for(Person_Position i:list){
            result.getPosition().add(positionRepository.findOne(i.getPositionId()));
        }
        result.setToken(uuid.toString());
        //添加缓存数据
        map.put("token",token1.getToken());
        map.put("user",result);
        return result;
    }



}
