package com.learn.springcloud.websocket.controller;

import com.learn.springcloud.websocket.entity.ReceiveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


/**
 * 实现理论可以参考 spring in action
 */
@Controller
public class SubController {
    @Autowired
    public SimpMessagingTemplate simpMessagingTemplate;


    /**
     *  client 通过地址subscribe 发送rm， 并且需要订阅 /topic/getResponse（SimpleBroker）的消息
     * @param rm
     */
    @MessageMapping("/subscribe")
    public void subscribe(ReceiveMessage rm) {
        for (int i = 1; i <= 20; i++) {
            //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
            simpMessagingTemplate.convertAndSend("/topic/getResponse", rm.getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * client 通过地址 queue 发送rm， 并且需要订阅/user/zhangsan/message 才能获取消息
     * @param rm
     */
    @MessageMapping("/queue")
    public void queuw(ReceiveMessage rm) {
        System.out.println("进入方法");
        for (int i = 1; i <= 20; i++) {
            /*广播使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
            "/user/" + 用户Id + "/message",其中"/user"是固定的*/
            simpMessagingTemplate.convertAndSendToUser("zhangsan", "/message", rm.getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}