package com.asiainfo.service.impl.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {

/*
* 密码加密解密
* */
    public static void main(String[] args){

        System.out.println("{bcrypt}"+new BCryptPasswordEncoder().encode("admin"));



    }
}
