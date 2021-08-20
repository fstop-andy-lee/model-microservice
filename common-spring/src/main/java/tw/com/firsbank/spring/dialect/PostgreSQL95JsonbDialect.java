package tw.com.firsbank.spring.dialect;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL95Dialect;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;

public class PostgreSQL95JsonbDialect extends PostgreSQL95Dialect {

	public PostgreSQL95JsonbDialect() {
		super();
		this.registerHibernateType(Types.OTHER, JsonNodeBinaryType.class.getName());
	}

}
