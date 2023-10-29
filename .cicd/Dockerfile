# Dockefile for product , 
FROM openjdk:18.0.2.1-jdk
ARG JAR_DEST
ENV JAR_DEST ${JAR_DEST}
ARG JAR_SOURCE
ENV JAR_SOURCE ${JAR_SOURCE}
# certificates, jks, app.properties 
RUN mkdir -p /opt/i27/
WORKDIR /opt/i27
RUN ls -la
COPY ["${JAR_SOURCE}", "/opt/i27/i27cart-product.jar"]
# JAR_SOURCE will be passed during the docker build command
RUN chmod 777 /opt/i27
EXPOSE 8761
USER root
CMD ["java", "-jar", "/opt/i27/i27cart-product.jar"]

# docker run --name productcont -p hp:cp
# docker run --name productcont -P ====> hp(dynamic) ====> container (fetching from dockerfile)
# using EXPOSE 