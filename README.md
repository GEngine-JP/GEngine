[![GitHub issues](https://img.shields.io/github/issues/GEngine-JP/GEngine.svg)](https://github.com/GEngine-JP/GEngine/issues)
[![GitHub forks](https://img.shields.io/github/forks/GEngine-JP/GEngine.svg)](https://github.com/GEngine-JP/GEngine/network)
[![GitHub stars](https://img.shields.io/github/stars/GEngine-JP/GEngine.svg)](https://github.com/GEngine-JP/GEngine/stargazers)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/GEngine-JP/GEngine/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)]()
[![GitHub followers](https://img.shields.io/github/followers/xiaomo1992.svg?style=social&label=Follow)]()
[![GitHub watchers](https://img.shields.io/github/watchers/GEngine-JP/GEngine.svg?style=social&label=Watch)]()

# ServerCore架构设计图

![](https://static.xiaomo.info/image/project/GameCore.png)

- 网络通讯模块
  ![](/docs/network.png)

# 注意事项

请一定要先安装`lombok`插件，少写很多getter/setter。不装插件会找不到getter/setter方法

# 添加仓库源

添加以下仓库到你的pom.xml文件中

```
    <repositories>
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/GEngine-JP/GEngine</url>
        </repository>
    </repositories>
```

如果项目的pom.xml文件不加这段内容会找不到对应的jar包

# 添加授权信息

windows默认放在`C:/Users/用户名/.m2/settings.xml`下   
mac os默认放在在 `~/.m2/settings.xml`,下

`settings.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
<activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>
  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>github</id>
          <name>GitHub OWNER Apache Maven Packages</name>
          <url>https://maven.pkg.github.com/GEngine-JP/GEngin</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
  <servers>
    <server>
      <id>github</id>
      <username>houko</username>
      <password>0586b09e7a7eb523c301de480f2cfff4eda67d02</password>
    </server>
  </servers>
</settings>
```

# 添加引用

在你的pom文件中`dependencies`添加以下引用

```
    <dependency>
      <groupId>info.xiaomo</groupId>
      <artifactId>g-engine</artifactId>
      <version>3.2.3</version>
    </dependency>
```

#### 更新日志

[release](https://github.com/GEngine-JP/GEngine/releases)

#### 协议声明

[LICENSE](LICENSE)
