package br.com.dobar.daos.controls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.dobar.daos.Dao;
import br.com.dobar.daos.DatabaseHelper.DBHCartao;
import br.com.dobar.daos.DatabaseHelper.DBHCredor;
import br.com.dobar.daos.DatabaseHelper.DBHDebito;
import br.com.dobar.daos.DatabaseHelper.DBHDevedor;
import br.com.dobar.daos.DatabaseHelper.DBHParcela;
import br.com.dobar.daos.DatabaseHelper.DBHTipo;
import br.com.dobar.daos.entities.Cartao;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.daos.entities.Debito;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Parcela;
import br.com.dobar.daos.entities.Tipo;
import br.com.dobar.utils.Constantes;

public class ParcelaControl extends Dao {

    /**
     * @param context
     */
    public ParcelaControl(Context context) {
        super(context);
    }

    /**
     * @param parcela
     * @return
     */
    public Long create(Parcela parcela) {
        ContentValues values = new ContentValues();

        values.put(DBHParcela.DATAPAGAMENTO, parcela.getDataPagamento());
        values.put(DBHParcela.DATAVENCIMENTO, parcela.getDataVencimento());
        values.put(DBHParcela.IDDEBITO, parcela.getDebito().getIddebito());
        values.put(DBHParcela.IDDEVEDOR, parcela.getDevedor().getIdDevedor());
        values.put(DBHParcela.NRPARCELA, parcela.getNrParcela());
        values.put(DBHParcela.PAGO, parcela.getPago());
        values.put(DBHParcela.VLPARCELA, parcela.getVlParcela().toString());

        Long result = getDb().insert(DBHParcela.TABELA, null, values);
        close();
        return result;
    }

    /**
     * @param parcela
     * @return
     */
    public int update(Parcela parcela) {
        ContentValues values = new ContentValues();

        values.put(DBHParcela.DATAPAGAMENTO, parcela.getDataPagamento());
        values.put(DBHParcela.DATAVENCIMENTO, parcela.getDataVencimento());
        values.put(DBHParcela.IDDEBITO, parcela.getDebito().getIddebito());
        values.put(DBHParcela.IDDEVEDOR, parcela.getDevedor().getIdDevedor());
        values.put(DBHParcela.NRPARCELA, parcela.getNrParcela());
        values.put(DBHParcela.PAGO, parcela.getPago());
        values.put(DBHParcela.VLPARCELA, parcela.getVlParcela().toString());

        int result = getDb().update(DBHParcela.TABELA, values, "_id = ?",
                new String[]{String.valueOf(parcela.getIdparcela())});
        close();
        return result;
    }

    /**
     * @param id
     * @return
     */
    public Integer delete(Long id) {
        String whereArgs[] = new String[]{id.toString()};
        int result = getDb().delete(DBHParcela.TABELA, "_id = ?", whereArgs);
        close();
        return result;
    }

    /**
     * @param iddebito
     * @return
     */
    public List<Parcela> getListByIddebito(Long iddebito) {
        Cursor cursor = getDb().rawQuery(
                "select * from tbparcela where iddebito = ?",
                new String[]{String.valueOf(iddebito)});
        List<Parcela> list = new ArrayList<Parcela>();
        while (cursor.moveToNext()) {
            Parcela parcela = new Parcela();
            parcela.setDataPagamento(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.DATAPAGAMENTO)));
            parcela.setDataVencimento(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.DATAVENCIMENTO)));
            parcela.setDebito(new Debito());
            parcela.getDebito().setIddebito(
                    cursor.getLong(cursor.getColumnIndex(DBHParcela.IDDEBITO)));
            parcela.setDevedor(new Devedor());
            parcela.getDevedor()
                    .setIdDevedor(
                            cursor.getLong(cursor
                                    .getColumnIndex(DBHParcela.IDDEVEDOR)));
            parcela.setIdparcela(cursor.getLong(cursor
                    .getColumnIndex(DBHParcela._ID)));
            parcela.setNrParcela(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.NRPARCELA)));
            parcela.setPago(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.PAGO)));
            parcela.setVlParcela(new BigDecimal(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.VLPARCELA))));

            list.add(parcela);
        }
        cursor.close();
        close();
        return list;
    }

    /**
     * @param strWhere
     * @return
     */
    public List<Parcela> getListCustom(String strWhere) {
        String strSql = "select "
                + "parc._id, "
                + "parc.vlparcela , "
                + "parc.nrparcela, "
                + "parc.datavencimento, "
                + "parc.pago, "
                + "parc.datapagamento, "
                + "parc.iddebito, "
                + "parc.iddevedor, "
                +

                "deb.datadebito, "
                + "deb.formpag, "
                + "deb.sincro, "
                + "deb.vldebito, "
                + "deb.descricao, "
                + "deb.ordem, "
                + "deb.idcartao, "
                + "deb.idcredor, "
                + "deb.idtipo, "
                +

                "cre.razao, "
                + "cre.telefone, "
                + "cre.ativo, "
                + "cre.email, "
                +

                "dev.devnome, "
                + "dev.dvtelefone, "
                + "dev.dvemail, "
                + "dev.dvativo, "
                +

                "tip.tipo "

                + "from tbparcela parc inner join tbdebito deb on(parc.iddebito = deb._id) "
                + "inner join tbdevedor dev on(parc.iddevedor = dev._id) "
                + "inner join tbcredor cre on(deb.idcredor = cre._id) "
                + "inner join tbtipo tip on(deb.idtipo = tip._id) ";

        if (strWhere.length() > 0) {
            strWhere = "where " + strWhere;
        }
        strWhere += "order by parc._id";
        strSql += strWhere;

        Cursor cursor = getDb().rawQuery(strSql, null);

        List<Parcela> list = new ArrayList<Parcela>();
        while (cursor.moveToNext()) {

            Parcela parcela = new Parcela();

            parcela.setIdparcela(cursor.getLong(cursor
                    .getColumnIndex(DBHParcela._ID)));
            parcela.setNrParcela(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.NRPARCELA)));
            parcela.setPago(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.PAGO)));
            parcela.setVlParcela(new BigDecimal(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.VLPARCELA))));
            parcela.setDataPagamento(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.DATAPAGAMENTO)));
            parcela.setDataVencimento(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.DATAVENCIMENTO)));
            // tbdevedor
            parcela.setDevedor(new Devedor());
            parcela.getDevedor().setIdDevedor(cursor.getLong(cursor.getColumnIndex(DBHParcela.IDDEVEDOR)));
            parcela.getDevedor().setNome(cursor.getString(cursor.getColumnIndex(DBHDevedor.NOME)));
            parcela.getDevedor().setEmail(cursor.getString(cursor.getColumnIndex(DBHDevedor.EMAIL)));
            parcela.getDevedor().setTelefone(cursor.getString(cursor.getColumnIndex(DBHDevedor.TELEFONE)));
            parcela.getDevedor().setAtivo(cursor.getString(cursor.getColumnIndex(DBHDevedor.ATIVO)));
            // tbdebito
            parcela.setDebito(new Debito());
            parcela.getDebito().setIddebito(
                    cursor.getLong(cursor.getColumnIndex(DBHParcela.IDDEBITO)));
            parcela.getDebito().setFormPagamento(
                    cursor.getInt(cursor.getColumnIndex(DBHDebito.FORMPAG)));
            parcela.getDebito().setDataDebito(
                    cursor.getString(cursor
                            .getColumnIndex(DBHDebito.DATADEBITO)));
            parcela.getDebito()
                    .setDescricao(
                            cursor.getString(cursor
                                    .getColumnIndex(DBHDebito.DESCRICAO)));
            parcela.getDebito().setOrdem(
                    cursor.getInt(cursor.getColumnIndex(DBHDebito.ORDEM)));
            parcela.getDebito().setVlDebito(
                    new BigDecimal(cursor.getString(cursor
                            .getColumnIndex(DBHDebito.VLDEBITO))));
            // tbcredor
            parcela.getDebito().setCredor(new Credor());
            parcela.getDebito().getCredor().setIdCredor(cursor.getLong(cursor.getColumnIndex(DBHDebito.IDCREDOR)));
            parcela.getDebito().getCredor().setRazao(cursor.getString(cursor.getColumnIndex(DBHCredor.RAZAO)));
            parcela.getDebito().getCredor().setTelefone(cursor.getString(cursor.getColumnIndex(DBHCredor.TELEFONE)));
            parcela.getDebito().getCredor().setEmail(cursor.getString(cursor.getColumnIndex(DBHCredor.EMAIL)));
            parcela.getDebito().getCredor().setAtivo(cursor.getString(cursor.getColumnIndex(DBHCredor.ATIVO)));
            // tbtipo
            parcela.getDebito().setTipo(new Tipo());
            parcela.getDebito()
                    .getTipo()
                    .setIdTipo(
                            cursor.getLong(cursor
                                    .getColumnIndex(DBHDebito.IDTIPO)));
            parcela.getDebito()
                    .getTipo()
                    .setTipo(
                            cursor.getString(cursor
                                    .getColumnIndex(DBHTipo.TIPO)));

            // verifica se o debito foi feito no cartao
            if (parcela.getDebito().getFormPagamento() == Constantes.CARTAO) {

                parcela.getDebito().setCartao(new Cartao());
                parcela.getDebito()
                        .getCartao()
                        .setIdcartao(
                                cursor.getLong(cursor
                                        .getColumnIndex(DBHDebito.IDCARTAO)));

                Cursor cursorTemp = getDb().rawQuery(
                        "select * from tbcartao where _id = ?",
                        new String[]{String.valueOf(parcela.getDebito()
                                .getCartao().getIdcartao())});

                while (cursorTemp.moveToNext()) {
                    parcela.getDebito()
                            .getCartao()
                            .setOperadora(
                                    cursorTemp.getString(cursorTemp
                                            .getColumnIndex(DBHCartao.OPERADORA)));
                    parcela.getDebito()
                            .getCartao()
                            .setTelefone(
                                    cursorTemp.getString(cursorTemp
                                            .getColumnIndex(DBHCartao.TELEFONE)));
                    parcela.getDebito()
                            .getCartao()
                            .setBandeira(
                                    cursorTemp.getString(cursorTemp
                                            .getColumnIndex(DBHCartao.BANDEIRA)));
                    parcela.getDebito()
                            .getCartao()
                            .setDiaFechamento(
                                    cursorTemp.getInt(cursorTemp
                                            .getColumnIndex(DBHCartao.FECHAMENTO)));
                    parcela.getDebito()
                            .getCartao()
                            .setDiaVencimento(
                                    cursorTemp.getInt(cursorTemp
                                            .getColumnIndex(DBHCartao.VENCIMENTO)));
                    parcela.getDebito()
                            .getCartao()
                            .setLimite(
                                    cursorTemp.getInt(cursorTemp
                                            .getColumnIndex(DBHCartao.LIMITE)));
                    parcela.getDebito()
                            .getCartao()
                            .setEmUso(
                                    cursorTemp.getString(cursorTemp
                                            .getColumnIndex(DBHCartao.EMUSO)));
                }
                cursorTemp.close();
            }
            list.add(parcela);
        }
        cursor.close();
        close();
        return list;
    }

    /**
     * @param sql
     */
    public void executaSQL(String sql) {
        getDb().execSQL(sql);
        close();
    }

    /**
     * @param idparcela
     * @return
     */
    public Parcela getByIdparcela(Long idparcela) {
        Cursor cursor = getDb()
                .rawQuery(
                        "select p.*, d.devnome from tbparcela p "
                                + "inner join tbdevedor d on(p.iddevedor = d._id)where p._id = ?",
                        new String[]{String.valueOf(idparcela)});
        Parcela parcela = new Parcela();
        while (cursor.moveToNext()) {

            parcela.setDataPagamento(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.DATAPAGAMENTO)));
            parcela.setDataVencimento(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.DATAVENCIMENTO)));
            parcela.setDebito(new Debito());
            parcela.getDebito().setIddebito(
                    cursor.getLong(cursor.getColumnIndex(DBHParcela.IDDEBITO)));
            parcela.setDevedor(new Devedor());
            parcela.getDevedor()
                    .setIdDevedor(
                            cursor.getLong(cursor
                                    .getColumnIndex(DBHParcela.IDDEVEDOR)));
            parcela.getDevedor().setNome(
                    cursor.getString(cursor.getColumnIndex(DBHDevedor.NOME)));
            parcela.setIdparcela(cursor.getLong(cursor
                    .getColumnIndex(DBHParcela._ID)));
            parcela.setNrParcela(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.NRPARCELA)));
            parcela.setPago(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.PAGO)));
            parcela.setVlParcela(new BigDecimal(cursor.getString(cursor
                    .getColumnIndex(DBHParcela.VLPARCELA))));

        }
        cursor.close();
        close();
        return parcela;
    }

    /**
     * @param idDevedor
     * @return
     */
    public BigDecimal getTotalByDevedor(Long idDevedor,
                                        java.sql.Date dataPagamento) {
        Cursor cursor = getDb()
                .rawQuery(
                        "select sum(vlparcela) emaberto from tbparcela where iddevedor = ? and datapagamento = ?",
                        new String[]{String.valueOf(idDevedor),
                                dataPagamento.toString()});
        BigDecimal emAberto = null;
        while (cursor.moveToNext()) {
            emAberto = new BigDecimal(cursor.getString(cursor
                    .getColumnIndex("emaberto")));
        }
        cursor.close();
        close();
        return emAberto;
    }

    /**
     * @param iddebito
     * @param nrparcela
     * @return
     */
    public Parcela getParcelaByIddebitoNrparcela(Long iddebito, String nrparcela) {
        Cursor cursor = getDb().rawQuery(
                "select * from tbparcela where iddebito = ? and nrparcela = ?",
                new String[]{String.valueOf(iddebito), nrparcela});
        Parcela parcela = null;
        while (cursor.moveToNext()) {
            parcela = new Parcela();
            parcela.setDataPagamento(cursor.getString(cursor.getColumnIndex(DBHParcela.DATAPAGAMENTO)));
            parcela.setDataVencimento(cursor.getString(cursor.getColumnIndex(DBHParcela.DATAVENCIMENTO)));
            parcela.setIdparcela(cursor.getLong(cursor.getColumnIndex(DBHParcela._ID)));
            parcela.setNrParcela(cursor.getString(cursor.getColumnIndex(DBHParcela.NRPARCELA)));
            parcela.setPago(cursor.getString(cursor.getColumnIndex(DBHParcela.PAGO)));
            parcela.setVlParcela(new BigDecimal(cursor.getString(cursor.getColumnIndex(DBHParcela.VLPARCELA))));

        }
        cursor.close();
        close();
        return parcela;
    }

}
