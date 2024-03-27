import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
    
    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    private static String mensagemMod;

    private int porta=1025;
    
    public void iniciar(){
    	System.out.println("Cliente iniciado na porta: "+porta);
    	
    	try {
            
            socket = new Socket("127.0.0.1", porta);
            
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            while (true) {
                exibirMenu();
                String opcao = br.readLine();
                
                if (opcao.equalsIgnoreCase("1")) {
                    lerMensagem();
                } else if (opcao.equalsIgnoreCase("2")) {
                    System.out.println("Digite a mensagem:");
                    String mensagem = br.readLine();
                    escreverMensagem(mensagem);
                } else if (opcao.equalsIgnoreCase("3")) {
                    break;
                } else {
                    System.out.println("Opção inválida.");
                }
            }
            
            socket.close();
            
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    private void exibirMenu() {
        System.out.println("\nEscolha uma opção:");
        System.out.println("1. Read");
        System.out.println("2. Write");
        System.out.println("3. Exit");
        System.out.print("Opção: ");
    }
    
    private void lerMensagem() {
        try {
            String json = "{\n\"method\": \"read\",\"args\": [\"\"]\n}";
            saida.writeUTF(json);
            String resultado = entrada.readUTF();
            System.out.println("Mensagem recebida do servidor: " + resultado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void escreverMensagem(String mensagem) {
        try {
            String json = "{\n\"method\": \"write\",\"args\": [\"" + mensagem + "\"]\n}";
            saida.writeUTF(json);
            System.out.println("Mensagem enviada para o servidor.");
            String resultado = entrada.readUTF();
            System.out.println("Mensagem recebida do servidor: " + resultado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Cliente().iniciar();
    }
}
