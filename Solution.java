
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Solution {
	public static void main(String[] args) {
		Solution solution = new Solution();
		// GOOD
		solution.run("{ [] ( ) }");
		solution.run("3*(z*(a-(x-3))/(y))");
		solution.run("(z*(a-(x+3))/(y)*z)");
		solution.run("{ [() []] }");
		// ---------------------------

		// Out of Order
		solution.run("{ [(] ) }");
		solution.run("{ [] () ][ }");
		// ------------------------------

		// missing opener/closer bracket
		solution.run("{ [ }");
		// --------------------

		// missing opener/closer parentheses
		solution.run("((a*b)");
		solution.run("{ [] ) }");
		solution.run(")()");
		// ----------------------------

		// Have more than 1 error
		solution.run("{ [] [ }");
		solution.run("{ [] )(} }");
		solution.run("{ [] (} ( }");
		solution.run("{ [] )[ }");

		// -------------------------------------
		// Null
		solution.run("");
		solution.run(" ");
	}

	public void run(String input) {
		System.out.println("Input = " + input + " is " + parse(input).toString() + "\n");
	}

	/**
	 * This method is used to validate specific error for
	 * braces/brackets/parentheses.
	 * 
	 * Time complexity: O(2n) = O(n)
	 * Space: O(n) cause storing n items
	 * 
	 * @param input This is the first parameter to parse method.
	 * @return Outcome This returns Outcome enum types according to the error such
	 *         as MISSING_OPENER_BRACE, MISSING_OPENER_BRACKET,
	 *         MISSING_OPENER_PARENTHESES,
	 *         MISSING_CLOSER_BRACE, MISSING_CLOSER_BRACKET,
	 *         MISSING_CLOSER_PARENTHESES,
	 *         NO_SYMBOL.
	 */
	private Outcome parse(String input) {
		// if the string is null/no bracket/brace/parentheses
		// @return Outcome This return enum type of NO_SYMBOL
		if (input == null || input.isEmpty() || input.trim().isEmpty()) {
			return Outcome.NO_SYMBOL;
		}

		char[] c = input.toCharArray();
		Map<Character, Integer> map = new HashMap<Character, Integer>();

		// Mapping int values to char keys
		map.put('}', 0);
		map.put(']', 0);
		map.put(')', 0);
		map.put('(', 0);
		map.put('{', 0);
		map.put('[', 0);

		// Store the amount of open and close bracket/brace/parentheses
		// Time complexity: O(n)
		// Space complexity: O(n)
		for (Character ch : c) {
			// inceasing the count for keys which is present
			map.computeIfPresent(ch, (key, val) -> val + 1);
		}
		// Checking for a specific missing braces/brackets/parentheses error
		if ((map.get('(') < map.get(')')))
			return Outcome.MISSING_OPENER_PARENTHESES;
		else if ((map.get('(') > map.get(')')))
			return Outcome.MISSING_CLOSER_PARENTHESES;
		else if ((map.get('[') < map.get(']')))
			return Outcome.MISSING_OPENER_BRACKET;
		else if ((map.get('[') > map.get(']')))
			return Outcome.MISSING_CLOSER_BRACKET;
		else if ((map.get('{') < map.get('}')))
			return Outcome.MISSING_OPENER_BRACE;
		else if ((map.get('{') > map.get('}')))
			return Outcome.MISSING_CLOSER_BRACE;

		// Clearing map so GC can collect
		map.clear();

		return checkBalanced(c);
	}

	/*
	 * This method is used to validate if braces/brackets/parentheses input are
	 * balanced or out of order
	 * Time complexity: O(n) since we will loop through the entire items of char[] c
	 * Space complexity: O(n) will store n items in the stack
	 * 
	 * @param c This is the first paramter to checkBalanced method
	 * 
	 * @return Outcome This returns Outcome enum types such as OUT_OF_ORDER, and
	 * GOOD if braces/brackets/parentheses input are balanced and not out of order
	 */
	private Outcome checkBalanced(char[] c) {
		Deque<Character> stack = new ArrayDeque<Character>(); // store open braces/brackets/parentheses
		List<Character> list = new ArrayList<Character>();
		list.add('}');
		list.add('{');
		list.add('(');
		list.add(')');
		list.add(']');
		list.add('[');
		// Checking for Out of order or is it a valid parenthesis
		// Time complexity: O(n)
		// Space complexity: O(n)
		for (Character ch : c) {
			// Skip any ch that is not braces/brackets/parentheses
			if (!list.contains(ch)) {
				continue;
			}
			if (ch == '}' || ch == ')' || ch == ']') {
				if (stack.isEmpty()) {
					stack.push(ch);
					continue;
				}
				char item = stack.peek();
				if (item == '(' && ch == ')') {
					stack.pop();
				} else if (item == '{' && ch == '}') {
					stack.pop();
				} else if (item == '[' && ch == ']') {
					stack.pop();
				} else {
					return Outcome.OUT_OF_ORDER;
				}
			} else
				stack.push(ch);
		}
		if (!stack.isEmpty())
			return Outcome.OUT_OF_ORDER;

		return Outcome.GOOD;
	}

	private enum Outcome {

		GOOD("GOOD. Nothing is wrong"),
		MISSING_OPENER_PARENTHESES("Missing '(' Opener Parentheses"),
		MISSING_OPENER_BRACE("Missing '{' Opener Brace"),
		MISSING_OPENER_BRACKET("Missing '[' Opener Bracket"),
		MISSING_CLOSER_PARENTHESES("Missing ')' Closer Parentheses"),
		MISSING_CLOSER_BRACE("Missing '}' Closer Brace"),
		MISSING_CLOSER_BRACKET("Missing ']' Closer Bracket"),
		OUT_OF_ORDER("Balanced but out of order"),
		NO_SYMBOL("There is no bracket/brace/parentheses in the provided input");

		private final String text;

		/**
		 * @param text
		 */
		Outcome(final String text) {
			this.text = text;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return text;
		}
	}
}
