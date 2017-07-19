FROM gateway/system:1.0.0

MAINTAINER liuyuan <405653510@qq.com>

ADD ./target/  /mnt/NettyReverseProxy/App/

WORKDIR /mnt/NettyReverseProxy/App/

ENTRYPOINT ["java", "-jar"]

CMD ["NettyReverseProxy-1.0.jar"]