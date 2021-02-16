[![Build Status](https://travis-ci.org/GameUnion/ServerCore.svg?branch=master)](https://travis-ci.org/GameUnion/ServerCore)
[![GitHub issues](https://img.shields.io/github/issues/GameUnion/ServerCore.svg)](https://github.com/GameUnion/ServerCore/issues)
[![GitHub forks](https://img.shields.io/github/forks/GameUnion/ServerCore.svg)](https://github.com/GameUnion/ServerCore/network)
[![GitHub stars](https://img.shields.io/github/stars/GameUnion/ServerCore.svg)](https://github.com/GameUnion/ServerCore/stargazers)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/GameUnion/ServerCore/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)]()
[![GitHub followers](https://img.shields.io/github/followers/houko.svg?style=social&label=Follow)]()
[![GitHub watchers](https://img.shields.io/github/watchers/GameUnion/ServerCore.svg?style=social&label=Watch)]()

# ServerCore架构设计图

![](https://static.xiaomo.info/image/project/GameCore.png)

- 网络通讯模块
  ![](/docs/network.png)

# 注意事项

请一定要先安装`lombok`插件，少写很多getter/setter。不装插件会找不到getter/setter方法

# 添加仓库

```
    <repositories>
        <repository>
            <id>ServerCore</id>
            <url>https://raw.github.com/GEngine-JP/GEngine/packages</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
```

如果项目的pom.xml文件不加这段内容会找不到对应的jar包

## 1. 引用

```
    <dependency>
      <groupId>info.xiaomo</groupId>
      <artifactId>g-engine</artifactId>
      <version>3.1.1</version>
    </dependency>
```

# 更新日志
[release](https://github.com/GEngine-JP/GEngine/releases)


# [协议声明](LICENSE)

       Copyright 2017-2021 GEngine-JP Group
    
       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
