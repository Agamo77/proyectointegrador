# proyectointegrador
Programa FumigacionesR Para Manejo de Cotizaciones

DESCRIPCION:
El programa FumigacionesR busca hacer mas eficiente el proceso de cotizacion de la empresa al crear una plataforma digital que permite a los clientes potenciales enviar su informacion por internet sin tener que hacer un primer contacto por telefono o mensaje de texto.

PROBLEMA IDENTIFICADO:
El problema identificado es que la empresa tiene una presencia en linea muy pobre. Esto hace que clientes mas jovenes, acostumbrados a resolver problemas mediante plataformas digitales, se inclinen por proveedores de servicios que ofrecen este tipo de opciones. Debido a esto la empresa deja de poder ofrecer a sus clientes sus servicios mostrando sus precios y obteniendo la oportunidad de labor de venta.

Ademas de esto, el segundo problema que se detecto es que la informacion de las cotizaciones no esta centralizada por lo que la persona que tendria la informacion de la cotizacion seria la que atendio a ese cliente por telefono.

Por ultimo, el problema del calculo de cotizaciones. Actualmente la cotizacion tarda de 2 a 3 horas en generarse debido a que los empleados tienen que encontrar un momento para acceder a sus sistemas y calcular manualmente el estimado.

SOLUCION:
Con el programa FumigacionesR se solucionan estos problemas de la siguiente manera:
1) El problema de la presencia en linea se ataca creando esta plataforma digital que sera accesible desde internet. Lo que aumentara la presencia de la empresa en internet.
2) Los clientes al momento de hacer sus solicitudes, envian la informacion a nuestra base de datos la cual puede ser consultada por cualquier administrador de la plataforma.
3) El calculo de las cotizaciones se hace automaticamente al momento de ser leidas desde la base de datos tomando en cuenta los costos que se le definan desde la plataforma de administrador.

Arquitectura:
La arquitectura de la aplicacion es simple. La aplicacion cuenta con los siguientes componentes:
1) Application/Web Servers hosteados por GlassFish7 y desarrollados en Apache Netbeans IDE 24
2) Base de datos relacional SQL Server Express que se alojara en el mismo servidor que la aplicacion.

![image](https://github.com/user-attachments/assets/69b32bc1-26ad-4555-97e1-c85ae6f12fa2)



