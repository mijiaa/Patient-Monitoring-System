package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * This class communicate with FHIR server to get JSON data with different api URL
 */
public class ServerModule {
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	private JSONObject readJsonFromUrl(String url) throws JSONException {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = null;

			try {
				jsonText = readAll(rd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private JSONObject readJsonFromLink(String url) throws JSONException {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = null;
			try {
				jsonText = readAll(rd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			JSONObject json = new JSONObject(jsonText);
			return json;

		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONObject getJSONObjectWQuery(String query) throws JSONException {
		String rootURL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
		String format = "_format=json";
		String URL = rootURL + query + format;
		return readJsonFromUrl(URL);
	}

	public JSONObject getJSONObjectWUrl(String url) throws JSONException {
		return readJsonFromLink(url);
	}
}