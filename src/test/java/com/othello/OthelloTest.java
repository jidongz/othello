package com.othello;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OthelloTest extends TestCase {

	public OthelloTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(OthelloTest.class);
	}

	public void testOthello() {
		assertTrue(true);
	}
}
