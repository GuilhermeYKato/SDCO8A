
/**
 * Lab2: Leitura de Base de Dados Nao-Distribuida
 * 
 * Autor: Caio Brito
 * Autor: Guilherme Kato
 * Ultima atualizacao: 26/03/2024
 * 
 */

import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;

	private int porta = 1025;
	private String mensagemCliente;

	public final static Path path = Paths
			.get("Lista1_Lab2/src/fortune-br.txt");
	private String msgFortuna;

	public class FileReader {

		public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				} // fim while

				// Printa o numero maximo de linhas do txt
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}

			return lineCount;
		}

		public void parser(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				int lineCount = 0;

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();
					StringBuffer fortune = new StringBuffer();
					while (!(line == null) && !line.equals("%")) {
						fortune.append(line + "\n");
						line = br.readLine();
						// System.out.print(lineCount + ".");
					}

					hm.put(lineCount, fortune.toString());
					// System.out.println(fortune.toString());

					// System.out.println(lineCount);
				} // fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public void read(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			// Usa o limite para gerar um número aleatório para as fortunas
			int upperBound = countFortunes();

			// Seleciona aleatoriamente um número entre 0 e o numero maximo de fortunas do
			// arquivo
			Random random = new Random();
			int randomKey = random.nextInt(upperBound + 1); // +1 porque o limite superior é exclusivo

			// Busca o numero aleatorio na Tabela Hash
			String randomFortune = hm.get(randomKey);
			// Imprime a fortuna
			// System.out.println("Fortuna Aleatória:");
			// System.out.println(randomFortune);
			msgFortuna = randomFortune;
		}

		public void write(HashMap<Integer, String> hm, String newFortune)
				throws FileNotFoundException {

			
			String novaFortuna = newFortune;
			

			// Abrir o arquivo para escrita
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
				// Escreve a nova fortuna no final do arquivo
				writer.write("\n%\n" + novaFortuna);
				System.out.println("Nova fortuna adicionada com sucesso!");
			} catch (IOException e) {
				System.out.println("Erro ao escrever a nova fortuna no arquivo.");
				e.printStackTrace();
			}

		}
	}

	public void parserFortuna(){
		
	}

	public void iniciar() {
		System.out.println("Servidor iniciado na porta: " + porta);
		FileReader fr = new FileReader();
		HashMap hm = new HashMap<Integer, String>();
		try {
			fr.parser(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// Criar porta de recepcao
			server = new ServerSocket(porta);
			socket = server.accept(); // Processo fica bloqueado, ah espera de conexoes
			while (true) {
				
				// Criar os fluxos de entrada e saida
				entrada = new DataInputStream(socket.getInputStream());
				saida = new DataOutputStream(socket.getOutputStream());

				mensagemCliente = entrada.readUTF();

				// Extraindo dados do JSON
				//////////////////////////////////////////

				// Convertendo a sequência de bytes em uma string
				String jsonString = new String(mensagemCliente);

				// Removendo caracteres indesejados do JSON
				jsonString = jsonString.replace("{", "");
				jsonString = jsonString.replace("}", "");
				jsonString = jsonString.replace("\"", "");
				jsonString = jsonString.replace("[", "");
				jsonString = jsonString.replace("]", "");

				// Separando o JSON em tokens usando ","
				String[] tokens = jsonString.split(",");

				// Criando um vetor para armazenar os valores extraídos
				String[] values = new String[4];
				for (String token : tokens) {
					String[] pair = token.split(":");
					if (pair.length == 2) {
						String key = pair[0].trim();
						String value = pair[1].trim();
						switch (key) {
							case "method":
								values[0] = value;
								break;
							case "args":
								values[2] = value;
								break;
							case "mensagem":
								values[3] = value;
								break;
						}
					}
				}

				// Compara se eh READ ou WRITE
				String resultado = "";
				String msgParaCliente = "";
				if (values[0].equals("read")) {
					fr.read(hm);
					msgParaCliente = "{\n"+" \"result\": "+ "\"" +msgFortuna+"\""+"\n}";

				} else {
					resultado = values[2];
					// Criando formato JSON
					msgParaCliente = "{\n" + "\"result\": " + "\"" +resultado+"\"\n}";
					fr.write(hm, resultado);
				}
				///////////////////////////////////////////
				
				// Envio dos dados (resultado)
				saida.writeUTF(msgParaCliente);

				// socket.close();
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		new Servidor().iniciar();

	}

}
