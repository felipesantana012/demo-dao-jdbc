package aplicacao;

import java.util.Date;
import java.util.List;

import model.dao.FabricaDao;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

public class Programa {

	public static void main(String[] args) {
		
		
		VendedorDao vendedorDao = FabricaDao.criarVendedorDao();
		
		System.out.println("==== Teste 01 : Vendedor findById ====");
		Vendedor vend = vendedorDao.findById(3);
		System.out.println("Resultado : " + vend);
		
		System.out.println();
		
		System.out.println("==== Teste 02 : Vendedor findByDepartamento ====");
		Departamento dep = new Departamento(2,null);
		List<Vendedor> list = vendedorDao.findByDepartamento(dep);
		for(Vendedor v : list) {
			System.out.println(v);
		}
		
		System.out.println();
		
		System.out.println("==== Teste 03 : Vendedor findByAll ====");
		 dep = new Departamento(2,null);
		 list = vendedorDao.findAll();
		for(Vendedor v : list) {
			System.out.println(v);
		}
		
		
		

	}

}
