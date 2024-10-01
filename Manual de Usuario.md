# Manual de Usuario – Ambiente de Desarrollo Base de Datos

## Descarga e Instalación de SQL Server Express

1. Ingresar a [Microsoft SQL Server Downloads](https://www.microsoft.com/es-co/sql-server/sql-server-downloads) y Descargar la versión **Express**.

2. Después de ejecutar el descargable, se debe seleccionar la instalación **personalizada**.

3. En la siguiente pantalla, se debe elegir la primera opción:  
   **New SQL Server standalone installation or add features to an existing installation**.

4. Aceptar los términos y condiciones, y continuar hasta llegar a la ventana donde se debe deseleccionar la extensión de **Azure**.

5. En la sección de **Feature Selection**, seleccionar:  
   - **Database Engine Services**  
   - **SQL Server Replications**

6. En **Configuración de Instancia**, dejar los valores por defecto.

7. En **Configuración del Server**, también dejar los valores por defecto.

8. En la configuración del motor de base de datos, se debe seleccionar la autenticación de **modo mixto**. Luego, configurar una contraseña y agregar **Add Current User** para poner como administrador al usuario del sistema.

9. Después de dar clic en **Next**, se instalarán los paquetes y la configuración. Al final, aparecerá una pantalla de **Completado** y se podrá dar clic en **Close**.

10. Volver a la pestaña de instalación y seleccionar la tercera opción para instalar el **SQL Server Management Tool**.

11. Esto llevará al usuario a la página de Microsoft, donde podrá descargar el ejecutable. Para descargar, se debe acceder al siguiente enlace:  
   [Descargar SQL Server Management Studio (SSMS)](https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-ver16).  
   El usuario podrá instalar la versión principal o la versión en español.

12. Después de descargar y ejecutar, se abrirá la aplicación donde se deben otorgar los permisos necesarios para completar la instalación.

13. Al finalizar, aparecerá un mensaje de éxito y se podrá cerrar la ventana.

14. Finalmente, el usuario podrá ejecutar el **SQL Server Management Studio**.

15. Dentro de la base de datos, el usuario deberá ingresar los datos que creó anteriormente y podrá acceder al **Motor de Base de Datos**.