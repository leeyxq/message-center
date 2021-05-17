# message-center

## 一、架构图

<img width="857" alt="架构图" src="https://user-images.githubusercontent.com/16420973/118438787-7a5d5580-b717-11eb-8fe0-7f873fda9547.png">

 设计思想

 1. 使用recevie接受层、push推流层和transport协议传输层三层设计思想，使用spring event解偶，高扩展

 2. push推流层有单机版和集群版

 3. transport协议传输层有tomcat-socket、netty-socket和socketio.js三种实现，也可扩展自己实现

## 二、功能

  1. 支持单机和集群运行模式
  2. 支持websocket和Comet等推流方案
  3. netty推流方案，高性能
  4. 支持消息重试推送（防止用户不在线，丢失消息）
  5. 支持统计（消息成功率，推送qps、rt等指标）



