package com.asiainfo.service.impl.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {


    public static void main(String[] args){

        System.out.println("{bcrypt}"+new BCryptPasswordEncoder().encode("1qaz!QAZ"));



    }
}
