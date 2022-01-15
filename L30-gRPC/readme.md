#### gRPC клиент-серверное приложение или "Убить босса"

Серверная часть:
- сервер по запросу клиента генерирует последовательность чисел.
- запрос от клиента содержит начальное значение (firstValue) и конечное(lastValue).
- Раз в две секунды сервер генерирует новое значение и "стримит" его клиенту:  
firstValue + 1 
firstValue + 2  
...  
lastValue  


Клиентская часть:
- клиент отправляет запрос серверу для получения последовательности чисел от 0 до 30.
- клиент запускает цикл от 0 до 50.
- раз в секунду выводит в консоль число (currentValue) по такой формуле:  
currentValue = [currentValue] + [ПОСЛЕДНЕЕ число от сервера] + 1
- начальное значение: currentValue = 0
- Число, полученное от сервера должно учитываться только один раз.
- Обратите внимание, сервер может вернуть несколько чисел, надо взять именно ПОСЛЕДНЕЕ.

Должно получиться примерно так:  
currentValue:1  
число от сервера:2  
currentValue:4 <--- число от сервера учитываем только один раз  
currentValue:5 <--- тут число от сервера уже не учитывается  
число от сервера:3  
currentValue:9  
currentValue:10  
new value:4 currentValue:15 currentValue:16  

Результат для ```Request: firstValue:0, lastValue:10```:  
```java
> Task :L30-gRPC:grpc-client:ClientGRPC.main()
2022-01-09 04:29:01.482 [main] INFO  ru.demo.ClientGRPC - Client is starting ...
2022-01-09 04:29:01.636 [main] INFO  ru.demo.CalculateExecutor - current value: 1
2022-01-09 04:29:01.907 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:1
2022-01-09 04:29:02.640 [main] INFO  ru.demo.CalculateExecutor - current value: 3
2022-01-09 04:29:03.643 [main] INFO  ru.demo.CalculateExecutor - current value: 4
2022-01-09 04:29:03.892 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:2
2022-01-09 04:29:04.647 [main] INFO  ru.demo.CalculateExecutor - current value: 7
2022-01-09 04:29:05.652 [main] INFO  ru.demo.CalculateExecutor - current value: 8
2022-01-09 04:29:05.895 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:3
2022-01-09 04:29:06.657 [main] INFO  ru.demo.CalculateExecutor - current value: 12
2022-01-09 04:29:07.661 [main] INFO  ru.demo.CalculateExecutor - current value: 13
2022-01-09 04:29:07.892 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:4
2022-01-09 04:29:08.665 [main] INFO  ru.demo.CalculateExecutor - current value: 18
2022-01-09 04:29:09.667 [main] INFO  ru.demo.CalculateExecutor - current value: 19
2022-01-09 04:29:09.896 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:5
2022-01-09 04:29:10.672 [main] INFO  ru.demo.CalculateExecutor - current value: 25
2022-01-09 04:29:11.675 [main] INFO  ru.demo.CalculateExecutor - current value: 26
2022-01-09 04:29:11.892 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:6
2022-01-09 04:29:12.676 [main] INFO  ru.demo.CalculateExecutor - current value: 33
2022-01-09 04:29:13.681 [main] INFO  ru.demo.CalculateExecutor - current value: 34
2022-01-09 04:29:13.897 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:7
2022-01-09 04:29:14.686 [main] INFO  ru.demo.CalculateExecutor - current value: 42
2022-01-09 04:29:15.690 [main] INFO  ru.demo.CalculateExecutor - current value: 43
2022-01-09 04:29:15.896 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:8
2022-01-09 04:29:16.695 [main] INFO  ru.demo.CalculateExecutor - current value: 52
2022-01-09 04:29:17.697 [main] INFO  ru.demo.CalculateExecutor - current value: 53
2022-01-09 04:29:17.893 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:9
2022-01-09 04:29:18.702 [main] INFO  ru.demo.CalculateExecutor - current value: 63
2022-01-09 04:29:19.706 [main] INFO  ru.demo.CalculateExecutor - current value: 64
2022-01-09 04:29:19.898 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - new value from server:10
2022-01-09 04:29:19.900 [grpc-default-executor-0] INFO  ru.demo.ResponseStreamObserver - request completed
2022-01-09 04:29:20.710 [main] INFO  ru.demo.CalculateExecutor - current value: 75
2022-01-09 04:29:21.714 [main] INFO  ru.demo.CalculateExecutor - current value: 76
2022-01-09 04:29:22.715 [main] INFO  ru.demo.CalculateExecutor - current value: 77
2022-01-09 04:29:23.716 [main] INFO  ru.demo.CalculateExecutor - current value: 78
2022-01-09 04:29:24.717 [main] INFO  ru.demo.CalculateExecutor - current value: 79
2022-01-09 04:29:25.718 [main] INFO  ru.demo.CalculateExecutor - current value: 80
2022-01-09 04:29:26.723 [main] INFO  ru.demo.CalculateExecutor - current value: 81
2022-01-09 04:29:27.724 [main] INFO  ru.demo.CalculateExecutor - current value: 82
2022-01-09 04:29:28.727 [main] INFO  ru.demo.CalculateExecutor - current value: 83
2022-01-09 04:29:29.732 [main] INFO  ru.demo.CalculateExecutor - current value: 84
2022-01-09 04:29:30.736 [main] INFO  ru.demo.CalculateExecutor - current value: 85
2022-01-09 04:29:31.740 [main] INFO  ru.demo.CalculateExecutor - current value: 86
2022-01-09 04:29:32.743 [main] INFO  ru.demo.CalculateExecutor - current value: 87
2022-01-09 04:29:33.747 [main] INFO  ru.demo.CalculateExecutor - current value: 88
2022-01-09 04:29:34.750 [main] INFO  ru.demo.CalculateExecutor - current value: 89
2022-01-09 04:29:35.753 [main] INFO  ru.demo.CalculateExecutor - current value: 90
2022-01-09 04:29:36.758 [main] INFO  ru.demo.CalculateExecutor - current value: 91
2022-01-09 04:29:37.762 [main] INFO  ru.demo.CalculateExecutor - current value: 92
2022-01-09 04:29:38.765 [main] INFO  ru.demo.CalculateExecutor - current value: 93
2022-01-09 04:29:39.766 [main] INFO  ru.demo.CalculateExecutor - current value: 94
2022-01-09 04:29:40.769 [main] INFO  ru.demo.CalculateExecutor - current value: 95
2022-01-09 04:29:41.774 [main] INFO  ru.demo.CalculateExecutor - current value: 96
2022-01-09 04:29:42.779 [main] INFO  ru.demo.CalculateExecutor - current value: 97
2022-01-09 04:29:43.782 [main] INFO  ru.demo.CalculateExecutor - current value: 98
2022-01-09 04:29:44.786 [main] INFO  ru.demo.CalculateExecutor - current value: 99
2022-01-09 04:29:45.790 [main] INFO  ru.demo.CalculateExecutor - current value: 100
2022-01-09 04:29:46.793 [main] INFO  ru.demo.CalculateExecutor - current value: 101
2022-01-09 04:29:47.794 [main] INFO  ru.demo.CalculateExecutor - current value: 102
2022-01-09 04:29:48.798 [main] INFO  ru.demo.CalculateExecutor - current value: 103
2022-01-09 04:29:49.803 [main] INFO  ru.demo.CalculateExecutor - current value: 104
2022-01-09 04:29:50.807 [main] INFO  ru.demo.CalculateExecutor - current value: 105
2022-01-09 04:29:51.810 [main] INFO  ru.demo.ClientGRPC - Client is shutting down...
```