package com.example.lazyco.backend.core.DatabaseConf;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.*;

import java.util.*;
import java.util.Collection;
import java.util.List;

public class ColumnOrderIntegrator implements Integrator {

    @Override
    public void integrate(Metadata metadata,
                          BootstrapContext bootstrapContext,
                          SessionFactoryImplementor sessionFactory) {

        for (PersistentClass pc : metadata.getEntityBindings()) {
            reorderTableColumns(pc);
        }
    }

    private void reorderTableColumns(PersistentClass pc) {
        Table table = pc.getTable();
        if (table == null) return;

        try {
            // Build desired order of Column objects
            List<Column> orderedColumns = new ArrayList<>();

            // 1) Identifier columns (works for simple and composite ids)
            KeyValue idValue = pc.getIdentifier();
            if (idValue != null) {
                addColumnsFromValue(idValue, orderedColumns);
            }

            // 2. Declared props on the entity
            for (Property prop : pc.getDeclaredProperties()) {
                addColumnsFromProperty(prop, orderedColumns);
            }

            // 3. Superclasses
            MappedSuperclass superClass = pc.getSuperMappedSuperclass();
            while (superClass != null) {
                // Use getDeclaredProperties() to get properties declared only on this class
                Collection<Property> superProps = superClass.getDeclaredProperties();
                for (Property prop : superProps) {
                    addColumnsFromProperty(prop, orderedColumns);
                }
                superClass = superClass.getSuperMappedSuperclass();
            }

            // 4. Add leftovers (FKs etc.)
            List<Column> all = new ArrayList<>(table.getColumns());
            for (Column c : all) {
                if (!orderedColumns.contains(c)) {
                    orderedColumns.add(c);
                }
            }

            // 5. Apply new order
            List<Column> columns = new ArrayList<>(orderedColumns);
            table.reorderColumns(columns);

        } catch (Exception e) {
            // Log the error but don't fail the application startup
            ApplicationLogger.error("Could not reorder columns for table " +
                    (table.getName() != null ? table.getName() : "unknown") +
                    ": " + e.getMessage(), e);
        }
    }

    private void addColumnsFromProperty(Property prop, List<Column> out) {
        if (prop.getValue() != null) {
            addColumnsFromValue(prop.getValue(), out);
        }
    }

    private void addColumnsFromValue(Value value, List<Column> out) {
        if (value == null) return;
        // Column implements Selectable interface
        List<Column> selectables = value.getColumns();
        for (Selectable selectable : selectables) {
            if (selectable instanceof Column column) {
                if (!out.contains(column)) {
                    out.add(column);
                }
            }
        }
    }
}