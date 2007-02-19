/* $Revision: 7636 $ $Author: ospjuth $ $Date: 2007-01-04 17:46:10 +0000 (Thu, 04 Jan 2007) $
 *
 * Copyright (C) 2007  Ola Spjuth <ospjuth@users.sf.net>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * @cdk.set       libio-cml-customizers
 */

package org.openscience.cdk.libio.cml;

import java.util.Iterator;

import nu.xom.Attribute;
import nu.xom.Element;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.libio.md.ChargeGroup;
import org.openscience.cdk.libio.md.MDMolecule;
import org.openscience.cdk.libio.md.Residue;
import org.openscience.cdk.tools.manipulator.MoleculeSetManipulator;
import org.xmlcml.cml.element.AbstractAtomArray;
import org.xmlcml.cml.element.CMLAtom;
import org.xmlcml.cml.element.CMLAtomArray;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.tools.MoleculeTool;

/**
 * Customize persistence of MDMolecule by adding support for residues and chargegroups
 * 
 * @author ola
 * @cdk.module libiomd
 */
public class MDMoleculeCustomizer implements ICMLCustomizer {

    /**
     * No customization for bonds
     */
	public void customize(IBond bond, Object nodeToAdd) throws Exception {
		// nothing to do
	}
	
	/**
	 * Customize Atom
	 */
    public void customize(IAtom atom, Object nodeToAdd) throws Exception {
    	// nothing to do
    }
    
	/**
	 * Customize Molecule
	 */
    public void customize(IAtomContainer molecule, Object nodeToAdd) throws Exception {
    	if (!(nodeToAdd instanceof CMLMolecule))
    		throw new CDKException("NodeToAdd must be of type nu.xom.Element!");

    	//The nodeToAdd
    	CMLMolecule molToCustomize = (CMLMolecule)nodeToAdd;

    	if ((molecule instanceof MDMolecule)){
        	MDMolecule mdmol = (MDMolecule) molecule;
        	molToCustomize.setConvention("md:mdMolecule");
        	molToCustomize.addNamespaceDeclaration("md", "http://www.bioclipse.net/mdmolecule/");

    		Convertor conv = new Convertor(true,null);

    		//Residues
        	if (mdmol.getResidues().size()>0){
            	Iterator it=mdmol.getResidues().iterator();
            	while (it.hasNext()){
            		Residue residue=(Residue) it.next();
            		int number=residue.getNumber();

                    CMLMolecule resMol = new CMLMolecule();
            		resMol.setDictRef("md:residue");
            		resMol.setTitle(residue.getName());

            		//Append resNo
            		CMLScalar residueNumber=new CMLScalar(number);
            		residueNumber.addAttribute(new Attribute("dictRef", "md:resNo"));
            		residueNumber.appendChild(String.valueOf(number));
            		resMol.appendChild(residueNumber);

            		//Append atoms
            		CMLAtomArray ar=new CMLAtomArray();
            		for (int i=0; i<residue.getAtomCount();i++){
//            			CMLAtom cmlAtom=new CMLAtom();
//            			cmlAtom.setId(residue.getID());
            			ar.addAtom(conv.cdkAtomToCMLAtom(residue, residue.getAtom(i)));
//            			ar.addAtom(cmlAtom);
            		}
            		resMol.addAtomArray(ar);
            		
            		molToCustomize.appendChild(resMol);
            	}
        	}

        	//Chargegroups
        	if (mdmol.getChargeGroups().size()>0){
            	Iterator it=mdmol.getChargeGroups().iterator();
            	while (it.hasNext()){
            		ChargeGroup chargeGroup=(ChargeGroup) it.next();
            		int number=chargeGroup.getNumber();

            		//FIXME: persist the ChargeGroup
            		CMLMolecule cgMol = new CMLMolecule();
            		cgMol.setDictRef("md:chargeGroup");
            		// etc: add name, refs to atoms etc

              		//Append chgrpNo
            		CMLScalar residueNumber=new CMLScalar(number);
            		residueNumber.addAttribute(new Attribute("dictRef", "md:chgrpNo"));
            		residueNumber.appendChild(String.valueOf(number));
            		cgMol.appendChild(residueNumber);

            		//Append atoms from chargeGroup as it is an AC
            		CMLAtomArray ar=new CMLAtomArray();
            		for (int i=0; i<chargeGroup.getAtomCount();i++){
//            			CMLAtom cmlAtom=new CMLAtom();
//            			cmlAtom.setId(residue.getID());
            			ar.addAtom(conv.cdkAtomToCMLAtom(chargeGroup, chargeGroup.getAtom(i)));
//            			ar.addAtom(cmlAtom);
            		}
            		cgMol.addAtomArray(ar);
            		
            		//Append switching atom
            		CMLAtom swAtom=conv.cdkAtomToCMLAtom(chargeGroup, chargeGroup.getSwitchingAtom());
            		swAtom.addAttribute(new Attribute("dictRef", "md:switchingAtom"));
            		cgMol.addAtom(swAtom,true);

            		molToCustomize.appendChild(cgMol);
            	}
        	}
    	}
    }
  	
}
