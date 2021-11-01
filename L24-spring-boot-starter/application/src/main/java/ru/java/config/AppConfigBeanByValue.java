package ru.java.config;

public class AppConfigBeanByValue {
    private final String paramName;

    public AppConfigBeanByValue(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
