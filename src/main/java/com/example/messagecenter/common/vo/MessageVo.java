package com.example.messagecenter.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 消息对象封装
 *
 * @author lixiangqian
 * @since 2021/5/16 00:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo {
    /**
     * 消息ID 后端自动生成
     */
    private String id;
    /**
     * 消息类型 1-指定用户 2-群发 3-发送所有在线用户
     */
    @NotBlank(message = "消息类型不能为空")
    @Pattern(regexp = "^[12]$", message = "消息类型(1-指定用户 2-群发 3-发送所有在线用户)值非法")
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
    /**
     * 请求ID（用于跟踪调试）
     */
    @NotBlank(message = "请求ID不能为空")
    private String reqId;
}
