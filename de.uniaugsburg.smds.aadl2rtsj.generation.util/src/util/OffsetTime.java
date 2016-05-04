/**
 */
package util;

import org.eclipse.emf.ecore.EObject;

import org.osate.aadl2.instance.ConnectionInstance;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Offset Time</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link util.OffsetTime#getMs <em>Ms</em>}</li>
 *   <li>{@link util.OffsetTime#getNs <em>Ns</em>}</li>
 *   <li>{@link util.OffsetTime#getUniqueId <em>Unique Id</em>}</li>
 *   <li>{@link util.OffsetTime#getIoTime <em>Io Time</em>}</li>
 *   <li>{@link util.OffsetTime#getConnection <em>Connection</em>}</li>
 * </ul>
 *
 * @see util.UtilPackage#getOffsetTime()
 * @model
 * @generated
 */
public interface OffsetTime extends EObject {
	/**
	 * Returns the value of the '<em><b>Ms</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ms</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ms</em>' attribute.
	 * @see #setMs(long)
	 * @see util.UtilPackage#getOffsetTime_Ms()
	 * @model
	 * @generated
	 */
	long getMs();

	/**
	 * Sets the value of the '{@link util.OffsetTime#getMs <em>Ms</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ms</em>' attribute.
	 * @see #getMs()
	 * @generated
	 */
	void setMs(long value);

	/**
	 * Returns the value of the '<em><b>Ns</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ns</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ns</em>' attribute.
	 * @see #setNs(long)
	 * @see util.UtilPackage#getOffsetTime_Ns()
	 * @model
	 * @generated
	 */
	long getNs();

	/**
	 * Sets the value of the '{@link util.OffsetTime#getNs <em>Ns</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ns</em>' attribute.
	 * @see #getNs()
	 * @generated
	 */
	void setNs(long value);

	/**
	 * Returns the value of the '<em><b>Unique Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unique Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unique Id</em>' attribute.
	 * @see #setUniqueId(int)
	 * @see util.UtilPackage#getOffsetTime_UniqueId()
	 * @model
	 * @generated
	 */
	int getUniqueId();

	/**
	 * Sets the value of the '{@link util.OffsetTime#getUniqueId <em>Unique Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unique Id</em>' attribute.
	 * @see #getUniqueId()
	 * @generated
	 */
	void setUniqueId(int value);

	/**
	 * Returns the value of the '<em><b>Io Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Io Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Io Time</em>' attribute.
	 * @see #setIoTime(String)
	 * @see util.UtilPackage#getOffsetTime_IoTime()
	 * @model
	 * @generated
	 */
	String getIoTime();

	/**
	 * Sets the value of the '{@link util.OffsetTime#getIoTime <em>Io Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Io Time</em>' attribute.
	 * @see #getIoTime()
	 * @generated
	 */
	void setIoTime(String value);

	/**
	 * Returns the value of the '<em><b>Connection</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection</em>' reference.
	 * @see #setConnection(ConnectionInstance)
	 * @see util.UtilPackage#getOffsetTime_Connection()
	 * @model
	 * @generated
	 */
	ConnectionInstance getConnection();

	/**
	 * Sets the value of the '{@link util.OffsetTime#getConnection <em>Connection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection</em>' reference.
	 * @see #getConnection()
	 * @generated
	 */
	void setConnection(ConnectionInstance value);


} // OffsetTime
