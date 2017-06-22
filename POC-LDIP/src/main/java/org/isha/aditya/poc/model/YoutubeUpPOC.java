package org.isha.aditya.poc.model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * This is the best way to upload video on youtube*/
public class YoutubeUpPOC 
{
	private static YouTube youtube;
    private static final String VIDEO_FILE_FORMAT = "video/*";
    private static List<String> scopes;
    
    private static String SAMPLE_VIDEO_FILENAME = "panther.mp4";
    
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
    
    public static void uploadVideo()
    {
    	scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
        try 
        {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "uploadvideo");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-cmdline-uploadvideo-sample").build();

            System.out.println("Uploading: " + SAMPLE_VIDEO_FILENAME);

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
            InputStream in = new FileInputStream("E:\\JavaTech\\POC-data\\Data\\panther.mp4");
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
