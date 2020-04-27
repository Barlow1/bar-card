package com.nexuscmarketing.bar_card.sql;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import com.nexuscmarketing.bar_card.model.Bar;
import com.nexuscmarketing.bar_card.model.BarCard;
import com.nexuscmarketing.bar_card.model.Reward;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.model.UserBarCard;
import com.nexuscmarketing.bar_card.utils.ConfigUtil;
import com.nexuscmarketing.bar_card.utils.ResourceUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class DatabaseHelper {

    private static ResultSet resObj;
    private static PreparedStatement stmtObj;
    private static Connection connObj;

    private DbSpec spec = new DbSpec();
    private DbSchema schema = new DbSchema(spec, "bar_card");
    private FunctionCall encrypt_password = new FunctionCall("ENCRYPT_PASSWORD");
    private FunctionCall decrypt_password = new FunctionCall("DECRYPT_PASSWORD");


    private DbTable USER = new DbTable(schema, "user");
    private DbColumn ID = new DbColumn(USER, "id", "VARCHAR", 36);
    private DbColumn PHONE = new DbColumn(USER, "phone", "VARCHAR", 36);
    private DbColumn EMAIL = new DbColumn(USER, "email", "VARCHAR", 50);
    private DbColumn PASSWORD = new DbColumn(USER, "password", "VARBINARY", 50);
    private DbColumn FIRST_NAME = new DbColumn(USER, "first_name", "VARCHAR", 50);
    private DbColumn LAST_NAME = new DbColumn(USER, "last_name", "VARCHAR", 50);
    private DbColumn ADMIN = new DbColumn(USER, "admin", "TINYINT", 3);


    private DbTable BAR = new DbTable(schema, "bar");
    private DbColumn BAR_ID = new DbColumn(BAR, "id", "VARCHAR", 36);
    private DbColumn BAR_NAME = new DbColumn(BAR, "bar_name", "VARCHAR", 50);
    private DbColumn ADMIN_ID = new DbColumn(BAR, "admin_id", "VARCHAR", 36);


    private DbTable BAR_CARD = new DbTable(schema, "bar_card");
    private DbColumn BAR_CARD_ID = new DbColumn(BAR_CARD, "id", "VARCHAR", 36);
    private DbColumn BAR_CARD_USER_ID = new DbColumn(BAR_CARD, "user_id", "VARCHAR", 36);
    private DbColumn BAR_CARD_BAR_ID = new DbColumn(BAR_CARD, "bar_id", "VARBINARY", 36);
    private DbColumn PUCNHES = new DbColumn(BAR_CARD, "punches", "INT", 10);
    private DbColumn REWARD_PUNCHES = new DbColumn(BAR_CARD, "reward_punches", "INT", 10);
    private DbColumn REWARD = new DbColumn(BAR_CARD, "reward", "VARCHAR", 50);
    private DbColumn LOGO = new DbColumn(BAR_CARD, "logo_name", "VARCHAR", 50);

    private DbTable BAR_CARD_TEMPLATE = new DbTable(schema, "bar_card_template");
    private DbColumn BAR_CARD_TEMPLATE_ID = new DbColumn(BAR_CARD_TEMPLATE, "id", "VARCHAR", 36);
    private DbColumn BAR_CARD_TEMPLATE_BAR_ID = new DbColumn(BAR_CARD_TEMPLATE, "bar_id", "VARCHAR", 36);
    private DbColumn BAR_CARD_TEMPLATE_IMG_NAME = new DbColumn(BAR_CARD_TEMPLATE, "img_name", "VARCHAR", 50);
    private DbColumn BAR_CARD_TEMPLATE_REWARD_PUNCHES = new DbColumn(BAR_CARD_TEMPLATE, "reward_punches", "INT", 10);
    private DbColumn BAR_CARD_TEMPLATE_REWARD = new DbColumn(BAR_CARD_TEMPLATE, "reward", "VARCHAR", 50);

    private DbTable REWARD_TABLE = new DbTable(schema, "reward");
    private DbColumn REWARD_TABLE_ID = new DbColumn(REWARD_TABLE, "id", "VARCHAR", 36);
    private DbColumn REWARD_TABLE_BAR_CARD_ID = new DbColumn(REWARD_TABLE, "bar_card_id", "VARCHAR", 36);
    private DbColumn REWARD_TABLE_TEMPLATE_ID = new DbColumn(REWARD_TABLE, "reward_template_id", "VARCHAR", 36);

    private DbTable REWARD_TEMPLATE = new DbTable(schema, "reward_template");
    private DbColumn REWARD_TEMPLATE_ID = new DbColumn(REWARD_TEMPLATE, "id", "VARCHAR", 36);
    private DbColumn REWARD_TEMPLATE_BAR_CARD_TEMPLATE_ID = new DbColumn(REWARD_TEMPLATE, "bar_card_template_id", "VARCHAR", 36);
    private DbColumn REWARD_TEMPLATE_TITLE = new DbColumn(REWARD_TEMPLATE, "title", "VARCHAR", 50);
    private DbColumn REWARD_TEMPLATE_DESCRIPTION = new DbColumn(REWARD_TEMPLATE, "description", "VARCHAR", 500);



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

    public User getExistingUser(String email, String password) throws Resources.NotFoundException {
        ArrayList<User> verifiedUser = new ArrayList<>();
        User existingUser = new User();
        String checkUser = new SelectQuery()
                .addColumns(ID, PHONE, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, ADMIN)
                .addFromTable(USER)
                .addCondition(BinaryCondition.equalTo(EMAIL, email))
                .addCondition(BinaryCondition.equalTo(decrypt_password.addCustomParams(PASSWORD), password))
                .validate().toString();
        try {
            connectDb();
            stmtObj = connObj.prepareStatement(checkUser);
            resObj = stmtObj.executeQuery();
            while (resObj.next()) {
                UUID userId = UUID.fromString(resObj.getString(1));
                existingUser.setId(userId);
                existingUser.setPhone(resObj.getString(2));
                existingUser.setEmail(resObj.getString(3));
                existingUser.setFirstName(resObj.getString(5));
                existingUser.setLastName(resObj.getString(6));
                existingUser.setAdmin(resObj.getInt(7));
                verifiedUser.add(existingUser);
            }
            disconnectDb();
        } catch (Exception e) {
            Log.e("getExistingUser", e.toString());
            disconnectDb();
        }
        if (!verifiedUser.isEmpty()) {
            return verifiedUser.get(0);
        }
        throw new Resources.NotFoundException("No existing user was found");
    }

    public void addUser(User user) throws SQLException {
        String InsertUser = new InsertQuery(USER)
                .addColumn(ID, user.getId())
                .addColumn(PHONE, user.getPhone())
                .addColumn(EMAIL, user.getEmail())
                .addColumn(PASSWORD, encrypt_password.addCustomParams(user.getPassword()))
                .addColumn(FIRST_NAME, user.getFirstName())
                .addColumn(LAST_NAME, user.getLastName())
                .addColumn(ADMIN, user.getAdmin())
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

    public void insertBarCard(Bar bar, User user) throws SQLException {
        String insertBarCard = new InsertQuery(BAR_CARD)
                .addColumn(BAR_CARD_ID, UUID.randomUUID())
                .addColumn(BAR_CARD_BAR_ID, bar.getId())
                .addColumn(BAR_CARD_USER_ID, user.getId())
                .addColumn(PUCNHES, 0)
                .addColumn(REWARD_PUNCHES, bar.getRewardPunches())
                .addColumn(REWARD, bar.getReward())
                .addColumn(LOGO, bar.getImageName())
                .validate().toString();

        try {
            connectDb();
            stmtObj = connObj.prepareStatement(insertBarCard);
            stmtObj.executeUpdate();
            disconnectDb();
        } catch (Exception e) {
            Log.e("insertBarCard", e.toString());
            disconnectDb();
            throw e;
        }

    }

    public void deleteBarCard(BarCard barCard) throws SQLException {
        String insertBarCard = new DeleteQuery(BAR_CARD)
                .addCondition(BinaryCondition.equalTo(BAR_CARD_ID, barCard.getId().toString()))
                .validate().toString();

        try {
            connectDb();
            stmtObj = connObj.prepareStatement(insertBarCard);
            stmtObj.executeUpdate();
            disconnectDb();
        } catch (Exception e) {
            Log.e("deleteBarCard", e.toString());
            disconnectDb();
            throw e;
        }

    }

    public ArrayList<Bar> getBarList() throws SQLException {
        ArrayList<Bar> barList = new ArrayList<>();
        String getBarList = new SelectQuery()
                .addFromTable(BAR)
                .addJoin(SelectQuery.JoinType.INNER, BAR, BAR_CARD_TEMPLATE, BinaryCondition.equalTo(BAR_ID, BAR_CARD_TEMPLATE_BAR_ID))
                .addColumns(BAR_ID, BAR_NAME, BAR_CARD_TEMPLATE_IMG_NAME, BAR_CARD_TEMPLATE_REWARD_PUNCHES, BAR_CARD_TEMPLATE_REWARD)
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(getBarList);
        resObj = stmtObj.executeQuery();

        while (resObj.next()) {
            Log.i("getBarList", resObj.getString(1));
            UUID barUUID = UUID.fromString(resObj.getString(1));
            barList.add(new Bar(barUUID, resObj.getString(2), resObj.getString(3), resObj.getInt(4), resObj.getString(5)));
        }
        disconnectDb();
        return barList;
    }

    public ArrayList<BarCard> getUserBarCards(Context context, UUID userId) throws SQLException {
        ArrayList<BarCard> userBarCards = new ArrayList<>();
        String getUserBarCards = new SelectQuery()
                .addFromTable(BAR_CARD)
                .addJoin(SelectQuery.JoinType.INNER, BAR_CARD, USER, BinaryCondition.equalTo(BAR_CARD_USER_ID, ID))
                .addJoin(SelectQuery.JoinType.INNER, BAR_CARD, BAR, BinaryCondition.equalTo(BAR_CARD_BAR_ID, BAR_ID))
                .addCondition(BinaryCondition.equalTo(ID, userId))
                .addColumns(BAR_CARD_ID, BAR_NAME, PUCNHES, REWARD_PUNCHES, REWARD, LOGO)
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(getUserBarCards);
        resObj = stmtObj.executeQuery();

        while (resObj.next()) {
            Log.i("getBarList", resObj.getString(1));
            UUID barCardUUID = UUID.fromString(resObj.getString(1));
            BarCard userCard = new BarCard();
            userCard.setId(barCardUUID);
            userCard.setBarName(resObj.getString(2));
            userCard.setPunches(resObj.getInt(3));
            userCard.setRewardPunches(resObj.getInt(4));
            userCard.setReward(resObj.getString(5));
            userCard.setImage(ResourceUtil.getDrawableIdByResName(context, resObj.getString(6)));
            userBarCards.add(userCard);
        }
        disconnectDb();
        return userBarCards;
    }

    public ArrayList<UserBarCard> getUsersWithBarCards(UUID barId, String query) throws SQLException {
        ArrayList<UserBarCard> userSearchResults = new ArrayList<>();
        query = '%' + query + '%';
        String getUsers = new SelectQuery()
                .addFromTable(USER)
                .addJoin(SelectQuery.JoinType.INNER, USER, BAR_CARD, BinaryCondition.equalTo(BAR_CARD_USER_ID, ID))
                .addCondition(BinaryCondition.equalTo(BAR_CARD_BAR_ID, barId))
                .addCondition(ComboCondition.or(BinaryCondition.like(EMAIL, query), BinaryCondition.like(PHONE, query), BinaryCondition.like(FIRST_NAME, query), BinaryCondition.like(LAST_NAME, query)))
                .addColumns(BAR_CARD_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE, PUCNHES)
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(getUsers);
        resObj = stmtObj.executeQuery();

        while (resObj.next()) {
            Log.i("getUsersWithBarCards", resObj.getString(1));
            UUID barCardUUID = UUID.fromString(resObj.getString(1));
            UserBarCard userBarCard = new UserBarCard();
            userBarCard.setBarCardId(barCardUUID);
            userBarCard.setFirstName(resObj.getString(2));
            userBarCard.setLastName(resObj.getString(3));
            userBarCard.setPhone(resObj.getString(4));
            userBarCard.setEmail(resObj.getString(5));
            userBarCard.setPunches(resObj.getInt(6));
            userSearchResults.add(userBarCard);
        }
        disconnectDb();
        return userSearchResults;
    }

    public Bar getAdminBar(UUID userId) throws SQLException {
        Bar adminBar = new Bar();
        String getUserBarCards = new SelectQuery()
                .addFromTable(BAR)
                .addJoin(SelectQuery.JoinType.INNER, BAR, BAR_CARD_TEMPLATE, BAR_ID, BAR_CARD_TEMPLATE_BAR_ID)
                .addCondition(BinaryCondition.equalTo(ADMIN_ID, userId))
                .addColumns(BAR_ID, BAR_NAME, BAR_CARD_TEMPLATE_IMG_NAME)
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(getUserBarCards);
        resObj = stmtObj.executeQuery();

        if (resObj.next()) {
            Log.i("getAdminBar", resObj.getString(1));
            UUID barId = UUID.fromString(resObj.getString(1));
            adminBar.setId(barId);
            adminBar.setBarName(resObj.getString(2));
            adminBar.setImageName(resObj.getString(3));
        }
        disconnectDb();
        return adminBar;
    }

    public void punchBarCard(UUID barCardId, int punches) throws SQLException {
        String punchBarCard = new UpdateQuery(BAR_CARD)
                .addCondition(BinaryCondition.equalTo(BAR_CARD_ID, barCardId))
                .addSetClause(PUCNHES, punches + 1)
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(punchBarCard);
        stmtObj.executeUpdate();
        disconnectDb();
    }

    public ArrayList<Reward> getUserRewards(UUID userId, Context context) throws SQLException {
        ArrayList<Reward> userRewards = new ArrayList<>();
        String getUserRewards = new SelectQuery()
                .addFromTable(REWARD_TABLE)
                .addJoin(SelectQuery.JoinType.INNER, REWARD_TABLE, REWARD_TEMPLATE, BinaryCondition.equalTo(REWARD_TABLE_TEMPLATE_ID, REWARD_TEMPLATE_ID))
                .addJoin(SelectQuery.JoinType.INNER, REWARD_TABLE,  BAR_CARD_TEMPLATE, BinaryCondition.equalTo(REWARD_TEMPLATE_BAR_CARD_TEMPLATE_ID, BAR_CARD_TEMPLATE_ID))
                .addJoin(SelectQuery.JoinType.INNER, REWARD_TABLE,  BAR_CARD, BinaryCondition.equalTo(REWARD_TABLE_BAR_CARD_ID, BAR_CARD_ID))
                .addCondition(BinaryCondition.equalTo(BAR_CARD_USER_ID, userId))
                .addColumns(REWARD_TABLE_ID, BAR_CARD_TEMPLATE_IMG_NAME, REWARD_TEMPLATE_TITLE, REWARD_TEMPLATE_DESCRIPTION)
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(getUserRewards);
        resObj = stmtObj.executeQuery();

        while (resObj.next()) {
            Log.i("getUserRewards", resObj.getString(1));
            UUID rewardUUID = UUID.fromString(resObj.getString(1));
            Reward reward = new Reward();
            reward.setId(rewardUUID);
            reward.setImage(ResourceUtil.getDrawableIdByResName(context, resObj.getString(2)));
            reward.setTitle(resObj.getString(3));
            reward.setDescription(resObj.getString(4));
            userRewards.add(reward);
        }
        disconnectDb();
        return userRewards;
    }

    public void claimReward(UUID rewardId) throws SQLException {
        String punchBarCard = new DeleteQuery(REWARD_TABLE)
                .addCondition(BinaryCondition.equalTo(REWARD_TABLE_ID, rewardId))
                .validate().toString();

        connectDb();
        stmtObj = connObj.prepareStatement(punchBarCard);
        stmtObj.executeUpdate();
        disconnectDb();
    }

}