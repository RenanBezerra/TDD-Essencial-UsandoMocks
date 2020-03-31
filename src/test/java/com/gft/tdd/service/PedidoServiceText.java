package com.gft.tdd.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gft.tdd.email.NotificarEmail;
import com.gft.tdd.model.Pedido;
import com.gft.tdd.model.StatusPedido;
import com.gft.tdd.model.builder.PedidoBuilder;
import com.gft.tdd.repository.Pedidos;
import com.gft.tdd.sms.NotificarSms;

public class PedidoServiceText {

	private PedidoService pedidoService;

	@Mock
	private Pedidos pedidos;

	@Mock
	private NotificarEmail notificarEmail;

	@Mock
	private NotificarSms notificarSms;

	private Pedido pedido;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		List<AcaoLancamentoPedido> acoes = Arrays.asList(pedidos, notificarEmail, notificarSms);
		pedidoService = new PedidoService(pedidos, acoes);
		pedido = new PedidoBuilder().comValor(100.0).para("João", "joao@joao.com", "9999-0000").construir();
	}

	@Test
	public void deveCalcularOImposto() throws Exception {
		double imposto = pedidoService.lancar(pedido);
		assertEquals(10.0, imposto, 0.0001);
	}

	@Test
	public void deveSalvarPedidoNoBancoDeDados() throws Exception {
		pedidoService.lancar(pedido);
		verify(pedidos).executar(pedido);

	}

	@Test
	public void deveNotificarPorEmail() throws Exception {
		pedidoService.lancar(pedido);
		verify(notificarEmail).executar(pedido);
	}

	@Test
	public void deveNotificarPorSms() throws Exception {
		pedidoService.lancar(pedido);
		verify(notificarSms).executar(pedido);
	}

	@Test
	public void devePagarPedidoPendente() throws Exception {
		Long codigoPedido = 135l;
		Pedido pedidoPendente = new Pedido();
		pedidoPendente.setStatus(StatusPedido.PENDENTE);
		when(pedidos.buscarPeloCodigo(codigoPedido)).thenReturn(pedidoPendente);

		Pedido pedidoPago = pedidoService.pagar(codigoPedido);

		assertEquals(StatusPedido.PAGO, pedidoPago.getStatus());

	}

	@Test(expected = StatusPedidoInvalidoException.class)
	public void deveNegarPagamento() throws Exception {

		Long codigoPedido = 135l;
		Pedido pedidoPendente = new Pedido();
		pedidoPendente.setStatus(StatusPedido.PAGO);
		when(pedidos.buscarPeloCodigo(codigoPedido)).thenReturn(pedidoPendente);

		Pedido pedidoPago = pedidoService.pagar(codigoPedido);

	}

}
