package com.example.messagecenter.receive.controller;

import com.example.messagecenter.receive.service.ReceiveMessageService;
import com.example.messagecenter.receive.vo.MessageReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author lixiangqian
 * @since 2021/5/15 18:14
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final ReceiveMessageService receiveMessageService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/sendMsg")
    @ResponseBody
    public String sendMsg(@Valid MessageReq messageReq) {
        receiveMessageService.sendMessage(messageReq.toMessageVo());
        return "success";
    }
}