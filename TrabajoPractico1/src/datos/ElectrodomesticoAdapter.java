package datos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.mysql.jdbc.PreparedStatement;
import entidades.Electrodomestico;
import entidades.Lavarropas;
import entidades.Television;

public class ElectrodomesticoAdapter {
	
	static ArrayList<Electrodomestico> elec = new ArrayList<Electrodomestico>();
	
	Connection myconn;
	Statement comando;
	ResultSet registro;
	String query =null;
	
	//MÉTODOS DEL CATÁLOGO
	public static ArrayList<Electrodomestico> getAll(){
		
		if (elec.size() == 0) {
		Electrodomestico e1;
		//Televisores
		e1 = new Television(100, "Gris", "F", 40, 30, true);
		elec.add(e1);
		e1 = new Television(50, "Blanco", "A", 40, 25, false);
		elec.add(e1);
		//Lavarropas
		e1 = new Lavarropas(100, "Gris", "F", 30, 30);
		elec.add(e1);
		e1 = new Lavarropas(50, "Blanco", "A", 30, 25);
		elec.add(e1);
		return elec;
		}
		else {
			return elec;
		}
	}
	public void deleteOne(Electrodomestico e){
		elec.remove(e);
	}	
	public void deleteOne(int id){
		for (Electrodomestico electrodomestico : elec) {
			if (electrodomestico.getId() == id) {
				elec.remove(electrodomestico);
				break;
			}
		}
	}
	public void addOne(Electrodomestico e){
		elec.add(e);		
	}
	public Electrodomestico getOne(int ID)
	{
		for (Electrodomestico electrodomestico : elec) {
			if (electrodomestico.getId() == ID) {
				return electrodomestico;
			}
		}
		Electrodomestico elec = new Electrodomestico();
		return elec;
	}
	public void update(Electrodomestico e){
		elec.set(e.getId(), e);
	}
	
	//MÉTODOS DE LA BASE DE DATOS
	/*
	public Electrodomestico getOneBD(int id){
		
	}
	*/
	public ArrayList<Electrodomestico> getAllBD(){
		ArrayList<Electrodomestico> prueba = new ArrayList<Electrodomestico>();     
		
		String sql="select id, cod_postal, nombre from localidades";
		Statement sentencia=null;
		ResultSet registro =null;
		try {			
			sentencia= ConexionDB.GetConnection().createStatement();
			registro= sentencia.executeQuery(sql);
			
			while(registro.next()){
				Electrodomestico e;
				int id = Integer.parseInt(registro.getString("id"));
	    		float p = Float.parseFloat(registro.getString("precioBase"));
	    		String c = registro.getString("color");
	    		String ce = registro.getString("consumoEnergetico");
	    		float pe = Float.parseFloat(registro.getString("peso"));
	    		float carga = Float.parseFloat(registro.getString("carga"));
	    		float res = Float.parseFloat(registro.getString("resolucion"));
	    		boolean sinto = Boolean.getBoolean(registro.getString("sintonizador"));
	    		if(carga == 0)
	    		{
	    			 e = new Television(id, p, c, ce, pe, res, sinto);
	    			
	    		}
	    		else {
					 e = new Lavarropas(id, p, c, ce, pe, carga);
				}
	    		prueba.add(e);
				
			}					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(registro!=null){registro.close();}
				if(sentencia!=null && !sentencia.isClosed()){sentencia.close();}
				ConexionDB.GetConnection().close();
			}
			catch (SQLException sqle){
				sqle.printStackTrace();
			}
		}
		
		return prueba;
	}
	public void addOneBD(Electrodomestico e){
        try
        {
            myconn = ConexionDB.GetConnection();

            
        	float pb = e.getPrecioBase();
        	String col = e.getColor();
        	String con = e.getConsumoEnergético();
        	float pes = e.getPeso();
        	float nothing = 0;
        	if(e instanceof Lavarropas){
        		float car = ((Lavarropas) e).getCarga();
                PreparedStatement preStmt = (PreparedStatement) myconn.prepareStatement("INSERT INTO electrodomestico(precioBase, color, consumoEnergetico, peso, resolucion, sintonizador, carga) VALUES(?,?,?,?,?)");
        		preStmt.setFloat(1, pb);
        		preStmt.setString(2, col);
        		preStmt.setString(3, con);
        		preStmt.setFloat(4, pes);
        		preStmt.setFloat(5, nothing);
        		preStmt.setFloat(6, nothing);
        		preStmt.setFloat(7, car);
        	}
        	else if (e instanceof Television){
        		float res = ((Television) e).getResolucion();
        		boolean sin = ((Television)e).isSintonizadorTDT();
        		
        		//En la base de datos los valores booleanos estan dados por 1 y 0, es necesario realizar la conversión.
        		int sinInt;
        		if(sin)
        			sinInt = 1;
        		else
        			sinInt = 0;
        		PreparedStatement preStmt = (PreparedStatement) myconn.prepareStatement("INSERT INTO electrodomestico(precioBase, color, consumoEnergetico, peso, resolucion, sintonizador, carga) VALUES(?,?,?,?,?,?)");
        		preStmt.setFloat(1, pb);
        		preStmt.setString(2, col);
        		preStmt.setString(3, con);
        		preStmt.setFloat(4, pes);
        		preStmt.setFloat(5, res);
        		preStmt.setBoolean(6, sin);  
        		preStmt.setFloat(7, nothing);
        	}
        	//registro = comando.executeQuery(query);
        	comando.execute(query);
    		liberaRecursosBD();
        }
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}
	}
	public void deleteOneBD(int id){
        try
        {
    		registro = comando.executeQuery("DELETE * FROM ELECTRODOMESTICO WHERE id=id");
    		liberaRecursosBD();
        }
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}
	}
	public void updateOneBD(Electrodomestico e){	
        try
        {
        	float pb = e.getPrecioBase();
        	String col = e.getColor();
        	String con = e.getConsumoEnergético();
        	float pes = e.getPeso();
        	if(e instanceof Lavarropas){
        		float car = ((Lavarropas) e).getCarga();
        		query = "UPDATE ELECTRODOMESTICO SET(precioBase=pb, color=col, consumoEnergético=con, peso=pes, carga=car) WHERE id=id";
        	}
        	else if (e instanceof Television){
        		float res = ((Television) e).getResolucion();
        		boolean sin = ((Television)e).isSintonizadorTDT();
        		query = "UPDATE ELECTRODOMESTICO SET(precioBase=pb, color=col, consumoEnergético=con, peso=pes, resolucion=res, sintonizador=sin) WHERE id=id";
        	}
    		registro = comando.executeQuery(query);
    		liberaRecursosBD();
        }
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}
	}	
	public void liberaRecursosBD(){
		try{
			

			if(registro != null)
				registro.close();
			
			comando.close();
			myconn.close();
		}
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}
	}
}
