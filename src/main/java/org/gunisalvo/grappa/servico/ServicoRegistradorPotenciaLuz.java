package org.gunisalvo.grappa.servico;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;

@RegistradorListener(endereco=123)
public class ServicoRegistradorPotenciaLuz implements ServicoRegistrador{

    private int posicaoPino1 = 1;
    private int posicaoPino2 = 2;
    private int posicaoPino3 = 3;
    
    private boolean valorPino1 = false;
    private boolean valorPino2 = false;
    private boolean valorPino3 = false;

    @Override
    public void processarServico(Object valorEndereco) {
        try{
            String resultado = valorEndereco.toString();
            Integer numero = resultado == null ? 0 : Integer.parseInt(resultado);
            
            Grappa.getAplicacao().log("Evento registrador potência de luz, valor: " + numero, NivelLog.INFO);
            
            switch (numero){
            
                case 0:
                    // binário: 000
                
                    valorPino1 = false;
                    valorPino2 = false;
                    valorPino3 = false;
                
                    break;
            
                case 1:
                    // binário: 001
                
                    valorPino1 = false;
                    valorPino2 = false;
                    valorPino3 = true;
                
                    break;
            
                case 2:
                    // binário: 010
                
                    valorPino1 = false;
                    valorPino2 = true;
                    valorPino3 = false;
                
                    break;
            
                case 3:
                    // binário: 011
                
                    valorPino1 = false;
                    valorPino2 = true;
                    valorPino3 = true;
                
                    break;
            
                case 4:
                    // binário: 100
                
                    valorPino1 = true;
                    valorPino2 = false;
                    valorPino3 = false;
                
                    break;
            
                case 5:
                    // binário: 101
                
                    valorPino1 = true;
                    valorPino2 = false;
                    valorPino3 = true;
                
                    break;
            
                case 6:
                    // binário: 110
                
                    valorPino1 = true;
                    valorPino2 = true;
                    valorPino3 = false;
                
                    break;
            
                case 7:
                    // binário: 111
                
                    valorPino1 = true;
                    valorPino2 = true;
                    valorPino3 = true;
                
                    break;
            
            }
        
            Grappa.getAplicacao().log("Registrando valores nos pinos elétricos: ", NivelLog.INFO);
        
            Barramento.processarPacote(new PacoteGrappa(posicaoPino1, Conexao.GPIO, TipoAcao.ESCRITA, valorPino1));
            Grappa.getAplicacao().log(String.format("Pino {0}: {1}", posicaoPino1, valorPino1), NivelLog.INFO);
            
            Barramento.processarPacote(new PacoteGrappa(posicaoPino2, Conexao.GPIO, TipoAcao.ESCRITA, valorPino2));
            Grappa.getAplicacao().log(String.format("Pino {0}: {1}", posicaoPino2, valorPino2), NivelLog.INFO);
            
            Barramento.processarPacote(new PacoteGrappa(posicaoPino3, Conexao.GPIO, TipoAcao.ESCRITA, valorPino3));
            Grappa.getAplicacao().log(String.format("Pino {0}: {1}", posicaoPino3, valorPino3), NivelLog.INFO);
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
