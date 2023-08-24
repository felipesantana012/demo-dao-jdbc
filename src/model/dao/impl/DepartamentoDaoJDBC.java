package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {

	private Connection conn;
	public DepartamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Departamento obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO departamento"
					+ "(Nome) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Error Inesperado, Nenhuma linha foi afetada");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	@Override
	public void update(Departamento obj) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE Departamento SET Nome = ? WHERE Id = ?");
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
		
	}
	@Override
	public void deleteById(Integer id) {

		PreparedStatement st  = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM Departamento WHERE Id = ?");
			st.setInt(1, id);
			
			int linhasAfetadas = st.executeUpdate();
			if(linhasAfetadas == 0) {
				throw new DbException("Error, id não deletado");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	@Override
	public Departamento findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
		st = conn.prepareStatement("SELECT * "
				+ "FROM Departamento "
				+ "WHERE Departamento.Id = ? ");
		
		st.setInt(1, id);
		rs = st.executeQuery();
		
		if(rs.next()) {
			Departamento dep = instanciarDepartamento(rs);
			return dep;
		}
		return null;
	}catch(SQLException e) {
		throw new DbException(e.getMessage());
	}
	 finally {
		 DB.closeStatement(st);
		 DB.closeResultSet(rs);
	 }
		
	}
	private Departamento instanciarDepartamento(ResultSet rs) throws SQLException {
		
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("Id"));
		dep.setNome(rs.getString("Nome"));
		return dep;
	}


	@Override
	public List<Departamento> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * "
					+ "FROM Departamento "
					+ "ORDER BY Nome;");
			
			rs = st.executeQuery();
			List<Departamento> list = new ArrayList<>();
			while(rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				list.add(dep);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
}
