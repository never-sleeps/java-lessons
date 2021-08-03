package ru.reflection;

public class Application {
    public static void main(String[] args) {
        String packageWithTests = "test";
        if (args.length > 0) {
            packageWithTests = args[0];
        }
        MyTestFramework runner = new MyTestFramework();
        runner.runPackage(packageWithTests);
    }
}
