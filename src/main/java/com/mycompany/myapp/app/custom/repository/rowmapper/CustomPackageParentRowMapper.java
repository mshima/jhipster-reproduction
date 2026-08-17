package com.mycompany.myapp.app.custom.repository.rowmapper;

import com.mycompany.myapp.app.custom.domain.CustomPackageParent;
import com.mycompany.myapp.repository.rowmapper.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CustomPackageParent}, with proper type conversions.
 */
@Service
public class CustomPackageParentRowMapper implements BiFunction<Row, String, CustomPackageParent> {

    private final ColumnConverter converter;

    public CustomPackageParentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CustomPackageParent} stored in the database.
     */
    @Override
    public CustomPackageParent apply(Row row, String prefix) {
        CustomPackageParent entity = new CustomPackageParent();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setParentName(converter.fromRow(row, prefix + "_parent_name", String.class));
        return entity;
    }
}
