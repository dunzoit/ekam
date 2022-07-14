package com.testvagrant.ekam.db.clients;

import com.testvagrant.ekam.db.entities.DBType;

public abstract class BigQueryClient extends DBClient {

    protected BigQueryClient(String database) {
        this(database, DBType.BIGQUERY.getDbString());
    }

    protected BigQueryClient(String database, String driverKey) {
        super(database, driverKey, DBType.BIGQUERY);
    }
}
