package org.gunisalvo.grappa.log;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;

@Log
@Interceptor
public class GeradorLog{

	@Inject
	private Grappa aplicacao;
	
	@AroundInvoke
	public Object gerarLog(InvocationContext context) throws Exception {

		String metodo;
		String classe;

		try {
			metodo = context.getMethod().getName();
			classe = context.getTarget().getClass().getName();
		} catch (NullPointerException ex) {
			metodo = "test-method";
			classe = "TestClass";
		}

		this.aplicacao.log("invocando: " + classe + "." + metodo, NivelLog.INFO);
		long tempoInicial = System.currentTimeMillis();

		try {

			return context.proceed();

		} catch (Exception ex) {
			this.aplicacao.log("ERRO <<INESPERADO>> invocando " + classe + "." + metodo, NivelLog.ERRO);
			this.aplicacao.log("causa >> " + ex.getMessage(), NivelLog.ERRO);
			throw ex;

		} finally {

			long tempoFinal = System.currentTimeMillis();
			this.aplicacao.log("Chamada " + classe + "." + metodo + " executada em "
					+ (tempoFinal - tempoInicial) + "ms", NivelLog.INFO);

		}

	}

}
