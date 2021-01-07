1. ~/.m2/settings.xml
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </repository>
        <repository>
          <id>github</id>
          <name>GitHub OWNER Apache Maven Packages</name>
          <url>https://maven.pkg.github.com/GameUnion/ServerCore</url>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <username>houko</username>
      <password>替换成自己的token</password>
    </server>
  </servers>
</settings>
```

2. 上传
`mvn deploy`
   
3. 使用
```
    <repositories>
        <repository>
            <id>ServerCore</id>
            <url>https://raw.github.com/GameUnion/ServerCore/packages</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
```


1. 一次引用全部
 ```
   <dependency>
   <groupId>info.xiaomo</groupId>
   <artifactId>all</artifactId>
   <version>RELEASE</version>
   </dependency>
 ```
2. 单独引用
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