package com.acuity.rdso.sbe_user.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public final class Utils {
	
	/**
	 * Tika parser to remove html tags
	 * @param stream
	 * @return
	 * @throws TikaException
	 * @throws SAXException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public static String extractContentUsingParser(InputStream stream) throws TikaException, SAXException, FileNotFoundException, IOException {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        parser.parse(stream, handler, metadata, context);
        return handler.toString();
    }
	
    /**
     * Wget functionality to parse a url
     * @param url
     * @return
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
	@SuppressWarnings("deprecation")
	public static String wget(String url) throws MalformedURLException, FileNotFoundException, IOException{
	    URL u;
	    InputStream is = null;
	    DataInputStream dis;
	    String s;
	    StringBuilder strBuilder = new StringBuilder();
	    
	    try
	    {
	      u = new URL(url);
	      is = u.openStream();
	      dis = new DataInputStream(new BufferedInputStream(is));
	      
	      while ((s = dis.readLine()) != null)
	      {
	    	strBuilder.append(s);
	      }
	      
	    }

	    finally
	    {
	      try
	      {
	        is.close();
	      }
	      catch (IOException ioe)
	      {
	    	  ioe.printStackTrace();
	      }
	    }
	    return strBuilder.toString();
	}
	
	/**
	 * Util to remove new lines from input string
	 * @param scappedData
	 * @return
	 */
	public static String removeNewLine(String scappedData){
		String cleanedStr = "";
		cleanedStr = scappedData.replaceAll("\r", "").replaceAll("\n", "");
		return cleanedStr;
	}
	
	/**
	 * Util to concatenate string with spaces in between
	 * @param name
	 * @return
	 */
	public static String concatenateString(String name){
		String concatenatedString = "";
		concatenatedString = name.replaceAll(" ", "_");
		return concatenatedString;
	}
	
	/**
	 * Util to get a base url from complete URL
	 * @param completeUrl
	 * @return
	 * @throws MalformedURLException
	 */
	public static String getBaseUrl(String completeUrl) throws MalformedURLException{
		URL url = new URL(completeUrl);
		return(url.getProtocol()+"//"+url.getHost()+":"+url.getPort());
	}
	
	/**
	 * Return a rest template which trusts all certs
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static RestTemplate getRestTemplate() throws ClientProtocolException, IOException{
		CloseableHttpClient httpClient= HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		
		return restTemplate;
	}
	
	/**
	 * Return a rest template which trusts all certs
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static RestTemplate getRestTemplateAcceptingAll() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		
		return restTemplate;
	}

}
