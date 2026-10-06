package com.ilerna.PowerGym.service;

import com.ilerna.PowerGym.model.Cliente;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Component
public class TransaccionLogger {

    private static final Path ARCHIVO_LOG = Path.of("log", "transacciones.json");

    private final ObjectMapper objectMapper;

    public TransaccionLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public synchronized void registrar(String operacion, Cliente cliente) {
        try {
            Files.createDirectories(ARCHIVO_LOG.getParent());
            ArrayNode transacciones = cargarTransacciones();

            ObjectNode transaccion = transacciones.addObject();
            transaccion.put("fecha", Instant.now().toString());
            transaccion.put("operacion", operacion);
            transaccion.put("entidad", "Cliente");
            transaccion.put("id", cliente.getId());
            transaccion.set("datos", crearDatosCliente(cliente));

            Files.writeString(ARCHIVO_LOG, objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(transacciones) + System.lineSeparator());
        } catch (IOException ex) {
            throw new UncheckedIOException("No se pudo registrar la transaccion", ex);
        }
    }

    public synchronized ArrayNode obtenerTransacciones() throws IOException {
        return cargarTransacciones();
    }

    private ArrayNode cargarTransacciones() throws IOException {
        if (!Files.exists(ARCHIVO_LOG) || Files.size(ARCHIVO_LOG) == 0) {
            return objectMapper.createArrayNode();
        }

        JsonNode contenido = objectMapper.readTree(ARCHIVO_LOG.toFile());
        if (contenido == null || !contenido.isArray()) {
            throw new IOException("El archivo de transacciones no contiene un array JSON");
        }
        return (ArrayNode) contenido;
    }

    private ObjectNode crearDatosCliente(Cliente cliente) {
        ObjectNode datos = objectMapper.createObjectNode();
        datos.put("nombre", cliente.getNombre());
        datos.put("apellidos", cliente.getApellidos());
        datos.put("dni", cliente.getDni());
        datos.put("entrenador", cliente.getEntrenador().getValorBd());
        datos.put("tipoMembresia", cliente.getTipoMembresia().getValorBd());
        datos.put("fechaAlta", cliente.getFechaAlta().toString());
        return datos;
    }
}
