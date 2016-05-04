/**
 */
package util;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see util.UtilFactory
 * @model kind="package"
 * @generated
 */
public interface UtilPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "util";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.uniaugsburg.de/smds/aadl2rtsj/generation/util";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "generationutil";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UtilPackage eINSTANCE = util.impl.UtilPackageImpl.init();

	/**
	 * The meta object id for the '{@link util.impl.OffsetTimeImpl <em>Offset Time</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see util.impl.OffsetTimeImpl
	 * @see util.impl.UtilPackageImpl#getOffsetTime()
	 * @generated
	 */
	int OFFSET_TIME = 0;

	/**
	 * The feature id for the '<em><b>Ms</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME__MS = 0;

	/**
	 * The feature id for the '<em><b>Ns</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME__NS = 1;

	/**
	 * The feature id for the '<em><b>Unique Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME__UNIQUE_ID = 2;

	/**
	 * The feature id for the '<em><b>Io Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME__IO_TIME = 3;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME__CONNECTION = 4;

	/**
	 * The number of structural features of the '<em>Offset Time</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Offset Time</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_TIME_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link util.OffsetTime <em>Offset Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Offset Time</em>'.
	 * @see util.OffsetTime
	 * @generated
	 */
	EClass getOffsetTime();

	/**
	 * Returns the meta object for the attribute '{@link util.OffsetTime#getMs <em>Ms</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ms</em>'.
	 * @see util.OffsetTime#getMs()
	 * @see #getOffsetTime()
	 * @generated
	 */
	EAttribute getOffsetTime_Ms();

	/**
	 * Returns the meta object for the attribute '{@link util.OffsetTime#getNs <em>Ns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ns</em>'.
	 * @see util.OffsetTime#getNs()
	 * @see #getOffsetTime()
	 * @generated
	 */
	EAttribute getOffsetTime_Ns();

	/**
	 * Returns the meta object for the attribute '{@link util.OffsetTime#getUniqueId <em>Unique Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique Id</em>'.
	 * @see util.OffsetTime#getUniqueId()
	 * @see #getOffsetTime()
	 * @generated
	 */
	EAttribute getOffsetTime_UniqueId();

	/**
	 * Returns the meta object for the attribute '{@link util.OffsetTime#getIoTime <em>Io Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Io Time</em>'.
	 * @see util.OffsetTime#getIoTime()
	 * @see #getOffsetTime()
	 * @generated
	 */
	EAttribute getOffsetTime_IoTime();

	/**
	 * Returns the meta object for the reference '{@link util.OffsetTime#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Connection</em>'.
	 * @see util.OffsetTime#getConnection()
	 * @see #getOffsetTime()
	 * @generated
	 */
	EReference getOffsetTime_Connection();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UtilFactory getUtilFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link util.impl.OffsetTimeImpl <em>Offset Time</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see util.impl.OffsetTimeImpl
		 * @see util.impl.UtilPackageImpl#getOffsetTime()
		 * @generated
		 */
		EClass OFFSET_TIME = eINSTANCE.getOffsetTime();

		/**
		 * The meta object literal for the '<em><b>Ms</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_TIME__MS = eINSTANCE.getOffsetTime_Ms();

		/**
		 * The meta object literal for the '<em><b>Ns</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_TIME__NS = eINSTANCE.getOffsetTime_Ns();

		/**
		 * The meta object literal for the '<em><b>Unique Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_TIME__UNIQUE_ID = eINSTANCE.getOffsetTime_UniqueId();

		/**
		 * The meta object literal for the '<em><b>Io Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_TIME__IO_TIME = eINSTANCE.getOffsetTime_IoTime();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OFFSET_TIME__CONNECTION = eINSTANCE.getOffsetTime_Connection();

	}

} //UtilPackage
