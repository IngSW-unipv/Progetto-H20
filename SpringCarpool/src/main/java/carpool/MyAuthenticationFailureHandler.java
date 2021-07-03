package carpool;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//Viene chiamata quando il login ha dati errati
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
    public @ResponseBody void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println("C'è stato un login errato!");
    }

}
