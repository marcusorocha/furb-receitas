package br.furb.filters;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import br.furb.utils.StringUtils;
import br.furb.utils.UserController;
import br.furb.utils.UserInfo;

import com.sun.jersey.core.util.Priority;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Provider
@Priority(2000)
public class AuthenticationFilter implements ContainerRequestFilter 
{
	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	
	@Override
	public ContainerRequest filter(ContainerRequest request) throws WebApplicationException
	{
		request.setSecurityContext(new Authorizer(getUserInfo(request)));		
			
	    return request;
	}
	
	private UserInfo getUserInfo(ContainerRequest request) 
	{
        String token = request.getHeaderValue(AUTHENTICATION_HEADER);
        
        if (StringUtils.IsNullOrEmpty(token))
        	return null;
               
        UserController uc = UserController.getInstance();
                        
        return uc.getUsuarioLogado(token);
 	}
}

class Authorizer implements SecurityContext 
{
    private UserInfo userInfo;
    private Principal principal;

    public Authorizer(final UserInfo user) 
    {
        this.userInfo = user;
        this.principal = new Principal() 
        {
            public String getName() 
            {
                return (user != null) ? userInfo.getUsuario().getNome() : "";
            }
        };
    }

    public Principal getUserPrincipal() 
    {
        return this.principal;
    }

    public boolean isUserInRole(String role)
    {
        return isSecure();
    }

    public boolean isSecure()
    {    	
    	return (userInfo != null);
    }

    public String getAuthenticationScheme() 
    {
        return SecurityContext.DIGEST_AUTH;
    }
}