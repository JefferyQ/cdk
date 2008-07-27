/* $Revision$ $Author$ $Date$    
 * 
 * Copyright (C) 2008  Egon Willighagen <egonw@users.sf.net>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.openscience.cdk.modulesuites;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.coverage.DiffCoverageTest;
import org.openscience.cdk.tools.diff.AtomContainerDiffTest;
import org.openscience.cdk.tools.diff.AtomDiffTest;
import org.openscience.cdk.tools.diff.AtomTypeDiffTest;
import org.openscience.cdk.tools.diff.BondDiffTest;
import org.openscience.cdk.tools.diff.ChemObjectDiffTest;
import org.openscience.cdk.tools.diff.ElectronContainerDiffTest;
import org.openscience.cdk.tools.diff.ElementDiffTest;
import org.openscience.cdk.tools.diff.IsotopeDiffTest;
import org.openscience.cdk.tools.diff.LonePairDiffTest;
import org.openscience.cdk.tools.diff.SingleElectronDiffTest;
import org.openscience.cdk.tools.diff.tree.AbstractDifferenceListTest;
import org.openscience.cdk.tools.diff.tree.AbstractDifferenceTest;
import org.openscience.cdk.tools.diff.tree.AtomTypeHybridizationDifferenceTest;
import org.openscience.cdk.tools.diff.tree.BondOrderDifferenceTest;
import org.openscience.cdk.tools.diff.tree.BooleanArrayDifferenceTest;
import org.openscience.cdk.tools.diff.tree.BooleanDifferenceTest;
import org.openscience.cdk.tools.diff.tree.ChemObjectDifferenceTest;
import org.openscience.cdk.tools.diff.tree.DoubleDifferenceTest;
import org.openscience.cdk.tools.diff.tree.IntegerDifferenceTest;
import org.openscience.cdk.tools.diff.tree.Point2dDifferenceTest;
import org.openscience.cdk.tools.diff.tree.Point3dDifferenceTest;
import org.openscience.cdk.tools.diff.tree.StringDifferenceTest;

/**
 * TestSuite that runs all the JUnit tests for the diff module.
 *
 * @cdk.module test-diff
 */
public class MdiffTests {

    public static Test suite () {
        TestSuite suite= new TestSuite("The CDK diff module Tests");

        suite.addTest(new JUnit4TestAdapter(DiffCoverageTest.class));	
        
        // cdk.tools.diff
        suite.addTest(new JUnit4TestAdapter(AtomDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(AtomTypeDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(ChemObjectDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(ElectronContainerDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(ElementDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(IsotopeDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(BondDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(LonePairDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(SingleElectronDiffTest.class));
        suite.addTest(new JUnit4TestAdapter(AtomContainerDiffTest.class));

        // cdk.tools.diff.tree
        suite.addTest(new JUnit4TestAdapter(AbstractDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(AbstractDifferenceListTest.class));
        
        suite.addTest(new JUnit4TestAdapter(ChemObjectDifferenceTest.class));

        suite.addTest(new JUnit4TestAdapter(BooleanDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(BooleanArrayDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(DoubleDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(IntegerDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(StringDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(BondOrderDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(AtomTypeHybridizationDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(Point2dDifferenceTest.class));
        suite.addTest(new JUnit4TestAdapter(Point3dDifferenceTest.class));

        return suite;
    }

}
