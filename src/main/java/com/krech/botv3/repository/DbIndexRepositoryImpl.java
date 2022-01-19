package com.krech.botv3.repository;

import com.krech.botv3.config.DbConfiguration;
import com.krech.botv3.config.DbConnector;
import com.krech.botv3.domain.Indexkey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ConditionalOnBean(DbConfiguration.class)
@Repository
public class DbIndexRepositoryImpl implements IndexRepository {

    private final DbConnector dbConnector;

    @Autowired
    public DbIndexRepositoryImpl(DbConnector dbConnector) {

        this.dbConnector = dbConnector;
    }

    @Override
    public void addNewIndexAndWords(Indexkey key, Set<String> newSetOfWords) {

        char firstLetter = key.getFirstChar();
        String otherLetters = key.getOtherChars();
        String firstLetterStr = Character.toString(firstLetter);

        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            String selectSql = "SELECT * FROM indexes AS i WHERE i.index = " + "'" + otherLetters + "'" +
                    "AND first_letter =" + "'" + firstLetterStr + "'";  //TODO сделать сложное условие через AND/ добавить открытие и закрытие транзакции
            try (ResultSet resultSet = stmt.executeQuery(selectSql)) {

                if (resultSet.next()) {
                    int indexId = resultSet.getInt("id");


                    for (String str : newSetOfWords) {
                        selectSql = "SELECT * FROM words WHERE name = " + "'" + str + "'";
                        try (ResultSet resultSet2 = stmt.executeQuery(selectSql)) {
                            if (resultSet2.next()) {
                                int id = resultSet2.getInt("id");
                                String insertSql = "INSERT idwords_idindex (id_word, id_index) " +
                                        "VALUES (" + "'" + id + "'" + ", " + "'" + indexId + "'" + ")";
                                stmt.executeUpdate(insertSql);
                            }
                        }
                    }
                } else {
                    String insertSql = "INSERT indexes (`index`, first_letter) " +
                            "VALUES (" + "'" + otherLetters + "'" + ", " + "'" + firstLetterStr + "'" + ")";
                    stmt.executeUpdate(insertSql);


                    addNewWordsToExistingIndex(key, newSetOfWords);


                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        //TODO   создать запись с индексом в таблице indexes + сделать селект на id индекса.
        // В цикле пройти по списку нью сет оф вордс и селектнуть каждое слово по имени, вытащить id, заинсертить в
        //  промежуточную таблицу.

    }

    @Override
    public Set<String> getWords(Indexkey key) {

        String firstLetterStr = Character.toString(key.getFirstChar());
        Set<String> words = new HashSet<>();

        try {
            dbConnector.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            String selectSql = "SELECT * FROM indexes as i " +
                    "JOIN idwords_idindex " +
                    "ON i.id = idwords_idindex.id_index " +
                    "JOIN words " +
                    "ON idwords_idindex.id_word = words.id " +
                    "WHERE i.index = " + "'" + key.getOtherChars() + "'" +
                    "AND i.first_letter = " + "'" + firstLetterStr + "'";

            try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    words.add(resultSet.getString("name"));
                }
                dbConnector.getConnection().commit();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }


        return words;
        //TODO вернуть все слова, которые привязаны к индексу. JOINы
    }

    @Override
    public Map<Indexkey, Set<String>> getAll() {
        Map<Indexkey, Set<String>> allIndexesWithThemWords = new HashMap<>();
        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            String selectAllIndexes = "SELECT * FROM indexes"; //TODO сделать через джоин выборку из индексес и вордс
            try (ResultSet resultSet = stmt.executeQuery(selectAllIndexes)) {
                while (resultSet.next()) {
                    Indexkey indexkey = new Indexkey();
                    indexkey.setOtherChars(resultSet.getString("index"));
                    indexkey.setFirstChar(resultSet.getString("first_letter").charAt(0));

                    Set<String> setOfWords = getWords(indexkey);

                    allIndexesWithThemWords.put(indexkey, setOfWords);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return allIndexesWithThemWords;
    }

    @Override
    public void addNewWordsToExistingIndex(Indexkey key, Set<String> newSetOfWords) {
        char firstLetter = key.getFirstChar();
        String otherLetters = key.getOtherChars();
        String firstLetterStr = Character.toString(firstLetter);

        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            String selectSql = "SELECT * FROM indexes AS i WHERE i.index = " + "'" + otherLetters + "'" +
                    "AND first_letter =" + "'" + firstLetterStr + "'";  //TODO сделать сложное условие через AND
            try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
                if (resultSet.next()) {
                    int indexId = resultSet.getInt("id");
                    for (String str : newSetOfWords) {
                        selectSql = "SELECT * FROM words WHERE name = " + "'" + str + "'";
                        try (ResultSet resultSet2 = stmt.executeQuery(selectSql)) {
                            if (resultSet2.next()) {
                                int id = resultSet2.getInt("id");
                                String insertSql = "INSERT idwords_idindex (id_word, id_index) " +
                                        "VALUES (" + "'" + id + "'" + ", " + "'" + indexId + "'" + ")";
                                stmt.executeUpdate(insertSql);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        //todo добавление слов к уже существующему индексу
    }
}
