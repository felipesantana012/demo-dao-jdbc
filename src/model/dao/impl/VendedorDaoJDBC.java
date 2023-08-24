package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

public class VendedorDaoJDBC implements VendedorDao{

	private Connection conn;
	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Vendedor obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO vendedor "
					+ "(Nome, Email, DataNasc, SalarioBase, DepartamentoId) "
					+ "VALUES "
					+ "(?,?,?,?,?)" , 
					Statement.RETURN_GENERATED_KEYS); //vei retornar o id do novo vendedor inserido;
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
				
			}else {
				throw new DbException("Error inesperado, nenhuma linha afetada!");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			
		}
		
		
		
	}

	@Override
	public void update(Vendedor obj) {


		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE vendedor "
					+ "SET Nome = ?, Email = ?, SalarioBase = ?, DataNasc = ?, DepartamentoId = ? "
					+ "WHERE Id = ?" );

			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDouble(3, obj.getSalarioBase());
			st.setDate(4, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			
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
			st = conn.prepareStatement("DELETE FROM vendedor WHERE Id = ?");
			st.setInt(1, id);
			
			int linhasAfetadas = st.executeUpdate();
			if(linhasAfetadas == 0) {
				throw new DbException("Error, id não deletado");
			}
			
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Vendedor findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT vendedor.*,departamento.Nome as DepNome\r\n"
					+ "FROM vendedor INNER JOIN departamento\r\n"
					+ "ON vendedor.DepartamentoId = departamento.Id\r\n"
					+ "where vendedor.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				Vendedor vend = instanciarVendedor(rs, dep);
				return vend;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	

	private Vendedor instanciarVendedor(ResultSet rs, Departamento dep) throws SQLException {
		
		Vendedor vend = new Vendedor();
		vend.setId(rs.getInt("Id"));
		vend.setNome(rs.getString("Nome"));
		vend.setEmail(rs.getString("Email"));
		vend.setSalarioBase(rs.getDouble("SalarioBase"));
		vend.setDataNasc(rs.getDate("DataNasc"));
		vend.setDepartamento(dep);
		return vend;
		
	}

	private Departamento instanciarDepartamento(ResultSet rs) throws SQLException {

		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartamentoId"));
		dep.setNome(rs.getString("DepNome"));
		return dep;
		
	}

	@Override
	public List<Vendedor> findAll() {
		
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT vendedor.*,departamento.Nome as DepNome "
					+ "FROM vendedor INNER JOIN departamento "
					+ "ON vendedor.DepartamentoId = departamento.Id "
					+ "ORDER BY Nome");
			
			
			rs = st.executeQuery();
			List <Vendedor> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while(rs.next()) {
				
				Departamento dep = map.get(rs.getInt("DepartamentoId"));
				
				if(dep == null) {
					 dep = instanciarDepartamento(rs);
					map.put(rs.getInt("DepartamentoId"), dep);
				}
				
				Vendedor vend = instanciarVendedor(rs, dep);
				list.add(vend);
				
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
	
	

	@Override
	public List<Vendedor> findByDepartamento(Departamento departamento) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT vendedor.*,departamento.Nome as DepNome "
					+ "FROM vendedor INNER JOIN departamento "
					+ "ON vendedor.DepartamentoId = departamento.Id "
					+ "where DepartamentoId = ? "
					+ "ORDER BY Nome");
			
			st.setInt(1, departamento.getId());
			rs = st.executeQuery();
			List <Vendedor> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while(rs.next()) {
				//verificando se dep ja existe no map
				Departamento dep = map.get(rs.getInt("DepartamentoId"));
				
				if(dep == null) {
					 dep = instanciarDepartamento(rs);
					 //adicionando no o id no map, para quando for buscar novamente ele ja vai exitir e não vai retornar null
					map.put(rs.getInt("DepartamentoId"), dep);
				}
				
				Vendedor vend = instanciarVendedor(rs, dep);
				list.add(vend);
				
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
