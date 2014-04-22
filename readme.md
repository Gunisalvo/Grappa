Instalação:
===

Distribuição Linux:
----

1. Montar um SD Card (preferencialmente classe 10) com o seguinte sistema operacional:

- **2014-01-07-wheezy-raspian.zip** http://downloads.raspberrypi.org/raspbian_latest

Setup:
----

2. Expandir Disco para ocupar todo o disco
3. Escolher linguagem com formatação UTF-8 (pt-BR, en-US, etc...)
4. Reduzir a memória gráfica para 16MB (valor mínimo, assim temos mais espaço alocado para processamento)

- Se você precisar quiser acessar os comandos acima em um setup finalizado digite: **raspi-config**

Preparando Ambiente:
----

5. sudo apt-get update
6. sudo apt-get upgrade
7. sudo apt-get install openjdk-7-jdk
8. sudo nano /etc/enviroment
9. inserir linha: JAVA_HOME="/usr/lib/jvm/java-7-openjdk-armhf"
10. salvar
11. source /etc/enviroment
12. sudo apt-get install tomcat7
13. sudo nano /etc/default/tomcat7
14. trocar variável para TOMCAT7_USER=root
15. trocar variável para TOMCAT7_GROUP=root
16. salvar e sair

Código Fonte:
----

18. sudo apt-get install maven
19. em /home/pi/ baixar codigo fonte:

- executar: **git clone https://github.com/Gunisalvo/Grappa.git**

20. cd Grappa/
21. mvn clean package
22. sudo cp /home/pi/Grappa/target/grappa.war /var/lib/tomcat7/webapps/grappa.war
23. sudo service tomcat7 restart

Diagramas Elétricos:
----

1. Mapeamento das portas GPIO: https://github.com/Gunisalvo/Grappa/blob/master/diagramas/mapa-gpio.png
