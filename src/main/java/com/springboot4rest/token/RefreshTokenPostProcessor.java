package com.springboot4rest.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.springboot4rest.property.PropertiesConfig;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>
{
	@Autowired
	private PropertiesConfig propertiesConfig;
	
	//Somente será true no caso de estar devolvendo um token criado
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}
	//Método executado somente se o método supports for true
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) 
	{
		
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest(); //Conversão de ServerHttpRequest para HttpServletRequest de resposta do servidor para resposta http
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse(); //Conversão de ServerHttpResponse para HttpServletResponse de resposta do servidor para resposta http
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body; //Conversão do corpo da resposta para a classe do OAuth2 para ter acesso ao método setRefreshToken
		
		String refresh_token = body.getRefreshToken().getValue(); //valor para setar no Cookie
		adicionarRefreshTokenNoCookie(refresh_token, servletRequest, servletResponse); //Adicionar token no Cookie protegido por https para que não seja acessado por javascript
		removerRefreshTokenDoBody(token); //tira o refresh_token da resposta do servidor
		
		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) 
	{
		token.setRefreshToken(null);
	}

	private void adicionarRefreshTokenNoCookie(String refresh_token, HttpServletRequest req, HttpServletResponse resp) 
	{
		Cookie refreshTokenCookie = new Cookie("refreshToken", refresh_token);
		refreshTokenCookie.setHttpOnly(true); //somente acessivel em protocolo http
		refreshTokenCookie.setSecure(propertiesConfig.getCfg().isEnableHttps()); //funciona apenas em https (mudar para produção https)
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token"); //o caminho do cookie será path root + oauth/token ex: localhost:8080/oauth/token
		refreshTokenCookie.setMaxAge(2592000);
		resp.addCookie(refreshTokenCookie);
	}

}
