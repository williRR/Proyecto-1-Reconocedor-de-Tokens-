# Configuración y Compilación del Proyecto

## 1. Verificar la Versión de Java
Abre tu terminal y ejecuta el siguiente comando para verificar la versión de Java instalada:

```bash
java -version
```
Por ejemplo, la salida puede ser:

```openjdk version "21.0.8"```


Esto indica que estás usando Java 21.


## 2. Configurar el `pom.xml`

Edita el archivo `pom.xml` de tu proyecto y asegúrate de que la versión del compilador de Maven coincida con la versión de Java que verificaste en el paso anterior. Modifica la sección `<properties>` de la siguiente manera:

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## 3. Compilar el Proyecto

Ahora, vuelve a la terminal y ejecuta el siguiente comando desde la raíz del proyecto. Este comando es crucial, ya que limpia cualquier compilación anterior, genera el código de JFlex y, finalmente, compila todo el proyecto.

```bash
mvn clean install
```

## 4. Recargar el Proyecto en IntelliJ IDEA

Para que IntelliJ IDEA reconozca los nuevos archivos generados por Maven, busca el panel de herramientas de Maven en el IDE. En este panel, encontrarás un botón para **Reload All Maven Projects**. Haz clic en este ícono.
