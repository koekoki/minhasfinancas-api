package com.BunkaBytes.IFriends.service;

import java.util.List;

import com.BunkaBytes.IFriends.model.entity.Lancamento;
import com.BunkaBytes.IFriends.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	void deletar(Lancamento lancamento);
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	void validar(Lancamento lancamento);
}	