# Aplicación de gestión de circuito hidráulico.

Esta aplicación emula el control de un circuito hidráulico.

Consta de dos partes, un interface de usuario y un procesador de comandos. 
Desde el interface de usuario se introducen comandos que después de ser validados son escritos en un archivo de comandos (comandos.txt).
El procesador de comandos lee los comandos desde el archivo y si son correctos los envía a la máquina de estados que emula el circuito
y registra la petición en un fichero log (fichero_log.txt).
La máquina comprueba si el cambio de estado es posible y si lo es lo ejecuta, escribiendo el nuevo estado en un archivo de estados (estados.txt) y registrando el 
cambio en un ficherolog.

La configuración de válvulas se introduce mediante un archivo json (configValvulas.json), y la configuración de la aplicación se puede modificar mediante el archivo config.xml, ambos son procesados en la clase configuracion.
En caso de no existir estos archivos o no ser válidos, se cargará una configuración por defecto.

# Aplicación creada en Java 8 usando Maven.
1. Get adoptopenjdk-8: https://adoptopenjdk.net/
2. Get maven 3.6.x: https://maven.apache.org/
3. Add above to your path if neccessary.
4. `git clone git@github.com:example/test.git`
5. `mvn clean package`
6. `java -jar target/supuesto8-1.0.0-SNAPSHOT.jar`
