package ufc.deha.ufc9.dolphin.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Classe destinada a abrigar m�todos abstratos para execu��o de instru��es SQL comuns como criar schemas, tabelas, inserir dados, etc.
 * @since 1.0
 * @version 1.0
 * 
 * @author Guilherme Ribeiro
 *
 */
public final class SqlFunctions {
	/**
	 * Executa uma instru��o SQL em um banco de dados acess�vel por uma conex�o.
	 * @param connection - Objeto Connection para comunica��o com o banco de dados.
	 * @param sql - instru��o SQL.
	 * @throws SQLException - caso a instru��o SQL esteja incorreta ou o usu�rio n�o tenha o privil�gio para execut�-la.
	 */
	public static void makeSql(Connection connection, String sql) throws SQLException {
		Statement stmt = null;
		//System.out.println(connection);
		try {
			stmt = connection.createStatement();
			stmt.execute(sql);
			
		}catch(SQLException e) {
			throw(e);
		}finally {
			//System.out.println(sql);
			try {
				stmt.close();
			}catch(Exception e) {}
		}
			
	}
	/**
	 * Executa uma instru��o SQL em um banco de dados acess�vel por uma conex�o.
	 * <p>A instru��o nesse ponto � executada por um {@link PreparedStatement} e portanto necessita de informa��es que a alimentem presentes
	 * na matriz fornecida. Os dados devem ser coerentes e em quantidade suficiente para alimentar o PreparedStatement.
	 * @param connection - Objeto Connection para comunica��o com o banco de dados.
	 * @param sql - instru��o SQL.
	 * @param matrix - dados que ser�o usados no preenchimento do PreparedStatement.
	 * @throws SQLException - caso a instru��o SQL esteja incorreta ou o usu�rio n�o tenha o privil�gio para execut�-la.
	 */
	public static void makePreparedSql(Connection connection, String sql, String[][] matrix) throws SQLException{
		PreparedStatement stmt = null;
		//System.out.println(sql);
		//System.out.println("Iniciando: ");
		//int j = 0;
		//int size = matrix.length;
		try {
			stmt = connection.prepareStatement(sql);
			for(String[] linha : matrix) {
				//System.out.println(j++ + "/" + size + "...");
				stmt.clearParameters();
				
				int i = 1;
				for(String value : linha) {
					stmt.setString(i, value);
					i++;
					}
				try {
					stmt.executeUpdate();
				}catch(SQLException e) {}
			}
		}catch(SQLException e) {
			throw(e);			
		}finally {
			try {
				stmt.close();
			}catch(SQLException e) {}
		}
	}
	/**
	 * Executa uma instru��o SQL em um banco de dados acess�vel por uma conex�o.
	 * <p>A instru��o nesse ponto � executada por um {@link PreparedStatement} e portanto necessita de informa��es que a alimentem presentes
	 * na matriz fornecida. Os dados devem ser coerentes e em quantidade suficiente para alimentar o PreparedStatement.
	 * @param connection - Objeto Connection para comunica��o com o banco de dados.
	 * @param sql - instru��o SQL.
	 * @param matrix - dados que ser�o usados no preenchimento do PreparedStatement.
	 * @throws SQLException - caso a instru��o SQL esteja incorreta ou o usu�rio n�o tenha o privil�gio para execut�-la.
	 */
	public static void makePreparedSql(Connection connection, String sql, Collection<String[]> matrix) throws SQLException{
		PreparedStatement stmt = null;
		//System.out.println(sql);
		//System.out.println("Iniciando: ");
		//int j = 0;
		//int size = matrix.size();
		try {
			stmt = connection.prepareStatement(sql);
			for(String[] linha : matrix) {
				//System.out.println(j++ + "/" + size + "...");
				stmt.clearParameters();
				
				int i = 1;
				for(String value : linha) {//int i = 1; i <= linha.length; i++) {
					stmt.setString(i, value);
					i++;
					}
				try {
					stmt.executeUpdate();
				}catch(SQLException e) {}
			}
		}catch(SQLException e) {
			throw(e);			
		}finally {
			try {
				stmt.close();
			}catch(SQLException e) {}
		}
	}
	/**
	 * Executa uma instru��o SQL em um banco de dados acess�vel por uma conex�o.
	 * <p>A instru��o nesse ponto � executada por um {@link PreparedStatement} e portanto necessita de informa��es que a alimentem presentes
	 * na matriz fornecida. Os dados devem ser coerentes e em quantidade suficiente para alimentar o PreparedStatement.
	 * @param connection - Objeto Connection para comunica��o com o banco de dados.
	 * @param sql - instru��o SQL.
	 * @param matrix - dados que ser�o usados no preenchimento do PreparedStatement.
	 * @throws SQLException - caso a instru��o SQL esteja incorreta ou o usu�rio n�o tenha o privil�gio para execut�-la.
	 */
	public static void makePreparedSql(Connection connection, String sql, List<List<String>> matrix) throws SQLException{
		PreparedStatement stmt = null;
		//System.out.println(sql);

		//System.out.println("Iniciando: ");
		int j = 0;
		int size = matrix.size();
		try {
			stmt = connection.prepareStatement(sql);
			for(List<String> linha : matrix) {
				System.out.print("\n" + ++j + "/" + size + "... | ");
				stmt.clearParameters();
				
				int i = 1;
				for(String value : linha) {//int i = 1; i <= linha.size(); i++) {
					System.out.print(value + " | ");
					stmt.setString(i, value);
					i++;
					}
				try {
					stmt.executeUpdate();
				}catch(SQLException e) {}
			}
		}catch(SQLException e) {
			throw(e);		
		}finally {
			try {
				stmt.close();			
			}catch(Exception e) {}
		}
	}
	
	

	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertMultipleRowsPrepared(Connection connection, String dataBaseName, String tableName, String[] columnNames, String[][] values)  throws SQLException {
		String sql = insertRowsPreparedSql( dataBaseName, tableName, columnNames );
		try(PreparedStatement stmt = connection.prepareStatement(sql)){
			int j = 0;

			for(String[] linha : values) {
				////System.out.println(Arrays.toString(linha));
				
				int i = 1;
				for(String value : linha) {
					if (i == 1) {
						stmt.setString(i, value.replaceAll(" ", ""));
					}else {
						stmt.setString(i, value);						
					}
					i++;
					}
	            stmt.addBatch();
	            j++;
	            
	            if (j % 1000 == 0 || j == values.length) {
	            	try {
	            		stmt.executeBatch(); // Execute every 1000 items.
	            		stmt.clearParameters();
	            	}catch(SQLException e) {
	            		throw(e);
	            	}
	            }
			}
		}catch(SQLException e) {
			throw(e);
		}
	}
	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertMultipleRowsPrepared(Connection connection, String dataBaseName, String tableName, List<String> columnNames, Collection<String[]> values)  throws SQLException {
		String sql = insertRowsPreparedSql( dataBaseName, tableName, columnNames );
		try(PreparedStatement stmt = connection.prepareStatement(sql)){
			int j = 0;

			for(String[] linha : values) {
				
				int i = 1;
				////System.out.println(Arrays.toString(linha));
				for(String value : linha) {
					if (i == 1) {
						stmt.setString(i, value.replaceAll(" ", ""));
					}else {
						stmt.setString(i, value);
						
					}
					i++;
					}
	            stmt.addBatch();
	            j++;
	            
	            if (j % 1000 == 0 || j == values.size()) {
	            	try {
	            		stmt.executeBatch(); // Execute every 1000 items.
	            		stmt.clearParameters();
	            	}catch(SQLException e) {
	            		throw(e);
	            	}
	            }
			}
		}catch(SQLException e) {
			throw(e);
		}
	}
	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertMultipleRowsPrepared(Connection connection, String dataBaseName, String tableName, String[] columnNames, List<List <String>> values)  throws SQLException {
		String sql = insertRowsPreparedSql( dataBaseName, tableName, columnNames );

		try(PreparedStatement stmt = connection.prepareStatement(sql)){
			int j = 0;
			//int size = values.size();

			for(List<String> linha : values) {
				System.out.println("Number: " + j + ": (" + linha.get(0) + ") - " + linha.get(1));
				int i = 1;
				////System.out.println( j + "/" + size + "... | ");
				for(String value : linha) {
					if (i == 1) {
						stmt.setString(i, value.replaceAll(" ", ""));
					}else {
						stmt.setString(i, value);						
					}
					i++;
					}
	            stmt.addBatch();
	            j++;
	            
	            if (j % 1000 == 0 || j == values.size()) {
	    			////System.out.println(sql);
	            	try {
	            		stmt.executeBatch(); // Execute every 1000 items.
	            		stmt.clearParameters();
	            	}catch(SQLException e) {
	            		throw(e);
	            	}
	            }
			}
		}catch(SQLException e) {
			throw(e);
		}
	}
	
	/**
	 * Altera o nome do banco de dados j� existente.
	 * @param connection
	 * @param oldSchemaName
	 * @param newSchemaName
	 * @throws SQLException
	 * 		Caso o banco de dados n�o exista ou se este n�o puder ser alterado.
	 * 
	 */
	public static void alterSchemaName(Connection connection, String oldSchemaName, String newSchemaName) throws SQLException {
		makeSql(connection, alterSchemaNameSql(oldSchemaName, newSchemaName));
	}
	/**
	 * Cria um schema no banco de dados se e somente se j� n�o existri outro schema com o mesmo nome.
	 * @param connection
	 * @param schemaName
	 * @throws SQLException
	 */
	public static void createSchemaIfNotExists(Connection connection, String schemaName)  throws SQLException {
		makeSql(connection, createSchemaIfNotExistsSql(schemaName));
		}
	/**
	 * Apaga schema.
	 * @param connection
	 * @param schemaName
	 * @throws SQLException
	 */
	public static void deleteSchemaIfExists(Connection connection, String schemaName)  throws SQLException {
		makeSql(connection, deleteSchemaIfExistsSql(schemaName));
	}
	/**
	 * Cria tipo em um schema no banco de dados se j� n�o existir tipo com o mesmo nome.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param columnTypes
	 * @throws SQLException
	 */
	public static void createTableIfNotExists(Connection connection, String dataBaseName, String tableName, String[] columnNames, String[] columnTypes)  throws SQLException {
		makeSql(connection, createTableIfNotExistsSql(dataBaseName, tableName, columnNames, columnTypes));
		}
	/**
	 * Cria tipo em um schema no banco de dados se j� n�o existir tipo com o mesmo nome.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param columnTypes
	 * @throws SQLException
	 */
	public static void createTableIfNotExists(Connection connection, String dataBaseName, String tableName, List<String> columnNames, List<String> columnTypes)  throws SQLException {
		makeSql(connection, createTableIfNotExistsSql(dataBaseName, tableName, columnNames, columnTypes));
		}
	/**
	 * Apaga tipo em um schema no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @throws SQLException
	 */
	public static void deleteTableIfExists(Connection connection, String dataBaseName, String tableName)  throws SQLException {
		makeSql(connection, deleteTableIfExistSql(dataBaseName, tableName));
		}	
	
	/**
	 * Apaga tipo em um schema no banco de dados por meio do nome canonico da tipo.
	 * <p>Exemplo: "\"BANCO\".\"TABELA1\"" ou "BANCO.TABELA1"
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @throws SQLException
	 */
	public static void deleteTableIfExists(Connection connection, String tableName)  throws SQLException {
		makeSql(connection, deleteTableIfExistSql(tableName));
		}
	
	/**
	 * Esvazia a tipo n�o a removendo do banco de dados.
	 * @param connection
	 * @param schemaName
	 * @param tableName
	 * @throws SQLException
	 */
	public static void deleteTableExistingData(Connection connection, String schemaName, String tableName) throws SQLException{
		makeSql(connection, deleteTableExistingDataSql(schemaName, tableName));
	}
	/**
	 * Limpa os dados de todas as tabelas no schema.
	 * @param connection
	 * @param schemaName
	 * @throws SQLException
	 */
	public static void deleteEveryTableExistingData(Connection connection, String schemaName) throws SQLException{
		makeSql(connection, deleteEveryTableExistingDataSql(schemaName));
	}	
	
	/**
	 * Insere uma linha de dados na tipo.
	 * <p> O nome das colunas devem ser fornecidos.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertRow(Connection connection, String dataBaseName, String tableName, String[] columnNames, String[] values)  throws SQLException {
		makeSql( connection, insertRowSql(dataBaseName, tableName, columnNames, values)) ;
	}
	/**
	 * Insere uma linha de dados na tipo.
	 * <p> O nome das colunas devem ser fornecidos.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertRow(Connection connection, String dataBaseName, String tableName, List<String> columnNames, List<String> values)  throws SQLException {
		makeSql( connection, insertRowSql(dataBaseName, tableName, columnNames, values)) ;
	}
	/**
	 * Atualiza os daos em uma row para as colunas fornecidas.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param oldValue
	 * @throws SQLException
	 */
	public static void updateRow (Connection connection, String dataBaseName, String tableName, String[] columnNames, String[] values, String selColumn, String oldValue)  throws SQLException {
		makeSql( connection, updateRowSql(dataBaseName, tableName, columnNames, values, selColumn, oldValue)) ;
	}
	/**
	 * Atualiza os daos em uma row para as colunas fornecidas.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param oldValue
	 * @throws SQLException
	 */
	public static void updateRow (Connection connection, String dataBaseName, String tableName, List<String> columnNames, List<String> values, String selColumn, String oldValue)  throws SQLException {
		makeSql( connection, updateRowSql(dataBaseName, tableName, columnNames, values, selColumn, oldValue)) ;
	}
	/**
	 * Apaga uma linha inteira do banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param selColumn
	 * @param oldValue
	 * @throws SQLException
	 */
	public static void deleteRow(Connection connection, String dataBaseName, String tableName, String selColumn, String oldValue)  throws SQLException {
		makeSql( connection, deleteRowSql(dataBaseName, tableName, selColumn, oldValue)) ;
	}
	/**
	 * Realiza as opera��es de deletar e inserir uma linha.
	 * <p> Correspondente a sobrescrever a linha.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void deleteAndInsertRow(Connection connection, String dataBaseName, String tableName, String[] columnNames, String[] values )  throws SQLException {
		deleteRow(connection, dataBaseName, tableName, columnNames[0], values[0]);
		insertRow(connection, dataBaseName,  tableName, columnNames, values);
		}
	/**
	 * Realiza as opera��es de deletar e inserir uma linha.
	 * <p> Correspondente a sobrescrever a linha.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void deleteAndInsertRow(Connection connection, String dataBaseName, String tableName, List<String> columnNames, List<String> values )  throws SQLException {
		deleteRow(connection, dataBaseName, tableName, columnNames.get(0), values.get(0));
		insertRow(connection, dataBaseName,  tableName, columnNames, values);
		}
	/**
	 * Insere uma linha caso n�o exista antes no banco de dados um valor igual ao de selColumn ou atualiza a linha caso contr�rio.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param oldValue
	 * @throws SQLException
	 */
	public static void insertOrUdateRow(Connection connection, String dataBaseName, String tableName, String[] columnNames, String[] values, String selColumn, String oldValue)  throws SQLException {
		updateRow(connection, dataBaseName, tableName, columnNames, values, columnNames[0], values[0]);
		insertRow(connection, dataBaseName, tableName, columnNames, values);
		}
	/**
	 * Insere uma linha caso n�o exista antes no banco de dados um valor igual ao de selColumn ou atualiza a linha caso contr�rio.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param oldValue
	 * @throws SQLException
	 */
	public static void insertOrUdateRow(Connection connection, String dataBaseName, String tableName, List<String> columnNames, List<String> values, String selColumn, String oldValue)  throws SQLException {
		updateRow(connection, dataBaseName, tableName, columnNames, values, columnNames.get(0), values.get(0));
		insertRow(connection, dataBaseName, tableName, columnNames, values);
		}
	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertRows(Connection connection, String dataBaseName, String tableName, String[] columnNames, String[][] values)  throws SQLException {
		makePreparedSql( connection, insertRowsPreparedSql(dataBaseName, tableName, columnNames), values);//, pos);
		}
	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertRows(Connection connection, String dataBaseName, String tableName, String[] columnNames, List<List<String>> values)  throws SQLException {
		makePreparedSql( connection, insertRowsPreparedSql(dataBaseName, tableName, columnNames), values);
		}
	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertRows(Connection connection, String dataBaseName, String tableName, List<String> columnNames, Collection<String[]> values)  throws SQLException {
		makePreparedSql( connection, insertRowsPreparedSql(dataBaseName, tableName, columnNames), values);
		}
	/**
	 * Insere um conjunto de linhas no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @throws SQLException
	 */
	public static void insertRows(Connection connection, String dataBaseName, String tableName, List<String> columnNames, List<List<String>> values)  throws SQLException {
		makePreparedSql( connection, insertRowsPreparedSql(dataBaseName, tableName, columnNames), values);
		}
	

	/**
	 * Altera o tipo de uma coluna no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnName
	 * @param newType
	 * @throws SQLException
	 */
	public static void alterColumnType(Connection connection, String dataBaseName, String tableName, String columnName, String newType)  throws SQLException {
		makeSql( connection, alterColumnTypeSql(dataBaseName, tableName, columnName, newType));
	}
	/**
	 * Altera o tipo de uma coluna no banco de dados.
	 * @param connection
	 * @param dataBaseName
	 * @param tableName
	 * @param columnName
	 * @param newType
	 * @throws SQLException
	 */
	public static void alterColumnType(Connection connection, String tableName, String columnName, String newType)  throws SQLException {
		makeSql( connection, alterColumnTypeSql(tableName, columnName, newType));
	}

	/**
	 * Instru��o SQL: ALTER SCHEMA oldSchemaName RENAME TO newSchemaName
	 * @param schemaName
	 * @return
	 */
	public static String alterSchemaNameSql(String oldSchemaName, String newSchemaName) {
		String sql = "ALTER SCHEMA " +  oldSchemaName + " RENAME TO " + newSchemaName;
		return sql;
		}
	/**
	 * Instru��o SQL: CREATE SCHEMA IF NOT EXISTS TESTE
	 * @param schemaName
	 * @return
	 */
	public static String createSchemaIfNotExistsSql(String schemaName) {
		String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
		return sql;
		}
	/**
	 * CREATE SCHEMA IF NOT EXISTS TESTE
	 * @param schemaName
	 * @return
	 */
	public static String deleteSchemaIfExistsSql(String schemaName) {
		String sql = "DROP SCHEMA IF EXISTS " + schemaName;
		return sql;
		}
	/**
	 * CREATE TABLE IF NOT EXISTS table_name (column1 datatype, column2 datatype, ...)
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param columnTypes
	 * @return
	 */
	public static String createTableIfNotExistsSql(String dataBaseName, String tableName, String[] columnNames, String[] columnTypes) {
		String sql = "CREATE TABLE IF NOT EXISTS " + dataBaseName + "." + tableName +"\n(";
		for (int i = 0; i < columnNames.length; i++) {
			sql = sql + columnNames[i] + " " + columnTypes[i];
		}
		sql = sql + "\nPRIMARY KEY(" + columnNames[0] + ") )";
		
		return sql;
		}
	/**
	 * CREATE TABLE IF NOT EXISTS table_name (column1 datatype, column2 datatype, ...)
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param columnTypes
	 * @return
	 */
	public static String createTableIfNotExistsSql(String dataBaseName, String tableName, List<String> columnNames, List<String> columnTypes) {
		String sql = "CREATE TABLE IF NOT EXISTS " + dataBaseName + "." + tableName +"\n(";
		
		for (int i = 0; i < columnNames.size(); i++) {
			sql = sql + columnNames.get(i) + " " + columnTypes.get(i) + ",";
		}
		sql = sql + "\nPRIMARY KEY(" + columnNames.get(0) + ") )";
		
		return sql;
		}
	/**
	 * DROP TABLE IF EXISTS dataBaseName.tableName
	 * @param dataBaseName
	 * @param tableName
	 * @return
	 */
	public static String deleteTableIfExistSql(String dataBaseName, String tableName) {
		String sql = "DROP TABLE IF EXISTS ";
		sql = sql + dataBaseName + "." + tableName;
		
		return sql;
		}
	/**
	 * DROP TABLE IF EXISTS tableName
	 * @param tableName
	 * @return
	 */
	public static String deleteTableIfExistSql(String tableName) {
		String sql = "DROP TABLE IF EXISTS ";
		sql = sql + tableName;
		
		return sql;
		}
	/**
	 * DELETE FROM schemaName.tableName
	 * @param schemaName
	 * @param tableName
	 * @return
	 */
	public static String deleteTableExistingDataSql( String schemaName, String tableName) {
		String sql = "DELETE FROM " + schemaName + "." + tableName;
		return sql;
	}
	/**
	 * TRUNCATE SCHEMA  schemaName AND COMMIT
	 * Apaga dados da tabelas, mas as mant�m.
	 * @param schemaName
	 * @return
	 */
	public static String deleteEveryTableExistingDataSql( String schemaName) {
		String sql = "TRUNCATE SCHEMA " + schemaName + " AND COMMIT";
		return sql;
	}

	/**
	 * INSERT INTO table_name (column1, column2, ...) VALUES (new_value1, new_value2, ...)
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowSql(String dataBaseName, String tableName, String[] columnNames, String[] values) {
		String sql = "INSERT INTO " + dataBaseName + "." + tableName + " (" + listToString(columnNames, null);
		sql = sql + "\nVALUES(" + listToString(values, "'") + ")";
		
		return sql;
		}	
	/**
	 * INSERT INTO table_name (column1, column2, ...) VALUES (new_value1, new_value2, ...)
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowSql(String tableName, String[] columnNames, String[] values) {
		String sql = "INSERT INTO " + tableName + " (" + listToString(columnNames, null);
		sql = sql + "\nVALUES(" + listToString(values, "'") + ")";
		
		return sql;
		}
	/**
	 *  INSERT INTO table_name (column1, column2, ...)  VALUES (new_value1, new_value2, ...)
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowSql(String dataBaseName, String tableName, List<String> columnNames, List<String> values) {
		String sql = "INSERT INTO " + dataBaseName + "." + tableName + " (" + listToString(columnNames, null);
		sql = sql + "\nVALUES(" + listToString(values, "'") + ")";
		
		return sql;
		}	
	/**
	 * INSERT INTO table_name (column1, column2, ...)  VALUES (new_value1, new_value2, ...)
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowSql(String tableName, List<String> columnNames, List<String> values) {
		String sql = "INSERT INTO " + tableName + " (" + listToString(columnNames, null);
		sql = sql + "\nVALUES(" + listToString(values, "'") + ")";
		
		return sql;
		}
	
	/**
	 * UPDATE table_name SET column1= new_value1, column2= new_value2, ... 
	 * WHERE column1= old_value1
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param old_value
	 * @return
	 */	
	public static String updateRowSql(String dataBaseName, String tableName, String[] columnNames, String[] values, String selColumn, String old_value) {
		int n = columnNames.length;
		String sql = "UPDATE " + dataBaseName + "." + tableName + "\nSET ";

		for (int i = 0; i < n; i++) {
			sql = sql + columnNames[i] + "= '" + values[i] + "'";
			if (i < n - 1) {
				sql = sql + ", ";
			}
		}
		
		sql = sql + "\nWHERE " + selColumn + "= " + old_value;
		
		return sql;
		}
	/**
	 * UPDATE table_name SET column1= new_value1, column2= new_value2, ... 
	 * WHERE column1= old_value1
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param old_value
	 * @return
	 */
	public static String updateRowSql(String dataBaseName, String tableName, List<String> columnNames, List<String> values, String selColumn, String old_value) {
		int n = columnNames.size();
		String sql = "UPDATE " + dataBaseName + "." + tableName + "\nSET ";

		for (int i = 0; i < n; i++) {
			sql = sql + columnNames.get(i) + "= '" + values.get(i) + "'";
			if (i < n - 1) {
				sql = sql + ", ";
			}
		}
		
		sql = sql + "\nWHERE " + selColumn + "= " + old_value;
		
		return sql;
		}
	/**
	 * DELETE FROM table_name WHERE selColumn= old_value	
	 * @param dataBaseName
	 * @param tableName
	 * @param selColumn
	 * @param oldValue
	 * @return
	 */
	public static String deleteRowSql(String dataBaseName, String tableName, String selColumn, String oldValue) {
		String sql = "DELETE FROM " + dataBaseName + "." + tableName;		
		sql = sql + "\nWHERE " + selColumn + "= " + oldValue;
		
		return sql;
		}

	/**
	 * INSERT INTO dataBasName.tableName (columnNames...) VALUES (values...) 
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowsSql(String dataBaseName, String tableName, String[] columnNames, String[] values) {
		String sql = "INSERT INTO " + dataBaseName + "." + tableName + " (" + listToString(columnNames, null) + ")";
		sql = sql + "\nVALUES(" + listToString(values, "'") + ")";
		
		return sql;
		}
	/**
	 * INSERT INTO dataBasName.tableName (columnNames...) VALUES (values...) 
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowsPreparedSql(String dataBaseName, String tableName, String[] columnNames) {//, int pos) {
		int n = columnNames.length;
		String sql = "INSERT INTO " + dataBaseName + "." + tableName + " (" + listToString(columnNames, null) + ")";
		sql = sql + "\nVALUES(" + argListToString(n) + ")";
		
		return sql;
		}
	/**
	 * INSERT INTO dataBasName.tableName (columnNames...) VALUES (values...) 
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertRowsPreparedSql(String dataBaseName, String tableName, List<String> columnNames) {//, int pos) {
		int n = columnNames.size();
		String sql = "INSERT INTO " + dataBaseName + "." + tableName + " (" + listToString(columnNames, null) + ")";
		sql = sql + "\nVALUES(" + argListToString(n) + ")";
		
		return sql;
		}
	/**
	 * INSERT INTO dataBasName.tableName (columnNames...) VALUES (values...)
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static String insertMultipleRowsPreparedSql(String dataBaseName, String tableName, List<String> columnNames, int quantity) {//, int pos) {
		int n = columnNames.size();
		String sql = "INSERT INTO " + dataBaseName + "." + tableName + " (" + listToString(columnNames, null) + ")";
		sql = sql + "\nVALUES\n";
		
		String str = "(" + argListToString(n) + ")";
		String[] array = new String[quantity];
		Arrays.fill(array, str);
		
		sql += String.join(",", array);
		sql += ";";				
		
		return sql;
		}
	/**
	 * DELETE FROM dataBaseName.tableName WHERE selColumn= values[0] OR sigla= values[1] OR selColumn = value[2] ... OR selColumn = value[n - 1]
	 * @param dataBaseName
	 * @param tableName
	 * @param selColumn
	 * @param values
	 * @return
	 */
	public static String deleteRowsSql(String dataBaseName, String tableName, String selColumn, String[] values) {
		String sql = "DELETE FROM " + dataBaseName + "." + tableName + "\nWHERE ";
		
		for(int i = 0; i<  values.length; i++) {
			if (i > 0) {
				sql = sql + "\nOR ";
			}
			sql = sql + selColumn + "= '" + values[i] + "'";
		}
		return sql;
	}
	/**
	 * DELETE FROM dataBaseName.tableName WHERE selColumn= values[0] OR sigla= values[1] OR selColumn = value[2] ... OR selColumn = value[n - 1]
	 * @param dataBaseName
	 * @param tableName
	 * @param selColumn
	 * @param values
	 * @return
	 */
	public static String deleteRowsPreparedSql(String dataBaseName, String tableName, String selColumn, int n) {
		String sql = "DELETE FROM " + dataBaseName + "." + tableName + "\nWHERE ";
		
		for(int i = 0; i< n; i++) {
			if (i > 0) {
				sql = sql + "\nOR ";
			}
			sql = sql  + selColumn + "= ?";
		}
		return sql;
	}
	/**
	 * UPDATE dataBaseName.table_name SET columnNames[0]= values[0], columnNames[1]= values[1], ...
	 * WHERE selColumn= oldValues[0] OR selColumn= oldValues[1]...  OR selColumn= oldValues[n - 1]
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param oldValues
	 * @return
	 */	  
	public static String updateRowsSql(String dataBaseName, String tableName, String[] columnNames, String[] values, String selColumn, String[] oldValues) {
		int n = columnNames.length;
		String sql = "UPDATE " + dataBaseName + "." + tableName + "\nSET ";

		for (int i = 0; i < n; i++) {
			sql = sql + columnNames[i] + "= '" + values[i] + "'";
			if (i < n - 1) {
				sql = sql + ", ";
			}
		}
		
		n = oldValues.length;
		for(int i = 0; i<  oldValues.length; i++) {
			if (i > 0) {
				sql = sql + "\nOR ";
				}
			sql = sql + selColumn + "= '" + oldValues[i] + "'";
			if (i < n - 1) {
				sql = sql + ", ";
			}
		}
		
		return sql;
		}
	/**
	 * UPDATE dataBaseName.table_name SET columnNames[0]= values[0], columnNames[1]= values[1], ...
	 * WHERE selColumn= oldValues[0] OR selColumn= oldValues[1]...  OR selColumn= oldValues[n - 1]
	 * @param dataBaseName
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param selColumn
	 * @param oldValues
	 * @return
	 */	
	public static String updateRowsPreparedSql(String dataBaseName, String tableName, String[] columnNames, int n, String selColumn, String[] oldValues) {
		String sql = "UPDATE " + dataBaseName + "." + tableName + "\nSET ";

		for (int i = 0; i < n; i++) {
			sql = sql + columnNames[i] + "= ?";
			if (i < n - 1) {
				sql = sql + ", ";
			}
		}
		
		n = oldValues.length;
		for(int i = 0; i<  oldValues.length; i++) {
			if (i > 0) {
				sql = sql + "\nOR ";
				}
			sql = sql + selColumn + "= '" + oldValues[i] + "'";
			if (i < n - 1) {
				sql = sql + ", ";
			}
		}
		
		return sql;
		}
	
	/**
	 * ALTER TABLE dataBaseName.table_name ALTER COLUMN columnName newType
	 * @param dataBaseName
	 * @param tableName
	 * @param columnName
	 * @param newType
	 * @return
	 */
	public static String alterColumnTypeSql(String dataBaseName, String tableName, String columnName, String newType) {
		String sql = "ALTER TABLE " + dataBaseName + "." + tableName + "\nALTER COLUMN " + columnName + " " + newType.toUpperCase();
		return sql;
		}
	/**
	 * ALTER TABLE table_name ALTER COLUMN columnName newType
	 * @param dataBaseName
	 * @param tableName
	 * @param columnName
	 * @param newType
	 * @return
	 */
	public static String alterColumnTypeSql(String tableName, String columnName, String newType) {
		String sql = "ALTER TABLE " + tableName + "\nALTER COLUMN " + columnName + " " + newType.toUpperCase();
		return sql;
		}
	
	/**
	 * Cria uma string com n pontos de interroga��o (?) separados por v�rgula.
	 * @param n
	 * @return
	 */
	private static String argListToString(int n) {
		if (n > 0) {
			String str = "?";
			for (int i = 1; i < n; i ++) {
				str = str + ", " + "?";
				}
			return str;
		}else {
			return "";
		}
	}	
	/**
	 * Converte uma lista em uma string separada por v�rgulas.
	 * @param <T>
	 * @param array
	 * @param 
	 * 		quote - string que cricular� as palvras da lista ao adicion�-las � string final.
	 * @return
	 */
	private static <T> String listToString(T[] array, String quote) {
	    	if(array == null) {
				return "";
			}else{
				int n = array.length;
				
				String str = ""; 
				
				if (quote == null)
					quote = "";
					
				for(int i = 0; i < n; i++) {
					str = str + quote + array[i].toString() + quote;					
					if ( i < n- 1) {
						str = str + ", ";
					}
				}
				return str;
			}
		}
	/**
	 * Converte uma lista em uma string separada por v�rgulas.
	 * @param <T>
	 * @param array
	 * @param 
	 * 		quote - string que cricular� as palvras da lista ao adicion�-las � string final.
	 * @return
	 */
	private static <T> String listToString(List<T> array, String quote) {
    	if(array == null) {
			return "";
		}else{
			int n = array.size();
			
			String str = ""; 
			
			if (quote == null)
				quote = "";
				
			for(int i = 0; i < n; i++) {
				str = str + quote + array.get(i).toString() + quote;					
				if ( i < n- 1) {
					str = str + ", ";
				}
			}
			return str;
		}
	}
}
