package com.app.floppysoftware.benitogg;

/**
 * Clase que define valores estáticos para la base de datos.
 */
public final class Contract {

    // Base de datos
    public static final String DATABASE_NAME = "benitogg.db";  // Nombre
    public static final int DATABASE_VERSION = 1;              // Versión

    // Nombres de los ficheros de texto con los datos iniciales
    public static final String FICHERO_OBJETOS = "objetos";      // Objetos
    public static final String FICHERO_ACTORES = "actores";      // Actores
    public static final String FICHERO_LUGARES = "lugares";      // Lugares
    public static final String FICHERO_CASOS = "casos";          // Casos

    /**
     * Constructor
     */
    public Contract() {
        // Nada
    }

    /**
     * Clase que define los valores estáticos para la tabla de objetos.
     */
    public final class Objetos {

        // Nombre de la tabla
        public static final String TABLE_NAME = "Objetos";

        // Columnas (campos)
        public static final String ID_NAME = "_id";                // Id del objeto
        public static final String NOMBRE_NAME = "nombre";         // Nombre
        public static final String LUGAR_ID_NAME = "lugar_id_fk";  // Lugar en que se encuentra
        public static final String ESTADO_NAME ="estado";          // Estado

        // Tipos de datos de las columnas
        public static final String ID_TYPE = "text";               // Id del objeto
        public static final String NOMBRE_TYPE = "text";           // Nombre
        public static final String LUGAR_ID_TYPE = "text";         // Lugar en que se encuentra
        public static final String ESTADO_TYPE = "text";           // Estado

        /**
         * Comando SQL de creación de la tabla:
         *
         * create table Objetos (
         *    _id text primary key not null,
         *    nombre text not null,
         *    lugar_id_fk text not null,
         *    estado text not null,
         *    foreign key(lugar_id_fk) references Lugares(_id)
         * );
         */
        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " primary key not null," +
                NOMBRE_NAME + " " + NOMBRE_TYPE + " not null," +
                LUGAR_ID_NAME + " " + LUGAR_ID_TYPE + " not null," +
                ESTADO_NAME + " " + ESTADO_TYPE + " not null," +
                "foreign key(" + LUGAR_ID_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")" +
                ")";

        // Comando SQL de eliminación de la tabla
        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }

    /**
     * Clase que define los valores estáticos para la tabla de lugares.
     */
    public final class Lugares {

        // Nombre de la tabla
        public static final String TABLE_NAME = "Lugares";

        // Columnas (campos)
        public static final String ID_NAME = "_id";                      // Id
        public static final String TITULO_NAME = "titulo";               // Título
        public static final String DETALLE_NAME = "detalle";             // Detalle
        public static final String X_NAME = "x";                         // Posición X en el mapa
        public static final String Y_NAME = "y";                         // Posición Y en el mapa
        public static final String LUGAR_NORTE_NAME = "lugar_norte_fk";  // Salida al norte
        public static final String LUGAR_SUR_NAME = "lugar_sur_fk";      // Salida al sur
        public static final String LUGAR_ESTE_NAME = "lugar_este_fk";    // Salida al este
        public static final String LUGAR_OESTE_NAME = "lugar_oeste_fk";  // Salida al oeste

        // Tipos de datos de las columnas
        public static final String ID_TYPE = "text";                     // Id
        public static final String TITULO_TYPE = "text";                 // Título
        public static final String DETALLE_TYPE = "text";                // Detalle
        public static final String X_TYPE = "int";                       // Posición X en el mapa
        public static final String Y_TYPE = "int";                       // Posición Y en el mapa
        public static final String LUGAR_NORTE_TYPE = "text";            // Salida al norte
        public static final String LUGAR_SUR_TYPE = "text";              // Salida al sur
        public static final String LUGAR_ESTE_TYPE = "text";             // Salida al este
        public static final String LUGAR_OESTE_TYPE = "text";            // Salida al oeste

        /**
         * Comando SQL de creación de la tabla:
         *
         * create table Lugares (
         *    _id text primary key not null,
         *    titulo text not null,
         *    detalle text not null,
         *    lugar_norte_fk text,
         *    lugar_sur_fk text,
         *    lugar_este_fk text,
         *    lugar_oeste_fk text,
         *    foreign key(lugar_norte_fk) references Lugares(_id),
         *    foreign key(lugar_sur_fk) references Lugares(_id),
         *    foreign key(lugar_este_fk) references Lugares(_id),
         *    foreign key(lugar_oeste_fk) references Lugares(_id)
         * );
         */
        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " primary key not null," +
                TITULO_NAME + " " + TITULO_TYPE + " not null," +
                DETALLE_NAME + " " + DETALLE_TYPE + " not null," +
                X_NAME + " " + X_TYPE + " not null," +
                Y_NAME + " " + Y_TYPE + " not null," +
                LUGAR_NORTE_NAME + " " + LUGAR_NORTE_TYPE + "," +
                LUGAR_SUR_NAME + " " + LUGAR_SUR_TYPE + "," +
                LUGAR_ESTE_NAME + " " + LUGAR_ESTE_TYPE + "," +
                LUGAR_OESTE_NAME + " " + LUGAR_OESTE_TYPE + "," +
                "foreign key (" + LUGAR_NORTE_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")," +
                "foreign key (" + LUGAR_SUR_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")," +
                "foreign key (" + LUGAR_ESTE_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")," +
                "foreign key (" + LUGAR_OESTE_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")" +
                ")";

        // Comando SQL de eliminación de la tabla
        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;

    }

    /**
     * Clase que define los valores estáticos para la tabla actores.
     */
    public final class Actores {

        // Nombre de la tabla
        public static final String TABLE_NAME = "Actores";

        // Columnas (campos)
        public static final String ID_NAME = "_id";                 // Id
        public static final String LUGAR_ID_NAME = "lugar_id_fk";   // Lugar en que se encuentra

        // Tipos de datos de las columnas
        public static final String ID_TYPE = "text";                // Id
        public static final String LUGAR_ID_TYPE = "text";          // Lugar en que se encuentra

        /**
         * Comando SQL de creación de la tabla:
         *
         * create table Actores (
         *    _id text primary key not null,
         *    lugar_id_fk text not null,
         *    foreign key(lugar_id_fk) referencies Lugares(_id)
         * );
         */
        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " primary key not null," +
                LUGAR_ID_NAME + " " + LUGAR_ID_TYPE + " not null," +
                "foreign key(" + LUGAR_ID_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")" +
                ")";

        // Comando SQL de eliminación de la tabla
        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }

    /**
     * Clase que define los valores estáticos de la tabla de casos.
     */
    public final class Casos {

        // Nombre de la tabla
        public static final String TABLE_NAME = "Casos";

        // Columnas (campos)
        public static final String ID_NAME = "_id";             // Id
        public static final String NOMBRE_NAME = "nombre";      // Nombre
        public static final String RESUELTO_NAME = "resuelto";  // Resuelto

        // Tipos de datos de las columnas
        public static final String ID_TYPE = "int";             // Id
        public static final String NOMBRE_TYPE = "text";        // Nombre
        public static final String RESUELTO_TYPE = "int";       // Resuelto (SQLite no admite boolean)

        /**
         * Comando SQL de creación de la tabla:
         *
         * create table Casos (
         *    _id int primary key not null,
         *    nombre text not null,
         *    resuelto int not null,
         * );
         */
        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " not null," +
                NOMBRE_NAME + " " + NOMBRE_TYPE + " not null," +
                RESUELTO_NAME + " " + RESUELTO_TYPE + " not null" +
                ")";

        // Comando SQL de eliminación de la tabla
        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }
}
