package aplicacao;

import java.util.Date;

import model.dao.FabricaDao;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

public class Programa {

	public static void main(String[] args) {
		
		Departamento obj = new Departamento(1,"Livros");
		
		System.out.println(obj);
		
		Vendedor vend = new Vendedor(21,"felipe santana" , "felipe08@gmail.com" , new Date(),3000.0 , obj);
		
		System.out.println(vend);
		
		VendedorDao vendedorDao = FabricaDao.criarVendedorDao();

	}

}
