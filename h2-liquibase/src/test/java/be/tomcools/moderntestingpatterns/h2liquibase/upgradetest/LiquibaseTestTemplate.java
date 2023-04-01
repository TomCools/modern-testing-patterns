package be.tomcools.moderntestingpatterns.h2liquibase.upgradetest;

import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class LiquibaseTestTemplate extends SpringLiquibase {

    public void runContext(String context) {
        runWithLiquibase(l -> {
            try {
                l.update(context);
            } catch (LiquibaseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void runWithLiquibase(Consumer<Liquibase> consumer) {
        Liquibase liquibase = null;

        try {
            Connection c = this.getDataSource().getConnection();
            liquibase = super.createLiquibase(c);
            consumer.accept(liquibase);
        } catch (SQLException | LiquibaseException var8) {
            throw new RuntimeException(var8);
        } finally {
            if (liquibase != null) {
                try {
                    liquibase.close();
                } catch (LiquibaseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
