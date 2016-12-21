package controleRempes.control.freebox;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.Action;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import controleRempes.MainControleRempes;
import controleRempes.ihm.MenuRempes;

public class LogFreebox implements Action {

	private static LogFreebox logFreebox = null;  

	private static String URL_AUTHORIZE = "http://mafreebox.freebox.fr/api/v3/login/authorize/";
	private static String URL_SESSION = "http://mafreebox.freebox.fr/api/v3/login/session/";
	private static String URL_LOGOUT = "http://mafreebox.freebox.fr/api/v3/login/logout/";

	private static String TOKEN_FILE = "AppToken.txt";

	private static String APP_NAME = "ControleRempes";
	private static String APP_VERSION = "V1.0";

	public enum statusAuthorize {unknown,pending,timeout,granted,denied};

	private String currentSession = null;

	private GuiControl guiControl = null;

	public static final ResourceBundle MESSAGES_BUNDLE = ResourceBundle.getBundle("properties.ihm.messages", Locale.getDefault());


	/**
	 * Si cette fonction n'est pas appelée en premier ca va planté
	 * @param guiControl
	 * @return
	 */
	public static Action getInstance(final GuiControl guiControl) {
		if (logFreebox == null) {
			logFreebox =  new LogFreebox();
		} 
		logFreebox.guiControl = guiControl;
		return logFreebox;
	}

	public static LogFreebox getInstance() 
	{
		if (logFreebox == null) {
			logFreebox =  new LogFreebox();
		} 
		return logFreebox;
	}

	public JSONObject readTokenAuthorize () throws FileNotFoundException {

		JSONObject jsonToken = null;
		FileReader tokentFile = new FileReader(TOKEN_FILE);

		try(BufferedReader br = new BufferedReader(tokentFile)) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			jsonToken = new JSONObject(sb.toString());
			//System.out.println("TokenAuthorize = " + jsonToken.toString());

		} catch (IOException e) {
			guiControl.showError(e.getMessage());
			e.printStackTrace();
		}
		return jsonToken;

	}

	public JSONObject authorize() {
		JSONObject jsonToken = null;
		String hostName = "PC-XP"; // default name
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			guiControl.showError(e1.getMessage()); 
			e1.printStackTrace();
		}	

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(URL_AUTHORIZE);
		httpPost.addHeader("content-type", "application/json");		
		//httpPost.addHeader("content-type", "application/x-www-form-urlencoded");		
		try {
			String paramString = "{\"app_id\":\"" + APP_NAME +"\",\"app_name\":\"Dady\",\"app_version\":\"" + 
					APP_VERSION + "\","+ "\"device_name\":\""+ hostName + "\"}";
			StringEntity params =new StringEntity(paramString);
			httpPost.setEntity(params);
		} catch (UnsupportedEncodingException e) {
			guiControl.showError(e.getMessage());
			e.printStackTrace();
		}

		guiControl.showInfo(MESSAGES_BUNDLE.getString("INFO_AUTHORIZE_BOX"));
		System.out.println("executing request " + httpPost.getRequestLine());

		try (CloseableHttpResponse response = httpclient.execute(httpPost)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();
			String retSrc = EntityUtils.toString(entityReponse); 
			System.out.println(retSrc);
			jsonToken = new JSONObject(retSrc);

			// save the token
			try  (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TOKEN_FILE), "utf-8"))) {
				writer.write(retSrc);
			} catch (IOException e) {				
				guiControl.showError(e.getMessage()); 
				e.printStackTrace();
			}	

		} catch (IOException e) {
			guiControl.showError(e.getMessage()); 
			e.printStackTrace();
		}
		return jsonToken;
	}

	JSONObject getChallengeToken(JSONObject jsonToken) {

		JSONObject result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		Object track_id = jsonToken.get("track_id");
		String uri = URL_AUTHORIZE + track_id.toString();
		HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("content-type", "application/json");	

		try (CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				String retSrc = EntityUtils.toString(entityReponse); 
				// get JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (IOException e) {
			guiControl.showError(e.getMessage()); 
			e.printStackTrace();
		} catch (Exception e1) {
			guiControl.showError(e1.getMessage()); 
			e1.printStackTrace();
		}
		return result;
	}


	public JSONObject getSessionToken(String password) {
		JSONObject jsonToken = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(URL_SESSION);
		httpPost.addHeader("content-type", "application/json");		
		try {
			String paramString = "{\"app_id\": \""+APP_NAME+ "\",\"password\": \"" + password + "\"}";
			StringEntity params =new StringEntity(paramString);
			httpPost.setEntity(params);
		} catch (UnsupportedEncodingException e) {
			guiControl.showError(e.getMessage()); 
			e.printStackTrace();
		}
		try (CloseableHttpResponse response = httpclient.execute(httpPost)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();
			String retSrc = EntityUtils.toString(entityReponse); 
			System.out.println(retSrc);
			jsonToken = new JSONObject(retSrc);
		}
		catch (IOException e) {
			guiControl.showError(e.getMessage()); 
			e.printStackTrace();
		}
		return jsonToken;
	}

	public JSONObject login() {
		JSONObject tokenAuthorize = null;;
		try {
			tokenAuthorize = readTokenAuthorize();
		} catch (FileNotFoundException e) {
			guiControl.showError(e.getMessage());			
			guiControl.showInfo(MESSAGES_BUNDLE.getString("ASK_AUTHORIZE_BOX"));
			tokenAuthorize = authorize();
		}

		System.out.println("TokenAuthorize = " + tokenAuthorize.toString());

		if (!tokenAuthorize.getBoolean("success")) {
			return null;
		}
		JSONObject resultAuthorize = tokenAuthorize.getJSONObject("result");
		String app_token = resultAuthorize.getString("app_token");

		// repeat ??
		// get the challenge token
		JSONObject challengeToken = getChallengeToken(resultAuthorize);
		boolean succes = challengeToken.getBoolean("success");

		JSONObject resultChallenge =  challengeToken.getJSONObject("result");
		statusAuthorize objStatus = resultChallenge.getEnum(LogFreebox.statusAuthorize.class,"status");
		String challenge = resultChallenge.getString("challenge");

		if (!succes || objStatus!=statusAuthorize.granted)
			return null;

		// get the session
		//String password = hmacSha1(app_token, challenge);
		String password = hmacSha1(challenge, app_token);
		JSONObject sessionToken = getSessionToken(password);

		return sessionToken;

	}

	private boolean checkPermission(JSONObject sessionToken) {
		boolean permission = false;
		if (sessionToken.getBoolean("success")) {			
			JSONObject resultSession = sessionToken.getJSONObject("result");

			JSONObject permissions = resultSession.getJSONObject("permissions");
			if (permissions.getBoolean("parental")) {
				currentSession = resultSession.getString("session_token");
				permission = true;
			} else {
				guiControl.showInfo(MESSAGES_BUNDLE.getString("ASK_PARENT_AUTORIZE"));
			}
		}
		return permission;
	}

	public JSONObject logout() {
		JSONObject jsonToken = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(URL_LOGOUT);
		httpPost.addHeader("X-Fbx-App-Auth",currentSession);

		try (CloseableHttpResponse response = httpclient.execute(httpPost)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();
			String retSrc = EntityUtils.toString(entityReponse); 
			System.out.println(retSrc);
			jsonToken = new JSONObject(retSrc);
		}
		catch (IOException e) {
			guiControl.showError(e.getMessage()); 
			e.printStackTrace();
		}
		return jsonToken;
	}

	private static String hmacSha1(String value, String key) {
		try {
			// Get an hmac_sha1 key from the raw key bytes
			byte[] keyBytes = key.getBytes();           
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

			// Get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			// Compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(value.getBytes());

			// Convert raw bytes to Hex
			byte[] hexBytes = new Hex().encode(rawHmac);

			//  Covert array of Hex bytes to a String
			return new String(hexBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(MenuRempes.AUTORIZATION)) {
			System.out.println("Demande autorisation");			
			JSONObject resultAutorise = authorize();

			if (resultAutorise.getBoolean("success")) {
				guiControl.showInfo(MESSAGES_BUNDLE.getString("AUTHORIZE_OK"));
			} else {
				guiControl.showInfo(MESSAGES_BUNDLE.getString("AUTHORIZE_KO"));
			}			 
		} else if (e.getActionCommand().equals(MenuRempes.CONNECTION_FREE)) {
			System.out.println("Demande connexion");

			connexion();

		} else if (e.getActionCommand().equals(MenuRempes.DECONNECTION)) {
			System.out.println("Demande deconnexion");
			//JSONObject result =  
			logout();
			setCurrentSession(null);
		}  
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	public String getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(String currentSession) {
		this.currentSession = currentSession;
	}

	public void connexion() {
		JSONObject sessionToken = login();

		if ( sessionToken!=null && checkPermission(sessionToken)) {
			MainControleRempes.getInstance().getStatusPanel().setStatusConnexion("Connexion accordée.");
			//MainControlePapa.showInfo("Connexion accordée.");
		} else {
			MainControleRempes.getInstance().getStatusPanel().setStatusConnexion("Echec de connexion.");
			guiControl.showError(MESSAGES_BUNDLE.getString("CONECTION_FAIL"));
		}
	}

}
