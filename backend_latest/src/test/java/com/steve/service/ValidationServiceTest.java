package com.steve.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.steve.service.ValidationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationServiceTest {

    @Autowired ValidationService validationService;

    @Test
    public void ValidateTest(){

        int count = validationService.confirmEvent(20, 2);
        System.out.println(count);

    }


}
