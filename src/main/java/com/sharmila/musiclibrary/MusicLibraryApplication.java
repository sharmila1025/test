package com.sharmila.musiclibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sharmila.musiclibrary.utils.ConfigUtils;




@SpringBootApplication
public class MusicLibraryApplication {

	
	
	public static void main(String[] args) {
		System.out.println("-------");
		
		SpringApplication.run(MusicLibraryApplication.class, args);
		
	}
	
//	@Bean
//	public ConfigUtils ConfigUtils()
//	{
//		return new ConfigUtils();
//	}
}
