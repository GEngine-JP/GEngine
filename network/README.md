
[![Build Status](https://travis-ci.org/ChessGame/GameCore.svg?branch=master)](https://travis-ci.org/ChessGame/GameCore)
[![GitHub issues](https://img.shields.io/github/issues/ChessGame/GameCore.svg)](https://github.com/ChessGame/GameCore/issues)
[![GitHub forks](https://img.shields.io/github/forks/ChessGame/GameCore.svg)](https://github.com/ChessGame/GameCore/network)
[![GitHub stars](https://img.shields.io/github/stars/ChessGame/GameCore.svg)](https://github.com/ChessGame/GameCore/stargazers)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/ChessGame/GameCore/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)]()
[![GitHub followers](https://img.shields.io/github/followers/xiaomoinfo.svg?style=social&label=Follow)]()
[![GitHub watchers](https://img.shields.io/github/watchers/ChessGame/GameCore.svg?style=social&label=Watch)]()



# 目录结构
    ├─protocol
    |    ├─消息接口 Message.java
    |    ├─消息池接口 MessagePool.java
    |    ├─网络消费者接口 NetworkConsumer.java
    |    ├─网络监听器接口 NetworkEventListener.java
    |    ├─网络服务接口 NetworkService.java
    |    ├─网络服务构建器 NetworkServiceBuilder.java
    |    ├─消息队列接口 MessageProcessor.java
    |    ├─消息过滤器接口 MessageFilter.java
    |    ├─handler
    |    |    ├─消息解码器 MessageDecoder.java
    |    |    ├─消息编码器 MessageEncoder.java
    |    |    └─消息执行器 MessageExecutor.java
    |    ├─websocket
    |    |    ├─Byte转websocket ByteToWebSocketFrameHandler.java
    |    |    ├─websocket转Byte WebSocketFrameToByteHandler.java
    |    |    └─编码器 WebSocketMessageEncoder.java
    |    ├─client


# 接口说明

## 消息接口 
所有通讯信息都应实现此接口，该接口默认继承自 Runnable,可以多线程运行

## 消息池
所有的通讯消息池都应实现此接口,所有通讯请求都应预先被添加到消息池。在通讯过程中未注册的消息则会被拦截

## 网络消费者接口
供消息执行器调用，执行对应的方法

## 网络监听器接口
监听网络的连接和断开

## 网络服务接口
创建一个netty的网络服务

## 消息编码器
把消息编码成二进制

## 消息解码器
把二进制数据解码成消息

## 消息执行器
执行请求消息对应的方法

## 消息队列接口
将消息分发到不同的队列执行

## 消息过滤器接口
在消息执行前后可以执行自定义方法



## 编码解码协议
- 请求头 = 长度(int->4字节) + 消息id(int->4字节)
- 消息   = 请求头 + 内容
- 最后的结果： 4 + 4 + n



# [协议声明](LICENSE)

       Copyright 2017 ChessGame Group
    
       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.