package com.ilerna.PowerGym.repository;

import com.ilerna.PowerGym.model.Cliente;
import com.ilerna.PowerGym.model.Entrenador;
import com.ilerna.PowerGym.model.TipoMembresia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {

    private static final String SQL_SELECT_BASE =
            "SELECT id, nombre, apellidos, dni, entrenador, tipo_membresia, fecha_alta "
                    + "FROM cliente";

    private final JdbcTemplate jdbcTemplate;

    public ClienteRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Cliente> findAll() {
        String sql = SQL_SELECT_BASE + " ORDER BY id";
        return jdbcTemplate.query(sql, clienteRowMapper());
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        String sql = SQL_SELECT_BASE + " WHERE id = ?";
        List<Cliente> resultado = jdbcTemplate.query(sql, clienteRowMapper(), id);
        return resultado.stream().findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE id = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return total != null && total > 0;
    }

    @Override
    public boolean existsByDni(String dni) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE dni = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, dni);
        return total != null && total > 0;
    }

    @Override
    public boolean existsByDniAndIdNot(String dni, Long id) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE dni = ? AND id <> ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, dni, id);
        return total != null && total > 0;
    }

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            return insertar(cliente);
        }
        return actualizar(cliente);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Cliente insertar(Cliente cliente) {
        String sql = "INSERT INTO cliente "
                + "(nombre, apellidos, dni, entrenador, tipo_membresia, fecha_alta) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            establecerParametros(ps, cliente);
            return ps;
        }, keyHolder);

        cliente.setId(keyHolder.getKey().longValue());
        return cliente;
    }

    private Cliente actualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET "
                + "nombre = ?, apellidos = ?, dni = ?, entrenador = ?, tipo_membresia = ?, fecha_alta = ? "
                + "WHERE id = ?";

        jdbcTemplate.update(sql,
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getEntrenador().getValorBd(),
                cliente.getTipoMembresia().getValorBd(),
                Date.valueOf(cliente.getFechaAlta()),
                cliente.getId());

        return cliente;
    }

    private void establecerParametros(PreparedStatement ps, Cliente cliente) throws SQLException {
        ps.setString(1, cliente.getNombre());
        ps.setString(2, cliente.getApellidos());
        ps.setString(3, cliente.getDni());
        ps.setString(4, cliente.getEntrenador().getValorBd());
        ps.setString(5, cliente.getTipoMembresia().getValorBd());
        ps.setDate(6, Date.valueOf(cliente.getFechaAlta()));
    }

    private RowMapper<Cliente> clienteRowMapper() {
        return (rs, rowNum) -> {
            Cliente cliente = new Cliente();
            cliente.setId(rs.getLong("id"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setApellidos(rs.getString("apellidos"));
            cliente.setDni(rs.getString("dni"));
            cliente.setEntrenador(Entrenador.desdeValorBd(rs.getString("entrenador")));
            cliente.setTipoMembresia(TipoMembresia.desdeValorBd(rs.getString("tipo_membresia")));
            cliente.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());
            return cliente;
        };
    }
}
