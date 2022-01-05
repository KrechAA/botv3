package com.krech.botv2.repository;

import com.krech.botv2.config.DbConnector;
import com.krech.botv2.domain.DbWordObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DbWordRepositoryImpl implements WordRepository {
    private final DbConnector dbConnector;

    @Autowired
    public DbWordRepositoryImpl(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }


    @Override
    public void addOneWord(String word) {

        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            if (getWord(word) == null) {
                String insertSql = "INSERT words (name, first_letter) " +
                        "VALUES (" + "'" + word + "'" + ", " + "'" + word.charAt(0) + "'" + ")";

                stmt.executeUpdate(insertSql);
                System.out.println("one entry was inserted");
            }
            //TODO искать в базе слово по всем буквам (метод getWord)
            //если слово не найдено, то добавляем его в БД. см. статью
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<String> getWordsByChar(char c) { //
        List<String> resultList = new ArrayList<>();
        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            String selectSql = "SELECT * FROM words WHERE first_letter = " + "'" + c + "'";
            try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    resultList.add(resultSet.getString("name"));
                }
                return resultList;
                //вынуть из объектов слова и запихнуть в лист стрингов

            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        //дописать возврат всех слов, начинающихся на букву из параметра
    }

    public DbWordObject getWord(String word) {

        try (Statement stmt = dbConnector.getConnection().createStatement()) {
            String selectSql = "SELECT * FROM words WHERE name = " + "'" + word + "'";
            try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
                if (resultSet.next()) {
                    return new DbWordObject(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("first_letter"));
                } else {
                    return null;
                }

            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }


        //TODO дописать метод. Возвращает объект с полями как столбцы в БД.(сделать новый класс)
    }
}
