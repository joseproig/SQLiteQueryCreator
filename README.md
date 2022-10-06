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

Seguidamente hay que crear un fichero .env en la carpeta principal del proyecto con el siguiente contenido (sustituyendo los campos de <mysql_user> y <mysql_passwd>):

```
# In all environments, the following files are loaded if they exist,
# the latter taking precedence over the former:
#
#  * .env                contains default values for the environment variables needed by the app
#  * .env.local          uncommitted file with local overrides
#  * .env.$APP_ENV       committed environment-specific defaults
#  * .env.$APP_ENV.local uncommitted environment-specific overrides
#
# Real environment variables win over .env files.
#
# DO NOT DEFINE PRODUCTION SECRETS IN THIS FILE NOR IN ANY OTHER COMMITTED FILES.
#
# Run "composer dump-env prod" to compile .env files for production use (requires symfony/flex >=1.2).
# https://symfony.com/doc/current/best_practices.html#use-environment-variables-for-infrastructure-configuration

###> symfony/framework-bundle ###
APP_ENV=dev
APP_SECRET=a7f97cc9b722853baaea7d295ae70284
###< symfony/framework-bundle ###

###> doctrine/doctrine-bundle ###
# Format described at https://www.doctrine-project.org/projects/doctrine-dbal/en/latest/reference/configuration.html#connecting-using-a-url
# IMPORTANT: You MUST configure your server version, either here or in config/packages/doctrine.yaml
#
# DATABASE_URL="sqlite:///%kernel.project_dir%/var/data.db"
DATABASE_URL="mysql://<mysql_user>:<mysql_passwd>@127.0.0.1:3306/querygenerator?serverVersion=8.0&charset=utf8mb4"
# DATABASE_URL="postgresql://symfony:ChangeMe@127.0.0.1:5432/app?serverVersion=13&charset=utf8"
###< doctrine/doctrine-bundle ###
DEFAULT_PASSWORD="elCarbassotFaElQuePot*2021/2022"
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

### Utils 🤓

La contraseña del servicio web es (tal y como se especifica en el .env): elCarbassotFaElQuePot*2021/2022


### Help 🥺

Por cualquier tipo de duda contactar con: joseproig1999@gmail.com

