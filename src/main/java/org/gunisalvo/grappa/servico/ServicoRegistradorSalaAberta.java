package org.gunisalvo.grappa.servico;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;

@RegistradorListener(endereco=11)
public class ServicoRegistradorSalaAberta implements ServicoRegistrador{

    private int EnderecoRegistradorPotencia = 123;
    
    private String valorSalaAberta = "7";
    private String valorSalaFechada = "0";

    @Override
    public void processarServico(Object valorEndereco) {
        try{
            String resultado = valorEndereco.toString();
            boolean SalaAberta = resultado == null ? false : Boolean.parseBoolean(resultado);
            
            Grappa.getAplicacao().log("Evento registrador de Sala, Sala Aberta : " + SalaAberta, NivelLog.INFO);
            
            Grappa.getAplicacao().log("Registrando valores no registrador de potência do endereço: " + 
                                      EnderecoRegistradorPotencia, NivelLog.INFO);
        
            String valorPotencia = new String();
            
            if (SalaAberta){
            
                valorPotencia = valorSalaAberta;
            
            } else {
            
                valorPotencia = valorSalaFechada;
            
            }
        
            Barramento.processarPacote(new PacoteGrappa(EnderecoRegistradorPotencia, Conexao.REGISTRADOR, TipoAcao.ESCRITA, valorPotencia));
            Grappa.getAplicacao().log("Valor: " + valorPotencia, NivelLog.INFO);
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}