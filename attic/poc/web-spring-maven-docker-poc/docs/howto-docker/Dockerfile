FROM debian:stretch

# sets the password to: pass
RUN useradd -m -p a9b5csUr5wwB6 -s /bin/bash user
RUN mkdir /home/user/apps

ADD openjdk-11+28_linux-x64_bin.tar.gz /home/user/apps
RUN cd /home/user/apps && ln -s jdk-11 java
ENV PATH /home/user/apps/java/bin:${PATH}

ADD apache-tomcat-9.0.27.tar.gz /home/user/apps
RUN cd /home/user/apps && ln -s apache-tomcat-9.0.27 tomcat
ADD server.xml /home/user/apps/tomcat/conf

ADD postgresql-42.2.5.jre6.jar /home/user/apps/tomcat/lib

RUN rm -r /home/user/apps/tomcat/webapps/*

ADD app.tar.gz /home/user/apps/tomcat/webapps/ROOT

RUN rm -r /home/user/apps/tomcat/logs
RUN rm -r /home/user/apps/tomcat/temp
RUN rm -r /home/user/apps/tomcat/work
RUN mkdir /home/user/apps/tomcat/logs
RUN mkdir /home/user/apps/tomcat/temp
RUN mkdir /home/user/apps/tomcat/work

RUN mkdir -p /home/user/apps/tomcat/conf/Catalina/localhost
RUN chown -R user:user /home/user/apps/tomcat/conf/Catalina

RUN chmod -R 755 /home/user
RUN chown -R root:root /home/user
RUN chown -R user:user /home/user/apps/tomcat/logs
RUN chown -R user:user /home/user/apps/tomcat/temp
RUN chown -R user:user /home/user/apps/tomcat/work

EXPOSE 8080

USER user

ENV LOG_DIR /home/user/apps/tomcat/logs

CMD ["/home/user/apps/tomcat/bin/catalina.sh", "run"]

