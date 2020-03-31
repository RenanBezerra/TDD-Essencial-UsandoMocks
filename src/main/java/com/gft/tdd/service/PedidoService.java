package com.gft.tdd.service;

import java.util.List;

import com.gft.tdd.model.Pedido;
import com.gft.tdd.model.StatusPedido;
import com.gft.tdd.repository.Pedidos;

public class PedidoService {

	// private Pedidos pedidos;

	private List<AcaoLancamentoPedido> acoes;
	private Pedidos pedidos;

	public PedidoService(Pedidos pedidos, List<AcaoLancamentoPedido> acoes) {
		this.pedidos = pedidos;
		this.acoes = acoes;

	}

	public double lancar(Pedido pedido) {
		double imposto = pedido.getValor() * 0.1;

		// pedidos.guardar(pedido);

		acoes.forEach(a -> a.executar(pedido));

		return imposto;
	}

	public Pedido pagar(Long codigo) {
		Pedido pedido = pedidos.buscarPeloCodigo(codigo);

		if (!pedido.getStatus().equals(StatusPedido.PENDENTE))
			throw new StatusPedidoInvalidoException();

		pedido.setStatus(StatusPedido.PAGO);
		return pedido;
	}

}
