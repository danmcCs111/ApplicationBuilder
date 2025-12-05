package HttpDatabaseRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublisher;

public interface HttpDatabaseRequest 
{
	public static String executeGetRequest(
			String endpoint, 
			int portNumber, 
			String requestData, 
			String requestTypeHeaderName, 
			String requestTypeHeaderValue)
	{
		BodyPublisher bp = HttpRequest.BodyPublishers.ofString(requestData);
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(endpoint + portNumber))
				.header(requestTypeHeaderName, requestTypeHeaderValue)
				.method("GET", bp)
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return (response==null)
			? null
			: response.body();
	}
	
	public static String executeGetRequest(String url)
	{
		HttpResponse<String> response = null;
		String comb = null;
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
        	response = client.send(request, BodyHandlers.ofString());
        	comb = response.body().toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return comb;
	}
	
	public static String addHttpsIfMissing(String url)
	{
		if(!url.startsWith("https://wwww.") && !url.startsWith("http://wwww."))
		{
			return "https://www." + url.strip();
		}
		return url;
	}
}
