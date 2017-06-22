package org.isha.aditya.poc.model;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MediaUpload 
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
	private static String fieldName;
	private static String fileName;
	/*
	private static boolean isInMemory;
	private static long sizeInBytes;
	*/
	
	public static String getFileName()
	{
		return fileName;
	}
	
	public static String getFieldName()
	{
		return fieldName;
	}
	
	public static void mediaInsert(HttpServletRequest request, HttpServletResponse response)
	{
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
		               System.out.println("Uploaded Filename: " + filePath + fileName);
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
		      System.out.println("No file is uploaded");
	   	}
	}
}
