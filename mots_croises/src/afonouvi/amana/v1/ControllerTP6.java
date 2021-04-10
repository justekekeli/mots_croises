package afonouvi.amana.v1;


import afonouvi.amana.modele.ChargerGrille;
import afonouvi.amana.modele.MotsCroisesTP6;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class ControllerTP6 {
	
	private MotsCroisesTP6 motCroises;
	
	
	@FXML // pour rendre la variable visible depuis SceneBuilder
	private GridPane grilleV1 ;
	@FXML // pour rendre la m�thode visible depuis SceneBuilder
	private void initialize() {
		ChargerGrille cg = new ChargerGrille();
		motCroises=cg.extraireGrille(10);
		
		//motCroises = MotsCroisesFactory.creerMotsCroises2x3() ;
		for (Node n : grilleV1.getChildren()){
			if (n instanceof TextField)
			{
				TextField tf = (TextField) n ;
				int lig = ((int) n.getProperties().get("gridpane-row")) +1 ;
				int col = ((int) n.getProperties().get("gridpane-column")) +1 ;
				
				//Cr�ation d'un lien bidirectionnel entre le texte du textfield et une valeur de proposition du modele
				
				attribuerEvenement(tf, lig, col);
			}
		}
		initMotsCroises();
	}
	
	@FXML
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
	
	@SuppressWarnings("unused")
	private void attribuerEvenement(TextField tf,int ligne,int colonne) {
		tf.textProperty().bindBidirectional(motCroises.propositionProperty(ligne, colonne));
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
	//definition en infobulle
		if(texte!=null) {tf.setTooltip(new Tooltip(texte));}
	//ajout d'un listener pour le click de souris r�v�lant la solution
	
		tf.setOnMouseClicked((e)->{ this.clicCase(e);});
	}
	
	@SuppressWarnings("unused")
	private void initMotsCroises() {
		TextField modele =(TextField) grilleV1.getChildren().get(0);
		grilleV1.getChildren().clear();
		for(int i=0;i<motCroises.getHauteur();i++) {
			for(int j=0;j<motCroises.getLargeur();j++) {
				if(!motCroises.estCaseNoire(i+1, j+1)) {
					TextField tf = new TextField();
					tf.setPrefWidth(modele.getPrefWidth());
					tf.setPrefHeight(modele.getPrefHeight());
					for (Object cle : modele.getProperties().keySet()){
						tf.getProperties().put(cle, modele.getProperties().get(cle)) ;
					} 
					attribuerEvenement(tf, i+1, j+1);
					grilleV1.add(tf, j, i);;
				}
				
			}
		}
		
	}
	
	

}
