package com.abrenchev.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySqlMetaDataImpl implements EntitySQLMetaData {
    private final String tableName;

    private final String idFieldName;

    private final List<String> nonIdFields;

    public EntitySqlMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.tableName = entityClassMetaData.getName();
        this.idFieldName = entityClassMetaData.getIdField().getName();
        this.nonIdFields = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList());
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
