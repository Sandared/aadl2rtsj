package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.internal.core.util.MethodParametersAttribute;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.FeatureInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

public class ActiveDirectedConnectionConverter{
	
  protected static String nl;
  public static synchronized ActiveDirectedConnectionConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    ActiveDirectedConnectionConverter result = new ActiveDirectedConnectionConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + NL + "public class ";
  protected final String TEXT_5 = "{" + NL + "\t" + NL + "\t";
  protected final String TEXT_6 = NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_7 = "(";
  protected final String TEXT_8 = "){" + NL + "\t\t";
  protected final String TEXT_9 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Write the value which will then be transmitted to the next connection in line" + NL + "\t * @param value the new value to be transmitted" + NL + "\t */" + NL + "\tpublic void putValue(";
  protected final String TEXT_10 = " value){" + NL + "\t\t";
  protected final String TEXT_11 = NL + "\t}" + NL + "}";

	private static String getConstructorParameters(ConnectionInstance connection){
		StringBuilder sb = new StringBuilder();
		for (ConnectionInstance con : getNextConnections(connection)) {
			sb.append(new MethodParameterStatement().generate(con));
		}
		return sb.toString().trim();
	}
	
	private static String getConstructorAssignmentStatements(ConnectionInstance connection){
		StringBuilder sb = new StringBuilder();
		for (ConnectionInstance con : getNextConnections(connection)) {
			sb.append(new ConstructorAssignmentStatement().generate(con));
		}
		return sb.toString().trim();
	}
	
	private static String getNextConnectionsMemberStatements(ConnectionInstance connection){
		StringBuilder sb = new StringBuilder();
		for (ConnectionInstance con : getNextConnections(connection)) {
			sb.append(new DeclarationMemberStatement().generate(con));
		}
		return sb.toString().trim();
	}
	
	private static String getNextConnectionsPutValueStatements(ConnectionInstance connection){
		StringBuilder sb = new StringBuilder();
		for (ConnectionInstance con : getNextConnections(connection)) {
			sb.append(new PutValueStatement().generate(con));
		}
		return sb.toString().trim();
	}
	
	private static String getNextConnectionsImportStatements(ConnectionInstance connection){
		StringBuilder sb = new StringBuilder();
		for (ConnectionInstance con : getNextConnections(connection)) {
			sb.append(new ImportStatement().generate(con));
		}
		return sb.toString().trim();
	}
	
	private static String getConnectionDataType(ConnectionInstance connection){
		// we don't consider splitting or aggregation of data types currently, so it's sufficient to check one connectionend for its datatype
		// the end must be a FeatureInstance as we only consider data ports at the moment
		return getDataType((FeatureInstance)connection.getSource());
	}
	
	//Helper Method
	private static List<ConnectionInstance> getNextConnections(ConnectionInstance connection){
		// TODO: maybe we need to treat data targets differently 
		return connection.getDestination().getSrcConnectionInstances();
	}
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ConnectionInstance connection)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(connection));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getNextConnectionsImportStatements(connection));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getClassName(connection));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getNextConnectionsMemberStatements(connection));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getClassName(connection));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getConstructorParameters(connection));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getConstructorAssignmentStatements(connection));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getConnectionDataType(connection));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getNextConnectionsPutValueStatements(connection));
    stringBuffer.append(TEXT_11);
    return stringBuffer.toString();
  }
}