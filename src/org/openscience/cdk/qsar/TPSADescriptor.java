/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2004  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openscience.cdk.qsar;

import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.Ring;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.aromaticity.HueckelAromaticityDetector;
import java.util.Vector;
import java.util.HashMap;
import org.openscience.cdk.RingSet;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.qsar.result.*;

/**
 *  Calculation of polar surface area based on fragment contributions (TPSA),
 *  For description of the methodology see : 
 *  http://pubs.acs.org/cgi-bin/article.cgi/jmcmar/2000/43/i20/html/jm000942e.html
 *
 * @author      mfe4
 * @cdk.created 2004-11-03
 * @cdk.module  qsar
 * @cdk.set     qsar-descriptors
 */

public class TPSADescriptor implements Descriptor {
	private boolean checkAromaticity = false;
	/**
	 *  Constructor for the TPSADescriptor object
	 */
	public TPSADescriptor() { }


	/**
	 *  Gets the specification attribute of the
	 *  TPSADescriptor object
	 *
	 *@return    The specification value
	 */
	public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
            "http://qsar.sourceforge.net/dicts/qsar-descriptors:tpsa",
		    this.getClass().getName(),
		    "$Id$",
            "The Chemistry Development Kit");
	}


	/**
	 *  Sets the parameters attribute of the
	 *  TPSADescriptor object
	 *
	 *@param  params            The new parameters value
	 *@exception  CDKException  Description of the Exception
	 */
	public void setParameters(Object[] params) throws CDKException {
		if (params.length > 1) {
			throw new CDKException("TPSADescriptor only expects one parameter");
		}
		if (!(params[0] instanceof Boolean)) {
			throw new CDKException("The first parameter must be of type Boolean");
		}
		// ok, all should be fine
		checkAromaticity = ((Boolean) params[0]).booleanValue();
	}


	/**
	 *  Gets the parameters attribute of the
	 *  TPSADescriptor object
	 *
	 *@return    The parameters value
	 */
	public Object[] getParameters() {
		// return the parameters as used for the descriptor calculation
		Object[] params = new Object[1];
		params[0] = new Boolean(checkAromaticity);
		return params;
	}


	/**
	 *  calculates the TPSA for an atom container
	 *  if checkAromaticity is true, the method check the aromaticity,
	 *  if false, means that the aromaticity has already been checked.
	 *  It is necessary to use before the addExplicitHydrogensToSatisfyValency method (HydrogenAdder classe).
	 *
	 *@param  ac                AtomContainer
	 *@return                   TPSA is a double
	 *@exception  CDKException  Possible Exceptions
	 */
	public DescriptorResult calculate(AtomContainer ac) throws CDKException {
		
		// contributions:
		// every contribution is given by an atom profile;
		// positions in atom profile strings are: symbol, max-bond-order, bond-order-sum,
		// number-of-neighboors, Hcount, aromatic-bonds, charge, is-in-3-membered-ring, 
		// single-bonds, double-bonds, triple-bonds.
		HashMap map = new HashMap();
		map.put("N+1.0+3.0+3+0+0.0+0+0+3+0+0", new Double(3.24)); // 1
		map.put("N+2.0+3.0+2+0+0.0+0+0+1+1+0", new Double(12.36)); // 2
		//       N+2.0+3.0+2+0+0.0+0+0+1+1+0
		map.put("N+3.0+3.0+1+0+0.0+0+0+0+0+1", new Double(23.79)); // 3
		map.put("N+2.0+5.0+3+0+0.0+0+0+1+2+0", new Double(11.68));  // 4
		//       N+2.0+5.0+3+0+0.0+0+0+1+2+0
		map.put("N+3.0+5.0+2+0+0.0+0+0+0+1+1", new Double(13.6)); // 5
		//       N+3.0+5.0+2+0+0.0+0+0+0+1+1
		map.put("N+1.0+3.0+3+0+0.0+0+1+3+0+0", new Double(3.01)); // 6
		//       N+1.0+3.0+3+0+0.0+0+1+3+0+0
		map.put("N+1.0+3.0+3+1+0.0+0+0+3+0+0", new Double(12.03));  // 7
		//       N+1.0+3.0+3+1+0.0+0+0+3+0+0
		//       N+1.0+3.0+3+1+0.0+0+0+3+0+0
		map.put("N+1.0+3.0+3+1+0.0+0+1+3+0+0", new Double(21.94)); // 8
		map.put("N+2.0+3.0+2+1+0.0+0+0+1+1+0", new Double(23.85));  //9
		map.put("N+1.0+3.0+3+2+0.0+0+0+3+0+0", new Double(26.02));  // 10
		map.put("N+1.0+4.0+4+0+1.0+0+0+4+0+0", new Double(0.0));  //11
		map.put("N+2.0+4.0+3+0+1.0+0+0+2+1+0", new Double(3.01));  //12
		map.put("N+3.0+4.0+2+0+1.0+0+0+1+0+1", new Double(4.36));  //13
		       //N+3.0+4.0+2+0+0.0+0+0+1+0+1
		map.put("N+1.0+4.0+4+1+1.0+0+0+4+0+0", new Double(4.44));  //14
		map.put("N+2.0+4.0+3+1+1.0+0+0+2+1+0", new Double(13.97));  //15
		map.put("N+1.0+4.0+4+2+1.0+0+0+4+0+0", new Double(16.61));  //16
		       //N+1.0+4.0+4+2+0.0+0+0+4+0+0
		map.put("N+2.0+4.0+3+2+1.0+0+0+2+1+0", new Double(25.59));  //17
		map.put("N+1.0+4.0+4+3+1.0+0+0+4+0+0", new Double(27.64));  //18
		map.put("N+1.5+3.0+2+0+0.0+2+0+0+0+0", new Double(12.89));  //19
		map.put("N+1.5+4.5+3+0+0.0+3+0+0+0+0", new Double(4.41));  //20
		map.put("N+1.5+4.0+3+0+0.0+2+0+1+0+0", new Double(4.93));  //21
		map.put("N+2.0+5.0+3+0+0.0+2+0+1+0+0", new Double(8.39));  //22
		map.put("N+1.5+4.0+3+1+0.0+2+0+1+0+0", new Double(15.79));  //23
		map.put("N+1.5+4.5+3+0+1.0+3+0+0+0+0", new Double(4.1));  //24
		map.put("N+1.5+4.0+3+0+1.0+2+0+1+0+0", new Double(3.88));  //25
		map.put("N+1.5+4.0+3+1+1.0+2+0+1+0+0", new Double(14.14));  //26

		map.put("O+1.0+2.0+2+0+0.0+0+0+2+0+0", new Double(9.23));  //27
		map.put("O+1.0+2.0+2+0+0.0+0+1+2+0+0", new Double(12.53));  //28
		map.put("O+2.0+2.0+1+0+0.0+0+0+0+1+0", new Double(17.07));  //29
		   //    O+2.0+2.0+1+0+0.0+0+0+0+1+0
		map.put("O+1.0+1.0+1+0+-1.0+0+0+1+0+0", new Double(23.06));  //30
		    //   O+1.0+2.0+2+0+0.0+0+0+2+0+0  //
		map.put("O+1.0+2.0+2+1+0.0+0+0+2+0+0", new Double(20.23));  //31
		       //O+1.0+1.0+1+0+0.0+0+0+1+0+0
		map.put("O+1.5+3.0+2+0+0.0+2+0+0+0+0", new Double(13.14));  //32
		
		map.put("S+1.0+2.0+2+0+0.0+0+0+2+0+0", new Double(25.3));  //33
		map.put("S+2.0+2.0+1+0+0.0+0+0+0+1+0", new Double(32.09));  //34
		map.put("S+2.0+4.0+3+0+0.0+0+0+2+1+0", new Double(19.21));  //35
		map.put("S+2.0+6.0+4+0+0.0+0+0+2+2+0", new Double(8.38));  //36
		map.put("S+1.0+2.0+2+1+0.0+0+0+2+0+0", new Double(38.8));  //37
		map.put("S+1.5+3.0+2+0+0.0+2+0+0+0+0", new Double(28.24));  //38
		map.put("S+2.0+5.0+3+0+0.0+2+0+0+1+0", new Double(21.7));  //39
		  //
		map.put("P+1.0+3.0+3+0+0.0+0+0+3+0+0", new Double(13.59));  //40
		map.put("P+2.0+3.0+3+0+0.0+0+0+1+1+0", new Double(34.14));  //41
		map.put("P+2.0+5.0+3+0+0.0+0+0+3+1+0", new Double(9.81));  //42
		map.put("P+2.0+4.0+3+1+0.0+0+0+2+1+0", new Double(23.47));  //43
		
		
		RingSet rs = (new AllRingsFinder()).findAllRings(ac);
		//if (checkAromaticity) {
			HueckelAromaticityDetector.detectAromaticity(ac, rs, true);
		//}
		RingSet rsAtom = null;
		Ring ring = null;
		String profile = "";
		Atom[] atoms = ac.getAtoms();
		Atom[] connectedAtomsFirst = null;
		Bond[] connectedBondsfirst = null;
		Vector profiles = new Vector();
		double maxOrder = 0;
		double orderSum = 0;
		double charge = 0;
		int isin3ring = 0;
		int isin3ringcounter = 0;
		int numberOfNeighboors = 0;
		int hCount = 0;
		int singleBondCount = 0;
		int doubleBondCount = 0;
		int tripleBondCount = 0;
		int aromaticBondCount = 0;
		int atomPosition = 0;
		for(int i = 0; i < atoms.length; i ++) {
			if( atoms[i].getSymbol().equals("N") || atoms[i].getSymbol().equals("O") || atoms[i].getSymbol().equals("S") || atoms[i].getSymbol().equals("P") ) {
				System.out.println("tpsa atom: "+atoms[i].getSymbol());
				singleBondCount = 0;
				doubleBondCount = 0;
				tripleBondCount = 0;
				aromaticBondCount = 0;
				hCount = 0;
				connectedBondsfirst = ac.getConnectedBonds(atoms[i]);
				for(int b = 0; b < connectedBondsfirst.length; b++) {
					if(connectedBondsfirst[b].getFlag(CDKConstants.ISAROMATIC)) {
						aromaticBondCount += 1;
					}
					else if(connectedBondsfirst[b].getOrder() == 1.0) {
						singleBondCount += 1;
					}
					else if(connectedBondsfirst[b].getOrder() == 2.0) {
						doubleBondCount += 1;
					}
					else if(connectedBondsfirst[b].getOrder() == 3.0) {
						tripleBondCount += 1;
					}
				}
				maxOrder = 0;
				if(singleBondCount > 0) {
					maxOrder = 1.0;
				}
				if(aromaticBondCount > 0) {
					maxOrder = 1.5;
				}
				if(doubleBondCount > 0) {
					maxOrder = 2.0;
				}
				if(tripleBondCount > 0) {
					maxOrder = 3.0;
				}
				isin3ringcounter = 0;
				isin3ring = 0;
				charge = atoms[i].getFormalCharge(); //
				connectedAtomsFirst = ac.getConnectedAtoms(atoms[i]);
				numberOfNeighboors = connectedAtomsFirst.length;
				for (int a=0; a <numberOfNeighboors;a++) {
					if(connectedAtomsFirst[a].getSymbol().equals("H")) {
						hCount += 1;
					}
				}
				atomPosition = ac.getAtomNumber(atoms[i]);
				orderSum = ac.getBondOrderSum(atoms[i]);
				// isin3ring checker
				if(rs.contains(atoms[i])) {
					rsAtom = rs.getRings(atoms[i]);
					rsAtom.sort();
					for (int f = 0; f < rsAtom.size(); f++)
					{
						ring = (Ring)rsAtom.elementAt(f);
						if (ring.getRingSize() == 3) {
							isin3ring = 1;
						}
					}
				}
				profile = atoms[i].getSymbol() +"+"+ maxOrder +"+"+ orderSum +"+"+ numberOfNeighboors +"+"+ hCount +"+"+ charge +"+"+ aromaticBondCount +"+"+ isin3ring +"+"+ singleBondCount +"+"+ doubleBondCount +"+"+ tripleBondCount;
				System.out.println("tpsa profile: "+profile);
				profiles.add(profile);
			}
		}
		double tpsa = 0;
		for(int p = 0; p < profiles.size(); p ++) {
			if(map.containsKey(profiles.elementAt(p))) {
				tpsa += ((Double)map.get(profiles.elementAt(p))).doubleValue();
				System.out.println("tpsa contribs: "+((Double)map.get(profiles.elementAt(p))).doubleValue());
			}
		}
		profiles.clear();
		return new DoubleResult(tpsa);
	}

	/**
	 *  Gets the parameterNames attribute of the
	 *  TPSADescriptor object
	 *
	 *@return    The parameterNames value
	 */
	public String[] getParameterNames() {
		// no param names to return
		return (null);
	}



	/**
	 *  Gets the parameterType attribute of the
	 *  TPSADescriptor object
	 *
	 *@param  name  Description of the Parameter
	 *@return       The parameterType value
	 */
	public Object getParameterType(String name) {
		return (null);
	}
}

