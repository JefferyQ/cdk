/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
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

import org.openscience.cdk.coverage.QsarCoverageTest;
import org.openscience.cdk.qsar.DescriptorExceptionTest;
import org.openscience.cdk.qsar.model.R2.RJavaEnvironmentTest;

/**
 * TestSuite that runs all the sample tests.
 *
 * @cdk.module test-qsar
 * @cdk.depends log4j.jar
 * @cdk.depends junit.jar
 */
public class MqsarTests {

    public static Test suite() {

        TestSuite suite = new TestSuite("All QSAR Tests");

        suite.addTest(QsarCoverageTest.suite());

        // Individual Tests - Please add correlatively
        suite.addTest(new JUnit4TestAdapter(DescriptorExceptionTest.class));
        
        // Stuff for R packages
        String rhome = System.getenv("R_HOME");
        String ldlibrarypath = System.getenv("LD_LIBRARY_PATH");

        if (rhome != null && rhome.equals("") &&
        	ldlibrarypath != null && ldlibrarypath.equals("")) {

//      	from cdk.test.qsar.model.R2
        	suite.addTest(org.openscience.cdk.qsar.model.R2.CNNRegressionModelTest.suite());
        	suite.addTest(org.openscience.cdk.qsar.model.R2.LinearRegressionModelTest.suite());
        	suite.addTest(org.openscience.cdk.qsar.model.R2.QSARRModelTests.suite());
        	suite.addTest(RJavaEnvironmentTest.suite());
        }

//      from cdk.test.qsar.model.R2
 
        return suite;
    }

}
