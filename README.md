Tarea 4 ELO-330
=========================

Nombre
------

**erp_upd** : Emulador de retardo y pérdida de paquetes en
transferencias UDP

Sinopsis
--------

    erp_upd retardo_promedio variación_retardo porcentaje_pérdida puerto_local [host_remoto] puerto_remoto 

Descripción
-----------

El programa **erp_upd** cumple con las especificaciones dadas en las instrucciones de la tarea. Además muestra por pantalla las estampas de tiempo al recibir y enviar los paquetes, con resoluciones en nanosegundos.

Nuestra metodología para generar el retraso fue mediante la función de sistema **usleep()**, que se utilizó para dormir el hilo de envio la cantidad de milisegundos deseada en el retraso, considerando la desviación porcentual. Es por esto que nuestro programa trabaja con retrasos inferiores a 1 segundo, debido a la resolución de **usleep()**. 

Este programa es identico funcionalmente a la tarea 3, pero escrito en **Java**.

Ejemplo
-------

Dentro de nuestra tarea 3 se adjuntó un programa externo que funciona como cliente y servidor UDP, a manera de verificar la funcionalidad del programa **erp_udp**.

Para compilar el programa y correr un ejemplo,

    $ make && make run

De forma general (despues de *make*),

    $ java -classpath bin/ erp_upd retardo_promedio variación_retardo porcentaje_pérdida puerto_local [host_remoto] puerto_remoto 


Retorno
-------

**erp_udp** permanece en estado de vigilia para procesamiento de paquetes, por lo que no retorna de forma autónoma. El programa se puede cerrar enviando la señal SIGINT a la máquina virtual Java (mediante Control+C).