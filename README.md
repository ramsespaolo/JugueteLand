# JugueteLand  - E-Commerce Nativo con Arquitectura Serverless en AWS

JugueteLand es una aplicación móvil nativa para la gestión y compra de juguetes, desarrollada con tecnologías modernas en el ecosistema Android y respaldada por una infraestructura en la nube totalmente escalable y bajo el pilar de la arquitectura *Serverless*. 

---

##  Stack Tecnológico

### Móvil (Frontend)
* **Lenguaje:** Kotlin
* **Framework UI:** Jetpack Compose (Diseño reactivo y moderno)
* **Arquitectura:** MVVM (Model-View-ViewModel) con flujos asíncronos distribuidos
* **Conectividad:** Retrofit 2 & OKHttp (Consumo de APIs REST)
* **Asincronía:** Kotlin Coroutines & StateFlow
* **Carga de Imágenes:** Coil (Carga asíncrona optimizada de URLs de internet)

### Nube (Backend & Base de Datos)
* **Proveedor:** Amazon Web Services (AWS)
* **Punto de Entrada:** AWS API Gateway (HTTP API)
* **Lógica de Negocio:** AWS Lambda (Entorno de ejecución Node.js 20.x)
* **Base de Datos:** Amazon DynamoDB (Base de datos NoSQL de alta escalabilidad)

---

##  Arquitectura de Integración en la Nube

La aplicación móvil se comunica con los servicios de AWS de manera segura y asíncrona a través de los métodos estándar de una API RESTful:

Documentación del Backend (AWS Lambda Functions)

A continuación, se detallan las dos funciones Lambda desarrolladas en Node.js utilizando el SDK de AWS v3, encargadas de procesar las transacciones de la aplicación móvil:

### 1. Función: `InsertarJuguete` (Método: `POST`)
Esta función recibe los datos estructurados de un nuevo juguete desde el formulario de administración móvil, genera un identificador único universal (UUID) y guarda el registro en la base de datos NoSQL.

* **Ruta en API Gateway:** `POST /juguetes`
* **Código Fuente (Node.js):**

```javascript
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, PutCommand } from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});
const docClient = DynamoDBDocumentClient.from(client);

export const handler = async (event) => {
    try {
        // Parsear el cuerpo de la petición enviada desde la app Android
        const body = JSON.parse(event.body || "{}");
        
        const command = new PutCommand({
            TableName: "Juguetes",
            Item: {
                id: body.id,
                nombre: body.nombre,
                precio: Number(body.precio),
                rating: Number(body.rating || 5),
                imagenUrl: body.imagenUrl,
                tieneOferta: Boolean(body.tieneOferta),
                esNuevo: Boolean(body.esNuevo),
                categoria: body.categoria
            }
        });

        await docClient.send(command);

        return {
            statusCode: 201,
            body: JSON.stringify({
                success: true,
                message: "Juguete insertado correctamente en DynamoDB"
            })
        };
    } catch (error) {
        return {
            statusCode: 500,
            body: JSON.stringify({ success: false, error: error.message })
        };
    }
};

import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { DynamoDBDocumentClient, ScanCommand } from "@aws-sdk/lib-dynamodb";

const client = new DynamoDBClient({});
const docClient = DynamoDBDocumentClient.from(client);

export const handler = async (event) => {
    try {
        const command = new ScanCommand({
            TableName: "Juguetes"
        });

        const response = await docClient.send(command);

        return {
            statusCode: 200,
            body: JSON.stringify({
                success: true,
                data: response.Items
            })
        };
    } catch (error) {
        return {
            statusCode: 500,
            body: JSON.stringify({ success: false, error: error.message })
        };
    }
};
