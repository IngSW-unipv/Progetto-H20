package carpool.configs;

import java.nio.file.Path;
import java.nio.file.Paths;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Tutta questa classe esiste perchè altrimenti l'applicazione NON vede la cartella user-photos come parte dell'applicazione
//Va ovviamente specificata, non è che il webserver si va a pescare cartelle a caso in giro per il sistema
@Configuration
public class WebResourceConfig implements WebMvcConfigurer {
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("user-photos", registry);
    }
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
         
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
         
        //Fondamentale che ci sia "file://" con 2 slash! Il tutorial diceva solo "file:/" ma su Ubuntu non va bene! 
        //Da vedere cosa succede su Windows, magari lì ci vuole "file:/"
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }
	
}
