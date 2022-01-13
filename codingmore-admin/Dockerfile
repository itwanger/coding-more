#使用jdk8作为基础镜像
FROM java:8
#指定作者
MAINTAINER tom
#暴漏容器的8088端口
EXPOSE 9002
#将复制指定的docker-demo-0.0.1-SNAPSHOT.jar为容器中的job.jar，相当于拷贝到容器中取了个别名
ADD target/learn-admin-1.0-SNAPSHOT.jar /test.jar
#创建一个新的容器并在新的容器中运行命令
RUN bash -c 'touch /job.jar'
#设置时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#相当于在容器中用cmd命令执行jar包  指定外部配置文件
ENTRYPOINT ["java","-jar","/job.jar"]
