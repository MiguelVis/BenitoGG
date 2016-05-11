/* +---------------------------------+
   | Proyecto BENITO G. G.           |
   | ------------------------------- |
   | (c) 2016 Miguel I. García López |
   |          FloppySoftware         |
   +---------------------------------+
*/

/**
 *  Devuelve el valor de un parámetro, leyéndolo de los
 *  argumentos de la URL.
 *
 *  @return   Valor del parámetro, o el valor por defecto,
 *            si no existe, o parece incorrecto.
 */
function getValor(parametro, defecto) {

	// Tomar URL
	var url = decodeURI(new String(document.location.href));

	// Comprobar si es el primer argumento
	var posArgs = url.indexOf("?" + parametro + "=");
	
	// Si no es el 1º, comprobar si es alguno posterior
	if(posArgs == -1) {
		posArgs = url.indexOf("&" + parametro + "=");
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
	// devolver el valor por defecto.
	return defecto;
}

/**
 *  Devuelve el nombre del jugador, leyéndolo de los
 *  argumentos de la URL, o un nombre ficticio, si
 *  no existe o no es correcto.
 *
 *  @return   Nombre del jugador
 */
function nombreJugador() {

	// Devolver el valor del parámetro que contiene
	// el nombre del jugador, o un valor ficticio.
	return getValor("nombre", "amig@");
}

/**
 *  Devuelve la escala de casos resueltos, leyéndola de los
 *  argumentos de la URL, o el valor -1,
 *  si no existe o no es correcta.
 *
 *  @return   Escala 0..4, o -1 en caso de error
 */
function escalaCasos() {

	// Devolver el valor del parámetro que contiene
	// la escala, o el valor -1, si no
	// existe el dato.
	var escala = getValor("escala", -1);
	
	// Comprobar la validez de la escala,
	// reemplazando el dato por -1, si no
	// parece correcta.
	if(escala < 0 || escala > 4 || isNaN(escala)) {
		escala = -1;
	}
	
	// Devolver escala
	return escala;
}

/**
 *  Indica si el usuario es zurdo, leyendo el dato de los
 *  argumentos de la URL.
 *
 *  @return   True si es zurdo, false en caso contrario o error
 */
function esZurdo() {

	// Devolver el valor del parámetro que contiene el dato,
	// o el valor 0, si no existe.
	var zurdo = getValor("zurdo", 0);
	
	// Devolver dato, como true o false
	return zurdo > 0;
}

