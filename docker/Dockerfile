FROM harbor.aishuaiche.com/library/java:8-jre
MAINTAINER liuruichao
ADD ./target/test-1.jar auth-demo.jar

ENV LANG C.UTF-8
ENV TZ Asia/Shanghai

EXPOSE 8080
ENTRYPOINT ["/usr/bin/java","-jar","auth-demo.jar"]
