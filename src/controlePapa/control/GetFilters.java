package controlePapa.control;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import controlePapa.MainControlePapa;

public class GetFilters {

	private static String URL_FILTERS =  "http://mafreebox.freebox.fr/api/v3/parental/filter/";
	private static String URL_PLANING = "/planning";

	public static JSONObject getfilters() {

		//GET /api/v3/parental/filter/
		JSONObject result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String uri = URL_FILTERS;
		HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("content-type", "application/json");	
		httpget.addHeader("X-Fbx-App-Auth",LogFreebox.getInstance().getCurrentSession());

		try (CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				String retSrc = EntityUtils.toString(entityReponse); 
				// get in JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (IOException e) {
			MainControlePapa.showError(e.getMessage()); 
			e.printStackTrace();
		} catch (Exception e1) {
			MainControlePapa.showError(e1.getMessage()); 
			e1.printStackTrace();
		}
		return result;
	}

	
	public static JSONObject getaFilter(int id) {
		//GET /api/v3/parental/filter/{id}
		JSONObject result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String uri = URL_FILTERS + id;
		HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("content-type", "application/json");	
		httpget.addHeader("X-Fbx-App-Auth",LogFreebox.getInstance().getCurrentSession());

		try (CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				String retSrc = EntityUtils.toString(entityReponse); 
				// get in JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (IOException e) {
			MainControlePapa.showError(e.getMessage()); 
			e.printStackTrace();
		} catch (Exception e1) {
			MainControlePapa.showError(e1.getMessage()); 
			e1.printStackTrace();
		}
		return result;
	}

	public static JSONObject getaPlaning(int id) {
		//GET /api/v3/parental/filter/{id}
		JSONObject result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String uri = URL_FILTERS + id + URL_PLANING;
		HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("content-type", "application/json");	
		httpget.addHeader("X-Fbx-App-Auth",LogFreebox.getInstance().getCurrentSession());

		try (CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				String retSrc = EntityUtils.toString(entityReponse); 
				// get in JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (IOException e) {
			MainControlePapa.showError(e.getMessage()); 
			e.printStackTrace();
		} catch (Exception e1) {
			MainControlePapa.showError(e1.getMessage()); 
			e1.printStackTrace();
		}
		return result;
	}
	
	static public JSONObject getCurrentFilter(final String hostName) {
		JSONObject result = null;
				
		JSONObject filters = getfilters();
		if (filters.getBoolean("success")) {			
			JSONArray arrayFilters = filters.getJSONArray("result");
			for(int i = 0; i < arrayFilters.length(); i++)
			{
			      JSONObject aFilter = arrayFilters.getJSONObject(i);
			      
			      JSONArray hosts = aFilter.getJSONArray("hosts");
					for(int j = 0; j < hosts.length(); j++)
					{
					      String host = hosts.get(j).toString();
					      if(hostName.equals(host)) {
					    	  return aFilter;
					      }
					}

			}
		}
		
		return result;
	}
	
	//GET /api/v3/parental/filter/{id}/planning
}
