import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.MqttClient;
import com.ewon.ewonitf.MqttMessage;

/**
 * Gestion des messages MQTT
 * @author Bertrand MICHAUX
 * @version 1
 */
public class MQTTHandler extends MqttClient{
	
	private ProjetIndus parent=null;

	/**
	 * Connexion au broker. Part du principe que le broker est sécurisé par un login/password.
	 * @see Settings.java
	 * @throws Exception
	 */
	public MQTTHandler(ProjetIndus parent) throws Exception {
		super(Settings.ID, Settings.BROOKER);
		this.parent=parent;

		setOption("username", Settings.USER);
		setOption("password", Settings.PASSWORD);
		setOption("port", Settings.PORT + "");


		connect();

		// Délai d'attente.
		Thread.sleep(4000);

		// Inscription aux events entrants
		subscribe(Settings.BASE_TOPIC + "evt/#", 1);

		// Censé avoir un 5 ici : on est connecté
		System.out.println(getStatus());
 

	}

	/**
	 * Envoie un message sur le broker avec la politique de <code>retain</code>.
	 * @param topic
	 * @param payload
	 * @throws EWException
	 */
	public void sendMessage(String topic, String payload) throws EWException {
		// TODO Ajouter la vérification si connecté
		MqttMessage msg = new MqttMessage(Settings.BASE_TOPIC + topic, payload);
		publish(msg, 0, true);
	}

	/**
	 * Méthode de gestion des messages entrant. Actuellement cette méthode affiche juste le message en console.
	 */
	public void callMqttEvent(int event) {

		System.out.println("Event : " + event);
		if(event !=1) {
			try {
				MqttMessage msg = readMessage();
				if(msg != null) 
				{
					
					String topic = msg.getTopic().substring(Settings.BASE_TOPIC.length()+4);
					String value = new String(msg.getPayload());
					System.out.println("  -> message : " +
							topic + " | " +
							value);
					
					
					
					if(topic.equals("redTruck1") || topic.equals("greenTruck1") || topic.equals("blueTruck1"))
					{
						parent.setTag(topic,value);
					}
					else
					{
						System.out.println("Topic non géré");
					}
					

				}
			} catch (EWException e) {
				e.printStackTrace();
			}
		}

	}

}
