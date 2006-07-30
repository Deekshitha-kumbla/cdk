package org.openscience.cdk.test.reaction.type;


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.ISetOfReactions;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.type.RearrangementCation3Reaction;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

/**
 * TestSuite that runs a test for the RearrangementCation3ReactionTest.
 * Generalized Reaction: [A+]=B => A|-[B+].
 *
 * @cdk.module test-reaction
 */
public class RearrangementCation3ReactionTest extends CDKTestCase {

	private IReactionProcess type;
	/**
	 * Constructror of the RearrangementCation3ReactionTest object
	 *
	 */
	public  RearrangementCation3ReactionTest() {
		type  = new RearrangementCation3Reaction();
	}
    
	public static Test suite() {
		return new TestSuite(RearrangementCation3ReactionTest.class);
	}
	/**
	 * A unit test suite for JUnit. Reaction: C-C=[O+] => C-[C+]O|
	 * Automatic sarch of the centre active.
	 *
	 * @return    The test suite
	 */
	public void testAutomaticSearchCentreActiveExample1() throws ClassNotFoundException, CDKException, java.lang.Exception {
        
		IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		/*C-C=[O+]*/
		IMolecule molecule = getMolecule1();
		setOfReactants.addMolecule(molecule);
		
        Object[] params = {Boolean.FALSE};
        type.setParameters(params);
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        
        
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        Assert.assertEquals(1, product.getAtomAt(1).getFormalCharge());
        Assert.assertEquals(0, product.getLonePairCount(molecule.getAtomAt(1)));
        
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
		/*C[C+]O|*/
        IMolecule molecule2 = getMolecule2();
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,qAC));
		
        Assert.assertEquals(3,setOfReactions.getReaction(0).getMappings().length);
        
        IAtom mappedProduct = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(2));
        assertEquals(mappedProduct, product.getAtomAt(2));
	}
	/**
	 * A unit test suite for JUnit. Reaction: C-C=[O+] => C-[C+]O|
	 * Manually put of the centre active.
	 *
	 * @return    The test suite
	 */
	public void testManuallyPutCentreActiveExample1() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		/*C-C=[O+]*/
		IMolecule molecule = getMolecule1();
		setOfReactants.addMolecule(molecule);
		
		/*manually put the centre active*/
		molecule.getAtomAt(1).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtomAt(2).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBondAt(1).setFlag(CDKConstants.REACTIVE_CENTER,true);

		
        Object[] params = {Boolean.TRUE};
        type.setParameters(params);
        
        /* iniciate */
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        
        /*C-[C+]O|*/
        IMolecule molecule2 = getMolecule2();
        
        QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
		Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,qAC));
	}
	/**
	 * A unit test suite for JUnit. Reaction: C-C=[O+] => C-[C+]O|
	 * Test of mapped between the reactant and product. Only is mapped the centre active.
	 *
	 * @return    The test suite
	 */
	public void testMappingExample1() throws ClassNotFoundException, CDKException, java.lang.Exception {
		IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newSetOfMolecules();
		/*C-C=[O+]*/
		IMolecule molecule = getMolecule1();
		setOfReactants.addMolecule(molecule);
		
		/*automatic search of the centre active*/
        Object[] params = {Boolean.FALSE};
        type.setParameters(params);
        
        /* iniciate */
        ISetOfReactions setOfReactions = type.initiate(setOfReactants, null);
        
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);

        Assert.assertEquals(3,setOfReactions.getReaction(0).getMappings().length);
        
        IAtom mappedProductA1 = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(1));
        assertEquals(mappedProductA1, product.getAtomAt(1));
        IBond mappedProductB1 = (IBond)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getBondAt(1));
        assertEquals(mappedProductB1, product.getBondAt(1));
        mappedProductA1 = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtomAt(2));
        assertEquals(mappedProductA1, product.getAtomAt(2));
	}
	/**
	 * get the molecule 1: C-C=[O+]
	 * 
	 * @return The IMolecule
	 */
	private IMolecule getMolecule1()throws ClassNotFoundException, CDKException, java.lang.Exception {
		Molecule molecule = (new SmilesParser()).parseSmiles("C-C=[O+]");
	    HydrogenAdder adder = new HydrogenAdder();
        adder.addImplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
        return molecule;
	}
	/**
	 * get the molecule 2: C[C+]O|
	 * 
	 * @return The IMolecule
	 */
	private IMolecule getMolecule2()throws ClassNotFoundException, CDKException, java.lang.Exception {
		Molecule molecule = (new SmilesParser()).parseSmiles("C[C+]O");
		HydrogenAdder adder = new HydrogenAdder();
        adder.addImplicitHydrogensToSatisfyValency(molecule);
        LonePairElectronChecker lpcheck = new LonePairElectronChecker();
        lpcheck.newSaturate(molecule);
        return molecule;
	}
}
