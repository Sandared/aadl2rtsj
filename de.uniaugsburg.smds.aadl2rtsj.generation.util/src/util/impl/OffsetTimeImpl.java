/**
 */
package util.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.osate.aadl2.instance.ConnectionInstance;

import util.OffsetTime;
import util.UtilPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Offset Time</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link util.impl.OffsetTimeImpl#getMs <em>Ms</em>}</li>
 *   <li>{@link util.impl.OffsetTimeImpl#getNs <em>Ns</em>}</li>
 *   <li>{@link util.impl.OffsetTimeImpl#getUniqueId <em>Unique Id</em>}</li>
 *   <li>{@link util.impl.OffsetTimeImpl#getIoTime <em>Io Time</em>}</li>
 *   <li>{@link util.impl.OffsetTimeImpl#getConnection <em>Connection</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OffsetTimeImpl extends MinimalEObjectImpl.Container implements OffsetTime {
	/**
	 * The default value of the '{@link #getMs() <em>Ms</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMs()
	 * @generated
	 * @ordered
	 */
	protected static final long MS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMs() <em>Ms</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMs()
	 * @generated
	 * @ordered
	 */
	protected long ms = MS_EDEFAULT;

	/**
	 * The default value of the '{@link #getNs() <em>Ns</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNs()
	 * @generated
	 * @ordered
	 */
	protected static final long NS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getNs() <em>Ns</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNs()
	 * @generated
	 * @ordered
	 */
	protected long ns = NS_EDEFAULT;

	/**
	 * The default value of the '{@link #getUniqueId() <em>Unique Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUniqueId()
	 * @generated
	 * @ordered
	 */
	protected static final int UNIQUE_ID_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getUniqueId() <em>Unique Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUniqueId()
	 * @generated
	 * @ordered
	 */
	protected int uniqueId = UNIQUE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getIoTime() <em>Io Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIoTime()
	 * @generated
	 * @ordered
	 */
	protected static final String IO_TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIoTime() <em>Io Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIoTime()
	 * @generated
	 * @ordered
	 */
	protected String ioTime = IO_TIME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConnection() <em>Connection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnection()
	 * @generated
	 * @ordered
	 */
	protected ConnectionInstance connection;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OffsetTimeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UtilPackage.Literals.OFFSET_TIME;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMs() {
		return ms;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMs(long newMs) {
		long oldMs = ms;
		ms = newMs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.OFFSET_TIME__MS, oldMs, ms));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getNs() {
		return ns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNs(long newNs) {
		long oldNs = ns;
		ns = newNs;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.OFFSET_TIME__NS, oldNs, ns));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getUniqueId() {
		return uniqueId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUniqueId(int newUniqueId) {
		int oldUniqueId = uniqueId;
		uniqueId = newUniqueId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.OFFSET_TIME__UNIQUE_ID, oldUniqueId, uniqueId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIoTime() {
		return ioTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIoTime(String newIoTime) {
		String oldIoTime = ioTime;
		ioTime = newIoTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.OFFSET_TIME__IO_TIME, oldIoTime, ioTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectionInstance getConnection() {
		if (connection != null && connection.eIsProxy()) {
			InternalEObject oldConnection = (InternalEObject)connection;
			connection = (ConnectionInstance)eResolveProxy(oldConnection);
			if (connection != oldConnection) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, UtilPackage.OFFSET_TIME__CONNECTION, oldConnection, connection));
			}
		}
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectionInstance basicGetConnection() {
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnection(ConnectionInstance newConnection) {
		ConnectionInstance oldConnection = connection;
		connection = newConnection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.OFFSET_TIME__CONNECTION, oldConnection, connection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UtilPackage.OFFSET_TIME__MS:
				return getMs();
			case UtilPackage.OFFSET_TIME__NS:
				return getNs();
			case UtilPackage.OFFSET_TIME__UNIQUE_ID:
				return getUniqueId();
			case UtilPackage.OFFSET_TIME__IO_TIME:
				return getIoTime();
			case UtilPackage.OFFSET_TIME__CONNECTION:
				if (resolve) return getConnection();
				return basicGetConnection();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case UtilPackage.OFFSET_TIME__MS:
				setMs((Long)newValue);
				return;
			case UtilPackage.OFFSET_TIME__NS:
				setNs((Long)newValue);
				return;
			case UtilPackage.OFFSET_TIME__UNIQUE_ID:
				setUniqueId((Integer)newValue);
				return;
			case UtilPackage.OFFSET_TIME__IO_TIME:
				setIoTime((String)newValue);
				return;
			case UtilPackage.OFFSET_TIME__CONNECTION:
				setConnection((ConnectionInstance)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case UtilPackage.OFFSET_TIME__MS:
				setMs(MS_EDEFAULT);
				return;
			case UtilPackage.OFFSET_TIME__NS:
				setNs(NS_EDEFAULT);
				return;
			case UtilPackage.OFFSET_TIME__UNIQUE_ID:
				setUniqueId(UNIQUE_ID_EDEFAULT);
				return;
			case UtilPackage.OFFSET_TIME__IO_TIME:
				setIoTime(IO_TIME_EDEFAULT);
				return;
			case UtilPackage.OFFSET_TIME__CONNECTION:
				setConnection((ConnectionInstance)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case UtilPackage.OFFSET_TIME__MS:
				return ms != MS_EDEFAULT;
			case UtilPackage.OFFSET_TIME__NS:
				return ns != NS_EDEFAULT;
			case UtilPackage.OFFSET_TIME__UNIQUE_ID:
				return uniqueId != UNIQUE_ID_EDEFAULT;
			case UtilPackage.OFFSET_TIME__IO_TIME:
				return IO_TIME_EDEFAULT == null ? ioTime != null : !IO_TIME_EDEFAULT.equals(ioTime);
			case UtilPackage.OFFSET_TIME__CONNECTION:
				return connection != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (ms: ");
		result.append(ms);
		result.append(", ns: ");
		result.append(ns);
		result.append(", uniqueId: ");
		result.append(uniqueId);
		result.append(", ioTime: ");
		result.append(ioTime);
		result.append(')');
		return result.toString();
	}

} //OffsetTimeImpl
