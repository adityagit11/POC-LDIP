package org.isha.aditya.poc.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

public class PicModel 
{
	/*
	 * PAGE ACCESS TOKEN:
	 * These access tokens are similar 
	 * to user access tokens, except that 
	 * they provide permission to APIs 
	 * that read, write or modify the data 
	 * belonging to a Facebook Page. 
	 * To obtain a page access token you 
	 * need to start by obtaining a user access 
	 * token and asking for the manage_pages 
	 * permission. Once you have the user access 
	 * token you then get the page access token via the Graph API.*/
	
	public static final String ACCESS_TOKEN = "EAACEdEose0cBAAFvChte7RohVGqSiC5XkaPt2MKiTi3qcSipUBwIGA6ULNUpPQy6jmMJEoxdXKLERr7opN76NRYVC4Nvzr1UBeNZCWTII5o4xBtdrJi2cFX4BKe4B8hlNSbIaFu4Y9NvKoexyPsleW5mK5Q9y7Qb9Exeojdtt0ZAZArOzihsLn3OvpbqerbAMb6jFIeFwZDZD";
	public static final String APP_ID = "";
	public static final String APP_SECTRET = "";
	public static final String PAGE_ID = "888792104477727";
	
	public static void main(String args[]) throws FileNotFoundException
	{
		/*
		//For post
		FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN);
		
		User user = facebookClient.fetchObject("me", User.class);
		
		Page page = facebookClient.fetchObject(PAGE_ID, Page.class);
		
		int counter = 0;
		facebookClient.publish(PAGE_ID + "/feed", FacebookType.class, Parameter.with("message", Integer.toString(counter) + ": Hello, facebook World!"));
		*/
		
		/*
		//FOR PHOTO
		InputStream in = new FileInputStream(new File("E:\\JavaTech\\POC-data\\Data\\front.png"));
		
		FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN);
		
		FacebookType publishVideoResponse =facebookClient.publish("me/photos",FacebookType.class,
	            BinaryAttachment.with("myphoto.jpg", in),
	            Parameter.with("message", "MY PHOTO POST"));
		*/
		
		/*
		//FOR VIDEO
		InputStream in = new FileInputStream(new File("E:\\JavaTech\\POC-data\\Data\\song.webm"));
		
		FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN);
		
		FacebookType publishVideoResponse = facebookClient.publish("me/videos", FacebookType.class, BinaryAttachment.with("myvideo.webm", in), Parameter.with("message", "MY VIDEO POST"));
		*/
		
		//PLAY HERE
		FacebookClient fbc = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
		
		/*
		Post post = fbc.fetchObject("1603993212957609",
				Post.class,
				Parameter.with("fields", "from,to,likes.limit(0).summary(true)"));
		
		*/
		Post.Likes likes = fbc.fetchObject("1603993212957609" + "/likes",
				Post.Likes.class,
				Parameter.with("summary", 1),
				Parameter.with("limit", 0));
		
		System.out.println(likes.getTotalCount());
	}
}
