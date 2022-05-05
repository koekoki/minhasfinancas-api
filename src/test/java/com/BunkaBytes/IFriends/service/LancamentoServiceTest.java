package com.BunkaBytes.IFriends.service;


import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.BunkaBytes.IFriends.exception.ErroAutenticacao;
import com.BunkaBytes.IFriends.exception.RegraNegocioException;
import com.BunkaBytes.IFriends.model.entity.Lancamento;
import com.BunkaBytes.IFriends.model.entity.Usuario;
import com.BunkaBytes.IFriends.model.enums.StatusLancamento;
import com.BunkaBytes.IFriends.model.repository.LancamentoRepository;
import com.BunkaBytes.IFriends.model.repository.LancamentoRepositoryTest;
import com.BunkaBytes.IFriends.service.impl.LancamentoServiceImpl;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
public class LancamentoServiceTest {
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
			Mockito.doNothing().when(service).validar(lancamentoASalvar);
			
			Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
			lancamentoSalvo.setId(1l);
			lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
			Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
			
			//execução
			Lancamento lancamento = service.salvar(lancamentoASalvar);
			
			//verificação
			Assertions.assertEquals(lancamento.getId(), lancamentoSalvo.getId());
			Assertions.assertEquals(lancamento.getStatus(), StatusLancamento.PENDENTE);
		});
	}	
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Assertions.assertThrows(RegraNegocioException.class, ()-> {
			//cenário
			Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
			Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
			
			//execução
			service.salvar(lancamentoASalvar);
			
			Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		});
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(1l);
			lancamento.setStatus(StatusLancamento.PENDENTE);

			Mockito.doNothing().when(service).validar(lancamento);
			Mockito.when(repository.save(lancamento)).thenReturn(lancamento);
			
			//execução
			service.atualizar(lancamento);
			
			//verificação
			Mockito.verify(repository, Mockito.times(1)).save(lancamento);
		});
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoTaSalvo() {
		Assertions.assertThrows(NullPointerException.class, ()-> {
			
			//cenário
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			
			//execução e verificação
			service.atualizar(lancamento);
			Mockito.verify(repository, Mockito.never()).save(lancamento);
		});
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(1l);
			
			//execução
			service.deletar(lancamento);
			Mockito.verify(repository).delete(lancamento);
		});
	}
	
	@Test
	public void deveLancarErroAoDeletarUmLancamentoQueAindaNaoTaSalvo() {
		Assertions.assertThrows(NullPointerException.class,()-> {
			//cenário
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			
			//execução
			service.deletar(lancamento);
			
			//verificação
			Mockito.verify(repository, Mockito.times(1)).delete(lancamento);
		});
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(1l);
			
			List<Lancamento> lista = Arrays.asList(lancamento);
			
			Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
			//execução
			List<Lancamento> result = service.buscar(lancamento);
			
			//verificação
			org.assertj.core.api.Assertions.assertThat(result).isNotEmpty().hasSize(1).contains(lancamento);
		});
	}
	
	@Test
	public void deveAtualizarStatusDeUmLancamento() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(1l);
			lancamento.setStatus(StatusLancamento.PENDENTE);
			
			StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
			Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
			
			//execução
			service.atualizarStatus(lancamento, novoStatus);
			
			//verificação
			Assertions.assertEquals(lancamento.getStatus(), novoStatus);
			Mockito.verify(service).atualizar(lancamento);
			
		});
	}
	
	@Test
	public void deveObterLancamentoPorId() {
		Assertions.assertDoesNotThrow(()-> {
			//cenário
			Long id = 1l;
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(id);
			
			Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
			
			//execução
			Optional<Lancamento> result = service.obterPorId(lancamento.getId());
			
			//verificação
			Assertions.assertTrue(result.isPresent());
			
		});
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		Assertions.assertDoesNotThrow( ()-> {
			//cenário
			Long id = 1l;
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(id);
			
			Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
			
			//execução
			Optional<Lancamento> result = service.obterPorId(lancamento.getId());
			
			//verificação
			Assertions.assertFalse(result.isPresent());
			
		});
	}
	
	@Test
	public void deveLancarErroAoValidarLancamento() {
			//cenário
			Lancamento lancamento = new Lancamento();
			
			//execução
			Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição valida.");
			
			lancamento.setDescricao("");
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição valida.");
			
			lancamento.setDescricao("Salario");
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
			
			lancamento.setMes(0);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
			
			lancamento.setMes(13);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
			
			lancamento.setMes(1);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido.");
			
			lancamento.setAno(201);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido.");
			
			lancamento.setAno(2019);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
			
			lancamento.setUsuario(new Usuario());
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
			
			lancamento.getUsuario().setId(1l);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor válido.");
			
			lancamento.setValor(BigDecimal.ZERO);
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor válido.");
			
			lancamento.setValor(BigDecimal.valueOf(100));
			
			exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.validar(lancamento));
			org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de lançamento.");
	}
	


}
