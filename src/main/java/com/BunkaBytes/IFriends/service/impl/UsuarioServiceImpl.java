package com.BunkaBytes.IFriends.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.BunkaBytes.IFriends.exception.ErroAutenticacao;
import com.BunkaBytes.IFriends.exception.RegraNegocioException;
import com.BunkaBytes.IFriends.model.entity.Usuario;
import com.BunkaBytes.IFriends.model.repository.UsuarioRepository;
import com.BunkaBytes.IFriends.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	
	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario= repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não existe.");
		}
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
