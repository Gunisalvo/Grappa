package org.gunisalvo.grappa.gpio.hardware;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.xml.LeitorConfiguracao;

public class RaspberryPi4JTest {

	//@Test
	public void test() {
		try{
		URL url = Thread.currentThread().getContextClassLoader().getResource("grappa.xml");
		GpioGrappa mapeamento = new LeitorConfiguracao().carregarGpio(url.getPath());
		RaspberryPi4J cobaia = new RaspberryPi4J(mapeamento);
		assertNotNull(cobaia);
		}catch(Exception ex){
			ex.printStackTrace();
		}catch(Error er){
			er.printStackTrace();
		}
	}

}
