package com.example.demo.control;

import com.example.demo.errorcatch.LoginException;
import com.example.demo.errorcatch.MyException;
import com.example.demo.errorcatch.RequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.resp.CreditsEntity;
import com.example.demo.resp.DelRespEntity;
import com.example.demo.resp.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PersonControl {
    @Autowired
    private PersonRepository personRepository;//用户表

    @Autowired
    private Person_PositionRepository ppRepository;//用户-职业中间表

    @Autowired
    private PositionRepository positionRepository;//职位表

    @Autowired
    private LoginRepository loginRepository;//用户登入账号密码表

    @Autowired
    private TokenRepository tokenRepository;//日志记录表

    @Autowired
    private LogRepository logRepository;

    public static Map<Object, Object> map = new HashMap<>();

    /*
    初始化职位表
     */
    @PostMapping(value = "/positions", produces = "application/json")
    public Object addPosition(){
        Position position1 = new Position(1,"学生");
        Position position2 = new Position(2,"老师");
        Position position3 = new Position(3,"管理员");
        Position position4 = new Position(4,"领导");
        System.out.println(position4.getId());
        List<Position> positions = new ArrayList<>();
        positions.add(position1);
        positions.add(position2);
        positions.add(position3);
        positions.add(position4);
        positionRepository.save(positions);
        return positions;
    }

    /*
    用户登入(账号密码)
     */
    @PostMapping(value = "/login", produces = "application/json")
    public CreditsEntity userLogin(@Validated @RequestBody UserLogin userLogin) throws Exception {
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
    public Object personList(@RequestParam("access_token") String token) throws MyException {
        if (verifyToken(token) == false) {
            throw new MyException("权限不足或账号错误");
        }
        //返回类数组初始化
        List<ResponseEntity> responseEntities = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Person> persons = personRepository.findAll(sort);
        for (Person person : persons) {
            //返回类cred填充数据
            ResponseEntity cred = new ResponseEntity();
            cred.setPersonId(person.getId());
            cred.setPersonName(person.getName());
            cred.setPersonAge(person.getAge());
            List<Person_Position> person_positions = ppRepository.myFindPId(person.getId());
            for (Person_Position p : person_positions) {
                Position position = positionRepository.myFindPo(p.getPositionId());
                cred.getPosition().add(position);
            }
            //加入返回类数组
            responseEntities.add(cred);
        }
        //记录日志文件
        initRecord(token, "查询所有人员");

        return responseEntities;
    }


    /*
    查询一个人员
     */
    @Transactional
    @GetMapping(value = "/personsOne")
    public ResponseEntity findPerson(@RequestParam("id") Integer id,
                             @RequestParam("access_token") String token) throws Exception {
        if (verifyToken(token) == false) {
            throw new MyException("权限不足或账号错误");
        }
        //初始化返回信息类
        ResponseEntity responseEntity = new ResponseEntity();
        //查询人员基本信息（不包括职业）
        Person person = personRepository.findOne(id);
        if (person == null) {
            throw new RequestException("查询用户不存在");
        }
        responseEntity.setPersonId(id);
        responseEntity.setPersonAge(person.getAge());
        responseEntity.setPersonName(person.getName());

        //查询人员职业信息存入返回类
        List<Person_Position> person_positions = ppRepository.myFindPId(id);
        for (Person_Position p : person_positions) {
            Position position = positionRepository.myFindPo(p.getPositionId());
            responseEntity.getPosition().add(position);
        }

        //记录日志文件
        String meths = "查询ID：" + id;
        initRecord(token, meths);
        return responseEntity;
    }
    /*
   按姓名查询人员
   */
    @Transactional
    @GetMapping(value = "/personsOneLike")
    public List<ResponseEntity> findPersonLike(@RequestParam("personName") String personName,
                                         @RequestParam("access_token") String token) throws Exception {
        if (verifyToken(token) == false) {
            throw new MyException("权限不足或账号错误");
        }
        //返回类数组初始化
        List<ResponseEntity> responseEntities = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        String name = "%"+personName+"%";
        List<Person> namePersons = personRepository.findByNameLike(name,sort);
        for (Person person : namePersons) {
            //返回类cred填充数据
            ResponseEntity cred = new ResponseEntity();
            cred.setPersonId(person.getId());
            cred.setPersonName(person.getName());
            cred.setPersonAge(person.getAge());
            List<Person_Position> person_positions = ppRepository.myFindPId(person.getId());
            for (Person_Position p : person_positions) {
                Position position = positionRepository.myFindPo(p.getPositionId());
                cred.getPosition().add(position);
            }
            //加入返回类数组
            responseEntities.add(cred);
        }
        //记录日志文件
        initRecord(token, "按姓名查询人员");
        return responseEntities;
    }



    /*
    新增人员信息
     */
    @Transactional
    @PostMapping(value = "/addpersons", produces = "application/json")
    public Success personAdd(@Validated @RequestBody CreditsEntity requestObj) throws Exception {
        if (verifyToken(requestObj.getToken()) == false) {
            throw new MyException("权限不足或账号错误");
        }
        //新增人员表信息
        Person person = new Person();
        person.setName(requestObj.getPersonName());
        person.setAge(requestObj.getPersonAge());
        person = personRepository.save(person);

        //新增人员-职位对照表信息
        for (Position position : requestObj.getPosition()) {
            Position myp = positionRepository.myFindPo(position.getId());
            if (position.getPositionName().equals(myp.getPositionName())) {
                Person_Position person_position = new Person_Position();
                person_position.setPerson(person);
                person_position.setPositionId(position.getId());
                ppRepository.save(person_position);
            } else {
                throw new RequestException("职业ID对应错误");
            }
        }


        //新增账号密码表信息
        if(requestObj.getUsername()!=null){
            UserLogin user = new UserLogin();
            user.setPerson(person);
            user.setUsername(requestObj.getUsername());
            user.setPassword(requestObj.getPassword());
            loginRepository.save(user);
        }

        //记录日志文件
        String meths = "新增ID：" + person.getId();
        initRecord(requestObj.getToken(), meths);

        //返回人员信息
        requestObj.setPersonId(person.getId());
        requestObj.setToken("");
        Success success = new Success(200, "添加成功",requestObj);
        return success;
    }


    /*
    更新一个人员
     */
    @Transactional
    @PutMapping(value = "/updatapersons", produces = "application/json")
    public Success personUpdate(@Validated @RequestBody CreditsEntity requestObj) throws Exception {
        if (verifyToken(requestObj.getToken()) == false) {
            throw new MyException("权限不足或账号错误");
        }
        Person person = personRepository.findOne(requestObj.getPersonId());
        if (person == null) {
            throw new RequestException("更新用户不存在");
        }
        person.setName(requestObj.getPersonName());
        person.setAge(requestObj.getPersonAge());

        //删除原本中间表信息
        List<Person_Position> person_positions = ppRepository.myFindPId(requestObj.getPersonId());
        ppRepository.delete(person_positions);

        //更新中间表信息
        for (Position position : requestObj.getPosition()) {
            Position myp = positionRepository.myFindPo(position.getId());
            if (position.getPositionName().equals(myp.getPositionName())) {
                Person_Position person_position = new Person_Position();
                person_position.setPositionId(position.getId());
                person_position.setPerson(person);
                ppRepository.save(person_position);
            } else {
                throw new RequestException("职业ID对应错误");
            }
        }

        //更新账号密码表信息
        UserLogin user = loginRepository.myFindPId(requestObj.getPersonId());
        if(user!=null){
            user.setPerson(person);
            user.setUsername(requestObj.getUsername());
            user.setPassword(requestObj.getPassword());
            loginRepository.save(user);
        }
        Success success = new Success(200, "更新成功",requestObj);
        //记录日志文件
        String meths = "更新ID：" + person.getId();
        initRecord(requestObj.getToken(), meths);
        requestObj.setToken("");
        return success;
    }

    /*
    批量删除人员
     */
    @Transactional
    @PostMapping(value = "/delpersons")
    public Success deletePersons(@Validated @RequestBody DelRespEntity delRespEntity) throws Exception {
        if (verifyToken(delRespEntity.getToken()) == false) {
            throw new MyException("权限不足或账号错误");
        }
        for(Integer id:delRespEntity.getIds()) {
            if (ppRepository.myFindPId(id) == null) {
                throw new RequestException("删除失败,用户不存在");
            }
            //删除中间表
            List<Person_Position> person_positions = ppRepository.myFindPId(id);
            ppRepository.delete(person_positions);
            //删除账号密码表信息
            UserLogin login = loginRepository.myFindPId(id);
            if (login != null) {
                loginRepository.delete(login);
            }
            //删除用户
            personRepository.delete(id);
            //删除Token表相关数据
            if (tokenRepository.findOne(id) != null) {
                tokenRepository.delete(id);
            }
            //如果缓存中有数据且与删除用户相同则清空缓存
            if (map.get("user") != null) {
                CreditsEntity creditsEntity = (CreditsEntity) map.get("user");
                if (creditsEntity.getPersonId() == id) {
                    PersonControl.map.put("token", "");
                    PersonControl.map.put("user", null);
                }
            }

        }
        Success success = new Success(200, "删除成功");
        //记录日志文件
        String meths = "删除ID：" + delRespEntity.getIds();
        initRecord(delRespEntity.getToken(), meths);
        return success;
    }

    /*
    批量删除人员
     */
    @Transactional
    @DeleteMapping(value = "/delperson")
    public Success deletePerson(@RequestParam("id") Integer id,
                                @RequestParam("access_token") String token) throws Exception {
        if (verifyToken(token) == false) {
            throw new MyException("权限不足或账号错误");
        }
        if (ppRepository.myFindPId(id) == null) {
            throw new RequestException("删除失败,用户不存在");
        }
        //删除中间表
        List<Person_Position> person_positions = ppRepository.myFindPId(id);
        ppRepository.delete(person_positions);
        //删除账号密码表信息
        UserLogin login = loginRepository.myFindPId(id);
        if(login!=null){
            loginRepository.delete(login);
        }
        //删除用户
        personRepository.delete(id);
        //删除Token表相关数据
        if (tokenRepository.findOne(id) != null) {
            tokenRepository.delete(id);
        }
        //如果缓存中有数据且与删除用户相同则清空缓存
        if (map.get("user") != null) {
            CreditsEntity creditsEntity = (CreditsEntity) map.get("user");
            if (creditsEntity.getPersonId() == id) {
                PersonControl.map.put("token", "");
                PersonControl.map.put("user", null);
            }
        }
        Success success = new Success(200, "删除成功");
        //记录日志文件
        String meths = "删除ID：" + id;
        initRecord(token, meths);
        return success;
    }

    //判断是否为初始Token用户操作
    public void initRecord(String token,String message){
        if(token.equals("888888")){
            Person person = new Person();
            person.setId(9999);
            person.setName("初始操作用户");
            logRecord(message,person);
        }else {
            Token token1 = tokenRepository.findByToken(token);
            Person person1 = personRepository.findOne(token1.getId());
            logRecord(message,person1);
        }
    }

    //数据库日志操作记录
    public void logRecord(String meth, Person person) {
        Logrecord logrecord = new Logrecord();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        logrecord.setData(df.format(new Date()).toString());
        logrecord.setBehavior(meth);
        logrecord.setPersonId(person.getId());
        logrecord.setPersonName(person.getName());
        logRepository.save(logrecord);
    }


    //数据库Token操作方法
    public CreditsEntity loginToken(UserLogin login) {
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
        for (Person_Position i : list) {
            result.getPosition().add(positionRepository.findOne(i.getPositionId()));
        }
        result.setToken(uuid.toString());

        //添加缓存数据
        map.put("token", token1.getToken());
        map.put("user", result);

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
        timer.schedule(task, 7200000);
    }

    //Token验证
    public boolean verifyToken(String token) {
        CreditsEntity result = new CreditsEntity();
        if (token.equals("888888")) {
            return true;
        }
        //缓存中寻找Token
        if (map.get("token") != null && map.get("token").equals(token)) {
            result = (CreditsEntity) map.get("user");
            for (Position p : result.getPosition()) {
                if (p.getPositionName().equals("管理员")) {
                    return true;
                }
            }
        }
        //数据库中寻找Token
        Token token1 = tokenRepository.findByToken(token);
        if (token1 == null) {
            return false;
        }
        List<Person_Position> list = ppRepository.myFindPId(token1.getId());
        for (Person_Position i : list) {
            result.getPosition().add(positionRepository.findOne(i.getPositionId()));
        }
        for (Position p : result.getPosition()) {
            if (p.getPositionName().equals("管理员")) {
                return true;
            }
        }
        return false;
    }


}
