package com.mycompany.myapp.app.child.repository.rowmapper;

import com.mycompany.myapp.app.child.domain.CustomPackageChild;
import com.mycompany.myapp.repository.rowmapper.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CustomPackageChild}, with proper type conversions.
 */
@Service
public class CustomPackageChildRowMapper implements BiFunction<Row, String, CustomPackageChild> {

    private final ColumnConverter converter;

    public CustomPackageChildRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CustomPackageChild} stored in the database.
     */
    @Override
    public CustomPackageChild apply(Row row, String prefix) {
        CustomPackageChild entity = new CustomPackageChild();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setChildName(converter.fromRow(row, prefix + "_child_name", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        entity.setCustomPackageParentId(converter.fromRow(row, prefix + "_custom_package_parent_id", Long.class));
        return entity;
    }
}
