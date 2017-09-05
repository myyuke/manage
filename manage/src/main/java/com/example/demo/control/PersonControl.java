package com.example.demo.control;

import com.example.demo.errorcatch.LoginException;
import com.example.demo.errorcatch.MyException;
import com.example.demo.errorcatch.RequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.resp.CreditsEntity;
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
    @PostMapping(value = "/login", produces = "application/json")
    public CreditsEntity userLogin(@Validated @RequestBody UserLogin userLogin) throws Exception{
        UserLogin login = loginRepository.findByUsername(userLogin.getUsername());
        if (login.getPassword().equals(userLogin.getPassword())) {
            System.out.println("登陆成功");
            //Token数据库操作方法loginToken()
            return loginToken(login);
        } else {
            throw new LoginException("账号或密码错误");
        }
    }

    /*
    查询所有人员信息
     */
    @GetMapping(value = "/persons")
    public Object personList(@RequestParam("access_token") String token)throws MyException{
        if(verifyToken(token)==false){
            throw new MyException("权限不足");
        }
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
            Token mytoken = tokenRepository.findOne(person.getId());
            if(mytoken!=null){
                cred.setToken(mytoken.getToken());
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
    public Object personAdd(@Validated @RequestBody CreditsEntity requestObj) throws Exception{
        if(verifyToken(requestObj.getToken())==false){
            throw new MyException("权限不足");
        }
        //新增人员表
        Person person = new Person();
        person.setName(requestObj.getPersonName());
        person.setAge(requestObj.getPersonAge());
        person = personRepository.save(person);

        //新增人员-职位对照表
        for (Position position : requestObj.getPosition()) {
            Position myp = positionRepository.myFindPo(position.getId());
            if(position.getPositionName().equals(myp.getPositionName())){
                Person_Position person_position = new Person_Position();
                person_position.setPerson(person);
                person_position.setPositionId(position.getId());
                ppRepository.save(person_position);
            }else {
                throw new RequestException("职业ID对应错误");
            }

        }
        //返回人员信息
        requestObj.setPersonId(person.getId());
        requestObj.setToken("");
        return requestObj;
    }


    /*
    查询一个人员
     */
    @GetMapping(value = "/personsOne")
    public Object findPerson(@RequestParam("id") Integer id,
                             @RequestParam("access_token") String token) throws Exception{
        if(verifyToken(token)==false){
            throw new MyException("权限不足");
        }
        //初始化返回信息类
        CreditsEntity creditsEntity = new CreditsEntity();
        //查询人员基本信息（不包括职业）
        Person person = personRepository.findOne(id);
        if(person==null){
            throw new RequestException("查询用户不存在");
        }
        creditsEntity.setPersonId(id);
        creditsEntity.setPersonAge(person.getAge());
        creditsEntity.setPersonName(person.getName());

        //查询人员职业信息存入返回类
        List<Person_Position> person_positions = ppRepository.myFindPId(id);
        for (Person_Position p : person_positions) {
            Position position = positionRepository.myFindPo(p.getPositionId());
            creditsEntity.getPosition().add(position);
        }
        Token mytoken = tokenRepository.findOne(person.getId());
        if(mytoken!=null){
            creditsEntity.setToken(mytoken.getToken());
        }
        return creditsEntity;
    }


    /*
    更新一个人员
     */
    @PutMapping(value = "/persons", produces = "application/json")
    public Success personUpdate(@Validated @RequestBody CreditsEntity requestObj) throws Exception{
        if(verifyToken(requestObj.getToken())==false){
            throw new MyException("权限不足");
        }
        Person person = personRepository.findOne(requestObj.getPersonId());
        if(person==null){
            throw new RequestException("更新用户不存在");
        }
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
        Success success = new Success(200,"更新成功");
        return success;
    }

    /*
    删除一个人员
     */
    @DeleteMapping(value = "/persons")
    public Success deletePerson(@RequestParam("id") Integer id,
                               @RequestParam("access_token") String token)throws Exception{
        if(verifyToken(token)==false){
            throw new MyException("权限不足");
        }
        if(ppRepository.myFindPId(id)==null){
            throw new RequestException("删除失败,用户不存在");
        }
        //删除中间表
        List<Person_Position> person_positions = ppRepository.myFindPId(id);
        ppRepository.delete(person_positions);
        //删除用户
        personRepository.delete(id);

        if(tokenRepository.findOne(id)!=null){
            tokenRepository.delete(id);
        }
        //如果缓存中有数据且与删除用户相同则清空缓存
        if(map.get("user")!=null){
            CreditsEntity creditsEntity = (CreditsEntity)map.get("user");
            if(creditsEntity.getPersonId()==id){
                PersonControl.map.put("token", "");
                PersonControl.map.put("user", null);
            }
        }
        Success success = new Success(200,"删除成功");
        return success;
    }



    //数据库Token操作方法
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
        //保存Token
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

        //定时消除缓存
        timeStart(token1.getId());

        return result;
    }

    //定时清除缓存方法
    public void timeStart(Integer id) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // task to run goes here
                tokenRepository.delete(id);
                PersonControl.map.put("token", "");
                PersonControl.map.put("user", null);
                System.out.println("缓存清除成功");
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 300000);
    }

    //Token验证
    public boolean verifyToken(String token){
        CreditsEntity result = new CreditsEntity();
        //缓存中寻找Token
        if(map.get("token")!=null && map.get("token").equals(token)){
            result = (CreditsEntity)map.get("user");
            for(Position p:result.getPosition()){
                if(p.getPositionName().equals("管理员")){
                    return true;
                }
            }
        }
        //数据库中寻找Token
        Token token1 = tokenRepository.findByToken(token);
        if(token1==null){
            return false;
        }
        List<Person_Position> list = ppRepository.myFindPId(token1.getId());
        for(Person_Position i:list){
            result.getPosition().add(positionRepository.findOne(i.getPositionId()));
        }
        for(Position p:result.getPosition()){
            if(p.getPositionName().equals("管理员")){
                return true;
            }
        }
        return false;
    }
}
