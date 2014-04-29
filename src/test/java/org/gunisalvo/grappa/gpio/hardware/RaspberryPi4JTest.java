package org.gunisalvo.grappa.gpio.hardware;

import static org.junit.Assert.*;

import java.net.URL;

import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.xml.LeitorConfiguracao;
import org.junit.Test;

public class RaspberryPi4JTest {

	@Test
	public void test() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("grappa.xml");
		GpioGrappa mapeamento = new LeitorConfiguracao().carregarGpio(url.getPath());
		RaspberryPi4J cobaia = new RaspberryPi4J(mapeamento);
	}

}
