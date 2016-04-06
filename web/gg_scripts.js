/* +---------------------------------+
   | Proyecto BENITO G. G.           |
   | ------------------------------- |
   | (c) 2016 Miguel I. García López |
   |          FloppySoftware         |
   +---------------------------------+
*/

/**
 *  Devuelve el nombre del jugador, leyéndolo de los
 *  argumentos de la URL, o un nombre ficticio, si
 *  no existe o no es correcto.
 *
 *  @return   Nombre del jugador
 */
function nombreJugador() {
	
	// Tomar URL
	var url = decodeURI(new String(document.location.href));

	// Comprobar si es el primer argumento
	var posArgs = url.indexOf("?nombre=");
	
	// Si no es el 1º, comprobar si es alguno posterior
	if(posArgs == -1) {
		posArgs = url.indexOf("&nombre=");
	}
	
	// Tratar el argumento si se ha encontrado
	if(posArgs != -1) {
		
		// Tomar cadena a partir de la posición encontrada
		var arg = url.substring(posArgs, url.length);
		
		// Tomar cadena a partir del signo igual
		arg = arg.substring(arg.indexOf("=") + 1, arg.length);
		
		// Calcular final de la cadena
		
		// Puede seguirle otro argumento
		var fin = arg.indexOf("&");
		
		// O puede ser el último
		if(fin == -1) {
			fin = arg.length;
		}
		
		// Tomar el valor del argumento
		arg = arg.substring(0, fin);
		
		// Devolver el valor si parece correcto
		if(arg.length > 0) {
			return arg;
		}
	}
	
	// No existe el argumento, o no parece válido;
	// devolver cadena ficticia
	return "amig@";	
}