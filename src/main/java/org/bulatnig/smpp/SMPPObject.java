package org.bulatnig.smpp;

import java.io.Serializable;

/**
 * General SMPP Object.
 * 
 * @author Bulat Nigmatullin
 *
 */
public class SMPPObject implements Serializable {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getName() + " Object {}";
	}
	
}
