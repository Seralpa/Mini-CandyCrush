import java.util.Random;
import java.util.Scanner;

public class MiniCandyCrush {

	final static int MOVIMIENTOS = 10, NUMFILAS = 9, NUMCOLS = 9;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int[][] tablero;
		byte s;
		boolean seguir = true;
		while (seguir) {
			System.out.println("Elija tipo de tablero:");
			System.out.println("1. Facil");
			System.out.println("2. Intermedio");
			System.out.println("3. Dificil");
			System.out.println("4. Tablero fijo");
			System.out.println("0. Salir");
			s = in.nextByte();
			while (s < 0 || s > 4) {
				System.out.println("Eso no es una opcion, prueba de nuevo");
				s = in.nextByte();
			}
			if (s == 0) {
				System.out.println("Hasta pronto");
				seguir = false;
			} else if (s == 4) {
				tablero = generarTableroFijo();
				jugar(in, tablero, 1);// se asume que en el tablero fijo las casillas solo pueden tomar 3 valores
			} else {
				tablero = generarTableroAleatrio(s);
				jugar(in, tablero, s);
			}
		}
		in.close();
	}

	public static int[][] generarTableroAleatrio(int nivel) {
		// Genera un tablero aleatorio del nivel de dificultad especificado que no
		// contiene cadenas y tiene movimientos disponibles
		int[][] tablero = new int[NUMFILAS + 4][NUMCOLS + 2];
		Random rnd = new Random();
		do {
			for (int i = 2; i < tablero.length - 2; i++) {
				for (int j = 1; j < tablero[i].length - 1; j++) {
					tablero[i][j] = rnd.nextInt(nivel + 2) + 1;
				}
			}
			eliminarCadenas(tablero, nivel);
		} while (!hayMovimientos(tablero));

		return tablero;
	}

	public static void jugar(Scanner in, int[][] tablero, int nivel) {
		// Ejecuta la mecanica del juego solicitando al usuario los movimientos y
		// actualizando el tablero y las puntuaciones
		int i1, i2, j1, j2, puntuacionMov, puntuacionTotal = 0, i = 0;
		while (i < MOVIMIENTOS) {
			mostrarTablero(tablero);
			System.out.println("introduce 2 posiciones a intercambiar o 0 0 0 0 para salir");
			i1 = in.nextInt();
			j1 = in.nextInt();
			i2 = in.nextInt();
			j2 = in.nextInt();
			in.nextLine();
			if (i1 == 0 && j1 == 0 && i2 == 0 && j2 == 0) {
				System.out.println("La partida ha finalizado por peticion del usuario");
				System.out.println("Puntuacion final: " + puntuacionTotal);
				System.out.println();
				System.out.println();
				System.out.println();
				return;
			}
			while (i1 < 1 || i1 > tablero.length - 2 || j1 < 1 || j1 > tablero.length - 1 || i2 < 1|| i2 > tablero.length - 2 || j2 < 1 || j2 > tablero.length - 1|| Math.abs(i1 - i2) + Math.abs(j1 - j2) != 1) {
				System.out.println("Movimiento invalido, pruebe otra vez ");
				i1 = in.nextInt();
				j1 = in.nextInt();
				i2 = in.nextInt();
				j2 = in.nextInt();
				in.nextLine();
			}
			swap(tablero, tablero.length - i1 - 2, j1, tablero.length - i2 - 2, j2);
			puntuacionMov = eliminarCadenas(tablero, nivel);
			if (puntuacionMov == 0) {
				System.out.println("Movimiento invalido, este movimiento no genera una cadena, pruebe otro");
				swap(tablero, tablero.length - i1 - 2, j1, tablero.length - i2 - 2, j2);
			} else {
				i++;
				puntuacionTotal += puntuacionMov;
				System.out.println("Puntuacion del movimiento: " + puntuacionMov);
				System.out.println("Puntuacion total: " + puntuacionTotal);
				System.out.println("Quedan " + (MOVIMIENTOS - i) + " movimientos");
				// Garantizar que hay movimientos disponibles
				if (!hayMovimientos(tablero)) {
					System.out.println("Tablero sin movimientos disponibles, generando uno nuevo...");
					tablero = generarTableroAleatrio(nivel);
				}
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("No quedan movimientos");
		System.out.println("Puntuacion final de la partida: " + puntuacionTotal);
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public static void mostrarTablero(int[][] tablero) {
		// Muestra el tablero por pantalla
		System.out.print("   ");
		for (int i = 1; i < tablero[0].length-1; i++) {
			System.out.print(" "+i);
		}
		System.out.println();
		for (int i = 2; i < tablero.length - 2; i++) {
			System.out.println();
			System.out.print((tablero.length - i-2)+"   ");
			for (int j = 1; j < tablero[i].length - 1; j++) {
				switch (tablero[i][j]) {
				case 1:
					System.out.print("o ");
					break;
				case 2:
					System.out.print("+ ");
					break;
				case 3:
					System.out.print("| ");
					break;
				case 4:
					System.out.print("$ ");
					break;
				case 5:
					System.out.print("~ ");
					break;
				default:
					System.out.print("ERROR " + tablero[i][j]);
					break;
				}
			}

		}
		System.out.println();
	}

	public static boolean hayMovimientos(int[][] tablero) {
		// Comprueba si hay movimientos disponibles en el tablero, si los hay devuelve
		// true y en caso contrario false
		int prev;
		for (int j = 1; j < tablero[0].length - 1; j++) {
			prev = 0;
			for (int i = 2; i < tablero.length - 2; i++) {
				if (tablero[i][j] == prev) {
					if (tablero[i - 3][j] == prev || tablero[i + 2][j] == prev || tablero[i - 2][j + 1] == prev
							|| tablero[i - 2][j - 1] == prev || tablero[i + 1][j + 1] == prev
							|| tablero[i + 1][j - 1] == prev) {
						return true;
					}
				}
				if (tablero[i][j] == tablero[i + 2][j]) {
					if (tablero[i][j] == tablero[i + 1][j - 1] || tablero[i][j] == tablero[i + 1][j + 1]) {
						return true;
					}
				}
				prev = tablero[i][j];
			}
		}
		return false;
	}

	public static void swap(int[][] tablero, int x1, int y1, int x2, int y2) {
		// Intercambia dos casillas de una matriz
		int tmp = tablero[x1][y1];
		tablero[x1][y1] = tablero[x2][y2];
		tablero[x2][y2] = tmp;
	}

	public static int eliminarCadenas(int[][] tablero, int nivel) {
		// Elimina todas las cadenas que contenga un tablero aplicando las normas del
		// juego y devuelve la puntuacion producida por dichas cadenas
		int prev, puntuacionBatida = 0, puntuacionTotal = 0, longitudCadena;
		Random rnd = new Random();
		do {
			puntuacionBatida = 0;
			for (int j = 1; j < tablero[0].length - 1; j++) {
				prev = 0;
				longitudCadena = 1;
				for (int i = 2; i < tablero.length - 1; i++) { // se deja avanzar a una fila del margen para poder
																// detectar las cadenas que toquen con el borde inferior
					if (tablero[i][j] == prev) {
						longitudCadena++;
						if (longitudCadena >= 3) {
							puntuacionBatida += 10;
						}
					} else {
						if (longitudCadena >= 3) {
							// bajar casillas
							for (int k = 1; i - longitudCadena - k > 1; k++) {
								tablero[i - k][j] = tablero[i - longitudCadena - k][j];
							}
							// rellenar por arriba
							for (int k = 0; k < longitudCadena; k++) {
								tablero[k + 2][j] = rnd.nextInt(nivel + 2) + 1;
							}
						}
						longitudCadena = 1;
						prev = tablero[i][j];
					}
				}
			}
			puntuacionTotal += puntuacionBatida;
		} while (puntuacionBatida != 0);
		return puntuacionTotal;
	}

	public static int[][] generarTableroFijo() {
		// Devuelve un tablero prefijado
		int[][] tablero = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
							{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
							{ 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
							{ 0, 1, 2, 2, 2, 1, 2, 2, 2, 1, 0 },
							{ 0, 3, 2, 3, 3, 3, 1, 3, 3, 3, 0 },
							{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
							{ 0, 2, 2, 2, 2, 1, 2, 2, 1, 3, 0 },
							{ 0, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0 },
							{ 0, 1, 1, 2, 1, 3, 1, 1, 1, 1, 0 },
							{ 0, 2, 2, 1, 2, 2, 2, 2, 1, 2, 0 },
							{ 0, 3, 2, 1, 3, 3, 3, 3, 3, 1, 0 },
							{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
							{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		return tablero;
	}
}
