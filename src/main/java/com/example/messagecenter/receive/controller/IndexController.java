package com.example.messagecenter.receive.controller;

import com.example.messagecenter.receive.service.ReceiveMessageService;
import com.example.messagecenter.receive.vo.MessageReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lixiangqian
 * @since 2021/5/15 18:14
 */
@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Api(tags = "通用接口")
public class IndexController {

    private final ReceiveMessageService receiveMessageService;

    @GetMapping("/tomcat")
    public String tomcat() {
        return "tomcat";
    }

    @GetMapping("/netty")
    public String netty() {
        return "netty";
    }

    @ApiOperation("发送消息")
    @PostMapping("/sendMsg")
    @ResponseBody
    public String sendMsg(@Valid @RequestBody MessageReq messageReq) {
        log.debug("sendMsg: {}", messageReq);
        receiveMessageService.sendMessage(messageReq.toMessageVo());
        return "success";
    }
}