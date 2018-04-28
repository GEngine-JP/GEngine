package info.xiaomo.http;

import info.xiaomo.http.annotation.ControllerPackage;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
 * @author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2018/4/28 20:33
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@ControllerPackage("info.xiaomo.http.controller")
public class HttpMain {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(80, HttpMain.class);
        httpServer.start();
    }
}
