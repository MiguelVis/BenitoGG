package com.app.floppysoftware.benitogg;

/**
 *
 */
public final class Contract {

    public static final String DATABASE_NAME = "benitogg.db";
    public static final int DATABASE_VERSION = 1;

    // Nombres de los ficheros de texto con los datos iniciales
    public static final String FICHERO_OBJETOS = "objetos";
    public static final String FICHERO_ACTORES = "actores";
    public static final String FICHERO_LUGARES = "lugares";
    public static final String FICHERO_CASOS = "casos";

    /**
     * Constructor
     *
     */
    public Contract() {
        //
    }

    public final class Objetos {
        public static final String TABLE_NAME = "Objetos";

        public static final String ID_NAME = "_id";
        public static final String NOMBRE_NAME = "nombre";
        public static final String LUGAR_ID_NAME = "lugar_id_fk";
        public static final String ESTADO_NAME ="estado";

        public static final String ID_TYPE = "text";
        public static final String NOMBRE_TYPE = "text";
        public static final String LUGAR_ID_TYPE = "text";
        public static final String ESTADO_TYPE = "text";

        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " primary key not null," +
                NOMBRE_NAME + " " + NOMBRE_TYPE + " not null," +
                LUGAR_ID_NAME + " " + LUGAR_ID_TYPE + "," +
                ESTADO_NAME + " " + ESTADO_TYPE + "," +
                "foreign key(" + LUGAR_ID_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")" +
                ")";

        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }

    /**
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
     *    );
     */
    public final class Lugares {
        public static final String TABLE_NAME = "Lugares";

        public static final String ID_NAME = "_id";
        public static final String TITULO_NAME = "titulo";
        public static final String DETALLE_NAME = "detalle";
        public static final String X_NAME = "x";
        public static final String Y_NAME = "y";
        public static final String LUGAR_NORTE_NAME = "lugar_norte_fk";
        public static final String LUGAR_SUR_NAME = "lugar_sur_fk";
        public static final String LUGAR_ESTE_NAME = "lugar_este_fk";
        public static final String LUGAR_OESTE_NAME = "lugar_oeste_fk";

        public static final String ID_TYPE = "text";
        public static final String TITULO_TYPE = "text";
        public static final String DETALLE_TYPE = "text";
        public static final String X_TYPE = "int";
        public static final String Y_TYPE = "int";
        public static final String LUGAR_NORTE_TYPE = "text";
        public static final String LUGAR_SUR_TYPE = "text";
        public static final String LUGAR_ESTE_TYPE = "text";
        public static final String LUGAR_OESTE_TYPE = "text";

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

        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;

    }

    public final class Actores {
        public static final String TABLE_NAME = "Actores";

        public static final String ID_NAME = "_id";
        public static final String LUGAR_ID_NAME = "lugar_id_fk";

        public static final String ID_TYPE = "text";
        public static final String LUGAR_ID_TYPE = "text";

        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " primary key not null," +
                LUGAR_ID_NAME + " " + LUGAR_ID_TYPE + " not null," +
                "foreign key(" + LUGAR_ID_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")" +
                ")";

        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }

    /**
     * create table Dichos (
     *    actor_id_fk text not null,
     *    lugar_id_fk text not null,
     *    detalle text,
     *    foreign key(actor_id) references Actores(_id),
     *    foreign key(lugar_id) references Lugares(_id)
     *    );
     */
    /********************************
    public final class Dichos {
        public static final String TABLE_NAME = "Dichos";

        public static final String ACTOR_ID_NAME = "actor_id_fk";
        public static final String LUGAR_ID_NAME = "lugar_id_fk";
        public static final String DETALLE_NAME = "detalle";

        public static final String ACTOR_ID_TYPE = "text";
        public static final String LUGAR_ID_TYPE = "text";
        public static final String DETALLE_TYPE = "text";

        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ACTOR_ID_NAME + " " + ACTOR_ID_TYPE + " not null," +
                LUGAR_ID_NAME + " " + LUGAR_ID_TYPE + " not null," +
                DETALLE_NAME + " " + DETALLE_TYPE + "," +
                "foreign key(" + ACTOR_ID_NAME + ") references " + Actores.TABLE_NAME + "(" + Actores.ID_NAME + ")" +
                "foreign key(" + LUGAR_ID_NAME + ") references " + Lugares.TABLE_NAME + "(" + Lugares.ID_NAME + ")" +
                ")";

        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }
     ****************************/

    /**
     * create table Casos (
     *    _id int primary key not null,
     *    nombre text not null,
     *    resuelto int not null
     *    );
     */
    public final class Casos {
        public static final String TABLE_NAME = "Casos";

        public static final String ID_NAME = "_id";
        public static final String NOMBRE_NAME = "nombre";
        public static final String RESUELTO_NAME = "resuelto";

        public static final String ID_TYPE = "int";
        public static final String NOMBRE_TYPE = "text";
        public static final String RESUELTO_TYPE = "int";

        public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
                ID_NAME + " " + ID_TYPE + " not null," +
                NOMBRE_NAME + " " + NOMBRE_TYPE + " not null," +
                RESUELTO_NAME + " " + RESUELTO_TYPE +
                ")";

        public static final String SQL_DROP_TABLE = "drop table " + TABLE_NAME;
    }
}
