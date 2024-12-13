#1. 指定构建上下文为当前目录，书写dockerfile时的copy可以以当前目录作为根目录 --build-context
docker build . -t yahaha-toolchain:v1.0 -f ./docker/Dockerfile

#2. 运行docker容器
docker run -d -p 8080:8080 yahaha-toolchain:v1.0