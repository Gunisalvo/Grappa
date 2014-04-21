Modo de usar:

1. Montar um SD Card (preferencialmente classe 10) com o seguinte sistema operacional:
2014-01-07-wheezy-raspian.zip >> http://downloads.raspberrypi.org/raspbian_latest

- Setup:

2. Expandir Disco para ocupar todo o disco
3. Escolher linguagem com formatação UTF-8 (pt-BR, en-US, etc...)
4. Reduzir a memória gráfica para 16MB (valor mínimo, assim temos mais espaço alocado apar processamento)

Se você precisar quiser acessar os comandos acima em um setup finalizado digite: raspi-config

- Preparando Ambiente:

5. sudo apt-get update
6. sudo apt-get upgrade
7. sudo apt-get install openjdk-7-jdk
7.1 sudo nano /etc/enviroment
7.2 inserir linha: JAVA_HOME="/usr/lib/jvm/java-7-openjdk-armhf"
7.3 salvar
7.4 source /etc/enviroment
8. sudo apt-get install tomcat7
8.1 sudo nano /etc/default/tomcat7
8.2 trocar variável para TOMCAT7_USER=root
8.2 trocar variável para TOMCAT7_GROUP=root

- Código Fonte:

9. sudo apt-get install maven
10. em /home/pi/ baixar codigo fonte:

git clone https://github.com/Gunisalvo/Grappa.git

11. cd Grappa/
12. mvn clean package
13. sudo cp /home/pi/Grappa/target/grappa.war /var/lib/tomcat7/webapps/grappa.war
14. sudo service tomcat7 restart
