package com.springboot4rest.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot4rest.model.Usuario;
import com.springboot4rest.repository.UsuarioRepository;

@Service
public class AppUserDetailService implements UserDetailsService 
{
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException 
	{
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		Usuario usuario =  usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha incorretos"));
		return new User(email, usuario.getSenha(), GetAuthorities(usuario));
	}

	private Collection<? extends GrantedAuthority> GetAuthorities(Usuario usuario) 
	{
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissoes().forEach(x -> authorities.add(new SimpleGrantedAuthority(x.getDescricao().toUpperCase())) );
		return authorities;
	}

}
