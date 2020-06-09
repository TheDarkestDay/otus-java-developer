package com.abrenchev.jdbc.mapper;

import java.util.ArrayList;
import java.util.List;

public class EntitySqlMetaDataImpl implements EntitySQLMetaData {
    private final String tableName;

    private final String idFieldName;

    private final List<String> nonIdFields;

    public EntitySqlMetaDataImpl(String tableName, String idFieldName, List<String> nonIdFields) {
        this.tableName = tableName;
        this.idFieldName = idFieldName;
        this.nonIdFields = nonIdFields;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + tableName;
    }

    @Override
    public String getSelectByIdSql() {
        return getSelectAllSql() + " where " + idFieldName + " = ?";
    }

    @Override
    public String getInsertSql() {
        var fieldNames = String.join(",", nonIdFields);

        var wildcardsList = new ArrayList<String>();

        for (int i = 0; i < nonIdFields.size(); i++) {
            wildcardsList.add("?");
        }

        return "insert into " + tableName + "(" + fieldNames + ") values (" + String.join(",", wildcardsList) + ")";
    }

    @Override
    public String getUpdateSql() {
        return null;
    }
}
