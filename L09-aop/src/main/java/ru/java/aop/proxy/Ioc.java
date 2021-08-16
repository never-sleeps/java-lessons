package ru.java.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {}

    public static InterfaceForDemo createProxyClass() {
        ClassLoader loader = Ioc.class.getClassLoader();
        Class<?>[] interfaces = new Class<?>[]{InterfaceForDemo.class};
        InvocationHandler handler = new DemoInvocationHandler(new ClassForDemo());
        return (InterfaceForDemo) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object obj;
        private final Set<String> methodsForLogging = new HashSet<>();

        DemoInvocationHandler(InterfaceForDemo obj) {
            this.obj = obj;

            Set<Method> logMethods = Arrays.stream(obj.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class)).collect(Collectors.toSet());
            logMethods.forEach(method -> this.methodsForLogging.add(getMethodDescription(method)));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodsForLogging.contains(getMethodDescription(method))) {
                System.out.println(getLogMessage(method, args));
            }
            return method.invoke(obj, args);
        }

        private String getMethodDescription(Method method) {
            return method.getName() + " " + Arrays.stream(method.getParameterTypes())
                    .map(Class::toString)
                    .collect(Collectors.joining(","));
        }

        private String getLogMessage(Method method, Object[] args) {
            String argsToString = ((args == null) || (args.length == 0)) ? "no params" : "params : " + Arrays.stream(args)
                    .map(arg -> String.format("%s(%s)", arg.getClass().getSimpleName(),arg.toString()))
                    .collect(Collectors.joining(","));
            return String.format("executed method: %s %s, %s", method.getReturnType(), method.getName(), argsToString);
        }
    }
}
