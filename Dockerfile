FROM alpine:3.10.1 as build
RUN apk --no-cache add openjdk11
RUN apk --no-cache add maven

# build JDK with less modules
RUN /usr/lib/jvm/default-jvm/bin/jlink \
#RUN jlink  #--no-header-files --no-man-pages \
    --compress=2 \
    --module-path /usr/lib/jvm/default-jvm/jmods \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.crypto.ec \
    --output /jdk-minimal

# fetch maven dependencies
WORKDIR /build
COPY target/*.jar . 
RUN mkdir -p dependency && (cd dependency; /usr/lib/jvm/default-jvm/bin/jar -xf ../*.jar)

# prepare a fresh Alpine Linux with JDK
FROM alpine:3.10.1
# get result from build stage
RUN apk --no-cache add lcms2
COPY --from=build /jdk-minimal /opt/jdk/

COPY --from=build /build/dependency/BOOT-INF/lib /app/lib
COPY --from=build /build/dependency/META-INF /app/META-INF
COPY --from=build /build/dependency/BOOT-INF/classes /app
EXPOSE 8080
CMD /opt/jdk/bin/java $JAVA_OPTS -cp app:app/lib/* com/ceti/wholesale/WholeSaleApplication
