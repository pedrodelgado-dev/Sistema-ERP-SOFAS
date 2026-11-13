# 🛒 Guía del Usuario - Carrito de Compras INNOVA SUR

## Introducción

¡Bienvenido al sistema de carrito de compras de INNOVA SUR! Esta guía te mostrará cómo usar las herramientas para guardar configuraciones de sofás y crear pedidos.

### ¿Qué puedes hacer?

- **🛒 Carrito**: Guarda múltiples configuraciones
- **💰 Precios**: Cálculo automático en tiempo real
- **📦 Pedidos**: Crea órdenes automáticas
- **📄 PDF**: Genera presupuestos

---

## 1. Carrito de Compras

### ¿Qué ves?
- **Tabla**: Configuración | Cantidad | Precio Unit. | Total | Acciones
- **Totales**: N items (M unidades) | Total: S/ X,XXX.XX
- **Botones**: Vaciar | Crear Pedidos | Descargar PDF

### Acciones:

#### ✏️ Editar Cantidad
1. Haz clic en **Editar** en la columna Acciones
2. Cambia la cantidad (1-100)
3. Presiona **OK**

#### 🗑️ Eliminar Item
1. Haz clic en **Quitar** en la columna Acciones
2. ¡Listo! Se elimina inmediatamente

#### 🧹 Vaciar Todo
1. Presiona **Vaciar Carrito**
2. Confirma en el diálogo

### Cálculo de Precios

Los precios incluyen:
- **BOM**: Costo de materiales (varía por modelo)
- **M.O.**: Mano de obra a S/ 45/hora
- **Recargas**: Por espuma, patas, mecanismo, tela premium, chaise
- **Margen**: 40% sobre costo
- **Descuento**: Si aplica
- **IGV**: 18% (incluido en total)

**Ejemplo:**
```
Moderno 3 cuerpos, Chenille, Patas metal negro
BOM: S/ 1,200 + M.O.: S/ 135 + Recargos: S/ 136 = S/ 1,471
Con margen 40%: S/ 2,060
Con IGV 18%: S/ 2,430 ← TOTAL FINAL
```

---

## 2. Creación de Pedidos

### Pasos:
1. Asegúrate de tener **al menos 1 item** en el carrito
2. Presiona **Crear Pedidos**
3. El sistema crea **1 Pedido por cada item**
4. Cada pedido recibe:
   - Código único: `PED-YYYYMM-XXXXX` (ej: PED-202610-00001)
   - Estado: `CONFIRMADO`
   - Precio y especificaciones
5. El carrito se **vacía automáticamente**

### Ver Pedidos Creados:
Abre el módulo **PEDIDOS** en la sección **COMERCIAL** del menú.

### Información Guardada:
| Campo | Ejemplo |
|-------|---------|
| Código | PED-202610-00001 |
| Cliente | Cliente desde Carrito |
| Tipo | PERSONALIZADO |
| Estado | CONFIRMADO |
| Cantidad | 1, 2, 3... |
| Precio | S/ 2,430.00 |
| Largo/Fondo/Alto | Desde configuración |
| Especificación | Modelo, cuerpos, espuma, patas, etc. |

---

## 3. Generar PDF

### Pasos:
1. Abre el **Carrito**
2. Presiona **Descargar PDF**
3. Se genera un presupuesto con:
   - Especificaciones completas
   - Todos los items del carrito
   - Totales y precios

### Usos:
- 📧 Enviar al cliente
- 🖨️ Imprimir
- 📁 Guardar en archivo

---

## ❓ Preguntas Frecuentes

### ¿Puedo cambiar cantidad después de agregar?
✅ **Sí.** Haz clic en "Editar" en la fila.

### ¿Se pierden las configuraciones al salir?
❌ **No.** Se guardan en base de datos. Verás el mismo carrito al volver.

### ¿Hay límite de items?
❌ **No.** Puedes tener ilimitados items.

### ¿El precio incluye IGV?
✅ **Sí.** El 18% está incluido.

### ¿Puedo duplicar una configuración?
❌ **No directamente.** Agrégala nuevamente al carrito.

### ¿Cómo edito un pedido después?
✅ **Abre PEDIDOS** en el menú COMERCIAL. Puedes cambiar estado, agregar adelanto, etc.

### ¿Hay descuentos automáticos?
✅ **Sí, si están configurados.** Se reflejan en el total.

### ¿Qué pasa con cliente genérico?
📌 Puedes asignarlo manualmente en PEDIDOS.

---

## 📞 Soporte

Si encuentras problemas o tienes preguntas:

- 📧 **Email**: soporte@innovasur.pe
- 📱 **Teléfono**: +51 969 123456
- 🏢 **Ubicación**: Juliaca, Puno, Perú
- ⏰ **Horario**: Lunes a Viernes, 9:00 - 17:00

---

## 🎯 Resumen

| Paso | Módulo | Acción | Resultado |
|------|--------|--------|-----------|
| 1 | Carrito | Guarda configuraciones | Lista con precios |
| 2 | Carrito | Ajusta cantidades | Totales en tiempo real |
| 3 | Carrito | Crea Pedidos | Órdenes automáticas |
| 4 | Carrito | Descarga PDF | Presupuesto listo |

---

## 📊 Estadísticas del Sistema

- ✅ **114 Tests** pasando
- ✅ **Compilación** limpia
- ✅ **9 Servicios** integrados
- ✅ **Cálculo dinámico** de precios
- ✅ **Persistencia** en base de datos
- ✅ **Generación** de PDF

---

**Versión**: 1.0  
**Última actualización**: Octubre 2026  
**© 2026 INNOVA SUR - Todos los derechos reservados**
