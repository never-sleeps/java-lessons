#### Автоматическое логирование.

Метод класса может быть помечен самодельной аннотацией @Log. Например:

```java
class AnyClass { 
    @Log 
    public void calculation(int param) {
        // ...
    } 
}
```

При вызове этого метода в консоль логируются:
- тип возвращаемого значения
- название метода 
- тип и значения параметров. 

Например:

```shell script
executed method: void calculation, no params
executed method: int calculation, params : Integer(1)
executed method: long calculation, params : Integer(1),Long(2)
executed method: double calculation, params : Integer(1),Long(2),Double(3.0)
```

Аннотация может быть поставлена на методы с любым числом и типом параметров.