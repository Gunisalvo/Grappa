package br.com.caelum.grappa.pin.hardware;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import br.com.caelum.grappa.model.PinConfiguration;
import br.com.caelum.grappa.pin.hardware.RaspberryPi4J;
import br.com.caelum.grappa.xml.ConfigurationParser;

public class RaspberryPi4JTest {

	//@Test
	public void test() {
		try{
		URL url = Thread.currentThread().getContextClassLoader().getResource("grappa.xml");
		PinConfiguration mapeamento = new ConfigurationParser().carregarGpio(url.getPath());
		RaspberryPi4J cobaia = new RaspberryPi4J(mapeamento);
		assertNotNull(cobaia);
		}catch(Exception ex){
			ex.printStackTrace();
		}catch(Error er){
			er.printStackTrace();
		}
	}

}
