# NettyReverseProxy

## 解决的痛点
>由于Nginx的反向代理只能代理后端服务器中的其中一个服务器，无法做到同时转发数据到后端多个服务器（一对N），所以此中间件由此需求产生。

## 原理图
![image](https://github.com/GreatGarlic/NettyReverseProxy/blob/master/preview/demo.png)

## 特点
>1.支持一对N数据同时转发（解决Nginx同时只能转发后端一台服务器的问题）
>
>2.支持后端服务器断线重连（断线重连时间间隔可配置）
>
>3.无代码侵入，独立部署
>
>ps:如果你使用Docker部署，即可实现真正的一键部署(基础镜像的构建与应用的打包通过多阶段构建写在一个Dockerfile中)
