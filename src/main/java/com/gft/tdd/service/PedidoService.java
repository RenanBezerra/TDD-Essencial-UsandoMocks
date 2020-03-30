package com.gft.tdd.service;

import com.gft.tdd.email.NotificarEmail;
import com.gft.tdd.model.Pedido;
import com.gft.tdd.repository.Pedidos;
import com.gft.tdd.sms.NotificarSms;

public class PedidoService {

	private Pedidos pedidos;
	private NotificarEmail notificarEmail;
	private NotificarSms notificarSms;
	
	public PedidoService(Pedidos pedidos, NotificarEmail notificarEmail, NotificarSms notificarSms) {
		this.pedidos = pedidos;
		this.notificarEmail = notificarEmail;
		this.notificarSms = notificarSms;
	}


	public double lancar(Pedido pedido) {
		double imposto = pedido.getValor() * 0.1;
		
		pedidos.guardar(pedido); 
		
		notificarEmail.enviar(pedido);
		notificarSms.notificar(pedido);
		return imposto;
	}

}
