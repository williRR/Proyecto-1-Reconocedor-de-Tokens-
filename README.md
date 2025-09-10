Pasos de Configuración y Compilación
1. Verificar la Versión de Java Instalada
Primero, abre tu terminal y ejecuta el siguiente comando para saber qué versión de Java tienes instalada en tu sistema.

Bash

java -version

La salida te mostrará la versión actual del JDK (Java Development Kit). Por ejemplo, si ves openjdk version "21.0.8", significa que estás usando Java 21.

2. Configurar el pom.xml
Edita el archivo pom.xml de tu proyecto y asegúrate de que la versión del compilador de Maven coincida con la versión de Java que verificaste en el paso anterior. Modifica la sección <properties> de la siguiente manera:

XML

<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
Guarda los cambios en el archivo.

3. Compilar el Proyecto
Ahora, vuelve a la terminal y ejecuta el siguiente comando desde la raíz del proyecto. Este comando es crucial, ya que limpia cualquier compilación anterior, genera el código de JFlex y, finalmente, compila todo el proyecto.

Bash

mvn clean install
Si la configuración es correcta, verás el mensaje BUILD SUCCESS.

4. Recargar el Proyecto en IntelliJ IDEA
Para que IntelliJ IDEA reconozca los nuevos archivos generados por Maven, busca el panel de herramientas de Maven en el IDE. En este panel, encontrarás un botón para "Reload All Maven Projects" . Haz clic en este ícono.

IntelliJ leerá la nueva configuración del pom.xml y marcará automáticamente el directorio target/generated-sources como una raíz de fuentes, resolviendo así el error de "símbolo no encontrado".
