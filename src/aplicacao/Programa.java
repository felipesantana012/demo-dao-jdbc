package aplicacao;

import java.util.Date;

import model.dao.FabricaDao;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

public class Programa {

	public static void main(String[] args) {
		
		
		VendedorDao vendedorDao = FabricaDao.criarVendedorDao();
		
		Vendedor vend = vendedorDao.findById(3);
		
		System.out.println("Resultado : " + vend);

	}

}
