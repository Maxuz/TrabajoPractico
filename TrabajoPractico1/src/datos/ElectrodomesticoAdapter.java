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
	
	
	//M�TODOS DE LA BASE DE DATOS
	
	public ArrayList<Electrodomestico> getAllBD(){
		ArrayList<Electrodomestico> prueba = new ArrayList<Electrodomestico>();     
		
		String sql="select * from electrodomestico";
		Statement sentencia=null;
		ResultSet rs =null;
		try {			
			sentencia= ConexionDB.getInstancia().getConn().createStatement();
			rs= sentencia.executeQuery(sql);
			
			while(rs.next()){
				Electrodomestico elec;
				if(rs.getFloat("carga") != 0)
					elec = new Lavarropas(rs.getInt("id"),rs.getFloat("precioBase"),rs.getString("color"),rs.getString("consumoEnergetico"),rs.getFloat("peso"),rs.getFloat("carga"));
				//int id,float p,String c,String ce,float pe,float carga
				else {
				//int id,float p,String c,String ce,float pe,float res,boolean sinto
					elec = new Television(rs.getInt("id"),rs.getFloat("precioBase"),rs.getString("color"),rs.getString("consumoEnergetico"),rs.getFloat("peso"),rs.getFloat("resolucion"),rs.getBoolean("sintonizador"));
				}	
	    		prueba.add(elec);
			}					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(registro!=null){registro.close();}
				if(sentencia!=null && !sentencia.isClosed()){sentencia.close();}
				ConexionDB.getInstancia().getConn().close();
			}
			catch (SQLException sqle){
				sqle.printStackTrace();
			}
		}
		
		return prueba;
	}
	public Electrodomestico getOneBD(int id){

		String sql="select * from electrodomestico where id=?";
	
		PreparedStatement sentencia=null;
		ResultSet rs=null;
		Electrodomestico elec=null;
		
		try {			
			sentencia= (PreparedStatement) ConexionDB.getInstancia().getConn().prepareStatement(sql);
			sentencia.setInt(1, id);
			rs= sentencia.executeQuery();
			
			if(rs.next()){
				if(rs.getFloat("carga") != 0)
					elec = new Lavarropas(rs.getInt("id"),rs.getFloat("precioBase"),rs.getString("color"),rs.getString("consumoEnergetico"),rs.getFloat("peso"),rs.getFloat("carga"));
				//int id,float p,String c,String ce,float pe,float carga
				else {
				//int id,float p,String c,String ce,float pe,float res,boolean sinto
					elec = new Television(rs.getInt("id"),rs.getFloat("precioBase"),rs.getString("color"),rs.getString("consumoEnergetico"),rs.getFloat("peso"),rs.getFloat("resolucion"),rs.getBoolean("sintonizador"));
				}		
			}					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(rs!=null){rs.close();}
				if(sentencia!=null && !sentencia.isClosed()){sentencia.close();}
				ConexionDB.getInstancia().CloseConn();
			}
			catch (SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return elec;
	}
	public void deleteOneBD(int id){
		
		String sql="delete from electrodomestico where id=?";
		PreparedStatement sentencia=null;
		int i;
		try {			
			sentencia= (PreparedStatement) ConexionDB.getInstancia().getConn().prepareStatement(sql);
			sentencia.setInt(1, id);
			i = sentencia.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(sentencia!=null && !sentencia.isClosed()){sentencia.close();}
				ConexionDB.getInstancia().CloseConn();
			}
			catch (SQLException sqle){
				sqle.printStackTrace();
			}
		}
		/*try
        {
    		registro = comando.executeQuery("DELETE * FROM ELECTRODOMESTICO WHERE id=id");
    		liberaRecursosBD();
        }
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		}*/
	}
	
	public void addOneBD(Electrodomestico elec){
		
		String sql="insert into electrodomestico(id,precioBase,color,consumoEnergetico,peso,resolucion,sintonizador,carga) values (?,?,?,?,?,?,?,?)";
		PreparedStatement sentencia=null;
		Connection conn= ConexionDB.getInstancia().getConn();
		
		try {
			sentencia= (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			sentencia.setInt(1, elec.getId());
			sentencia.setFloat(2, elec.getPrecioBase());
			sentencia.setString(3, elec.getColor());
			sentencia.setString(4, elec.getConsumoEnerg�tico());
			sentencia.setFloat(5, elec.getPeso());
			if (elec instanceof Lavarropas) {
				sentencia.setFloat(6, 0);
				sentencia.setBoolean(7, false);
				sentencia.setFloat(8, ((Lavarropas) elec).getCarga());
			}
			else {
				sentencia.setFloat(6, ((Television) elec).getResolucion());
				sentencia.setBoolean(7,((Television) elec).isSintonizadorTDT());
				sentencia.setFloat(8, 0);
			}
			
			int filasAfectadas=sentencia.executeUpdate();
			ResultSet cps= sentencia.getGeneratedKeys();
			if(cps.next()){
				int elecID=cps.getInt(1);
				elec.setId(elecID);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(sentencia!=null && !sentencia.isClosed()){sentencia.close();}
				ConexionDB.getInstancia().CloseConn();
			}
			catch (SQLException sqle){
				sqle.printStackTrace();
			}
			
		}
		/* try
        {
            PreparedStatement preStmt = null;
        	myconn = ConexionDB.getInstancia().getConn();
            
        	float pb = e.getPrecioBase();
        	String col = e.getColor();
        	String con = e.getConsumoEnerg�tico();
        	float pes = e.getPeso();
        	float nothing = 0;
        	if(e instanceof Lavarropas){
        		float car = ((Lavarropas) e).getCarga();
                preStmt = (PreparedStatement) myconn.prepareStatement("INSERT INTO electrodomestico(precioBase, color, consumoEnergetico, peso, resolucion, sintonizador, carga) VALUES(?,?,?,?,?,?,?)");
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
        		
        		//En la base de datos los valores booleanos estan dados por 1 y 0, es necesario realizar la conversi�n.
        		int sinInt;
        		if(sin)
        			sinInt = 1;
        		else
        			sinInt = 0;
        		preStmt = (PreparedStatement) myconn.prepareStatement("INSERT INTO electrodomestico(precioBase, color, consumoEnergetico, peso, resolucion, sintonizador, carga) VALUES(?,?,?,?,?,?,?)");
        		preStmt.setFloat(1, pb);
        		preStmt.setString(2, col);
        		preStmt.setString(3, con);
        		preStmt.setFloat(4, pes);
        		preStmt.setFloat(5, res);
        		preStmt.setBoolean(6, sin);  
        		preStmt.setFloat(7, nothing);
        	}
        	//registro = comando.executeQuery(query);
        	registro = comando.execute(query);
    		liberaRecursosBD()
        }
		catch(SQLException sqle){
			System.out.println(sqle.getMessage());
		};*/
	}
	public void updateOneBD(Electrodomestico e){	
        try
        {
        	float pb = e.getPrecioBase();
        	String col = e.getColor();
        	String con = e.getConsumoEnerg�tico();
        	float pes = e.getPeso();
        	if(e instanceof Lavarropas){
        		float car = ((Lavarropas) e).getCarga();
        		query = "UPDATE ELECTRODOMESTICO SET(precioBase=pb, color=col, consumoEnerg�tico=con, peso=pes, carga=car) WHERE id=id";
        	}
        	else if (e instanceof Television){
        		float res = ((Television) e).getResolucion();
        		boolean sin = ((Television)e).isSintonizadorTDT();
        		query = "UPDATE ELECTRODOMESTICO SET(precioBase=pb, color=col, consumoEnerg�tico=con, peso=pes, resolucion=res, sintonizador=sin) WHERE id=id";
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
	
	//M�TODOS DEL CAT�LOGO

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
}
