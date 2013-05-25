package com.drleinbach.minecraftstats.dao;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * My implementation of what is know as a 'BeanMapper'. This
 * DefaultRowMapper combines the javax annotations for JPA and
 * the RowMapper from the Spring JDBC library. Only private
 * members annotated with the Column will be set by this mapper.
 * <p/>
 * The type for this object should be an interface, while the class
 * provided to the constructor should be the implementation of that
 * interface.
 * <p/>
 * Created: 5/5/13
 *
 * @author Daniel
 */
public class DefaultRowMapper<T> implements RowMapper<T> {

    /**
     * Logger, used to record diagnostic information
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultRowMapper.class);

    /**
     * The class that is being provided to this Mapper. Normally the class
     * is the implementation of the value provided during definition.
     */
    private Class<? extends T> clazz;

    /**
     * Parametrized constructor that takes a class, that is the same or a
     * subclass of the class provided during definition.
     *
     * @param clazz - The class used during mapping.
     */
    public DefaultRowMapper(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    /**
     * This row mapper performs a dynamic mapping based on the classes provided
     * to the constructor and during definition. As long as the private
     * members of the class provided to the constructor are annotated with the
     * Columns annotation, they will be set by this method.
     * <p/>
     * If any errors occur during the row mapping that value will be set to null and
     * the mapper will continue to the end.
     *
     * @param resultSet - Results of the database query
     * @param i         - Current row in the result set
     * @return Object mapped from the result set.
     * @throws SQLException
     */
    @Override
    public T mapRow(ResultSet resultSet, int i) throws SQLException {
        LOGGER.debug("Result number: " + i);
        Object mappedObject;
        try {
            mappedObject = clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("Exception has occurred", e);
            return null;
        }

        Field[] fields = clazz.getDeclaredFields();

        // Loop through all of the fields for the model object
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);

            // If the column annotation was found
            if (column != null) {
                LOGGER.debug("Column annotation found");

                boolean inAccessable = false;
                // If the field is private set it too accessible
                if (!field.isAccessible()) {
                    inAccessable = true;
                    field.setAccessible(inAccessable);
                }
                try {

                    // If an alternative column name was not given use field name
                    if (column.name().isEmpty()) {
                        field.set(mappedObject, resultSet.getObject(field.getName()));

                        // alternative column name was provided for field
                    } else {
                        field.set(mappedObject, resultSet.getObject(column.name()));
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception has occurred", e);
                } finally {
                    // Return field to original state
                    if (inAccessable) {
                        field.setAccessible(false);
                    }
                }
            } else {
                LOGGER.debug("Column annotation was not found, skipping field");
            }
        }

        return clazz.cast(mappedObject);

    }
}
