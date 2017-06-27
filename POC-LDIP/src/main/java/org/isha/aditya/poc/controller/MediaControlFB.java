package org.isha.aditya.poc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Album;
import com.restfb.types.FacebookType;
import com.restfb.types.GraphResponse;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.ResumableUploadStartResponse;
import com.restfb.types.ResumableUploadTransferResponse;
import com.restfb.types.User;

class DataInsert2
{
	private static File file;
	private static int maxFileSize;
	private static int maxMemSize;
	private static String filePath;
	private static String contentType;
	
	//Apache FileUpload Members
	private static DiskFileItemFactory factory;
	private static ServletFileUpload upload;
	
	//File uploaded details
	private static String fileName;
	/*
	private static boolean isInMemory;
	private static long sizeInBytes;
	*/
	
	public static String getFilePath()
	{
		return filePath;
	}

	public static String getFileName()
	{
		return fileName;
	}
	
	public static void run(HttpServletRequest request, HttpServletResponse response)
	{
		/*
		 * Setting the limit of max 
		 * upload size is 50MB*/
		maxFileSize = 50000 * 1024;
		maxMemSize = 50000 * 1024;
		
		filePath = "E:\\JavaTech\\POC-data\\Data\\";
		
		contentType = request.getContentType();
		
		if ((contentType.indexOf("multipart/form-data") >= 0)) 
		{
			factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			factory.setRepository(new File("E:\\JavaTech\\POC-data\\Temp\\"));
			upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);
			try 
			{ 
				// Parse the request to get file items.
		        List<FileItem> fileItems = upload.parseRequest(request);
		        // Process the uploaded file items
		        Iterator<FileItem> i = fileItems.iterator();
		        System.out.println("File is being Uploaded......");
		        while ( i.hasNext () ) 
		        {
		        	FileItem fi = (FileItem)i.next();
		        	if ( !fi.isFormField () ) 
		        	{
		               // Get the uploaded file parameters
		               fileName = fi.getName();
		               /*
		               fieldName = fi.getFieldName();
		               isInMemory = fi.isInMemory();
		               sizeInBytes = fi.getSize();
		               */
		               
		               // Write the file
		               if( fileName.lastIndexOf("\\") >= 0 ) 
		               {
		                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
		               } 
		               else 
		               {
		                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
		               }
		               fi.write( file ) ;
		               System.out.println("DONE! Uploading Filename: " + filePath + fileName + " to the server drive");
		            }
		        }
		      } 
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		} 
		else 
		{
		      System.out.println("No file is uploaded to server drive");
	   	}
	}
}

class DataPushF
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
	
	private static final String ACCESS_TOKEN = "EAACEdEose0cBAJL1OOA6jvs5DErFh3dvuLtk2i7z6i02pOc3c3Mj6Lc6vAwNFRLRGmd3xXQq9SSqFZBLGZByGpSKc4OEXsZCo8ydBbsbi6BZBcojvmtBaHLZAr5tVNVZB90ZAoHjUYtEozx7m9pJPnLWJXWdLDEoFJxxgjoBEXUS5n64oAIXNqfVpvXEGANLPmNiX9p9bORZBQZDZD";
	private static final String APP_ID = "";
	private static final String APP_SECTRET = "";
	private static final String PAGE_ID = "888792104477727";
	
	/*
	 * This below method
	 * pushes the text onto facebook
	 * */
	public static void pushText(String message)
	{
		String TEXT_POST = message;
		FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
		facebookClient.publish(PAGE_ID + "/feed", FacebookType.class, Parameter.with("message", TEXT_POST));
		System.out.println("SUCCESS! - TEXT POSTED");
	}
	
	public static String pushPicture(String FILE_PATH, String FILE_NAME)
	{
		try
		{
			InputStream in = new FileInputStream(new File(FILE_PATH + FILE_NAME));
			
			FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
			
			FacebookType publishPictureResponse =facebookClient.publish("me/photos",FacebookType.class,
		            BinaryAttachment.with("myphoto.jpg", in),
		            Parameter.with("message", "MY PHOTO POST"));
			
			System.out.println("SUCCESS! - PICTURE POSTED");

			return publishPictureResponse.getId();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "-1";
		}
		
	}
	
	public static String createNewAlbum(String ALBUM_NAME, String ALBUM_DESC)
	{
		FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
		
		FacebookType album = facebookClient.publish("me/albums",
				FacebookType.class,
				Parameter.with("name", ALBUM_NAME),
				Parameter.with("message", ALBUM_DESC));
		
		return album.getId();
	}
	
	public static String pushPictureToAlbum(String ALBUM_ID, String FILE_PATH, String FILE_NAME)
	{
		try
		{
			FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
			
			InputStream in = new FileInputStream(new File(FILE_PATH + FILE_NAME));
			
			FacebookType uploadPicToAlbum = facebookClient.publish(ALBUM_ID + "/photos",
					FacebookType.class,
					BinaryAttachment.with(FILE_NAME, in),
					Parameter.with("message", "MY PHOTO INTO ALBUM"));
			System.out.println("SUCCESS! - PICTURE POSTED IN ALBUM: " + ALBUM_ID);
			
			return uploadPicToAlbum.getId();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "-1";
		}
	}
	
	public static String pushSmallVideo(String FILE_PATH, String FILE_NAME)
	{
		try
		{
			InputStream in = new FileInputStream(new File(FILE_PATH + FILE_NAME));
			
			FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
			
			FacebookType publishVideoResponse = facebookClient.publish("me/videos",
					FacebookType.class, 
					BinaryAttachment.with("myvideo.webm", in), 
					Parameter.with("message", "MY VIDEO POST"));
			
			System.out.println("SUCCESS! - VIDEO POSTED");
			
			return publishVideoResponse.getId();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "-1";
		}
	}
	
	/*
	 * A new feature facebook added with
	 * Graph API 2.4 is resumable upload support.
	 * Large videos (up to 2GB) shoudl be
	 * uploaded with this new approach
	 * */
	public static String pushLargeVideo(String FILE_PATH, String FILE_NAME)
	{
		try
		{
			//THE START PHASE

			File videoFile = new File(FILE_PATH + FILE_NAME);
			InputStream in = new FileInputStream(videoFile);
			long fileSizeInByes = videoFile.length();
			
			//we need the file size in bytes to make the start request
			FacebookClient fbc = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
			ResumableUploadStartResponse returnValue = fbc.publish("me/videos",
					ResumableUploadStartResponse.class, //return value
					Parameter.with("upload_phase", "start"), //uploading phase
					Parameter.with("file_size", fileSizeInByes)); //The file size
			
			long startOffset = returnValue.getStartOffset();
			long endOffset = returnValue.getEndOffset();
			long length = endOffset - startOffset;
			
			//The upload session ID is very important.
			String uploadSessionID = returnValue.getUploadSessionId();
			
			//THE TRANSFER PHASE
			
			while(length>0)
			{
				byte fileBytes[] = new byte[(int)length];
				in.read(fileBytes);
				
				ResumableUploadTransferResponse filePart = fbc.publish("me/videos",
						ResumableUploadTransferResponse.class,
						BinaryAttachment.with("video_file_chunk", fileBytes),
						Parameter.with("upload_phase", "transfer"),
						Parameter.with("start_offset", startOffset),
						Parameter.with("upload_session_id", uploadSessionID));
				
				startOffset = filePart.getStartOffset();
				endOffset = filePart.getEndOffset();
				length = endOffset - startOffset;
			}
			
			GraphResponse finishResponse = fbc.publish("me/videos",
					GraphResponse.class,
					Parameter.with("upload_phase", "finish"),
					Parameter.with("upload_session_id", uploadSessionID));
			
			System.out.println("SUCCESS - VIDEO UPLOADED!");
			return finishResponse.getId();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "-1";
		}
	}
	
	public static long getLikeCount(String POST_ID)
	{
		FacebookClient fbc = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
		
		Post.Likes likes = fbc.fetchObject(POST_ID + "/likes",
				Post.Likes.class,
				Parameter.with("summary", 1),
				Parameter.with("limit", 0));
		
		return likes.getTotalCount();
	}
	
	public static long getShareCount(String POST_ID)
	{
		FacebookClient fbc = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
		
		Post.Shares shares = fbc.fetchObject(POST_ID + "/shares",
				Post.Shares.class,
				Parameter.with("summary", 1),
				Parameter.with("limit", 0));
		
		return shares.getCount();
	}
	
	public static long getCommentCount(String POST_ID)
	{
		FacebookClient fbc = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
		
		Post.Comments comments = fbc.fetchObject(POST_ID + "/comments",
				Post.Comments.class,
				Parameter.with("summary", 1),
				Parameter.with("limit", 0));
		
		return comments.getTotalCount();
	}
}

public class MediaControlFB extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String fileName;
    private static String filePath;
	
    public MediaControlFB() 
    {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("Media transfer protocol activated..");
		
		/*
		 * First step of LDIP which is Video Insertion
		 * The below statis method run() on Class DataInsert
		 * copies the file from client's drive to the 
		 * server's drive*/
		DataInsert.run(request, response);
		
		/*
		 * After copying the file we get two 
		 * Strings regarding the server's system
		 * 1. FILE_NAME
		 * 2. FILE_PATH
		 * 
		 * and we forward this to the DataPushY class
		 * for later operations.
		 * */
		fileName = DataInsert.getFileName();
		filePath = DataInsert.getFilePath();

		/*
		 * To upload a new content
		 * using the page-admin credentials
		 * the vendor has three options
		 * 
		 * 1. Push text-message to facebook page
		 * 2. Push picture to facebook page
		 * 3. Push small video (~ 100 MB) to facebook page
		 * 4. Push large video (~ 2 GB) to facebook page
		 * 5. Create a new album on facebook page
		 * 6. Push successive picture to above album on facebook page
		 * 7. Get the like, share, comment count back from the above Picture
		 * */
		
		DataPushF.pushText("TEST 1");
		//String POST_ID = DataPushF.pushPicture(filePath, fileName);
		//DataPushF.pushSmallVideo(filePath, fileName);
		//DataPushF.pushLargeVideo(filePath, fileName);
		
		//long likeCount = DataPushF.getLikeCount(POST_ID);
		//System.out.println(likeCount);
		
		
		//long commentCount = DataPushF.getCommentCount(POST_ID);
		//System.out.println(commentCount);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		//FAIL SAFE WAY
		if(POST_ID.equals("-1"))
			System.out.println("Failed!");
		else
		{
			String postId = POST_ID;
			long likeCount = DataPushF.getLikeCount(postId);
			long shareCount = DataPushF.getShareCount(postId);
			long commentCount = DataPushF.getCommentCount(postId);
			
			System.out.println(likeCount+" "+shareCount+" "+commentCount);
		}
		*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		 * To create a new album you 
		 * Vendor has to first create a new
		 * empty albumn. The album will be created on day-basis
		 * So every day a new album will be created.
		 * To create a new album:
		 * */
		//String ALBUM_ID = DataPushF.createNewAlbum("THIS IS ALBUM NAME", "THIS IS ALBUM DESCRIPTION");
		
		/*
		 * Now once the vendor creates a new album
		 * you can push multiple images to this albumn
		 * */
		//DataPushF.pushPictureToAlbum(ALBUM_ID, filePath, fileName);
	}
}
