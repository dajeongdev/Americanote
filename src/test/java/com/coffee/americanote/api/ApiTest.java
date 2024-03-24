package com.coffee.americanote.api;

import com.coffee.americanote.navermap.AddressToCoordinate;
import com.coffee.americanote.navermap.CrawlingCafe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApiTest {

    @Autowired
    private CrawlingCafe crawlingCafe;
    @Autowired
    private AddressToCoordinate addressToCoordinate;

    @Test
    public void ctest1() throws InterruptedException {
        crawlingCafe.process();
        //System.out.println(crawlingCafe.coffeeInfoms.size());
    }

    @Test
    public void ctest2() {
        String[] result = addressToCoordinate.addressToCoordinate("서울 마포구 월드컵북로 48-3 1층");
        System.out.println("result[0] = " + result[0]);
        System.out.println("result[1] = " + result[1]);
    }
}
