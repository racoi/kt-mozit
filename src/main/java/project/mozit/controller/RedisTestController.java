package project.mozit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/redis/set")
    public String setValue(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return "Set key: " + key + " with value: " + value;
    }

    @GetMapping("/redis/get")
    public String getValue(@RequestParam String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? "Value: " + value : "Key not found";
    }
}
