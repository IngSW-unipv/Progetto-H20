package carpool.configs;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Configuration
public class WebConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.addErrorPages(
                new ErrorPage(HttpStatus.FORBIDDEN, "/403"),
                new ErrorPage(HttpStatus.NOT_FOUND, "/404"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
    }
    
    @GetMapping("/403")
    public String forbidden(Model model) {
        return "error/403";
        }

    @GetMapping("/404")
    public String notFound(Model model) {
        return "error/404";
        }

    @GetMapping("/500")
    public String internal(Model model) {
        return "error/500";
        }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
        }
}
