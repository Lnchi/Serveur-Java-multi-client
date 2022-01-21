

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurMT extends Thread {	
	private int nbClient  ; 
	static ArrayList<String> info_ccp = new ArrayList<>();
	public void run(){
		 
	        try {
	        	ServerSocket ss = null;
	            int portEcoute = 1234;
               // ouverture du service et lancer l'écoute sur le port 1234 
	            ss = new ServerSocket(portEcoute);
	            boolean fini = false;
	            Socket sService = null;  //declarer la socket qui va servire le client
	            while (!fini) {
	                // attente et acceptation d'une demande
	                sService = ss.accept();
	                ++nbClient; // le numero du client connecte au serveur 
	              
	                //création du socket service celle a travers laquelle le service sera rendu 
	                new ServiceThread(sService,nbClient).start();
	                // attente des autres demandes de connexion 
	                }
	               //  on clot la connexion
	            ss.close(); 
	                } catch (Exception e) {
	                // traiter l'exception
	                System.out.println("Erreur "+e);}}

	       
	    
	
	 
	class ServiceThread extends Thread{
		private int numeroClient ; 
		private Socket socket;
		 public ServiceThread(Socket s,int numClient ) {
				                    super();
				                    this.socket = s;
				                    this.numeroClient=numClient ;}
		 public void run(){
			 try {   
			   BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
               String ip=socket.getRemoteSocketAddress().toString();
               System.out.println("Connexion du client numero:"+numeroClient+ ", IP:" +ip); 
          	   pw.println("Bienvenu vous etes le client numero:" +numeroClient);//Le thread envoi un message de bienvenu au client 
              
	            	                                                 
	                while(true) {
	                pw.println("Veuillez entrer votre numero cpp de votre compte"); //Le thread envoi un message demandant au client
                     // d'introduire son numero cpp associe a son compte.
                     String nccp = is.readLine();
                     System.out.println("Le client" +ip+ " a envoyé le numero de ccp"+nccp); //Le thread affiche au niveau du serveur 
                     //le numero ccp introduit par le client                      
                     String req;
                     if (verifierNumCcp(nccp)) {
	                 req="Veuillez entrer l'opération voulu: a pour créditer, b pour débiter et c pour consulter ";   
	                 pw.println(req);//Le thread envoi
                     //la requete :un message demandant au client de choisir une des opération
	                 String s = is.readLine();
	                 System.out.println("Le client" +ip+ " a envoyé la requete"+req);//Le thread affiche au niveau du serveur 
                                                                                    //la donnée envoyé par le client
	                 if (s=="a") { //Condition compare si la donnée lu est similaire a a . 
	                   pw.println("Saissisez la somme à créditer"); //Alors le Thread envoi une requete demandant au
	                                                               //client d'introduire la somme à créditer 
	                   String somme = is.readLine();
	                   System.out.println("Le client" +ip+ " a envoyé la somme"+somme);//Le serveur affiche la somme envoyé
	            	   String nom=fchercher_nom(nccp);
	            	   String newSolde= Crediter(somme,nccp);//Appele à la méthode synchronisé Crediter
	            	   pw.println("(" +nccp+"," +nom+"," +newSolde+")");//Le serveur envoi le triplet(nccp,nom,solde) au client                   
	            		                                     }                                                
	                  else if (s=="b")   { 
	            	   pw.println("Saissisez la somme à débiter");
	            	    String somme = is.readLine();
	            	    System.out.println("Le client" +ip+ " a envoyé la somme"+somme);//Alors le Thread envoi une requete demandant au
                                                                                      //client d'introduire la somme à débiter
	            	    String solde= fchercher_solde(nccp);
	            	    double double_solde= Double.parseDouble(solde);
	            	   double double_somme= Double.parseDouble(somme);                                                
	            		  if (double_solde>double_somme) { //Condition vérifie si la somme est inférieur au solde 
	            		       String newSolde= Debiter(somme,nccp);//Alors le serveur appele la méthode synchronisé Debiter
	            		       String nom=fchercher_nom(nccp);
	            		       pw.println("(" +nccp+"," +nom+"," +newSolde+")");
	            		       
	            		    }else pw.println("0,Solde insuffisant,0"); //Sinon Le serveur envoi un message ("0,Solde insuffisant,0")
	            		                                               //au client
	            		                           }                                           
	            		  else {
	            		       String msg =Consulter(nccp) ;//Sinon le serveur appelle la méthode consulter 
	            		        pw.println(""+msg);  }
	            	                      }
                         else {            
                             pw.println("0,Inexistant,0"); //Dans le cas ou le numero ccp n'existe pas.
									 }
	            	                          }
			                						  
	                          }catch (Exception e) { System.out.println("Erreur "+e);} }
		                    }

	
	
	
	

	
	//la fonction responsable de vider le fichier dans une structure arrayliste 
		public void FileToTable(String chemin) {
		   FileReader flux;     // instancier un objet de la classe FileReader pour gerer le flux en lecture de fichier 
		   BufferedReader temp; // instancier un objet de la classe BufferedReader pour gerer les flux de
		                       //caractere tamponnes avec un fichier 
		   String stra;
			       try {
			         flux = new FileReader(new File(chemin)); // flux en  lecture d'un fichier char/char
			         temp = new BufferedReader(flux);          // tampon pour stocker le contenu lu 
			             while ((stra = temp.readLine()) != null) {  // lire une ligne tantque c'est pas fini  
			            	  for (int i = 0; i < info_ccp.size(); i++) {
			            		   if (!"".equals(stra)) {        // si le mot n'est pas vide
		                    	             info_ccp.add(stra);           // ajouter le mot a la structure
		                                                  }              
			            		                                        }temp.close();
		                                                                 }
			              }//traiter les exceptions
			                      catch (FileNotFoundException er) {
			                    	//dans le cas ou le chemin du fichier est faux ou quand le fichier est introuvable 
			           		        System.out.println("fichier n'est pas trouve ");   }
			                      catch (IOException e) {
			                         System.out.println("Erreur >_< "); }
			                                       }
		
		

	synchronized public String  Debiter(String somme, String  nccp) {
		                                            
		   String  solde= fchercher_solde(nccp);//On récupère le solde associe au compte de client 
		                                        //grace a la méthode fchercher_solde
		    double double_solde= Double.parseDouble(solde);//On convertit les String en Double
		    double double_somme= Double.parseDouble(somme);
		    double_solde=double_solde-double_somme;// On retire du solde la somme à débiter
		                                                  
		     String s =String.valueOf(double_solde); //On convertit le nouveau solde en String 
		     MajTableau(s,nccp); // On mis à jour le solde dans le tableau 
		                                                        
		                                                        
			 notify();  
			 return s;//On retourn le nouveau solde 
     	}
	
	
	synchronized public String  Crediter(String somme, String  nccp) {
        String  solde= fchercher_solde(nccp); //On récupère le solde associe au compte de client 
                                              //grace a la méthode fchercher_solde
        double double_solde= Double.parseDouble(solde); //On convertit les String en Double
        double double_somme= Double.parseDouble(somme);
        double_solde=double_solde+double_somme; //On ajoute la somme à créditer au solde
        	
        String s =String.valueOf(double_solde);
              MajTableau(s,nccp); //On mis à jour le solde dans le tableau 
              
              
                             notify();  
                             return s; //On retourn le nouveau solde 
           }

	

	// la fonction responsable de l'opperation Consulter 
	synchronized public String  Consulter(String  nccp) {
	  String nom=null;
	  String solde=null;
		 nom=fchercher_nom(nccp);// fonction pour chercher le nom du 
		                         //client en introduisant le numero ccp du client
		 solde=fchercher_solde(nccp);// fonction pour chercher le solde du client
		                             //en introduisant le numero ccp du client  
		    String msg= concatenation(nccp,nom,solde);  // concatener le triplet numeroccp et
		                                          //le nom et prenom plus le solde du client
				      notify(); //avertir  les threads en attente
				      return msg; }// retourner le triplet au thread qui a fait
	                               //un appel a la fonction Consulter 

	
	
	
	public void  MajTableau(String s , String nccp) {
		
		for (int i = 0; i < info_ccp.size(); i++) { //Boucle qui parcourt le tableau words de type String
			if ((info_ccp.get(i)).equals(nccp)) {   //Condition:on compare le contenu de l'index i et 
                                                 //le numéro ccp passé en paramètre s'ils sont similaire
				
				 int index = i+3 ;
				 info_ccp.set(index, s);  //On mis à jour le solde dans le tableau en écrasant
				                       // l'ancien solde avec le nouveau solde passé en paramètre dans la méthode.
				
			}
			
		}
		
	}
	
	public String fchercher_solde(String nccp) {
		  String ssolde=null;
		 
		for (int i = 0; i < info_ccp.size(); i++) { //Boucle qui parcourt le tableau words de type String
			 if ((info_ccp.get(i)).equals(nccp)) { //Condition:on compare le contenu de l'index i et 
				                                //le numéro ccp passé en paramètre s'ils sont similaire
				 int index = i+3 ; 
			    ssolde=info_ccp.get(index);//On récupère le solde associe au compte ccp 
			 } 
			 
			}return ssolde; //retourne le solde 
		
	}
	
	public String  fchercher_nom(String nccp) {
	
		String nom_prenom=null;
		for (int i = 0; i < info_ccp.size(); i++) {//Boucle qui parcourt le tableau words de type String
			 if ((info_ccp.get(i)).equals(nccp)) {//Condition:on compare le contenu de l'index i et 
                                              //le numéro ccp passé en paramètre s'ils sont similaire
				 int index = i ;
			     nom_prenom=info_ccp.get(index+1); //On récupère le nom_prenom associe au compte ccp 
			    
			 } 
			
			} return nom_prenom ; //retourne le nom complet  
		
	}
	
	
	// la fonction qui verifie l'existance  du client 	
public boolean verifierNumCcp (String nccp) { 
	for (int i = 0; i < info_ccp.size() ; i++) {// parcourire la structure
		                              // comparer chaque elements de la 
		                              //structure avec le numero ccp fournis en paramettre  
         if (info_ccp.get(i).equals(nccp)) {
	      return true ; } // retourner true dans le cas ou le client existe 
	
		}
	return false; //retourner false dans le cas ou le client n'existe pas 
	 
	 }
//la fonction qui permet de concatener le triplet nccp et nom et solde du client
public static String concatenation ( String nccp, String nom , String solde) {
	return nccp+ nom+ solde;
}


//la fonction responsable de remplir le fichier a partir de la structure
public void TableToFile(String chemin) throws FileNotFoundException {
     try {         
  	   FileWriter fw = new FileWriter(new File(chemin)); // flux en  ecriture  d'un fichier 
          for (int i = 0; i < info_ccp.size(); i++) {//parcourire la structure 
             fw.write(info_ccp.get(i) + "\r\n");               // ecrire le mot dans le fichier
         }            fw.close();    // fermer le flux 
      //traiter les execeptions 
     } catch (IOException e) {
      	
        System.out.println("Erreur >_< ");
     }
 }
	
public static void main(String[] args) {
	// TODO Auto-generated method stub
	//le chemain du ficher compte_ccp.txt
	String str = "C:\\Users\\Djouher\\Desktop\\compteccp.txt";
	
	//cree une instance de ServeurFinal
	ServeurMT serveur = new ServeurMT();
	serveur.FileToTable(str); // Copier le contenu du  fichier dans une structure arrayListe    
	   
	new ServeurMT().start();  //démarrer le thread 
                             
	try {
		  serveur.TableToFile(str);                   // ecrire le contenu de la liste dans un fichier    
	} catch (FileNotFoundException ex) {
	    System.err.println("Exception de type "+ex);
	}  
	       }
	 
	   
}
		 
		   
	


