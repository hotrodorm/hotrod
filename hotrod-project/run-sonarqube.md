# Run SonarQube

## Install Docker

See online instructions to install docker.

## Pull and run the SonarQube docker image:

    docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest

## Automate SonarQube start on boot

Add the following line to the crontab:

    @reboot echo "[Starting sonarqube at `date`]" >>/home/user1/reboot-docker.log; docker start sonarqube 2>&1 >>/home/user1/reboot-docker.log

Reboot to test this, and run: `docker ps` to validate that SonarQube is up after restart.

## Login and change the password

Open the page http://192.168.56.244:9000 and login with (admin/admin). Change the password (as requested) to: admin / admin1

## Create a Local Project

- Click on `<> Manually`.
    Project Display Name: hotrod
    Project key:          hotrod
    Main Branch name:     master
    -> [Set Up]
- Click on `Locally`:
    Token Name: Analyze "hotrod"
    Expires in: No expiration
    > [Generate]     
- Produces a token such as: `sqp_ca542fce4b9a3511d8149938d75a17ceab11cf09`

## Run Sonar

    cd ~/git/hotrod/hotrod-project
    git pull
    mvn clean verify sonar:sonar -Dsonar.projectKey=hotrod -Dsonar.projectName='hotrod' -Dsonar.host.url=http://192.168.56.244:9000 -Dsonar.token=sqp_ca542fce4b9a3511d8149938d75a17ceab11cf09

Or in one go:

    cd ~/git/hotrod/hotrod-project && git pull && mvn clean verify sonar:sonar -Dsonar.projectKey=hotrod -Dsonar.projectName='hotrod' -Dsonar.host.url=http://192.168.56.244:9000 -Dsonar.token=sqp_ca542fce4b9a3511d8149938d75a17ceab11cf09

See the report at:

     http://192.168.56.244:9000/dashboard?id=hotrod

(use admin / admin1)

