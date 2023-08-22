package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Vendedor obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
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
