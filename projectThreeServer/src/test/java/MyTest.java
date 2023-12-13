import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	int nums [] = new int[3];
	String pHand[] = new String[3];
	String dHand [] = new String[3];
	int wager = 0;
	GameLogic gl = new GameLogic();

	@Test
		//Player beats dealer with ace high card
	void testHighCard(){
		pHand[0] = "s1";
		pHand[1] = "s3";
		pHand[2] = "h3";

		dHand[0] = "s5";
		dHand[1] = "s8";
		dHand[2] = "h3";
		assertEquals("player", gl.determineWinner(pHand, dHand));
	}

	@Test
		//dealers hand doesn't qualify
	void testQueenHigh(){
		dHand[0] = "s5";
		dHand[1] = "s8";
		dHand[2] = "h3";

		assertFalse(gl.queenHigh(dHand));
	}

	@Test
		//player wins has straight flush and get pair plus bonus
	void testPairPlusBonus(){
		pHand[0] = "s1";
		pHand[1] = "s2";
		pHand[2] = "s3";

		wager = 5;

		assertEquals(200, gl.pairPlusBonus(pHand, wager));
	}

	@Test
		//second-highest card used to determine winner
	void testSecondHighest(){
		pHand[0] = "s11";
		pHand[1] = "s8";
		pHand[2] = "h3";

		dHand[0] = "c11";
		dHand[1] = "s7";
		dHand[2] = "h2";

		assertEquals("player", gl.determineWinner(pHand, dHand));
	}

	@Test
		//draw
	void testDraw(){
		pHand[0] = "s11";
		pHand[1] = "s8";
		pHand[2] = "h2";

		dHand[0] = "c11";
		dHand[1] = "c8";
		dHand[2] = "s2";

		assertEquals("draw", gl.determineWinner(pHand, dHand));
	}

	@Test
		//Dealer beats player with flush
	void testFlush(){
		pHand[0] = "s1";
		pHand[1] = "h3";
		pHand[2] = "s7";

		dHand[0] = "s5";
		dHand[1] = "s8";
		dHand[2] = "s3";
		assertEquals("dealer", gl.determineWinner(pHand, dHand));
	}
}