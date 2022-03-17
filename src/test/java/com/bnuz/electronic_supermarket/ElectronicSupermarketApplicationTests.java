package com.bnuz.electronic_supermarket;

import com.bnuz.electronic_supermarket.common.utils.GsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElectronicSupermarketApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    void testGson(){
        List<String> a = new ArrayList<>();
        a.add("121212");
        a.add("123413");
        String s = GsonUtil.getGson().toJson(a);
        System.out.println(s);
        List list = GsonUtil.getGson().fromJson(s, List.class);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}
