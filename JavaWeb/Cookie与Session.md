## Cookie与Session
> Cookie 当用户通过HTTP访问一个服务器时，服务器会将一些Key/Value键值对返回给客户端浏览器。

> Session 保存在服务端的一个数据结构，用来记录用户的状态
### 为什么会有Cookie？
因为HTTP是一种无状态协议，用户一次访问请求后，后端服务器无法知道下一次来访的用户是不是上次访问的用户。
### 为什么会有Session?
如果每一次都要访问都要返回Cookie，会增加传输量。
### Cookie和Session的区别
- Session不会被禁用，Cookie可以被客户端禁用
- session能够存储Java对象，Cookie只能存储String类型对象
- Cookie可以被客户端修改，Session安全性高
### 分布式Session框架
单独使用CookieheSession都是不可取的。
> 如果使用Cookie，可以解决分布式部署问题，用户每次访问都会将最新的值带回服务器，
> 解决了同一个用户的请求可能不在同一台服务器处理而导致的Cookie不一致的问题。
- 存在的问题
    - Cookie存储限制。浏览器对用户Cookie的存储具有限制
    - Cookie管理的混乱。
    - 安全性。

- 分布式Session框架可以解决的问题
    - Session配置的统一管理
    - Cookie使用的监控和统一规范管理
    - Session存储的多元化
    - Session配置的动态修改
    - Session加密key的定期修改
    - 充分的容灾机制，保持框架的使用稳定性
    - Session各种存储的监控和报警支持
    - Session框架的可扩展性，兼容更多Session机制，如wapSession
- 如何解决跨域名共享Cookie问题
    - 要实现Session同步，需要另一个跳转应用。这个应用可以被一个或多个域名访问。
    - 跳转应用从一个域名获取sessionID
    - 将sessionID作为Cookie写到两个域名下
- 如何解决Cookie被盗取
    - 设置Session签名
    - 将签名作为Cookie在用户的浏览器进程中和服务器传递
- 什么是单点登录?
    - 多系统共存下，用户在一处登陆就不用在其他系统登陆
- 单点登录的Cookie
    - 后端生成一个sessionID 设置到Cookie，后面所有请求都会带上Cookie
    - 服务端从cookie里取sessionID，再查询到用户信息
---
参考《深入理解JavaWeb技术内幕》
