package afonouvi.amana.v2_Etape3;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import afonouvi.amana.modele.ChargerGrille;
import afonouvi.amana.modele.MotsCroisesTP6;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;


public class ControllerTP6_V2 {
	
	private MotsCroisesTP6 motCroises;
	
	@FXML // grille de mots croisés 
	private GridPane grilleV2 ;
	
	@FXML//menu pour le choix d'une grille contenu dans le base de données
	private Menu choixGrille ;
	
	@FXML//option de fermeture de l'application de mots croisés
	private MenuItem fermer;
	
	@FXML// option de choix aléatoire de grille dans la base de données
	private MenuItem grilleAleatoire;

	
	@FXML // pour rendre la méthode visible depuis SceneBuilder
	private void initialize() {
		//grille 10 qui apparait par défaut dans l'application
		ChargerGrille cg = new ChargerGrille();
		motCroises=cg.extraireGrille(10);
		
		//par défaut c'est la première case qui a le focus
		grilleV2.getChildren().get(0).requestFocus();
		
		//initalisation des textfield de la grille
		initMotsCroises();
				
		//ajout de chaque grille de la base de données au menu de choix de grille
		Map<Integer,String> motsCroises = cg.grillesDisponibles();
		for(int i : motsCroises.keySet()) {
			MenuItem item = new MenuItem();
			item.setText(motsCroises.get(i));
			item.setOnAction(e -> {
				motCroises=cg.extraireGrille(i);
				initMotsCroises();
			});
			choixGrille.getItems().add(item);
		}
		//action pour le choix aléatoire d'une grille
		grilleAleatoire.setOnAction(e -> {
				motCroises=cg.extraireGrille(new Random().nextInt(11)+1);
				initMotsCroises();
			});
		//action de fermeture de l'application
		fermer.setOnAction(e -> System.exit(1));
		
	
	}
	
	
	private void initMotsCroises() {
		//récupération des propriété de la première case
		TextField modele =(TextField) grilleV2.getChildren().get(0);
		grilleV2.getChildren().clear();
				
		for(int i=0;i<motCroises.getHauteur();i++) {
			for(int j=0;j<motCroises.getLargeur();j++) {
					
					TextField tf = new TextField();
					tf.setPrefWidth(modele.getPrefWidth());
					tf.setPrefHeight(modele.getPrefHeight());
					
					for (Object cle : modele.getProperties().keySet()){
						tf.getProperties().put(cle, modele.getProperties().get(cle)) ;
					} 
					//les cases noires ne sont pas éditables et ont un font en gris
					if(motCroises.estCaseNoire(i+1, j+1)) {
						tf
						.setStyle("-fx-control-inner-background: #632C87;");
						tf.setEditable(false);
					}
					grilleV2.add(tf, j, i);
					
					//création d'un lien bidirectionnel entre le Gripane et la classe de mot croisés
					tf.textProperty().bindBidirectional(motCroises.propositionProperty(i+1, j+1));
					//définition de l'infobulle de définition
					setInfobulle(tf, i+1, j+1);
					
					//liste des charactères possible pour chaque case
					String[] lettreTab = {"A","B","C","D","E","F","G","H","I","J"
							,"K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
					List<String> lettres =  Arrays.asList(lettreTab);
					
					tf.textProperty().addListener(new ChangeListener<String>() {
				        @Override
				        public void changed(ObservableValue<? extends String> observable,
					    String oldValue, String newValue) {	
				        	if(tf.getText().length()>0) {
				        		String nouveau=" ";
				        		//pour supprimer les espaces et ne prendre en compte q'un caractère par case
				        		for(int y =0;y<tf.getText().length();y++) {
				        			if(tf.getText().charAt(y)!=' ') {
				        				nouveau = ""+tf.getText().toUpperCase().charAt(y);
				        				break;
				        			}
				        		}
				        		 if(lettres.contains(nouveau)) {
				        			   tf.setText(nouveau);
							        	//animation avec ScaleTransition
						        		ScaleTransition st = new ScaleTransition(Duration.seconds(3), tf);
						        		st.setFromX(0.5);
						        		st.setFromY(0.5);			                	    
						        		st.setToX(1);
						        		st.setToY(1);
						        		st.play();
				        				focusNext(tf);
							        }else {
							        	 tf.setText(" ");
							        }							        			        	 
				        	}
				        	
			        	}
					});
					
					//gestion d'évènement de touche de clavier pressé
					tf.setOnKeyPressed(new EventHandler<KeyEvent>(){
						@Override
						public void handle(KeyEvent arg0) {
							if(arg0.getCode()==KeyCode.UP) {
								focusUp(tf);
							}else if(arg0.getCode()==KeyCode.LEFT) {
								focusPrevious(tf);
							}else if(arg0.getCode()==KeyCode.RIGHT) {
								focusNext(tf);
							}else if(arg0.getCode()==KeyCode.DOWN) {
								focusDown(tf);
							}else if(arg0.getCode()==KeyCode.ENTER) {
								
								char solution =motCroises.getSolution(GridPane.getRowIndex(tf)+1, GridPane.getColumnIndex(tf)+1);
								char proposition = (tf.getText().replace(" ", "").length()>0)?tf.getText().replace(" ", "").charAt(0) : ' ';
								if(proposition==solution) {
									tf
									.setStyle("-fx-control-inner-background: green;");
								}
							}
							else if(arg0.getCode()==KeyCode.BACK_SPACE) {
								if(!motCroises.estCaseNoire(GridPane.getRowIndex(tf)+1, GridPane.getColumnIndex(tf)+1)) {
									tf
									.setStyle("-fx-control-inner-background: white;");
								}
								focusPrevious(tf);
							}
							
						}
					});
					tf.setOnMouseClicked((e)->{ this.clicCase(e);});
			}
		}
		
	}
	
	//création d'un l'infobulle combinant la définition horizontale et la définition verticale
	private void setInfobulle(TextField tf,int ligne,int colonne) {
		String texte=null;
		String horizDef=motCroises.getDefinition(ligne, colonne, true);
		String vertiDef=motCroises.getDefinition(ligne, colonne, false);
		if(horizDef!=null && vertiDef!=null) {
			texte=horizDef+"/"+vertiDef;
		}else if(horizDef!=null) {
			texte=""+horizDef;
		}else if (vertiDef!=null) {
			texte=""+vertiDef;
		}
		if(texte!=null) {tf.setTooltip(new Tooltip(texte));}
		
		
	}
	@FXML//fonction pour réveler à un clic de souris
	public void clicCase(MouseEvent e) {
		
		if (e.getButton() == MouseButton.MIDDLE){
			// C'est un clic "au centre"
			TextField caseM = (TextField) e.getSource();
			// n� ligne de la case (cf. boucle du 1.2)
			int lig = ((int) caseM.getProperties().get("gridpane-row")) +1 ;
			// n� colonne de la case (cf. boucle du 1.2)
			int col = ((int) caseM.getProperties().get("gridpane-column")) +1 ;
			// demande de révélation de la solution sur (lig,col)
			motCroises.reveler(lig, col);

		}
	}
	//méthode pour passer à la case suivante sur la ligne
	private void focusNext(TextField current) {
		  int index = grilleV2.getChildren().indexOf(current);
		  index++;
		  if(index<grilleV2.getChildren().size()) {
			  TextField nextField  = (TextField) grilleV2.getChildren().get(index);
			  nextField.requestFocus();		  
		  }
		  
	  
		}
	//méthode pour passer à la case précédente
	private void focusPrevious(TextField current) {
		  int index = grilleV2.getChildren().indexOf(current);
		  index--;
		 if(index>=0) {
			 TextField  previous  = (TextField) grilleV2.getChildren().get(index);
			  previous.requestFocus();			  
		  }
		 
	}
	//méthode pour passer à la case en dessus
	private void focusUp(TextField current) {
		  int index = grilleV2.getChildren().indexOf(current);
		  int up = index-motCroises.getLargeur();
		  TextField upField = (TextField)grilleV2.getChildren().get(up);
		  upField.requestFocus();

	}
	//méthode pour passer à la case en dessous
	private void focusDown(TextField current) {
		 int index = grilleV2.getChildren().indexOf(current);
		  int down = index+motCroises.getLargeur();
		  TextField downField = (TextField)grilleV2.getChildren().get(down);
		  downField.requestFocus();

	}
	
	
	

	
	
	

}
