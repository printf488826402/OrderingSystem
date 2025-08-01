package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    // TODO 视频中new实体类对象，这里不应该直接注入;
    /*@Autowired
    private Employee employee;*/

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();


        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);


        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }


        // 密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /*@Override*/
    /*新增员工*/
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程的id:"+Thread.currentThread().getId());

        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置对象状态，默认正常状态为1，表示正常，0表示锁定
        //ctrl+shift+u大写转换
        employee.setStatus(StatusConstant.ENABLE);
        //设置默认密码123456，进行md5加密处理
        //密码换成密码常量
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //TODO 自定义注解自动填充公共字段
        //设置当前记录的创建时间和修改时间
        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前创建人id修改人id
        //BaseContext.getCurrentId()
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());*/

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //select * from employee limit 0,10
        //将employeePageQueryDTO中的属性替换掉上述SQL语句的0，10(页码，每页记录数)

        //或通过pagehelper插件实现分页查询
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        Long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrstop(Integer status, Long id) {
        //update employee set status = ? where id = ?

        /*Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/

        //用构建器
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }
    //如果只是更新status字段，使用直接传递status和id是完全可以的
    //如果未来可能需要更新更多字段，建议改为使用Employee对象传递参数
    //总的来说，两种方式都是可行的，
    //选择哪种主要取决于具体业务需求和未来的扩展性考虑。

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee=employeeMapper.getById(id);
        employee.setPassword("****");//将传给前端的代码设置为****，增加安全性。
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        //使用对象属性拷贝，再单独设置修改时间、修改人id
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        //TODO 自定义注解自动填充公共字段
/*        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());*/
        employeeMapper.update(employee);

    }
}
