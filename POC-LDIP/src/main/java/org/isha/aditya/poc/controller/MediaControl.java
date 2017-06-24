package org.isha.aditya.poc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Page;
import com.restfb.types.User;

class DataInsert
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

class Authentication
{
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";
    private static final String CLIENT_SECRETS_JSON = "E:\\JavaTech\\Edelweiss\\POC-LDIP\\src\\main\\resources\\client_secrets.json";
    
    public static Credential authorize(List<String> scopes, String credentialDatastore) throws IOException 
    {

        // Load client secrets.
    	InputStream in = new FileInputStream(CLIENT_SECRETS_JSON);
        Reader clientSecretReader = new InputStreamReader(in);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

        // Checks that the defaults have been replaced (Default = "Enter X here").
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) 
        {
            System.out.println(
                    "Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential "
                            + "into src/main/resources/client_secrets.json");
            System.exit(1);
        }

        // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
                .build();

        // Build the local server and bind it to port 8080
        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

        // Authorize.
        return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
    }
}

class DataPushY
{
	private static YouTube youtube;
    private static final String VIDEO_FILE_FORMAT = "video/*";
    private static List<String> scopes;
    
    /*
     * Uploaded video members*/
    private static String uploadedVideoId;
    private static String uploadedVideoTitle;
    
    public static String getUploadedVideoId()
    {
    	return uploadedVideoId;
    }
    
    public static String getuploadedVideoTitle()
    {
    	return uploadedVideoTitle;
    }
    
    public static void uploadVideo(String FILE_PATH, String FILE_NAME)
    {
    	scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
        try 
        {
            // Authorize the request.
            Credential credential = Authentication.authorize(scopes, "uploadvideo");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Authentication.HTTP_TRANSPORT, Authentication.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-uploadvideo-sample").build();

            System.out.println("Uploading: " + FILE_NAME);

            Video videoObjectDefiningMetadata = new Video();
            		
            					VideoStatus status = new VideoStatus();
            					status.setPrivacyStatus("public");
            
            videoObjectDefiningMetadata.setStatus(status);
            
            					VideoSnippet snippet = new VideoSnippet();
            					
            										Calendar cal = Calendar.getInstance();
            					
            					snippet.setTitle("LDIP Demonstration " + cal.getTime());
            					snippet.setDescription("Video uploaded by Aditya Singh " + cal.getTime());
            					
										            List<String> tags = new ArrayList<String>();
										            tags.add("Project: LDIP POC");
										            tags.add("Intern: Aditya Singh");
										            tags.add("Mentor: Sunil Gupta");
										            tags.add("YouTube Data API V3");
								snippet.setTags(tags);
								
            videoObjectDefiningMetadata.setSnippet(snippet);
            
            //The video to upload get's here
            InputStream in = new FileInputStream(FILE_PATH + FILE_NAME);
            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, in);
            YouTube.Videos.Insert videoInsert = youtube.videos().insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
            uploader.setDirectUploadEnabled(false);
            
            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress");
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload Completed!");
                            break;
                        case NOT_STARTED:
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);

            // Call the API and upload the video.
            Video returnedVideo = videoInsert.execute();
            
            /*
             * These two methods getId() and getTitle()
             * called on Video object returns the ID and TITLE of the
             * video uploaded by the user.*/
            uploadedVideoId = returnedVideo.getId();
            uploadedVideoTitle = returnedVideo.getSnippet().getTitle();

        } 
        catch (GoogleJsonResponseException e) 
        {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } 
        catch (Throwable t) 
        {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
    }
}

/*
 * The controller class for 
 * LDIP. It works in 3 stages
 * 1. User upload the video from drive to Server
 * 2. Server pushes the file from its drive to youtube channel*/
public class MediaControl extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static String fileName;
	private static String filePath;
	
	public MediaControl() 
	{
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//We don't require the method right now
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
		 * The following method uploadVideo() on 
		 * Class DataPushY uploads the video on the 
		 * Youtube's channel using the clinet_secrets.json file.
		 * */
		DataPushY.uploadVideo(filePath, fileName);
		String videoId = DataPushY.getUploadedVideoId();
		
		/*
		 * The last step is to obtain the video ID
		 * and push it to the result.jsp
		 * */
		request.setAttribute("videoid", videoId);
		RequestDispatcher view = request.getRequestDispatcher("result.jsp");
		view.forward(request, response);
	}
}
