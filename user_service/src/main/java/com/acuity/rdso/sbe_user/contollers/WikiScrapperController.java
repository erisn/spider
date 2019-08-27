package com.acuity.rdso.sbe_user.contollers;


import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import com.acuity.rdso.sbe_user.model.MovieModel;
import com.acuity.rdso.sbe_user.model.RoleModel;
import com.acuity.rdso.sbe_user.services.WikiScrapperService;
import com.acuity.rdso.sbe_user.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path = "/scrapeWiki")
public class WikiScrapperController {
	
	@Value("${elasticSearch.url}")
	private String elasticSearchUrl;
	
	private static final Logger Logger = LoggerFactory.getLogger(WikiScrapperController.class);

	@Autowired
	private WikiScrapperService wikiScrapperService;

	//@Autowired
	//RestTemplate restTemplate;

	/**
	 * End point for bulk upload of movies to S3 bucket
	 * 
	 * @return
	 * @throws TikaException
	 * @throws SAXException
	 */
	@RequestMapping(value = "/bulkmovies", method = RequestMethod.GET)
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public ResponseEntity<String> scrapeWikiMoviesBulk() throws TikaException, SAXException {
		wikiScrapperService.scrapeWikiMovieS3();
		return new ResponseEntity<>("Done scraping", HttpStatus.OK);
	}

	/**
	 * End point for bulk celebrities
	 * @return
	 * @throws TikaException
	 * @throws SAXException
	 */
	@RequestMapping(value = "/bulkcelebrities/{role}", method = RequestMethod.GET)
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public ResponseEntity<String> scrapeWikiCelebritiesBulk(@PathVariable("role") String role) {
		try {
			wikiScrapperService.scrapeWikiCelebritiesS3(role);
		} catch (TikaException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>("Done scraping", HttpStatus.OK);
	}

	/**
	 * End point for pushing movie to elastic search
	 * 
	 * @param url
	 * @param imdb
	 * @param movieName
	 */
	@RequestMapping(value = "/movie/{url}/{imdb}/{movieName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public ResponseEntity<MovieModel> scrapeWikiMovie(@PathVariable("url") String url,
	        @PathVariable("imdb") String imdb, @PathVariable("movieName") String movieName,
	        HttpServletRequest request) {
		MovieModel movieModel = null;
		
		try {
			
			RestTemplate restTemplate = Utils.getRestTemplateAcceptingAll();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
		    
			Logger.info("Url :"+url+" imdb:"+imdb+" movieName:"+movieName);
			
			movieModel = wikiScrapperService.scrapeWikiMovie(url, imdb, movieName);

			// Post to elastic search
			ObjectMapper Obj = new ObjectMapper(); 
			String jsonStr = Obj.writeValueAsString(movieModel);
			HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, headers);
			
			Logger.info(jsonStr);

			ResponseEntity<String> result  = restTemplate.exchange(elasticSearchUrl+"/movies/update/movie", HttpMethod.POST, httpEntity, String.class);
			//ResponseEntity<String> result  = restTemplate.exchange("http://localhost:9090/movies/update/movie", HttpMethod.POST, httpEntity, String.class);

		} catch (MalformedURLException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (SAXException s) {
			Logger.info(s.toString());
			s.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (TikaException t) {
			Logger.info(t.toString());
			t.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (FileNotFoundException f) {
			Logger.info(f.toString());
			f.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (KeyManagementException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (NoSuchAlgorithmException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		} catch (KeyStoreException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<MovieModel>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<MovieModel>(movieModel, HttpStatus.OK);
	}

	/**
	 * End point for pushing actor to elastic search
	 * 
	 * @param name
	 */
	@RequestMapping(value = "/actor/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public ResponseEntity<RoleModel> scrapeWikiActor(@PathVariable("name") String name, HttpServletRequest request) {
		RoleModel roleModel = null;
		try {
			
			roleModel = wikiScrapperService.scrapeWikiActor(name);
			
			// Post to elastic search
			RestTemplate restTemplate = Utils.getRestTemplateAcceptingAll();
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			
			ObjectMapper Obj = new ObjectMapper(); 
			String jsonStr = Obj.writeValueAsString(roleModel);
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, headers);
			ResponseEntity<String> result  = restTemplate.exchange(elasticSearchUrl+"/movies/index/celebrity/single", HttpMethod.POST, httpEntity, String.class);
			//ResponseEntity<String> result  = restTemplate.exchange("http://localhost:9090/movies/index/celebrity/single", HttpMethod.POST, httpEntity, String.class);		
			
		} catch (MalformedURLException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (SAXException s) {
			Logger.info(s.toString());
			s.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (TikaException t) {
			Logger.info(t.toString());
			t.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (FileNotFoundException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (KeyManagementException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (NoSuchAlgorithmException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (KeyStoreException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(roleModel, HttpStatus.OK);
	}

	/**
	 * End point for pushing director to elastic search
	 * 
	 * @param name
	 */
	@RequestMapping(value = "/director/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public ResponseEntity<RoleModel> scrapeWikiDirector(@PathVariable("name") String name, HttpServletRequest request) {
		RoleModel roleModel = null;
		try{
			roleModel = wikiScrapperService.scrapeWikiDirector(name);
			
			RestTemplate restTemplate = Utils.getRestTemplateAcceptingAll();
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			
			ObjectMapper Obj = new ObjectMapper(); 
			String jsonStr = Obj.writeValueAsString(roleModel);
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(jsonStr, headers);
			ResponseEntity<String> result  = restTemplate.exchange(elasticSearchUrl+"/movies/index/celebrity/single", HttpMethod.POST, httpEntity, String.class);
		} catch (MalformedURLException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (SAXException s) {
			Logger.info(s.toString());
			s.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (TikaException t) {
			Logger.info(t.toString());
			t.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (FileNotFoundException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (KeyManagementException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (NoSuchAlgorithmException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		} catch (KeyStoreException e) {
			Logger.info(e.toString());
			e.printStackTrace();
			return new ResponseEntity<RoleModel>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(roleModel, HttpStatus.OK);
	}
}
