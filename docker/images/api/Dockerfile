FROM openjdk:11-jre
COPY gamification-api.jar /app/
COPY wait-for-it.sh /usr/wait-for-it.sh
RUN chmod +x /usr/wait-for-it.sh
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/gamification-api.jar"]
