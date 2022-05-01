package com.BunkaBytes.IFriends.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BunkaBytes.IFriends.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
