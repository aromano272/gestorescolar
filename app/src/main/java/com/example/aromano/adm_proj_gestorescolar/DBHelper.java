package com.example.aromano.adm_proj_gestorescolar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.CalendarView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by aRomano on 27/05/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    private static SQLiteDatabase db;

    private static final String DATABASE_NAME = "gestorescolar.db";
    private static final int DATABASE_VERSION = 1;

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
    public static final String col_alunos_apelido = "apelido";
    public static final String col_alunos_datanasc = "datanasc";

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
    public static final String col_aulas_horasaida = "horasaida";
    public static final String col_aulas_sala = "sala";
    public static final String const_aulas_fktb_aulastb_cadeiras = String.format(
            "constraint fktb_aulastb_cadeiras foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_aulas_idcadeira, tb_cadeiras, col_cadeiras_id);

    public static final String tb_aulasfrequentadas = "tb_aulasfrequentadas";
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

    public static final String tb_exames = "tb_exames";
    public static final String col_exames_id = "_id";
    public static final String col_exames_idcadeira = "idcadeira";
    public static final String col_exames_datahora = "datahora";
    public static final String col_exames_sala = "sala";
    public static final String col_exames_descricao = "descricao";
    public static final String const_exames_fktb_examestb_cadeiras = String.format(
            "constraint fktb_examestb_cadeiras foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_exames_idcadeira, tb_cadeiras, col_cadeiras_id);

    public static final String tb_notasexame = "tb_notasexame";
    public static final String col_notasexame_id = "_id";
    public static final String col_notasexame_idaluno = "idaluno";
    public static final String col_notasexame_idexame = "idexame";
    public static final String col_notasexame_nota = "nota";
    public static final String const_notasexame_fktb_notasexametb_alunos = String.format(
            "constraint fktb_notasexametb_alunos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_notasexame_idaluno, tb_alunos, col_alunos_id);
    public static final String const_notasexame_fktb_notasexametb_exames = String.format(
            "constraint fktb_notasexametb_exames foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_notasexame_idexame, tb_exames, col_exames_id);
    public static final String const_notasexame_unqidalunoidexame = String.format(
            "constraint unqidalunoidexame unique (%s,%s)",
            col_notasexame_idaluno, col_notasexame_idexame);

    public static final String tb_trabalhos = "tb_trabalhos";
    public static final String col_trabalhos_id = "_id";
    public static final String col_trabalhos_idcadeira = "idcadeira";
    public static final String col_trabalhos_dataentrega = "dataentrega";
    public static final String col_trabalhos_descricao = "descricao";
    public static final String const_trabalhos_fktb_trabalhostb_cadeiras = String.format(
            "constraint fktb_trabalhostb_cadeiras foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_trabalhos_idcadeira, tb_cadeiras, col_cadeiras_id);

    public static final String tb_notastrabalho = "tb_notastrabalho";
    public static final String col_notastrabalho_id = "_id";
    public static final String col_notastrabalho_idaluno = "idaluno";
    public static final String col_notastrabalho_idtrabalho = "idtrabalho";
    public static final String col_notastrabalho_nota = "nota";
    public static final String const_notastrabalho_fktb_notastrabalhotb_alunos = String.format(
            "constraint fktb_notastrabalhotb_alunos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_notastrabalho_idaluno, tb_alunos, col_alunos_id);
    public static final String const_notastrabalho_fktb_notastrabalhotb_trabalhos = String.format(
            "constraint fktb_notastrabalhotb_trabalhos foreign key (%s) references %s (%s) on update cascade on delete cascade",
            col_notastrabalho_idtrabalho, tb_trabalhos, col_trabalhos_id);
    public static final String const_notastrabalho_unqidalunoidtrabalho = String.format(
            "constraint unqidalunoidtrabalho unique (%s,%s)",
            col_notastrabalho_idaluno, col_notastrabalho_idtrabalho);


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_tb_alunos = String.format(
                "create table tb_alunos (" +
                        "%s integer primary key," +
                        "%s varchar(20) not null unique," +
                        "%s varchar(20)," +
                        "%s varchar(20)," +
                        "%s date not null" +
                        ");",
                tb_alunos, col_alunos_id, col_alunos_username, col_alunos_nome, col_alunos_apelido, col_alunos_datanasc);
        String create_tb_cadeiras = String.format(
                "create table tb_cadeiras (" +
                        "%s integer primary key," +
                        "%s varchar(20) not null unique," +
                        "%s varchar(6) not null unique," +
                        "%s int" +
                        ");",
                tb_cadeiras, col_cadeiras_id, col_cadeiras_nome, col_cadeiras_abbr, col_cadeiras_creditos);
        String create_tb_matriculas = String.format(
                "create table tb_matriculas (" +
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
                "create table tb_aulas (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null check("+ col_aulas_diasemana + " between 0 and 6)," +
                        "%s varchar(20) not null," +
                        "%s varchar(20) not null," +
                        "%s varchar(20) not null," +
                        "%s" +
                        ");",
                tb_aulas, col_aulas_id, col_aulas_idcadeira, col_aulas_diasemana, col_aulas_horaentrada, col_aulas_horasaida, col_aulas_sala,
                const_aulas_fktb_aulastb_cadeiras);
        String create_tb_aulasfrequentadas = String.format(
                "create table tb_aulasfrequentadas (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s," +
                        "%s," +
                        "%s" +
                        ");",
                tb_aulasfrequentadas, col_aulasfrequentadas_idaluno, col_aulasfrequentadas_idaula, const_aulasfrequentadas_fktb_aulasfrequentadastb_alunos,
                const_aulasfrequentadas_fktb_aulasfrequentadastb_aulas, const_aulasfrequentadas_unqidalunoidaula);
        String create_tb_exames = String.format(
                "create table tb_exames (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s datetime not null," +
                        "%s varchar(20) not null," +
                        "%s varchar(max)," +
                        "%s" +
                        ");",
                tb_exames, col_exames_id, col_exames_idcadeira, col_exames_datahora, col_exames_sala, col_exames_descricao,
                const_exames_fktb_examestb_cadeiras);
        String create_tb_notasexame = String.format(
                "create table tb_notasexame (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s float not null," +
                        "%s," +
                        "%s," +
                        "%s" +
                        ");",
                tb_notasexame, col_notasexame_id, col_notasexame_idaluno, col_notasexame_idexame, col_notasexame_nota, const_notasexame_fktb_notasexametb_alunos,
                const_notasexame_fktb_notasexametb_exames, const_notasexame_unqidalunoidexame);
        String create_tb_trabalhos = String.format(
                "create table tb_trabalhos (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s datetime not null," +
                        "%s varchar(max)," +
                        "%s" +
                        ");",
                tb_trabalhos, col_trabalhos_id, col_trabalhos_idcadeira, col_trabalhos_dataentrega, col_trabalhos_descricao,
                const_trabalhos_fktb_trabalhostb_cadeiras);
        String create_tb_notastrabalho = String.format(
                "create table tb_notastrabalho (" +
                        "%s integer primary key," +
                        "%s int not null," +
                        "%s int not null," +
                        "%s float not null," +
                        "%s," +
                        "%s," +
                        "%s" +
                        ");",
                tb_notastrabalho, col_notastrabalho_id, col_notastrabalho_idaluno, col_notastrabalho_idtrabalho, col_notastrabalho_nota, const_notastrabalho_fktb_notastrabalhotb_alunos,
                const_notastrabalho_fktb_notastrabalhotb_trabalhos, const_notastrabalho_unqidalunoidtrabalho);

        try {
            db.execSQL(create_tb_alunos);
            db.execSQL(create_tb_cadeiras);
            db.execSQL(create_tb_matriculas);
            db.execSQL(create_tb_aulas);
            db.execSQL(create_tb_aulasfrequentadas);
            db.execSQL(create_tb_exames);
            db.execSQL(create_tb_notasexame);
            db.execSQL(create_tb_trabalhos);
            db.execSQL(create_tb_notastrabalho);
            debug_insertTestingValues(db);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "onUpgrade() called");
        db.execSQL("drop table if exists " + tb_alunos);
        db.execSQL("drop table if exists " + tb_cadeiras);
        db.execSQL("drop table if exists " + tb_matriculas);
        db.execSQL("drop table if exists " + tb_aulas);
        db.execSQL("drop table if exists " + tb_aulasfrequentadas);
        db.execSQL("drop table if exists " + tb_exames);
        db.execSQL("drop table if exists " + tb_notasexame);
        db.execSQL("drop table if exists " + tb_trabalhos);
        db.execSQL("drop table if exists " + tb_notastrabalho);

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
        cv.put(col_alunos_apelido, aluno.getApelido());
        cv.put(col_alunos_datanasc, aluno.getDatanasc());

        //SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        //fmt.setCalendar(aluno.getDatanasc());
        //String dateFormatted = fmt.format(aluno.getDatanasc());
        //cv.put(col_alunos_datanasc, dateFormatted);

        return (int) db.insert(tb_alunos, null, cv);
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
            String apelido = c.getString(c.getColumnIndex("apelido"));
            String datanasc = c.getString(c.getColumnIndex("datanasc"));
            //GregorianCalendar cal;
            //try {
            //    DateFormat df = new SimpleDateFormat("dd MM yyyy");
            //    Date date = df.parse(datanasc);
            //    cal = (GregorianCalendar) GregorianCalendar.getInstance();
            //    cal.setTime(date);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //    break;
            //}

            Aluno aluno = new Aluno(username, nome, apelido, datanasc);
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
        cv.put(col_aulas_horasaida, aula.getHorasaida());
        cv.put(col_aulas_sala, aula.getSala());

        return (int) db.insert(tb_aulas, null, cv);
    }



    // aulasfreq
    public int createAulasFrequentadas(int idaluno, Aula aula) {
        ContentValues cv = new ContentValues();
        cv.put(col_aulasfrequentadas_idaluno, idaluno);
        cv.put(col_aulasfrequentadas_idaula, aula.getIdaula());

        return (int) db.insert(tb_aulasfrequentadas, null, cv);
    }

    public ArrayList<Aula> readAulasFrequentadas(int idaluno) {
        ArrayList<Aula> aulasfreq = new ArrayList<>();
        String query = "select " +
                "tb_cadeiras.nome, " +
                "tb_cadeiras.abbr, " +
                "tb_aulas.diasemana, " +
                "tb_aulas.horaentrada, " +
                "tb_aulas.horasaida, " +
                "tb_aulas.sala " +
                "from tb_aulasfrequentadas " +
                "inner join tb_aulas on tb_aulas._id = tb_aulasfrequentadas.idaula " +
                "inner join tb_alunos on tb_alunos._id = tb_aulasfrequentadas.idaluno " +
                "inner join tb_cadeiras on tb_cadeiras._id = tb_aulas.idcadeira " +
                "where tb_alunos._id = " + idaluno + ";";

        Cursor c = db.rawQuery(query, null);

        if(c == null || c.getCount() == 0) {
            Log.d("debug readAulasFreq", "null");
            return null;
        }

        while(c.moveToNext()) {
            String nome = c.getString(c.getColumnIndex("nome"));
            String abbr = c.getString(c.getColumnIndex("abbr"));
            int diasemana = c.getInt(c.getColumnIndex("diasemana"));
            String horaentrada = c.getString(c.getColumnIndex("horaentrada"));
            String horasaida = c.getString(c.getColumnIndex("horasaida"));
            String sala = c.getString(c.getColumnIndex("sala"));

            Cadeira cadeira = new Cadeira(nome, abbr);
            Aula aula = new Aula(cadeira, diasemana, horaentrada, horasaida, sala);
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
        cv.put(col_trabalhos_dataentrega, trabalho.getDataentrega());
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

