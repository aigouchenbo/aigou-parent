package cn.itsource.aigou.controller;

import cn.itsource.aigou.util.RedisUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @PostMapping("/redis")
    public void set(String key,String value){
        RedisUtils.INSTANCE.set(key,value);
    }

    @GetMapping("/redis")
    public void get(String key){
        RedisUtils.INSTANCE.get(key);
    }

}
