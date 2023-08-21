package model.dao;

import model.dao.impl.VendedorDaoJDBC;

public class FabricaDao {
	
	public static VendedorDao criarVendedorDao() {
		return new VendedorDaoJDBC();
	}

}
