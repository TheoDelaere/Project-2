
/**
 * Paramètres généraux de l'application.
 * Editer ces paramètres avant la mise en route dans un eWon.
 * Le système écoute les message dans le subtopic <em>evt</em> de BASE_TOPIC.
 * @author Bertrand MICHAUX
 * @version 1
 * 
 */
public final class Settings {

	/**
	 * Ip du broker, un nom DNS devrait fonctionner
	 */
	static final String 	BROOKER = "helhatechniquecharleroi.xyz";

	/**
	 * Port du broker. <b>Par défaut 1883</b>.
	 */
	static final int 		PORT = 1883;

	/**#
	 * Identité de l'utilisateur
	 */
	static final String 	USER = "groupe3";

	/**
	 * Délai de pool des tags, en ms.
	 */
	static final long 		POOL_DELAY = 1000;

	/**
	 * Mot de passe associé à l'utilisateur.
	 */
	static final String 	PASSWORD = "groupe3";

	/**
	 * Chemin de base pour la lecture et l'écriture des topics.
	 */
	static final String 	BASE_TOPIC = "/groupe3/";

	/**
	 * Identifiant de la connexion. Doit être nimporte quoi tant que
	 * c'est différent d'un autre client
	 */
	static final String 	ID = "ewonBM";

}
