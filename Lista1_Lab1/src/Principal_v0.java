
/**
 * Lab1: Leitura de Base de Dados Nao-Distribuida
 * 
 * Autor: Caio Brito
 * Autor: Guilherme Kato
 * Ultima atualizacao: 26/03/2024
 * 
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class Principal_v0 {

	public final static Path path = Paths
			.get("Lista1_Lab1/src/fortune-br.txt");
	private int NUM_FORTUNES = 0;
	private int Max_Line_Fortune = 0;

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
				Max_Line_Fortune = lineCount;
				System.out.println(lineCount);
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
			int upperBound = Max_Line_Fortune;

			// Seleciona aleatoriamente um número entre 0 e o numero maximo de fortunas do arquivo
			Random random = new Random();
			int randomKey = random.nextInt(upperBound + 1); // +1 porque o limite superior é exclusivo
			
			// Busca o numero aleatorio na Tabela Hash
			String randomFortune = hm.get(randomKey);
			// Imprime a fortuna 
			System.out.println("Fortuna Aleatória:");
			System.out.println(randomFortune);

		}

		public void write(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			// Solicitar que o usuário insira a nova fortuna
			System.out.println("Digite a nova fortuna:");
			Scanner scanner = new Scanner(System.in);
			String novaFortuna = scanner.nextLine();
			scanner.close();

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

	public void iniciar() {

		FileReader fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			fr.write(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Principal_v0().iniciar();
	}

}
