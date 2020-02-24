package com.nexuscmarketing.bar_card.sql;

import android.util.Log;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.utils.ConfigUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHelper {

    static ResultSet resObj;
    static PreparedStatement stmtObj;
    static Connection connObj;

    private DbSpec spec = new DbSpec();
    private DbSchema schema = new DbSchema(spec, "bar_card");
    private FunctionCall encrypt_password = new FunctionCall("ENCRYPT_PASSWORD");
    private FunctionCall decrypt_password = new FunctionCall("DECRYPT_PASSWORD");


    private DbTable USER = new DbTable(schema, "user");
    private DbColumn ID = new DbColumn(USER, "id", "VARCHAR", 36);
    private DbColumn EMAIL = new DbColumn(USER, "email", "VARCHAR", 50);
    private DbColumn PASSWORD = new DbColumn(USER, "password", "VARBINARY", 50);
    private DbColumn FIRST_NAME = new DbColumn(USER, "first_name", "VARCHAR", 50);
    private DbColumn LAST_NAME = new DbColumn(USER, "last_name", "VARCHAR", 50);


    public void connectDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connObj = DriverManager.getConnection(
                    ConfigUtil.getProperty("db.host"), ConfigUtil.getProperty("db.username"), ConfigUtil.getProperty("db.password"));
        } catch (Exception e) {
            Log.e("Database", "Error connecting to the database");
        }
    }

    public void disconnectDb() {
        try {
            stmtObj.close();
            connObj.close();
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void getAllUsers() {
        String sql = new SelectQuery()
                .addColumns(EMAIL)
                .addFromTable(USER)
                .validate().toString();
        try {
            connectDb();
            stmtObj = connObj.prepareStatement(sql);
            resObj = stmtObj.executeQuery();

            while (resObj.next()) {
                Log.i("tag", resObj.getString(1));

            }
            disconnectDb();
        } catch (Exception e) {
            Log.e("tag", e.toString());
            disconnectDb();
        }
    }

    public Boolean isExistingUser(String email, String password) {
        ArrayList<String> verifiedUser = new ArrayList<>();
        String checkUser = new SelectQuery()
                .addColumns(EMAIL, PASSWORD)
                .addFromTable(USER)
                .addCondition(BinaryCondition.equalTo(EMAIL, email))
                .addCondition(BinaryCondition.equalTo(decrypt_password.addCustomParams(PASSWORD), password))
                .validate().toString();
        try {
            connectDb();
            stmtObj = connObj.prepareStatement(checkUser);
            resObj = stmtObj.executeQuery();
            while (resObj.next()) {
                verifiedUser.add(resObj.getString(1));
            }
            disconnectDb();
        } catch (Exception e) {
            Log.e("isExistingUser", e.toString());
            disconnectDb();
        }
        return !verifiedUser.isEmpty();
    }

    public void addUser(User user) throws SQLException {
        String InsertUser = new InsertQuery(USER)
                .addColumn(ID, user.getId())
                .addColumn(EMAIL, user.getEmail())
                .addColumn(PASSWORD, encrypt_password.addCustomParams(user.getPassword()))
                .addColumn(FIRST_NAME, user.getFirstName())
                .addColumn(LAST_NAME, user.getLastName())
                .validate().toString();
        try {
            connectDb();
            stmtObj = connObj.prepareStatement(InsertUser);
            stmtObj.executeUpdate();
            disconnectDb();
        } catch (Exception e) {
            Log.e("addUser", e.toString());
            disconnectDb();
            throw e;
        }
    }


}