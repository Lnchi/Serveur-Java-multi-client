import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {  
	
	private PrintWriter printWriter; 
	private BufferedReader bufferedReader ;
	private Scanner clavier;
	private Socket s;
	
	public Client () { 
		
		try { 
			s = new Socket("localhost",1234);
			
			bufferedReader= new BufferedReader(new InputStreamReader(s.getInputStream()));
			printWriter =new PrintWriter(s.getOutputStream(),true); 
			this.start(); //Démarrage du thread pour vérifier s'il y'a 
			              //une réponse du serveur et l'afficher au niveau du client
			             clavier = new Scanner(System.in); 
			while(true) {
				System.out.println("Saissiser votre requete"); 
				String req=clavier.nextLine(); //Le client saisie sa requete 
				printWriter.println(req); 
				}
			
			
		}catch (Exception e) {
			e.printStackTrace() ; 
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client() ; //Le thread est lancé 

	}
	public void run() {
		try {
		String rep; 
		
		while((rep=bufferedReader.readLine())!=null) { 
			//Tant que la reponse est différente de null le client l'affiche 
           
			
				System.out.println(rep);
			}  
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
}
