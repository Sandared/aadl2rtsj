package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.Subcomponent;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.List;

public class DataImplConverter{
	
  protected static String nl;
  public static synchronized DataImplConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    DataImplConverter result = new DataImplConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "public class ";
  protected final String TEXT_3 = "{" + NL + "\t" + NL + "\t";
  protected final String TEXT_4 = NL + "}";

	private static String getSubcomponentMemberStatements(DataImplementation component){
		List<Subcomponent> subcomponents = component.getAllSubcomponents();
		if(subcomponents.size() > 0){
			StringBuilder sb = new StringBuilder();
			sb.append("\t/*\n");
			sb.append("\t* SUBCOMPONENTS\n");
			sb.append("\t*/\n");
			//skip the first one as it is the component itself
			for(int i = 0; i < subcomponents.size(); i++){
				sb.append(new MemberStatement().generate(subcomponents.get(i)));
			}
			return sb.toString();
		}
		else{
			return "";
		}
	}
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(DataImplementation component)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(component));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getClassName(component));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getSubcomponentMemberStatements(component));
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}