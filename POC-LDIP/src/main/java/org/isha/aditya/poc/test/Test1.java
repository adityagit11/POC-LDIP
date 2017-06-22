package org.isha.aditya.poc.test;

import org.isha.aditya.poc.model.YoutubeUpPOC;

public class Test1 
{

	public static void main(String[] args) 
	{
		YoutubeUpPOC.uploadVideo();
		System.out.println(YoutubeUpPOC.getUploadedVideoId());
	}

}
