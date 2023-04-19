package org.hotrod.runtime.converter;

import java.sql.Connection;

/**
 * 
 * @author Vladimir Alarcon
 * 
 *         <p>
 *         Converts back and forth between transitional values and application
 *         values.
 *         </p>
 * 
 *         <p>
 *         The <b>intermediate</b> value is the intermediate Java value read
 *         from the database (or written to it) using JDBC.
 *         </p>
 * 
 *         <p>
 *         The <b>application</b> value is the value the application uses after
 *         all transformation from the database.
 *         </p>
 *
 *         <p>
 *         For example, a database column DECIMAL(4) can be used to represent a
 *         java.lang.Boolean type (that a database does not support natively)
 *         using the numeric values zero (0) and one (1). This database column
 *         value can be <b>first</b> read through JDBC as a java.lang.Short.
 *         <b>Then</b>, it can be converted into a java.lang.Boolean. In this
 *         case:
 *         </p>
 * 
 *         <ul>
 *         <li>DECIMAL(4): the database column type.</li>
 *         <li>java.lang.Short: the type of the intermediate value. This value
 *         is not available to the application and is briefly used by the
 *         persistence layer while reading/writing from/into the database.</li>
 *         <li>java.lang.Boolean: the type of the application value. This is the
 *         value that the application sees, and is usually stored as a class
 *         property of this type.</li>
 *         </ul>
 *
 * @param <R> The raw type read/written to the database.
 * @param <A> The application type.
 */
public interface TypeConverter<R, A> {

  A decode(R raw, Connection conn);

  R encode(A value, Connection conn);

}
