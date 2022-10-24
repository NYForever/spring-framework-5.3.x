package rwn.service;

import com.rwn.spring.*;

/**
 * @author wenan.ren
 * @date 2022/4/13 14:35
 * @Description
 */
@Scope
@Component("userService")
public class UserService implements BeanNameAwar {

    @Autowried
    private OrderService orderService;

    private String beanName;

    private String admin;

    public void testUser() {
        System.out.println(orderService);
    }

    @PostConstruct
    public void initAdmin(){
        //查询数据库，给admin赋值
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
