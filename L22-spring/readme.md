**Dependency Inversion Principle (DIP)** – принцип ООП, согласно которому:  
- модули верхних уровней не должны зависеть от модулей нижних уровней;  
- все модули должны зависеть от абстракций;  
- абстракции не должны зависеть от деталей.

**Inversion of Control (IoC)** – принцип ООП, согласно которому поток управления программы контролируется фреймворком или просто чужим кодом.
IoC позволяет повысить модульность и расширяемость программы за счет снижения зависимостей между ее компонентами.   
Еще одно название «Голливудский принцип» (Hollywood Principle): «Не звоните мне, я сам Вам позвоню.».

**Dependency injection (DI)** (Внедрение зависимости) — одна из реализаций принципа IoC — процесс предоставления программному компоненту внешней зависимости.
Согласно принципу единой обязанности, объект передает "заботу" о построении нужных ему зависимостей внешнему механизму, который специально для этого предназначен. 
Основные плюсы внедрения зависимости:  
- сокращается объем связующего кода;  
- упрощается конфигурация приложения;  
- возможность управлять общими зависимостями в одном репозитории;  
- улучшается возможность тестирования.



**Реализация Dependency injection в Spring Framework.**
Реализация в Spring основана на 2-х ключевых Java-концепциях: интерфейсах и компонентах JavaBean. 
Используя Spring в качестве поставщика DI, разработчик получает гибкость определения конфигурации зависимостей внутри приложений разными путями:  
- XML based configuration;  
- Java based configuration;  
- Annotation based configuration. 

-----------------------------------------------
#### XML based configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.springframework.org/schema/beans" 
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <bean id="ioService" class="ru.java.example.service.ConsoleIOService"/>

    <bean id="helloWorldService“ class="ru.java.example.service.HelloWorldServiceImpl"> 
        <constructor-arg name="ioService" ref="ioService"/>
    </bean>
</beans>
```

```java
public class Application {
    public static void main(String ...args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        HelloWorldService helloWorldService = context.getBean(HelloWorldService.class);
        helloWorldService.sayHelloWorld();
    } 
}
```

-----------------------------------------------
#### Java based configuration

```java
@Configuration
public class AppConfig { 
    @Bean
    public HelloWorldService helloWorldService(IOService ioService) { 
        return new HelloWorldService(ioService);
    }
    @Bean
    public IOService ioService() { 
        return new ConsoleIOService ();
    } 
}

@Configuration 
@ComponentScan 
public class Application {
    public static void main(String ...args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class); 
        HelloWorldService helloWorldService = context.getBean(HelloWorldService.class); 
        helloWorldService.sayHelloWorld();
    } 
}
```

-----------------------------------------------
#### Annotation based configuration

```java
@Service
public class IOServiceConsole implements IOService { ... } 

@Service
public class HelloWorldService { 
    private final IOService ioService;

    // Можно не писать над единственным конструктором (но оно там подразумевается)
    @Autowired
    public HelloWorldService (IOService ioService) {
        this.ioService = ioService; 
    }
}

@Configuration 
@ComponentScan 
public class Application {
    public static void main(String ...args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class); 
        HelloWorldService helloWorldService = context.getBean(HelloWorldService.class); 
        helloWorldService.sayHelloWorld();
    } 
}
```

-----------------------------------------------
#### Виды автосвязывания (автовайринга)

```java
// через конструктор (сразу после создания объект будет готов к использованию)
@Service
public class HelloWorldService { 
    private final IOService ioService;
    @Autowired
    public HelloWorldService (IOService ioService) {...} 
}
```

```java
// через поле
@Service
public class HelloWorldService {
    @Autowired
    private IOService ioService; 
}
```
```java
// через сеттер
@Service
public class HelloWorldService { 
    private IOService ioService;
    @Autowired
    public setIOService (IOService ioService) {...} 
}
```