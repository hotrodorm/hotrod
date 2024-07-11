package org.hotrod.runtime.converter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author Vladimir Alarcon
 * 
 *         <p>
 *         Converts back and forth between raw database values and application
 *         domain values.
 *         </p>
 * 
 *         <p>
 *         The <b>raw</b> value is the Java value read from the database (or
 *         written to it) using JDBC.
 *         </p>
 * 
 *         <p>
 *         The application <b>domain</b> value is the value the application uses
 *         after the converter is applied to the raw value.
 *         </p>
 *
 *         <p>
 *         For example, a database column DECIMAL(4) can be used to represent a
 *         java.lang.Boolean type (that a database does not support natively)
 *         using the numeric values zero (0) and one (1). This database column
 *         value can be <b>first</b> read through JDBC as a raw java.lang.Short
 *         value. <b>Then</b>, it can be converted into a java.lang.Boolean. In
 *         this case:
 *         </p>
 * 
 *         <ul>
 *         <li>DECIMAL(4): the database column type.</li>
 *         <li>java.lang.Short: the raw type. This value of this type is not
 *         available to the application and is only briefly used by the
 *         persistence layer while reading/writing from/into the database.</li>
 *         <li>java.lang.Boolean: the domain value. This is the value that the
 *         application sees.</li>
 *         </ul>
 *
 * @param <R> The raw type read/written to the database.
 * @param <A> The domain (application) type.
 */
public interface TypeConverter<R, A> {

  A decode(R raw, Connection conn) throws SQLException;

  R encode(A domain, Connection conn) throws SQLException;

}
