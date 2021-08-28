package ru.java.structural.proxy.security;


public final class SecurityAccessImpl implements SecurityAccess {
    @Override
    public void access() {
        System.out.println("access to bank account");
    }
}
