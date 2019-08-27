package com.acuity.rdso.sbe_user.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import com.acuity.rdso.sbe_user.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acuity.rdso.sbe_user.model.MovieModel;
import com.acuity.rdso.sbe_user.model.RoleModel;
import com.acuity.rdso.sbe_user.services.AmazonS3Service;

@Service
public class WikiScrapperService {
	
	private static final Logger Logger = LoggerFactory.getLogger(WikiScrapperService.class);

	@Value("${wikiscrapper.path.file.movies}")
	private String filePathMovies;
	
	@Value("${wikiscrapper.path.file.celebrities}")
	private String filePathCelebrities;
	
	
	@Value("${s3.movies.folder}")
	private String s3MoviesFolder;
	
	@Value("${s3.celebrities.folder}")
	private String s3CelebritiesFolder;
	
	@Value("${s3.movies.bucket}")
	private String s3MoviesBucket;
	
	@Value("${s3.celebrities.bucket}")
	private String s3CelebritiesBucket;

	@Autowired
	private AmazonS3Service amazonS3Service;

	private static String baseUrl = "https://en.wikipedia.org/wiki/";

	/**
	 * Method to update the s3 bucket with all movies
	 * 
	 * @throws TikaException
	 * @throws SAXException
	 */
	public void scrapeWikiMovieS3() throws TikaException, SAXException {

		List<String> urls;
		String[] splitLine = null;
		
		ObjectMapper Obj = new ObjectMapper(); 
		String jsonStr;
		
		try {
			urls = parseWikiMovieUrls();

			for (String temp : urls) {
				splitLine = temp.split("\\s+");
				MovieModel movieModel = new MovieModel();
				try{
					movieModel.setBody(Utils.extractContentUsingParser(new ByteArrayInputStream(Utils.wget(splitLine[0]).getBytes())));
				}catch (Exception e){
					continue;
				}
				//movieModel.setBody(Utils.extractContentUsingParser(new ByteArrayInputStream(Utils.wget(splitLine[0]).getBytes())));
				movieModel.setDocumentName(splitLine[1]);
				movieModel.setSearchFieldValue(splitLine[1]);
				movieModel.setSource("wiki");
				movieModel.setSourceUrl(splitLine[0]);
				movieModel.setCreationDt(LocalDateTime.now().toString());
				
				jsonStr = Obj.writeValueAsString(movieModel);
				amazonS3Service.store(jsonStr,s3MoviesBucket, s3MoviesFolder+"/"+splitLine[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method to update the s3 bucket with all movies
	 * 
	 * @throws TikaException
	 * @throws SAXException
	 */
	public void scrapeWikiCelebritiesS3(String role) throws TikaException, SAXException {

		List<String> celebrityNames;
		ObjectMapper Obj = new ObjectMapper(); 
		String jsonStr;
		try {
			celebrityNames = parseWikiCelebrityNames();

			for (String name : celebrityNames) {
				RoleModel roleModel = new RoleModel();
				try{
					roleModel.setBody(Utils.extractContentUsingParser(new ByteArrayInputStream(Utils.wget(baseUrl + Utils.concatenateString(name)).getBytes())));
				}catch (Exception e){
					//do nothing
					continue;
				}
				//roleModel.setBody(Utils.extractContentUsingParser(new ByteArrayInputStream(Utils.wget(baseUrl + Utils.concatenateString(name)).getBytes())));
				roleModel.setDocumentName(name);
				roleModel.setSource("wiki");
				roleModel.setSourceUrl(baseUrl + Utils.concatenateString(name));
				roleModel.setCreationDate(LocalDateTime.now().toString());
				roleModel.setRole(role);
				
				jsonStr = Obj.writeValueAsString(roleModel);
				
				amazonS3Service.store(jsonStr, s3CelebritiesBucket, s3CelebritiesFolder+"/"+Utils.concatenateString(name));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	/**
	 * Method to scrape movie from wiki
	 * 
	 * @param url
	 * @param imdb
	 * @param movieName
	 * @return
	 * @throws TikaException
	 * @throws SAXException
	 */
	public MovieModel scrapeWikiMovie(String url, String imdb, String movieName)
	        throws TikaException, SAXException, FileNotFoundException, IOException {
		
		MovieModel movieModel = new MovieModel();

		Logger.info("The URL being tested is :"+baseUrl + url);
		movieModel.setBody(
		        Utils.extractContentUsingParser(new ByteArrayInputStream(Utils.wget(baseUrl + url).getBytes())));
		movieModel.setSourceUrl(baseUrl + url);
		movieModel.setCreationDt(LocalDateTime.now().toString());
		movieModel.setSearchFieldValue(imdb);
		movieModel.setDocumentName(movieName);
		movieModel.setSource("wiki");

		return movieModel;

	}

	/**
	 * Method to scrape actor from wiki
	 * 
	 * @param name
	 * @return
	 * @throws SAXException
	 * @throws TikaException
	 */
	public RoleModel scrapeWikiActor(String name)
	        throws TikaException, SAXException, FileNotFoundException, IOException {
		
		RoleModel roleModel = new RoleModel();

		Logger.info("The URL being tested is :"+ baseUrl + Utils.concatenateString(name));
		roleModel.setBody(Utils.extractContentUsingParser(
		        new ByteArrayInputStream(Utils.wget(baseUrl + Utils.concatenateString(name)).getBytes())));
		roleModel.setDocumentName(name);
		roleModel.setSourceUrl(baseUrl + Utils.concatenateString(name));
		roleModel.setRole("Actor");
		roleModel.setSource("wiki");
		roleModel.setCreationDate(LocalDateTime.now().toString());

		return roleModel;
	}

	/**
	 * Method to scrape director from wiki
	 * 
	 * @param name
	 * @return
	 */
	public RoleModel scrapeWikiDirector(String name)
	        throws TikaException, SAXException, FileNotFoundException, IOException {
		
		RoleModel roleModel = new RoleModel();

		Logger.info("The URL being tested is :"+ baseUrl + Utils.concatenateString(name));
		
		roleModel.setBody(Utils.extractContentUsingParser(
		        new ByteArrayInputStream(Utils.wget(baseUrl + Utils.concatenateString(name)).getBytes())));
		roleModel.setDocumentName(name);
		roleModel.setSourceUrl(baseUrl + Utils.concatenateString(name));
		roleModel.setRole("Director");
		roleModel.setSource("wiki");
		roleModel.setCreationDate(LocalDateTime.now().toString());

		return roleModel;
	}

	/**
	 * Method to parse wiki urls from a text file
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> parseWikiMovieUrls() throws IOException {
		BufferedReader reader;
		List<String> list = new ArrayList<>();
		File file = new File(filePathMovies);
		reader = new BufferedReader(new FileReader(file));
		list = reader.lines().collect(Collectors.toList());
		reader.close();

		return list;
	}
	
	/**
	 * Method to parse wiki urls from a text file
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> parseWikiCelebrityNames() throws IOException {
		BufferedReader reader;
		List<String> list = new ArrayList<>();
		File file = new File(filePathCelebrities);
		reader = new BufferedReader(new FileReader(file));
		list = reader.lines().collect(Collectors.toList());
		reader.close();

		return list;
	}

}
