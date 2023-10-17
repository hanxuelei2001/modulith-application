package com.lejingling.modulith;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class AotApplication {
    public static void main(String[] args) {
        // 解决无法加载 DTD 文件的问题
        // 允许访问外部 DTD 文件的属性。在创建 MyBatis 的 SqlSessionFactory 之前
        System.setProperty("javax.xml.accessExternalDTD", "all");
        //// 测试自动上传
        SpringApplication.run(AotApplication.class, args);
        // 查看泛型底层逻辑
        //Person.Man man = new Person.Man();
        //Person<Person.Man> person = new Person<>(man);
        //
        //Person.Woman woman = new Person.Woman();
        //Person<Person.Woman> person2 = new Person<>(woman);
        //
        //person.printT();
        //person2.printT();

    }
    //class  Person<T> {
    //    private T t;
    //    public Person(T t) {
    //        this.t = t;
    //    }
    //
    //    public void  printT() {
    //        System.out.println(t);
    //    }
    //
    //    public static class Man {
    //
    //    }
    //
    //    public static class Woman {
    //
    //    }
    //
    //}

}
