package com.promptdb.auth.utils;

public interface PasswordEncryptor {

    public String createHash(String password);

    public Boolean checkHash(String password, String hashedPassword);
}
