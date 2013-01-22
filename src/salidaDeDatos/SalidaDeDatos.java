package salidaDeDatos;

/**
 * Esta clase ha sido REUTILIZADA de la practica 1 'A first Approach to Cycling'
 * realizada por Juan Carlos Marco y Juan Luis Pérez
 * 
 */
public class SalidaDeDatos {

	/**
	 * este metodo saca por pantalla el mensaje introducido con el formato
	 * indicado, el cual debe de existir en el propio metodo
	 * 
	 * @param mensaje
	 *            La informacion a mostrar
	 * @param formato
	 *            El formato para mostrar la informacion
	 */
	public void mostrarPorPantalla(String mensaje, String formato) {

		StringBuffer mensajefinal = new StringBuffer();

		switch (formato) {
			case "cadencia": {
				mensajefinal.append(mensaje + " pedaladas por segundo ");
				break;
			}
			case "velocidad": {
				mensajefinal.append("Velocidad actual:" + mensaje + " m/s ");
				break;
			}
	
			case "hh:mm:ss": {
				int i = 0;
				while (i < mensaje.length()) {
					if (mensaje.charAt(i) == ' ') {
						mensajefinal.insert(i, ":");
	
					} else {
						mensajefinal.insert(i, mensaje.charAt(i));
					}
	
					i++;
				}
				break;
				}
								
				
				default:{ mensajefinal.append(mensaje);}
		}
		
		
		System.out.println(mensajefinal.toString());
	}

	/**
	 * este metodo recibe un String con los datos a mostrar y con el formato
	 * inclusive y es capaz de sacarlo formateado de dicha forma
	 * 
	 * @param mensaje
	 *            contiene el mensaje a mostrar y el formato a usar funciona de
	 *            la siguiente manera : mensaje = "mensaje#formato"
	 */
	public void mostrarPorPantalla(String mensaje) {

		StringBuffer mensajefinal = new StringBuffer();
		StringBuffer formato = new StringBuffer();
		String cuerpomensaje = new String();
		
		int posicionempiezaformato = 0;

		// buscamos la posicion a partir de la cual empieza el formato
		while (posicionempiezaformato < mensaje.length()
				&& mensaje.charAt(posicionempiezaformato) != '#') {
			
			posicionempiezaformato++;
		}
		
		formato.append(mensaje.substring(posicionempiezaformato + 1).toString());
		cuerpomensaje = mensaje.substring(0, posicionempiezaformato);
		
		// como ya sabemos donde empieza el formato, ahora
		//
		// comparamos para sacar la salida formateada
		// con dicho formato
		switch (formato.toString()) {
		
			case "cadencia": {
				mensajefinal.append(" pedaladas por segundo ");
				break;
			}
			case "velocidad": {
				mensajefinal.append("Velocidad actual:").append(" m/s ");
				break;
			}
			case "distancia": {
				mensajefinal.append("Distancia recorrida:"
						+ mensaje.substring(0, posicionempiezaformato) + " m ");
				break;
			}
			case "hh:mm:ss": {
				int caractermensaje = 0;
				while (caractermensaje < posicionempiezaformato) {
					if (mensaje.charAt(caractermensaje) == ' ') {
						mensajefinal.insert(caractermensaje, ":");
	
					} else {
						mensajefinal.insert(caractermensaje,
								mensaje.charAt(caractermensaje));
					}
	
					caractermensaje++;
					break;
				}
			}
			case "iteration": {
				mensajefinal.append("Iteracion nº:").append(cuerpomensaje);
	
				break;
			}
			case "tabu": {
				mensajefinal.append("TABU\n").append(cuerpomensaje);
	
				break;
			}
			case "NN": {
				mensajefinal.append("NN = ").append(cuerpomensaje);
				
				break;
			}
			default: {
				mensajefinal.append(cuerpomensaje);
			}
		}

		System.out.println(mensajefinal.toString());
	}

}
