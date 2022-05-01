package com.BunkaBytes.IFriends.service;

import com.BunkaBytes.IFriends.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
}
