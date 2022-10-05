# SQLiteQueryCreator 🚀


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
  "mysqlConverterPath": <ubicacion_de_las_librerias_de_python> (Ejemplo: "/Users/joseproigtorres/Library/Python/3.8/bin", en concreto donde este ubicada la libreria sqlite3mysql),
  "mysql_user": <usuario_de_mysql_con_permisos> (Debe tener la mayoria de permisos, recomendado root o similar),
  "mysql_passwd": <password>
```

Una vez hecho esto hay que clonarse, en otra carpeta, el siguiente repositorio ([WebTreatmentSQLGenerator](https://github.com/joseproig/WebTreatmentSQLGenerator))

```
git clone https://github.com/joseproig/WebTreatmentSQLGenerator.git
```

Seguidamente, tenemos que hacer un **checkout** de este repositorio hacia la branch de desarrollo (dev)

```
git checkout dev
```


