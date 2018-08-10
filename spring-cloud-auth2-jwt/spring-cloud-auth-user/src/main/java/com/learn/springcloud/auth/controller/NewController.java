package com.learn.springcloud.auth.controller;


import com.learn.springcloud.auth.exception.BadRequestException;
import com.learn.springcloud.auth.exception.PhoneNoHasExistedException;
import com.learn.springcloud.auth.exception.ResourceNotFoundException;
import com.learn.springcloud.auth.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;


@RestController
@RequestMapping("/api/code/usertest")
public class NewController {
    private final static Logger LOGGER = LoggerFactory.getLogger(NewController.class);

    /**
     * 获取全部用户
     * @returne
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getUser() throws Exception {

        List<Map<String, String>> userList = new ArrayList<>(3);
        Map<String, String> resultMap = new HashMap<>(3);
        resultMap.put("userName", "zhangsan");
        resultMap.put("age", "18");
        resultMap.put("gender", "1");
        resultMap.put("userId", "1111");
        userList.add(resultMap);

        if (new Random().nextInt(100) % 3 == 0) {
            LOGGER.warn("模拟系统不存在任何用户的情况，抛出异常");
            throw new ResourceNotFoundException("当前用户不存在");
        }
        return new Result("所有用户信息获取成功", userList);
    }

    /**
     * 获取单个用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Object getUserById(@PathVariable String userId)  throws Exception{

        Map<String, String> resultMap = new HashMap<>(3);
        if (StringUtils.isEmpty(userId)) {
            throw new BadRequestException("参数userId 为空");
        }
        resultMap.put("userName", "zhangsan_0ne");
        resultMap.put("age", "18");
        resultMap.put("gender", "1");
        resultMap.put("userId", userId);

        // resultMap.isEmpty()
        if (new Random().nextInt(100) % 2 == 0) {
            LOGGER.warn("模拟userId对应的用户不存在的情况，抛出异常");
            throw  new ResourceNotFoundException("用户不存在");
        }
        return new Result<Map>("获取当期用户信息成功", resultMap);
    }

    /**
     * 新增一个用户
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public void addUser(@RequestBody User user, UriComponentsBuilder ucBuilder) throws Exception {
        Map<String, String> resultMap = new HashMap<>(3);
        resultMap.put("userName", user.getUserName());
        resultMap.put("age", user.getAge());
        resultMap.put("gender", user.getGender());
        resultMap.put("userId", String.valueOf(new Random(100).nextInt()));

        LOGGER.warn("模拟新增用户[{}]", user);
        if ("zhangsan".equals(user.getUserName())) {
            LOGGER.warn("模拟用户已经存在的情况，抛出异常");
            throw new PhoneNoHasExistedException(String.format("用户[%s]已经存在", user.getUserName()));
        }

    }

    /**
     * 修改一个用户
     * @param user
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public void editUser(@RequestBody User user, @PathVariable String userId) throws Exception {
        Map<String, String> resultMap = new HashMap<>(5);
        resultMap.put("userName", user.getUserName());
        resultMap.put("age", user.getAge());
        resultMap.put("gender", user.getGender());
        resultMap.put("userId", user.getUserId());

        LOGGER.warn("模拟修改用户[{}]", user);
        if (!NumberUtils.isNumber(userId)) {
            LOGGER.warn("模拟请求参数错误，抛出异常");
            throw new BadRequestException("请求参数userId必须为数字");
        }
        if ("1111".equals(userId)) {
            throw new ResourceNotFoundException(String.format("用户[%s]不存在", userId));
        }
    }


    /**
     * 删除一个用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String userId) throws Exception{

        LOGGER.warn("模拟删除用户[{}]", userId);
        if ("1111".equals(userId)) {
            LOGGER.warn("模拟用户不存在的情况，抛出异常");
            throw new ResourceNotFoundException(String.format("用户[%s]不存在", userId));
        }
    }

    /**
     * 删除所有用户
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllUsers() throws Exception{
        LOGGER.warn("模拟删除所有用户");
    }

    @RequestMapping(value = "batchDelete", method = RequestMethod.DELETE)
    public void batchDelete(List<String> ids) {
        LOGGER.warn("批量删除的用户ID[{}]", ids.toString());

    }


    // 批量删除List<String> userIdList = new ArrayList<>();
    // 批量修改 List<User> userList = new ArrayList<>();

}
