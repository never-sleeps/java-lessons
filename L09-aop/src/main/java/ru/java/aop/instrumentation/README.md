
1. [replacement for addition by subtraction](#replacement-for-addition-by-subtraction)
2. [adding logging when executing method](#adding-logging-when-executing-method)
3. [adding setter](#adding-setter)



### replacement for addition by subtraction

Source class was replaced by changer.class  

Source class:
```java
public class AnyClass {
    public int summator(int x, int y) {
        return x + y;
    }
}
```

changer.class:
```java
public class AnyClass {
    public AnyClass() {
    }

    public int summator(int x, int y) {
        return x - y;
    }
}
```

### adding logging when executing method

Source class was replaced by proxyASM.class  

Source class:
```java
public class MyClassImpl {

    public void secureAccess(String param) {
        System.out.println("secureAccess, param:" + param);
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
```

```java
public class MyClassImpl {
    public MyClassImpl() {
    }

    public void secureAccessProxied(String param) {
        System.out.println("secureAccess, param:" + param);
    }

    public String toString() {
        return "MyClassImpl{}";
    }

    public void secureAccess(String var1) {
        System.out.println("logged param:" + var1);
        this.secureAccessProxied(var1);
    }
}
```

### adding setter

```shell script
java -javaagent:setterDemo.jar -jar setterDemo.jar
```

Source class was replaced by setter.class

Source class:
```java
public class MyClass {

    private int value = 10;

    public int getValue() {
        return value;
    }
}
```

setter.class:
```java
public class MyClass {
    private int value = 10;

    public MyClass() {
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int var1) {
        this.value = var1;
    }
}
```