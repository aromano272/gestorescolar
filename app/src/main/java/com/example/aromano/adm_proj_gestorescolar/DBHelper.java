package com.example.aromano.adm_proj_gestorescolar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by aRomano on 27/05/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    private static SQLiteDatabase db;

    private static final String DATABASE_NAME = "gestorescolar.db";
    private static final int DATABASE_VERSION = 14;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    public static synchronized DBHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }



    public static final String tb_alunos = "tb_alunos";
    public static final String col_alunos_id = "_id";
    public static final String col_alunos_username = "username";
    public static final String col_alunos_nome = "nome";
    public static final String col_alunos_email = "email";

    public static final String tb_cadeiras = "tb_cadeiras";
    public static final String col_cadeiras_id = "_id";
    public static final String col_cadeiras_nome = "nome";
    public static final String col_cadeiras_abbr = "abbr";
    public static final String col_cadeiras_creditos = "creditos";

    public static final String tb_matriculas = "tb_matriculas";
    public static final String col_matriculas_id = "_id";
    public static final String col_matriculas_idaluno = "idaluno";
    public static final String col_matriculas_idcadeira = "idcadeira";
    public static final String const_matriculas_fktb_matriculastb_alunos = String.format(
            "constraint fktb_matriculastb_alunos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_matriculas_idaluno, tb_alunos, col_alunos_id);
    public static final String const_matriculas_fktb_matriculastb_cadeiras = String.format(
            "constraint fktb_matriculastb_cadeiras foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_matriculas_idcadeira, tb_cadeiras, col_cadeiras_id);
    public static final String const_matriculas_unqidalunoidcadeira = String.format(
            "constraint unqidalunoidcadeira unique (%s,%s)",
            col_matriculas_idaluno, col_matriculas_idcadeira);

    public static final String tb_aulas = "tb_aulas";
    public static final String col_aulas_id = "_id";
    public static final String col_aulas_idcadeira = "idcadeira";
    public static final String col_aulas_diasemana = "diasemana";
    public static final String col_aulas_horaentrada = "horaentrada";
    public static final String col_aulas_sala = "sala";
    public static final String const_aulas_fktb_aulastb_cadeiras = String.format(
            "constraint fktb_aulastb_cadeiras foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_aulas_idcadeira, tb_cadeiras, col_cadeiras_id);

    public static final String tb_aulasfrequentadas = "tb_aulasfrequentadas";
    public static final String col_aulasfrequentadas_id = "_id";
    public static final String col_aulasfrequentadas_idaluno = "idaluno";
    public static final String col_aulasfrequentadas_idaula = "idaula";
    public static final String const_aulasfrequentadas_fktb_aulasfrequentadastb_alunos = String.format(
            "constraint fktb_aulasfrequentadastb_alunos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_aulasfrequentadas_idaluno, tb_alunos, col_alunos_id);
    public static final String const_aulasfrequentadas_fktb_aulasfrequentadastb_aulas = String.format(
            "constraint fktb_aulasfrequentadastb_aulas foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_aulasfrequentadas_idaula, tb_aulas, col_aulas_id);
    public static final String const_aulasfrequentadas_unqidalunoidaula = String.format(
            "constraint unqidalunoidaula unique (%s,%s)",
            col_aulasfrequentadas_idaluno, col_aulasfrequentadas_idaula);

    public static final String tb_eventos = "tb_eventos";
    public static final String col_eventos_id = "_id";
    public static final String col_eventos_idcadeira = "idcadeira";
    public static final String col_eventos_tipo = "tipo";
    public static final String col_eventos_datahora = "datahora";
    public static final String col_eventos_descricao = "descricao";
    public static final String col_eventos_sala = "sala";
    public static final String const_eventos_fktb_eventostb_cadeiras = String.format(
            "constraint fktb_eventostb_cadeiras foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_eventos_idcadeira, tb_cadeiras, col_cadeiras_id);

    public static final String tb_notas = "tb_notas";
    public static final String col_notas_id = "_id";
    public static final String col_notas_idaluno = "idaluno";
    public static final String col_notas_idevento = "idevento";
    public static final String col_notas_nota = "nota";
    public static final String const_notas_fktb_notastb_alunos = String.format(
            "constraint fktb_notastb_alunos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_notas_idaluno, tb_alunos, col_alunos_id);
    public static final String const_notas_fktb_notastb_eventos = String.format(
            "constraint fktb_notastb_eventos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_notas_idevento, tb_eventos, col_eventos_id);
    public static final String const_notas_unqidalunoidevento = String.format(
            "constraint unqidalunoidevento unique (%s,%s)",
            col_notas_idaluno, col_notas_idevento);



    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_tb_alunos = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s varchar(20) not null unique," +
                        "%s varchar(20)," +
                        "%s varchar(20)" +
                        ");",
                tb_alunos, col_alunos_id, col_alunos_username, col_alunos_nome, col_alunos_email);
        String create_tb_cadeiras = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s varchar(20) not null unique," +
                        "%s varchar(6) not null unique," +
                        "%s int" +
                        ");",
                tb_cadeiras, col_cadeiras_id, col_cadeiras_nome, col_cadeiras_abbr, col_cadeiras_creditos);
        String create_tb_matriculas = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s," +
                        "%s," +
                        "%s" +
                        ");",
                tb_matriculas, col_matriculas_id, col_matriculas_idaluno, col_matriculas_idcadeira,
                const_matriculas_fktb_matriculastb_alunos, const_matriculas_fktb_matriculastb_cadeiras, const_matriculas_unqidalunoidcadeira);
        String create_tb_aulas = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null check("+ col_aulas_diasemana + " between 0 and 6)," +
                        "%s int not null," +
                        "%s varchar(20) not null," +
                        "%s" +
                        ");",
                tb_aulas, col_aulas_id, col_aulas_idcadeira, col_aulas_diasemana, col_aulas_horaentrada, col_aulas_sala,
                const_aulas_fktb_aulastb_cadeiras);
        String create_tb_aulasfrequentadas = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s," +
                        "%s," +
                        "%s" +
                        ");",
                tb_aulasfrequentadas, col_aulasfrequentadas_id, col_aulasfrequentadas_idaluno, col_aulasfrequentadas_idaula, const_aulasfrequentadas_fktb_aulasfrequentadastb_alunos,
                const_aulasfrequentadas_fktb_aulasfrequentadastb_aulas, const_aulasfrequentadas_unqidalunoidaula);
        String create_tb_eventos = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s datetime not null," +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s" +
                        ");",
                tb_eventos, col_eventos_id, col_eventos_idcadeira, col_eventos_tipo, col_eventos_datahora, col_eventos_descricao,
                col_eventos_sala, const_eventos_fktb_eventostb_cadeiras);
        String create_tb_notas = String.format(
                "create table %s (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s float not null," +
                        "%s," +
                        "%s," +
                        "%s" +
                        ");",
                tb_notas, col_notas_id, col_notas_idaluno, col_notas_idevento, col_notas_nota, const_notas_fktb_notastb_alunos,
                const_notas_fktb_notastb_eventos, const_notas_unqidalunoidevento);


        try {
            db.execSQL(create_tb_alunos);
            db.execSQL(create_tb_cadeiras);
            db.execSQL(create_tb_matriculas);
            db.execSQL(create_tb_aulas);
            db.execSQL(create_tb_aulasfrequentadas);
            db.execSQL(create_tb_eventos);
            db.execSQL(create_tb_notas);
            debug_insertTestingValues(db);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "onUpgrade() called");
        db.execSQL("drop table if exists " + tb_notas);
        db.execSQL("drop table if exists " + tb_eventos);
        db.execSQL("drop table if exists " + tb_aulasfrequentadas);
        db.execSQL("drop table if exists " + tb_aulas);
        db.execSQL("drop table if exists " + tb_matriculas);
        db.execSQL("drop table if exists " + tb_cadeiras);
        db.execSQL("drop table if exists " + tb_alunos);

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    // aluno
    public int createAlunos(Aluno aluno) {
        ContentValues cv = new ContentValues();
        cv.put(col_alunos_username, aluno.getUsername());
        cv.put(col_alunos_nome, aluno.getNome());
        cv.put(col_alunos_email, aluno.getEmail());

        return (int) db.insert(tb_alunos, null, cv);
    }

    public Aluno readAlunos(String user) {
        Aluno aluno = null;
        String query = "select * from tb_alunos where username = '" + user + "';";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() <= 0) {
            Log.d("debug readAlunos", "null");
            return null;
        }

        while(c.moveToNext()) {
            int idaluno = c.getInt(c.getColumnIndex("_id"));
            String username = c.getString(c.getColumnIndex("username"));
            String nome = c.getString(c.getColumnIndex("nome"));
            String email = c.getString(c.getColumnIndex("email"));

            aluno = new Aluno(idaluno, username, nome, email);
        }
        c.close();
        return aluno;
    }

    public ArrayList<Aluno> readAlunos() {
        ArrayList<Aluno> alunos = new ArrayList<>();

        String query = "select * from tb_alunos;";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readAlunos", "null");
            return null;
        }

        while(c.moveToNext()) {
            int idaluno = c.getInt(c.getColumnIndex("_id"));
            String username = c.getString(c.getColumnIndex("username"));
            String nome = c.getString(c.getColumnIndex("nome"));
            String email = c.getString(c.getColumnIndex("email"));

            Aluno aluno = new Aluno(idaluno, username, nome, email);
            alunos.add(aluno);
        }
        c.close();
        return alunos;
    }

    // aulas
    public int createAulas(Aula aula) {
        ContentValues cv = new ContentValues();
        cv.put(col_aulas_idcadeira, aula.getCadeira().getIdcadeira());
        cv.put(col_aulas_diasemana, aula.getDiaSemana());
        cv.put(col_aulas_horaentrada, aula.getHoraentrada());
        cv.put(col_aulas_sala, aula.getSala());

        return (int) db.insert(tb_aulas, null, cv);
    }



    // aulasfreq
    public int createAulasFrequentadas(Aluno aluno, Aula aula) {
        ContentValues cv = new ContentValues();
        cv.put(col_aulasfrequentadas_idaluno, aluno.getIdaluno());
        cv.put(col_aulasfrequentadas_idaula, aula.getIdaula());

        return (int) db.insert(tb_aulasfrequentadas, null, cv);
    }

    public ArrayList<Aula> readAulasFrequentadas(Aluno aluno) {
        ArrayList<Aula> aulasfreq = new ArrayList<>();
        String query = "select " +
                "tb_cadeiras.nome, " +
                "tb_cadeiras.abbr, " +
                "tb_aulas.diasemana, " +
                "tb_aulas.horaentrada, " +
                "tb_aulas.sala " +
                "from tb_aulasfrequentadas " +
                "inner join tb_aulas on tb_aulas._id = tb_aulasfrequentadas.idaula " +
                "inner join tb_alunos on tb_alunos._id = tb_aulasfrequentadas.idaluno " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_aulas.idcadeira " +
                "where tb_alunos._id = " + aluno.getIdaluno() + ";";


        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readAulasFreq", "null");
            return null;
        }

        while(c.moveToNext()) {
            String nome = c.getString(c.getColumnIndex("nome"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            int diasemana = c.getInt(c.getColumnIndex("diasemana"));
            int horaentrada = c.getInt(c.getColumnIndex("horaentrada"));
            String sala = c.getString(c.getColumnIndex("sala"));

            Cadeira cadeira = new Cadeira(nome, abbr);
            Aula aula = new Aula(cadeira, diasemana, horaentrada, sala);
            aulasfreq.add(aula);
        }
        c.close();
        return aulasfreq;
    }

    // cadeiras
    public int createCadeiras(Cadeira cadeira) {
        ContentValues cv = new ContentValues();
        cv.put(col_cadeiras_nome ,cadeira.getName());
        cv.put(col_cadeiras_abbr ,cadeira.getAbbr());
        cv.put(col_cadeiras_creditos ,cadeira.getCreditos());

        return (int)db.insert(tb_cadeiras, null, cv);
    }

    public ArrayList<Cadeira> readCadeiras() {
        ArrayList<Cadeira> cadeiras = new ArrayList<>();
        String query = "select * from tb_cadeiras;";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readCadeiras", "null");
            return null;
        }

        while(c.moveToNext()) {
            int idcadeira = c.getInt(c.getColumnIndex("_id"));
            String nome = c.getString(c.getColumnIndex("nome"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            int creditos = c.getInt(c.getColumnIndex("creditos"));

            Cadeira cadeira = new Cadeira(idcadeira, nome, abbr, creditos);
            cadeiras.add(cadeira);
        }
        c.close();
        return cadeiras;
    }

    public Cadeira readCadeiras(String abbr) {
        Cadeira cadeira = null;
        String query = "select * from tb_cadeiras where abbr = '" + abbr + "';";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readCadeiras", "null");
            return null;
        }

        while(c.moveToNext()) {
            int idcadeira = c.getInt(c.getColumnIndex("_id"));
            String nome = c.getString(c.getColumnIndex("nome"));
            int creditos = c.getInt(c.getColumnIndex("creditos"));

            cadeira = new Cadeira(idcadeira, nome, abbr, creditos);
        }
        c.close();
        return cadeira;
    }

    // tb_eventos
    public int createEventos(Evento evento) {
        ContentValues cv = new ContentValues();
        cv.put(col_eventos_idcadeira, evento.getCadeira().getIdcadeira());
        cv.put(col_eventos_tipo, evento.getTipo());
        cv.put(col_eventos_datahora, evento.getDatahora());
        cv.put(col_eventos_sala, evento.getSala());
        cv.put(col_eventos_descricao, evento.getDescricao());

        return (int) db.insert(tb_eventos, null, cv);
    }

    public void updateEventos(Evento evento) {
        ContentValues cv = new ContentValues();
        cv.put(col_eventos_idcadeira, evento.getCadeira().getIdcadeira());
        cv.put(col_eventos_tipo, evento.getTipo());
        cv.put(col_eventos_datahora, evento.getDatahora());
        cv.put(col_eventos_sala, evento.getSala());
        cv.put(col_eventos_descricao, evento.getDescricao());

        db.update(tb_eventos, cv, col_eventos_id + "=" + evento.getIdevento(), null);
    }

    public void deleteEventos(int idevento) {
        db.delete(tb_eventos, col_eventos_id + "=" + idevento, null);
    }

    public ArrayList<Evento> readEventos(Aluno aluno) {
        ArrayList<Evento> eventos = new ArrayList<>();
        String query = "select " +
                "tb_eventos._id as idevento, " +
                "tb_eventos.tipo, " +
                "tb_eventos.datahora, " +
                "tb_eventos.descricao, " +
                "tb_eventos.sala, " +
                "tb_eventos.idcadeira, " +
                "tb_cadeiras.abbr, " +
                "tb_cadeiras.nome, " +
                "tb_cadeiras.creditos " +
                "from tb_eventos " +
                "inner join tb_matriculas on tb_matriculas.idcadeira = tb_eventos.idcadeira " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_eventos.idcadeira " +
                "where tb_matriculas.idaluno = " + aluno.getIdaluno() + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readEventos", "null");
            return null;
        }

        while(c.moveToNext()) {
            int idevento = c.getInt(c.getColumnIndex("idevento"));
            String tipo = c.getString(c.getColumnIndex("tipo"));
            String datahora = c.getString(c.getColumnIndex("datahora"));
            int idcadeira = c.getInt(c.getColumnIndex("idcadeira"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            String nome = c.getString(c.getColumnIndex("nome"));
            int creditos = c.getInt(c.getColumnIndex("creditos"));
            String sala = c.getString(c.getColumnIndex("sala"));
            String descricao = c.getString(c.getColumnIndex("descricao"));

            Cadeira cadeira = new Cadeira(idcadeira, nome, abbr, creditos);
            Evento evento = new Evento(idevento, cadeira, tipo, datahora, descricao, sala);
            eventos.add(evento);
        }
        c.close();
        return eventos;
    }



    // tb_notas
    public int createNotas(Nota nota) {
        ContentValues cv = new ContentValues();
        cv.put(col_notas_idaluno, nota.getAluno().getIdaluno());
        cv.put(col_notas_idevento, nota.getEvento().getIdevento());
        cv.put(col_notas_nota, nota.getNota());

        return (int) db.insert(tb_notas, null, cv);
    }

    public ArrayList<Nota> readNotas(Aluno aluno) {
        ArrayList<Nota> notas = new ArrayList<>();
        String query = "select " +
                "tb_notas._id as idnota, " +
                "tb_notas.idevento, " +
                "tb_notas.nota, " +
                "tb_eventos.datahora, " +
                "tb_eventos.descricao, " +
                "tb_eventos.tipo, " +
                "tb_eventos.sala, " +
                "tb_eventos.idcadeira, " +
                "tb_cadeiras.nome, " +
                "tb_cadeiras.abbr, " +
                "tb_cadeiras.creditos " +
                "from tb_notas " +
                "inner join tb_eventos on tb_eventos._id = tb_notas.idevento " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_eventos.idcadeira " +
                "where idaluno = " + aluno.getIdaluno() + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readNotas", "null");
            return null;
        }

        while(c.moveToNext()) {
            int idnota = c.getInt(c.getColumnIndex("idnota"));
            int idevento = c.getInt(c.getColumnIndex("idevento"));
            float notavalor = c.getFloat(c.getColumnIndex("nota"));
            String datahora = c.getString(c.getColumnIndex("datahora"));
            String descricao = c.getString(c.getColumnIndex("descricao"));
            String tipo = c.getString(c.getColumnIndex("tipo"));
            String sala = c.getString(c.getColumnIndex("sala"));
            int idcadeira = c.getInt(c.getColumnIndex("idcadeira"));
            int creditos = c.getInt(c.getColumnIndex("creditos"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            String nome = c.getString(c.getColumnIndex("nome"));

            Cadeira cadeira = new Cadeira(idcadeira, nome, abbr, creditos);
            Evento evento = new Evento(idevento, cadeira, tipo, datahora, descricao, sala);
            Nota nota = new Nota(idnota, aluno, evento, notavalor);
            notas.add(nota);
        }
        c.close();
        return notas;
    }

    // matriculas
    public int createMatriculas(Aluno aluno, Cadeira cadeira) {
        ContentValues cv = new ContentValues();
        cv.put(col_matriculas_idaluno, aluno.getIdaluno());
        cv.put(col_matriculas_idcadeira, cadeira.getIdcadeira());

        return (int) db.insert(tb_matriculas, null, cv);
    }






    /*
    // exames
    public int createExames(Exame exame) {
        ContentValues cv = new ContentValues();
        cv.put(col_exames_idcadeira, exame.getCadeira().getIdcadeira());
        cv.put(col_exames_datahora, exame.getDatahora());
        cv.put(col_exames_sala, exame.getSala());
        cv.put(col_exames_descricao, exame.getDescricao());

        return (int) db.insert(tb_exames, null, cv);
    }

    public ArrayList<Exame> readExames(int idaluno) {
        ArrayList<Exame> exames = new ArrayList<>();
        String query = "select " +
                "tb_exames.datahora, " +
                "tb_exames.descricao, " +
                "tb_exames.sala, " +
                "tb_cadeiras.abbr, " +
                "tb_cadeiras.nome " +
                "from tb_exames " +
                "inner join tb_matriculas on tb_matriculas.idcadeira = tb_exames.idcadeira " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_exames.idcadeira " +
                "where tb_matriculas.idaluno = " + idaluno + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readExames", "null");
            return null;
        }

        while(c.moveToNext()) {
            String datahora = c.getString(c.getColumnIndex("datahora"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            String nome = c.getString(c.getColumnIndex("nome"));
            String sala = c.getString(c.getColumnIndex("sala"));
            String descricao = c.getString(c.getColumnIndex("descricao"));

            Cadeira cadeira = new Cadeira(nome, abbr);
            Exame exame = new Exame(cadeira, datahora, sala, descricao);
            exames.add(exame);
        }
        c.close();
        return exames;
    }

    // matriculas
    public int createMatriculas(int idaluno, int idcadeira) {
        ContentValues cv = new ContentValues();
        cv.put(col_matriculas_idaluno, idaluno);
        cv.put(col_matriculas_idcadeira, idcadeira);

        return (int) db.insert(tb_matriculas, null, cv);
    }

    // notasexame
    public int createNotaExames(NotaExame nota) {
        ContentValues cv = new ContentValues();
        cv.put(col_notasexame_idaluno, nota.getIdaluno());
        cv.put(col_notasexame_idexame, nota.getExame().getIdexame());
        cv.put(col_notasexame_nota, nota.getNota());

        return (int) db.insert(tb_notasexame, null, cv);
    }

    public ArrayList<NotaExame> readNotaExames(int idaluno) {
        ArrayList<NotaExame> notas = new ArrayList<>();
        String query = "select " +
                "tb_notasexame.nota, " +
                "tb_cadeiras.nome, " +
                "tb_cadeiras.abbr " +
                "from tb_notasexame " +
                "inner join tb_exames on tb_exames._id = tb_notasexame.idexame " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_exames.idcadeira " +
                "where tb_notasexame.idaluno = " + idaluno + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readNotaExames", "null");
            return null;
        }

        while(c.moveToNext()) {
            String abbr = c.getString(c.getColumnIndex("abbr"));
            String nome = c.getString(c.getColumnIndex("nome"));
            float nota = c.getFloat(c.getColumnIndex("nota"));

            Cadeira cadeira = new Cadeira(nome, abbr);
            Exame exame = new Exame(cadeira);
            NotaExame notaExame = new NotaExame(idaluno, exame, nota);
            notas.add(notaExame);
        }
        c.close();
        return notas;
    }

    // notastrabalho
    public int createNotaTrabalhos(NotaTrabalho nota) {
        ContentValues cv = new ContentValues();
        cv.put(col_notastrabalho_idaluno, nota.getIdaluno());
        cv.put(col_notastrabalho_idtrabalho, nota.getTrabalho().getIdtrabalho());
        cv.put(col_notastrabalho_nota, nota.getNota());

        return (int) db.insert(tb_notasexame, null, cv);
    }

    public ArrayList<NotaTrabalho> readNotaTrabalhos(int idaluno) {
        ArrayList<NotaTrabalho> notas = new ArrayList<>();
        String query = "select " +
                "tb_notastrabalho.nota, " +
                "tb_cadeiras.nome, " +
                "tb_cadeiras.abbr " +
                "from tb_notastrabalho " +
                "inner join tb_trabalhos on tb_trabalhos._id = tb_notastrabalho.idtrabalho " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_trabalhos.idcadeira " +
                "where tb_notastrabalho.idaluno = " + idaluno + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readNotaTrabalhos", "null");
            return null;
        }

        while(c.moveToNext()) {
            String abbr = c.getString(c.getColumnIndex("abbr"));
            String nome = c.getString(c.getColumnIndex("nome"));
            float nota = c.getFloat(c.getColumnIndex("nota"));

            Cadeira cadeira = new Cadeira(nome, abbr);
            Trabalho trabalho = new Trabalho(cadeira);
            NotaTrabalho notaTrabalho = new NotaTrabalho(idaluno, trabalho, nota);
            notas.add(notaTrabalho);
        }
        c.close();
        return notas;
    }

    // trabalho
    public int createTrabalhos(Trabalho trabalho) {
        ContentValues cv = new ContentValues();
        cv.put(col_trabalhos_idcadeira, trabalho.getCadeira().getIdcadeira());
        cv.put(col_trabalhos_dataentrega, trabalho.getDatahora());
        cv.put(col_trabalhos_descricao, trabalho.getDescricao());

        return (int) db.insert(tb_trabalhos, null, cv);
    }

    public ArrayList<Trabalho> readTrabalhos(int idaluno) {
        ArrayList<Trabalho> trabalhos = new ArrayList<>();
        String query = "select " +
                "tb_trabalhos.dataentrega, " +
                "tb_trabalhos.descricao, " +
                "tb_cadeiras.abbr, " +
                "tb_cadeiras.nome " +
                "from tb_trabalhos " +
                "inner join tb_matriculas on tb_matriculas.idcadeira = tb_trabalhos.idcadeira " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_trabalhos.idcadeira " +
                "where tb_matriculas.idaluno = " + idaluno + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readTrabalhos", "null");
            return null;
        }

        while(c.moveToNext()) {
            String dataentrega = c.getString(c.getColumnIndex("dataentrega"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            String nome = c.getString(c.getColumnIndex("nome"));
            String descricao = c.getString(c.getColumnIndex("descricao"));

            Cadeira cadeira = new Cadeira(nome, abbr);
            Trabalho trabalho = new Trabalho(cadeira, dataentrega, descricao);
            trabalhos.add(trabalho);
        }
        c.close();
        return trabalhos;
    }
*/











    // ******************************************
    //                   DEBUG
    // ******************************************
    public void debug_printTable(String... tables) {
        for(String table : tables) {
            Cursor cursor = db.rawQuery("select * from " + table, null);
            try {
                while(cursor.moveToNext()) {
                    String row = "";
                    for(int i = 0; i < cursor.getColumnCount(); i++) {
                        if(cursor.getType(i) == Cursor.FIELD_TYPE_FLOAT) {
                            //Log.d(cursor.getColumnName(i), String.valueOf(cursor.getFloat(i)));
                            row += cursor.getColumnName(i) + ": " + String.valueOf(cursor.getFloat(i));
                        } else if(cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER){
                            //Log.d(cursor.getColumnName(i), String.valueOf(cursor.getInt(i)));
                            row += cursor.getColumnName(i) + ": " + String.valueOf(cursor.getInt(i));
                        } else {
                            //Log.d(cursor.getColumnName(i), cursor.getString(i));
                            row += cursor.getColumnName(i) + ": " + cursor.getString(i);
                        }
                        row += " | ";
                    }
                    Log.d("Debug", row);
                }
            } catch (Exception e) {
                Log.e("SQL", e.toString());
            } finally {
                cursor.close();
            }
        }
    }

    public void debug_insertTestingValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        /*
        String[] exercises = {
                "Barbell Bench Press", "Hammer Strength Pulldowns", "Inclined Dumbbell Bench Press", "One-Arm Dumbbell Row",
                "Shoulder Press", "Side Lateral Raise", "Rubber Band Spread", "Dumbbell Bicep Curl",
                "Tricep Pushdown", "Squat", "Leg Press"
        };

        String[] workouts = {"Push", "Pull", "Legs"};

        // idexercise idworkout sets reps weight
        float[][] planning = {
                {1,1,5,5,17.5f}, {3,1,3,8,20}, {5,1,3,8,17.5f}, {6,1,3,8,7}, {9,1,3,8,14},
                {2,2,4,8,30}, {4,2,3,8,20}, {6,2,3,8,7}, {7,2,3,8,0}, {8,2,3,8,10},
                {10,3,5,8,15}, {11,3,5,8,15}
        };

        // idworkout isCompleted
        int[][] diaryworkouts = {
                {1,0}
        };

        // idexercise iddiaryworkout reps weight
        float[][] diaryexercises = {
                {1,1,5,17.5f,1}, {1,1,5,15,1}, {3,1,8,20,1}, {3,1,5,20,1}
        };

        for(String exercise : exercises) {
            cv.clear();
            cv.put(col_exercises_name, exercise);
            db.insert(tb_exercises, null, cv);
        }

        for(String workout : workouts) {
            cv.clear();
            cv.put(col_workouts_name, workout);
            db.insert(tb_workouts, null, cv);
        }

        for(float[] plan : planning) {
            cv.clear();
            cv.put(col_planning_idexercise, (int)plan[0]);
            cv.put(col_planning_idworkout, (int)plan[1]);
            cv.put(col_planning_sets, (int)plan[2]);
            cv.put(col_planning_reps, (int)plan[3]);
            cv.put(col_planning_weight, plan[4]);
            db.insert(tb_planning, null, cv);
        }

        for(int[] diaryworkout : diaryworkouts) {
            cv.clear();
            cv.put(col_diaryworkouts_idworkout, diaryworkout[0]);
            cv.put(col_diaryworkouts_isCompleted, diaryworkout[1]);
            db.insert(tb_diaryworkouts, null, cv);
        }

        for(float[] diaryexercise : diaryexercises) {
            cv.clear();
            cv.put(col_diaryexercises_idexercise, (int)diaryexercise[0]);
            cv.put(col_diaryexercises_iddiaryworkout, (int)diaryexercise[1]);
            cv.put(col_diaryexercises_reps, (int)diaryexercise[2]);
            cv.put(col_diaryexercises_weight, diaryexercise[3]);
            db.insert(tb_diaryexercises, null, cv);
        }*/
    }

}

