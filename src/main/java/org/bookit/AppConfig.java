package org.bookit;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan("org.bookit")
@ImportResource("classpath:jersey-spring-applicationContext.xml")
public class AppConfig {

	@Bean
	public Directory directory() throws IOException{
		File home = new File(System.getProperty("user.home"));
		File index = new File(home,".bookit");
		if (!index.exists())
			index.mkdir();
		
		return new SimpleFSDirectory(index);
	}
}
