
[![Build Status](https://travis-ci.org/ChessGame/GameCore.svg?branch=master)](https://travis-ci.org/ChessGame/GameCore)
[![GitHub issues](https://img.shields.io/github/issues/ChessGame/GameCore.svg)](https://github.com/ChessGame/GameCore/issues)
[![GitHub forks](https://img.shields.io/github/forks/ChessGame/GameCore.svg)](https://github.com/ChessGame/GameCore/network)
[![GitHub stars](https://img.shields.io/github/stars/ChessGame/GameCore.svg)](https://github.com/ChessGame/GameCore/stargazers)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/ChessGame/GameCore/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)]()
[![GitHub followers](https://img.shields.io/github/followers/xiaomoinfo.svg?style=social&label=Follow)]()
[![GitHub watchers](https://img.shields.io/github/watchers/ChessGame/GameCore.svg?style=social&label=Watch)]()

# GameCore架构设计图
![](https://static.xiaomo.info/image/project/GameCore.png)


#### 注意事项
请一定要先安装`lombok`插件，少写很多getter/setter。不装插件会找不到getter/setter方法

#### 引用方式
第一种方式比较方便,可以引用整个标准库,第二种方式比较灵活。如果你只用得上其中某个模块的功能，适合单独引用。

# 1. 一次引用全部

```
    <dependency>
      <groupId>info.xiaomo</groupId>
      <artifactId>all</artifactId>
      <version>RELEASE</version>
    </dependency>
```

# 2. 单独引用

```
        <dependency>
            <groupId>info.xiaomo</groupId>
            <artifactId>base</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>info.xiaomo</groupId>
            <artifactId>config</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>info.xiaomo</groupId>
            <artifactId>persist</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>info.xiaomo</groupId>
            <artifactId>logger</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>info.xiaomo</groupId>
            <artifactId>protocol</artifactId>
            <version>RELEASE</version>
        </dependency>
```


# [LICENSE](LICENSE)

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