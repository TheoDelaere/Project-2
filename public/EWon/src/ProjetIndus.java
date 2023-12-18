import java.util.Hashtable;

import com.ewon.ewonitf.DefaultEventHandler;
import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.IOManager;
import com.ewon.ewonitf.TagControl;

/**
 *
 */

/**
 * Surveillance des tags et envoi vers un serveur MQTT.
 * Cette classe est dynamique : elle suit les tags dans l'IOServer. Si certains sont rajoutés en cours de route
 * où si d'autres sont supprimés, le système les pendra en compte.
 * Pour limiter les accs réseaux, seuls les tags dont la valeur à changé par rapport au cycle suivant seront renvoyé sur le MQTT.
 * @author Bertrand MICHAUX
 * @version 1
 *
 */
public class ProjetIndus extends Thread{

	/**
	 * Table des tags control
	 */
	private static Hashtable tags = new Hashtable();
	/**
	 * Table des dernières valeurs envoyées
	 */
	private Hashtable lastSend = new Hashtable();
	/**
	 * Pour un usage futur. Permettra d'arrêter la surveillance.
	 */
	private boolean continuer = true;
	/**
	 * Gestionaire du protocole MQTT.
	 */
	private MQTTHandler mh;

	/**
	 * Constructeur par défaut.
	 * <ul>
	 * 	<li>Lance le gestionaire de connexion au broker</li>
	 * 	<li>Démarre le thread de surveillance.</li>
	 * </ul>
	 */
	public ProjetIndus() {
		try {
			mh = new MQTTHandler(this);
			System.out.println("  -> Démarrage MU");
			start();
		} catch (IllegalThreadStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
	}

	/**
	 * Thread de mise à jour, en fonction de POOL_DELAY
	 */
	public void run() {
		while(continuer) {
			try {
				updateTC();
				sleep(Settings.POOL_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Vérification et mise à jour des tags dans les structures de données.
	 */
	private void updateTC() {

		for (int i = 0; i < IOManager.getNbTags(); i++) {
			try {
				TagControl tc = new TagControl(TagControl.BY_NDX, i);
				String tagName = tc.getTagName();

				if(!lastSend.containsKey(tagName)) {
					lastSend.put(tagName, tc.getTagValueAsString());
					tags.put(tagName, tc);

					sendTopic(tc);
				}
				else {
					if(!lastSend.get(tagName).equals(tc.getTagValueAsString()))
					{
						lastSend.put(tagName, tc);
						sendTopic(tc);
					}
				}
			} catch (EWException e) {
				System.out.println("Erreur de récup du tag");
				e.printStackTrace();
			}

		}
	}

	/**
	 * Envoie le topic sur le broker mqtt
	 * @param tc
	 */
	private void sendTopic(TagControl tc)
	{
		try {
			lastSend.put(tc.getTagName(), tc.getTagValueAsString());
			mh.sendMessage(tc.getTagName(), tc.getTagValueAsString());
		} catch (EWException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set la valeur d'un tag dans l'ioServer.
	 * Il est nécessaire de récupérer le tagControl par son nom grâce à la
	 * hashtable.
	 * @param topic	le nom du tag à modifier, à l'identique.
	 * @param value la valeur à lui mettre. 
	 * Un cast automatique est réalisé donc un "1" en setTagValueAsString sur un tag booléen fonctionne.
	 */
	public static void setTag(String topic, String value) {
		System.out.println("SET " + topic);
		try 
		{
			TagControl tc = (TagControl)tags.get(topic);
			if(tc != null) {
				tc.setTagValueAsString(value);
			}
			else
				System.err.println("Unknown tag");
		} catch (EWException e) {
			System.err.println("Fail to set tag value to " + topic);
		}
		
	}

	/**
	 * Start point
	 * @param args
	 */
	public static void main(String[] args) {

		
		try 
		{
			System.out.println("Demarrage projet industriel HELHa");
			new ProjetIndus();
						
			
			DefaultEventHandler.runEventManager();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



}
