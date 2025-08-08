
package service;

import java.sql.*;
import java.util.*;

import modelo.PessoaSalarioConsolidado;

public class PessoaService {
	private Connection getConnection() throws SQLException {
	    try {
	        Class.forName("org.postgresql.Driver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "lorena123");
	}
	
    public List<PessoaSalarioConsolidado> listarSalarios() {
        List<PessoaSalarioConsolidado> lista = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pessoa_salario_consolidado")) {

            while (rs.next()) {
                PessoaSalarioConsolidado p = new PessoaSalarioConsolidado();
                p.setPessoaId(rs.getInt("pessoa_id"));
                p.setNomePessoa(rs.getString("nome_pessoa"));
                p.setNomeCargo(rs.getString("nome_cargo"));
                p.setSalario(rs.getDouble("salario"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void recalcularSalarios() {
        String dropTable = "DROP TABLE IF EXISTS pessoa_salario_consolidado";
        String createTable = "CREATE TABLE pessoa_salario_consolidado ("
                + " pessoa_id INT," + " nome_pessoa VARCHAR(150),"
                + " nome_cargo VARCHAR(100),"
                + " salario NUMERIC(10, 2))";
        String insertData = "INSERT INTO pessoa_salario_consolidado (pessoa_id, nome_pessoa, nome_cargo, salario) "
                + "SELECT p.id, p.nome, c.nome, SUM(CASE WHEN v.tipo = 'CREDITO' THEN v.valor WHEN v.tipo = 'DEBITO' THEN -v.valor ELSE 0 END) "
                + "FROM pessoa p "
                + "JOIN cargo c ON p.cargo_id = c.id "
                + "JOIN cargo_vencimentos cv ON c.id = cv.cargo_id "
                + "JOIN vencimentos v ON cv.vencimento_id = v.id "
                + "GROUP BY p.id, p.nome, c.nome";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(dropTable);
            stmt.execute(createTable);
            stmt.execute(insertData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	public void adicionarPessoa(String nome, Integer cargoId, String cidade, String email, String cep,
			String endereco, String pais, String usuario, String telefone, java.util.Date dataNascimento) {
		String sql = "INSERT INTO pessoa (nome, cargo_id, cidade, email, cep, enderco, pais, usuario, telefone, data_nascimento) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn = getConnection()) {

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, nome);
				if (cargoId != null)
					ps.setInt(2, cargoId.intValue());
				else
					ps.setNull(2, Types.INTEGER);
				ps.setString(3, cidade);
				ps.setString(4, email);
				ps.setString(5, cep);
				ps.setString(6, endereco);
				ps.setString(7, pais);
				ps.setString(8, usuario);
				ps.setString(9, telefone);
				if (dataNascimento != null) {
					ps.setDate(10, new java.sql.Date(dataNascimento.getTime()));
				} else {
					ps.setNull(10, Types.DATE);
				}
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void atualizarPessoa(int id, String nome, int cargoId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE pessoa SET nome = ?, cargo_id = ? WHERE id = ?");) {
            ps.setString(1, nome);
            ps.setInt(2, cargoId);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirPessoa(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM pessoa WHERE id = ?");) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}