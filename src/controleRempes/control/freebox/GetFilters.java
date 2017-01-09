package controleRempes.control.freebox;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetFilters {

	private static String URL_FILTERS =  "http://mafreebox.freebox.fr/api/v3/parental/filter/";
	private static String URL_PLANING = "/planning";

	public static JSONObject getfilters() throws FreeboxException {

		//GET /api/v3/parental/filter/
		JSONObject result = null;
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final HttpGet httpget = new HttpGet(URL_FILTERS);
		httpget.addHeader("content-type", "application/json");	
		httpget.addHeader("X-Fbx-App-Auth",LogFreebox.getInstance().getCurrentSession());

		try (final CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			final HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				String retSrc = EntityUtils.toString(entityReponse); 
				// get in JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (Exception e) {
			throw new FreeboxException("Error while executing http request",e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public static JSONObject getaFilter(int id) throws FreeboxException {
		//GET /api/v3/parental/filter/{id}
		JSONObject result = null;
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final String uri = URL_FILTERS + id;
		final HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("content-type", "application/json");	
		httpget.addHeader("X-Fbx-App-Auth",LogFreebox.getInstance().getCurrentSession());

		try (CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			final HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				final String retSrc = EntityUtils.toString(entityReponse); 
				// get in JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (Exception e) {
			throw new FreeboxException("Error while executing http request",e);
		} finally {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return result;
	}

	public static JSONObject getaPlaning(int id) throws FreeboxException {
		//GET /api/v3/parental/filter/{id}/planning
		JSONObject result = null;
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final String uri = URL_FILTERS + id + URL_PLANING;
		final HttpGet httpget = new HttpGet(uri);
		httpget.addHeader("content-type", "application/json");	
		httpget.addHeader("X-Fbx-App-Auth",LogFreebox.getInstance().getCurrentSession());

		try (CloseableHttpResponse response = httpclient.execute(httpget)){
			System.out.println(response.getStatusLine());
			HttpEntity entityReponse = response.getEntity();

			if (entityReponse != null) {
				final String retSrc = EntityUtils.toString(entityReponse); 
				// get in JSON structure
				result = new JSONObject(retSrc); //Convert String to JSON Object
				System.out.println(result.toString());
			}
		} catch (Exception e) {
			throw new FreeboxException("Error while executing http request",e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	static public JSONObject getCurrentFilter(final String hostName) throws FreeboxException {
		JSONObject result = null;

		final JSONObject filters = getfilters();
		if (filters.getBoolean("success")) {			
			final JSONArray arrayFilters = filters.getJSONArray("result");
			break_label : for(int i = 0; i < arrayFilters.length(); i++)
			{
				final JSONObject aFilter = arrayFilters.getJSONObject(i);			      
				final JSONArray hosts = aFilter.getJSONArray("hosts");
				for(int j = 0; j < hosts.length(); j++)
				{
					String host = hosts.get(j).toString();
					if(hostName.equals(host)) {
						result = aFilter;
						break break_label;
					}
				}
			}
		}

		return result;
	}



	//GET /api/v3/parental/filter/{id}/planning
}
