# SQLiteQueryCreator 🚀

### Requisitos 📋

Java JDK 15.0 ([Link Instalacion](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html))

Python 3.8 ([Link Instalacion](https://www.python.org/downloads/release/python-380/))

Mysql 8.0 ([Link Instalacion](https://dev.mysql.com/downloads/installer/))

sqlite3 ([Link Instalacion](https://www.sqlite.org/download.html))

sqlite3mysql ([Link Instalacion](https://github.com/techouse/sqlite3-to-mysql))



### Instalación 🔧

En primer lugar hay que clonarse este repositorio en local

```
git clone https://github.com/joseproig/SQLiteQueryCreator.git
```

Más tarde, se debe hacer un **checkout** a la branch de desarrollo (dev)

```
git checkout dev
```

Posteriormente, hay que entrar en Intellij Idea o similar, y cargar la libreria de **logos.jar** que se encuentra en **src/fileUtils**

Más tarde, se debe entrar en el fichero mysql_config.json que se encuentra en **src/fileUtils** y cambiar las credenciales con las siguientes:
```
{
  "mysqlConverterPath": <ubicacion_de_las_librerias_de_python> (Ejemplo: "/Users/joseproigtorres/Library/Python/3.8/bin", en concreto donde este ubicada la libreria sqlite3mysql),
  "mysql_user": <usuario_de_mysql_con_permisos> (Debe tener la mayoria de permisos, recomendado root o similar),
  "mysql_passwd": <password>
  }
```

De esta manera, ya podriamos ejecutar el código de esta parte del proyecto.

Una vez hecho esto hay que clonarse, en otra carpeta, el siguiente repositorio ([WebTreatmentSQLGenerator](https://github.com/joseproig/WebTreatmentSQLGenerator))

```
git clone https://github.com/joseproig/WebTreatmentSQLGenerator.git
```

Seguidamente, tenemos que hacer un **checkout** de este repositorio hacia la branch de desarrollo (dev)

```
git checkout dev
```

Posteriormente, ya podemos compilar los diferentes archivos de JS y CSS, juntamente con los archivos de Tailwind

```
npm run 
npx tailwindcss -i ./assets/styles/app.css -o ./public/build/app.css
```

Finalmente, ya podremos ejecutar el servidor de Symfony

```
symfony server:start
```

### Puertos 📌

Para ver la página web: 8080
Para comunicarse con la parte de BackEnd: 8086
(Es importante ejecutar las dos partes en el mismo sistema)


