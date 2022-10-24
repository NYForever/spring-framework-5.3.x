package rwn.service;


/**
 * @author wenan.ren
 * @date 2022/4/6 17:39
 * @Description
 */
public class TestClass {

    public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.testUser();
        System.out.println(userService.getBeanName());

//        System.out.println(applicationContext.getBean("orderService"));

    }

}
