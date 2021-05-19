package com.example.messagecenter.receive.vo;

import com.example.messagecenter.common.vo.MessageVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 消息请求封装
 *
 * @author lixiangqian
 * @since 2021/5/16 12:12
 */
@Data
public class MessageReq {
    /**
     * 请求ID（用于跟踪调试）
     */
    @NotBlank(message = "请求ID不能为空")
    private String reqId;

    /**
     * 消息类型 1-指定用户 2-群发 3-发送所有在线用户
     */
    @NotBlank(message = "消息类型不能为空")
    @Pattern(regexp = "^[123]$", message = "消息类型(1-指定用户 2-群发 3-发送所有在线用户)值非法")
    private String type;

    /**
     * 指定用户
     */
    private String toUser;

    /**
     * 指定组
     */
    private String toGroup;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String msg;

    public MessageVo toMessageVo() {
        var messageVo = new MessageVo();
        BeanUtils.copyProperties(this, messageVo);
        return messageVo;
    }
}
