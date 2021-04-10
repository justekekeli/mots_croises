/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afonouvi.amana.modele;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChargerGrille{ 
    private Connection connexion ;
    public Connection connecterBD() throws SQLException, ClassNotFoundException{
        Connection connect; 
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/base_bousse","root",""); 
        return connect;  
    }   
    // Retourne la liste des grilles disponibles dans la B.D. 
    // Chaque grille est décrite par la concaténation des valeurs    
    // respectives des colonnes nom_grille, hauteur et largeur.   
    // L’élément de liste ainsi obtenu est indexé par le numéro de    
    // la grille (colonne num_grille).    
    // Ainsi "Français débutants (7x6)" devrait être associé à la clé 10 
    public Map<Integer, String> grillesDisponibles(){
        Map<Integer, String> list = new HashMap<>();
        String req= "SELECT * FROM TP5_GRILLE";
        Statement st=null;
        ResultSet set=null;
        try {
        	     connexion= connecterBD();
                 st = connexion.createStatement();
                 set = st.executeQuery(req);
                 while(set.next()){
                     int num= set.getInt("num_grille");
                     String nom= set.getString("nom_grille");
                     int largeur = set.getInt("largeur");
                     int hauteur = set.getInt("hauteur");
                     list.put(num, nom+" "+largeur+"x"+hauteur);
                 }
                 set.close();
                 set = null;
                 st.close();
                 st = null;
                 connexion.close(); 
                 connexion = null;
        } catch (SQLException ex) {
            Logger.getLogger(ChargerGrille.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
        	if (set != null) 
        	{
        		try { set.close(); 
        		} catch (SQLException e)
        	{ e.printStackTrace(); }
        	set = null;
        	}
        	if (st != null) 
        	{
        		try { st.close(); 
        		} catch (SQLException e) { e.printStackTrace();  }
        		st = null;
        	}
        	if (connexion != null) {
        		try { 
        			connexion.close(); 
        			} 
        		catch (SQLException e) { e.printStackTrace();  }
        		connexion = null;
        	}
        }
        return list;
    }  
    @SuppressWarnings("resource")
	public MotsCroisesTP6 extraireGrille(int numGrille){
    	MotsCroisesTP6 motCroise = null;
    	int largeur=0;
    	int hauteur=0;
        PreparedStatement pst =null;
        ResultSet set=null;
        try {
        	   connexion= connecterBD();
               pst = 
               connexion.prepareStatement("SELECT TP5_GRILLE.* FROM TP5_GRILLE WHERE num_grille = ?");
               pst.setInt(1, numGrille);
               set = pst.executeQuery();
                while(set.next()){
                    largeur = set.getInt("largeur");
                    hauteur = set.getInt("hauteur");              
                         
                }
                motCroise = new MotsCroisesTP6(hauteur, largeur);
               // System.out.println()
                set=null;
                pst = 
                connexion.prepareStatement("SELECT * FROM TP5_MOT WHERE num_grille = ?");
                pst.setInt(1, numGrille);
                set  = pst.executeQuery();
                while(set.next()){
                    boolean horizBool = (set.getInt("horizontal")==1)?true:false;
                    int ligne =set.getInt("ligne");
                    int colonne =set.getInt("colonne");
                    String sol=set.getString("solution");
                    itererSolution(motCroise,ligne, colonne, sol, horizBool);                   
                    String definition= set.getString("definition");
                    motCroise.setDefinition(ligne, colonne, horizBool, definition);
                   
                }
                set.close();
                set = null;
                pst.close();
                pst = null;
                connexion.close();
                connexion = null;  
               
        } catch (Exception ex) {
            Logger.getLogger(ChargerGrille.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
        	if (set != null) 
        	{
        		try { set.close(); 
        		} catch (SQLException e)
        	{ e.printStackTrace(); }
        	set = null;
        	}
        	if (pst != null) 
        	{
        		try { pst.close(); 
        		} catch (SQLException e) { e.printStackTrace();  }
        		pst = null;
        	}
        	if (connexion != null) {
        		try { 
        			connexion.close(); 
        			} 
        		catch (SQLException e) { e.printStackTrace();  }
        		connexion = null;
        	}
        }
        
        return motCroise;
        
    }
    //associer les solutions à chaque case
    private void itererSolution(MotsCroisesTP6 motC,int ligne,int col,String mot,boolean horiz) {
    	char []solutionTab = mot.toUpperCase().toCharArray();
    	int i=0;
    	if(horiz) {
    		while(i<solutionTab.length && motC.coordCorrectes(ligne, col)) {
    			motC.setCaseNoire(ligne, col, false);
    			motC.setSolution(ligne, col, solutionTab[i]);
    			i++;
    			col++;
    		}
    		
    	}else {
    		while(i<solutionTab.length && motC.coordCorrectes(ligne, col)) {
    			motC.setCaseNoire(ligne, col, false);
    			motC.setSolution(ligne, col, solutionTab[i]);
    			i++;
    			ligne++;
    		}
    	}
    }
    
}