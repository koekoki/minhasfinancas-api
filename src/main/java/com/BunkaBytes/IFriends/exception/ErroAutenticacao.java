package com.BunkaBytes.IFriends.exception;

public class ErroAutenticacao extends RuntimeException{
	
	public ErroAutenticacao (String mensagem) {
		super(mensagem);
	}
}
