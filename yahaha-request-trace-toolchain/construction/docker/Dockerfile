#1. 指定基础镜像. amd64结构
FROM --platform=linux/amd64 centos:8

#2. 安装perl:5.36.1等其他工具
RUN  cd /etc/yum.repos.d/ \
     && sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-* \
     && sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-* \
     && yum clean all \
     && yum makecache \
     && yum -y update \
     && yum install wget -y \
     && wget -O /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-vault-8.5.2111.repo \
     && yum clean all \
     && yum makecache \
     && yum install perl -y

# 3. 安装open-jdk8, amd64

RUN mkdir -p /program/java \
    && cd /program/java \
    && wget https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u432-b06/openlogic-openjdk-8u432-b06-linux-x64.tar.gz \
    && tar -xzvf /program/java/openlogic-openjdk-8u432-b06-linux-x64.tar.gz

ENV JAVA_HOME=/program/java/openlogic-openjdk-8u432-b06-linux-x64
ENV JRE_HOME=${JAVA_HOME}/jre
ENV CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
ENV PATH=${JAVA_HOME}/bin:$PATH

# 4. 构建配置
#4.0 软件目录 java lib、construction 目录、logs、config目录

#4.1 拷贝火焰图工具集到/yahaha/construction/FlameGraph
COPY FlameGraph/ /yahaha/construction/FlameGraph/

#4.2 拷贝application.properties 到/yahaha/config/，并修改applicaition.properties 的配置项：yahaha.request.trace.toolchain.flame_graph_path=/yahaha/construction/FlameGraph/flamegraph.pl
COPY application.properties /yahaha/config/

# 4.3 拷贝lib库，如jar包到/yahaha/lib
COPY yahaha-request-trace-toolchain-1.0.jar /yahaha/lib/yahaha-request-trace-toolchain-1.0.jar

#5 暴露http 服务端口
EXPOSE 8080

#6 启动脚本
CMD ["java", "-Dspring.config.location=/yahaha/config/application.properties", "-jar", "/yahaha/lib/yahaha-request-trace-toolchain-1.0.jar", "daemon off;"]
#ENTRYPOINT ["top", "-b"]
# docker run -p 8080:8080 yahaha-toolchain