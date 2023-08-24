package aplicacao;

import java.util.ArrayList;
import java.util.List;

import model.dao.DepartamentoDao;
import model.dao.FabricaDao;
import model.entities.Departamento;

public class Programa2 {

	public static void main(String[] args) {

		DepartamentoDao departamentoDao = FabricaDao.criarDepartamentoDao();
		
		
		System.out.println("==== Teste 01 findById Departamento");
		Departamento dep = departamentoDao.findById(1);
		System.out.println("Resultado : " + dep);
	
		System.out.println();
		
		System.out.println("==== Teste 02 findAll Departamento");
		List<Departamento> list = new ArrayList<>();
		list = departamentoDao.findAll();
		for(Departamento d : list) {
			System.out.println(d);
		}
		
		System.out.println();
		
		System.out.println("==== Teste 03 Insert Departamento");
		dep = new Departamento(9,"comida");
		departamentoDao.insert(dep);
		System.out.println("Insert! Novo Id : " + dep.getId());
		
		System.out.println();
		
		System.out.println("==== Teste 04 Update Departamento");
		dep = departamentoDao.findById(7);
		dep.setNome("Esporteeees");
		departamentoDao.update(dep);
		System.out.println("Update Realizado");
		
		System.out.println("==== Teste 05 Delete Departamento");
		departamentoDao.deleteById(8);
		System.out.println("Delete Realizado");
		
		

	}

}
