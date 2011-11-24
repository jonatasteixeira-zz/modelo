/*
 * iM�tica: http://www.matematica.br/
 * iVprog : http://www.matematica.br/ivprog
 *          Visual Interactive Programming is an applet prepared by IME-USP taking Alice system
 *          2010
 * 
 * @author LOB
 * @description Para tratamento multi-linguas
 */

package ima.resourceBundle;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

// Singleton for Internacionalization
public class I18n  {

	private static I18n instance = null;

	private static ResourceBundle bundle;

	private static String static_lang, static_country; // para configurar lingua em chamado do iComb: java icomb.IComb lang=pt

	private static Locale currentLocale = null;

	// Decomp�e: 'lang=pt_BR' em String("pt","BR")
	private static boolean decompoeConfig (String str) {
		// System.out.println("[B.decompoeConfig] inicio: str="+str);
		if (str==null)
			return false;
		StringTokenizer tokens = new StringTokenizer(str,"=");
		String item;
		int tam_item;

		//- System.out.println("[RR.decompoeConfig] #tokens="+tokens.countTokens());
		if (tokens.hasMoreTokens()) {
			item = tokens.nextToken();
			/*//-*/ System.out.println("[RR.decompoeConfig] item="+item);
			if (item==null) return false;
			if (item.equals("lang") && tokens.hasMoreTokens()) {
				// pegou 'pt_BR'
				item = tokens.nextToken();
				tam_item = item.length();
				/*//-*/ System.out.println("[RR.decompoeConfig] item="+item+" #item="+tam_item);
				if (tam_item>2) {
					// � da forma: 'pt_BR'
					static_lang = item.substring(0,2); //
					if (tam_item>4)
						static_country = item.substring(3,tam_item).toUpperCase(); //
					/*//-*/ System.out.println("[RR.decompoeConfig] static_lang="+static_lang+" static_country="+static_country);
					return true;
				}
				else {
					// � da forma: 'pt'
					static_lang = item.substring(0,2); //
					// 
					return true;
				}
			}
			else
				if (item.equals("bg") && tokens.hasMoreTokens()) {
					// pegou 'pt_BR'
					item = tokens.nextToken();
					if (item!=null && item.equals("contrast1")) // Bundle.msg("contraste")
						; // .setContraste(true); // para colocar em modo contraste
				}
				else { // problema: veio 'lang='
					return false; // new String[2];
				}
		}
		return false; // new String[2];
	} // boolean decompoeConfig(String str)

	// iMA aplicativo: define static_ling, tem prioridade sobre outros m�todos
	//                 java -jar iVprog.jar lang=es
	// Arquivo: 'ima.lang' define a lingua (conte�do: "lang=pt", "lang=en" ou "lang=es")
	// Par�m. : 'lingua', 'pais' ou 'lang' (nesta ordem) -> param name='lang' value="pt" ou "en" ou "es" (default: "pt")
	public static void setConfig (String [] args) {
		// lang=pt; lang=en; lang=es
		int i = -1;
		if (static_lang==null || static_lang=="") // evita sobrescrever definicao de 'igeom.cfg'
			static_lang = "pt"; // default
		static_country   = "BR";
		if (args!=null && args.length>0) {
			String item;
			for (i=0; i<args.length; i++) {
				// tokens = new StringTokenizer(" ",args[i]);
				item = args[i].toLowerCase().trim(); // tokens.nextToken().toLowerCase();
				/*//-*/ System.out.println(" ("+i+","+item+") ");
				try {
					if (decompoeConfig(item)) {
						// System.out.println(" <- OK");
					}
				} catch (Exception e) { System.err.println("Erro: leitura de parametros para configuracao: "+e);
				e.printStackTrace(); }
			} // for (i=0; i<args.length; i++)
		}
		try {
			Locale loc = new Locale(static_lang,static_country);
			Locale.setDefault(loc);
			currentLocale = loc;
		} catch (Exception e2) { e2.printStackTrace(); }
		//- java.util.Properties prop = System.getProperties(); //
	} // static void setConfig(String [] args)

	//
	public static void defineBundle (boolean chamaDefine) {
		String msg_nome_default = "i18n", msg_nome,
				lang_aux, country_aux;
		lang_aux = (static_lang!=null && static_lang.length()>0 && static_lang.charAt(0)!='_') ? "_"+static_lang : static_lang;
		country_aux = (static_country!=null && static_country.length()>0 && static_country.charAt(0)!='_') ? "_"+static_country : static_country;

		// Com linha abaixo => aplicativo tenta entrar no prim. 'bundle=ResouceBundle...' com 'i18n_pt_BR_pt_BR"
		// msg_nome = "i18n"+lang_aux.toLowerCase()+country_aux.toUpperCase();
		msg_nome = "i18n";

		// 'i18n*.properties'
		try { //try1
			// 'i18n_lingua_pais.properties'
			try { //try2
				bundle = ResourceBundle.getBundle("ima.resourceBundle."+msg_nome,currentLocale); // (msg_nome+".properties");
				//- System.out.println("1: msg_nome="+msg_nome);
			} catch (Exception e_lingua_pais) { // (java.util.MissingResourceException mre)	
				// Tente agora s� com lingua
				msg_nome = "i18n"+lang_aux.toLowerCase();
				// 'i18n_lingua_pais.properties'
				try { //try3
					bundle = ResourceBundle.getBundle("ima.resourceBundle."+msg_nome); // (msg_nome+".properties");
					//- System.out.println("2: msg_nome="+msg_nome);
				} catch (Exception e_lingua) { // (java.util.MissingResourceException mre)
					// msgGeomInteratInternet = Geometria Interativa na Internet

					try { //try4
						// usualmente entra aqui: ao fazer 'java ...'
						bundle = ResourceBundle.getBundle("ima.resourceBundle.i18n"); // ./i18n_en_US.properties
					} catch (Exception e) {
						System.err.println(" Tenta: tentaResourceURL:"+" msg_nome="+msg_nome+": "+e);
						// tentaResourceURL(msg_nome);
						e.printStackTrace();
					} //try-catch4

				} //try-catch3

			} //try-catch2

		} catch (java.util.MissingResourceException mre) {
			System.err.println("Erro: RB: "+mre);
			// tentaResourceURL("Messages");
		} // catch (java.util.MissingResourceException mre)

		// 
		String s_aux = "iMath project";
		if (bundle!=null) try {
			s_aux = bundle.getString("iMA"); // "Programa��o Visual/Interativa na Internet"
		} catch (Exception e) { System.err.println("Erro: RB: missing 'iMA' name: "+e); }
		System.out.println("\n .: iMA : "+s_aux+" :.\n"); // iComb: 

	} // void defineBundle(boolean chamaDefine)

	// Set the locale language
	public static void setLanguage (String lang, String country) {
		Locale currentLocale = null;

		// Default: Portuguese, Brazil
		if (lang==null || lang=="")
			lang = "pt";
		if (country==null || country=="") {
			if (lang.equals("pt"))
				country = "BR";
			else
				country = "US";
		}
		static_lang = lang;
		static_country = country;

		currentLocale = new Locale(lang, country);
		try {
			// bundle = ResourceBundle.getBundle("ima.resourceBundle.i18n",currentLocale);
			bundle = ResourceBundle.getBundle("ima.resourceBundle.i18n",currentLocale); // ./i18n_en_US.properties
		} catch(MissingResourceException e) {
			System.err.println("Erro: falta o arquivo de mensagens para linguas! Definido: "+lang+"_"+country+": "+e);
			System.err.println("Error: there is missing the message file! Definided: "+lang+"_"+country+": "+e);
		}
	}

	// Set the locale language: from applet with only 'lang' parameter
	public static void setLanguage (String langParam) {
		// Locale currentLocale = null;
		String lang, country;
		StringTokenizer tokens = new StringTokenizer(langParam,"_"); // decompose lang in 2 itens: "en_US" in <"en","US">
		if (tokens.countTokens()>0) {
			static_lang = tokens.nextToken(); // it gets the first item: "en"
			if (tokens.hasMoreTokens())
				static_country = tokens.nextToken(); // it gets the second item: "US"
			lang = static_lang;
			country = static_country;
		}
		else {
			System.err.println("Error: "+langParam+" => lang="+static_lang+", country="+static_country);
			lang = "pt";
			country = "BR";
		}
		try {
			currentLocale = new Locale(lang,country);
			bundle = ResourceBundle.getBundle("ima.resourceBundle.i18n",currentLocale); // ./i18n_en_US.properties
		} catch(MissingResourceException e) {
			System.err.println("Erro: falta o arquivo de mensagens para linguas! Definido: "+static_lang+"_"+static_country+": "+e);
			System.err.println("Error: there is missing the message file! Definided: "+static_lang+"_"+static_country+": "+e);
		}
		System.out.println("I18n "+langParam+" => lang="+static_lang+", country="+static_country+": "+currentLocale);

	} // void setLanguage(String langParam)

	// public static I18n getInstance () {
	//   if (instance == null) instance = new I18n("en","us");
	//   return instance;
	//   }

	public static void changeInstance (String language, String country)  {
		// instance = new I18n(language,country);
	}

	public static String getString (String key) {
		if (bundle==null) return key; // para evitar erro de 'java.lang.NullPointerException' no caso de n�o ter criado Bundle
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static String getString (String key, String [] parametros) { // Object[] parametros
		try {
			String message = bundle.getString(key);
			int pos = message.indexOf("?");
			int k=0;
			while (pos >=0) {
				if (k< parametros.length) {
					message = message.substring(0,pos) + parametros[k++] + message.substring(pos+1);
					pos = message.indexOf("?");
				}
				else {
					break;
				}
			}
			return message;
		} catch(MissingResourceException e) {
			return key;
		}
	}

}
