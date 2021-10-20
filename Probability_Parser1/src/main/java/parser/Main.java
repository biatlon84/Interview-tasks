package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	public static int numberOfDice = 0;
	public static boolean transfer = false;
	public static boolean end = false;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		LexemeBuffer lexemeBuffer = new LexemeBuffer(in.nextLine());
		expr(lexemeBuffer);
	}

	public enum LexemeType {
		LEFT_BRACKET, RIGHT_BRACKET, OP_PLUS, GREATER, OP_MINUS, OP_MUL, OP_DIV, NUMBER, EOF, DICE;
	}

	public static class Lexeme {
		LexemeType type;

		public Lexeme(LexemeType type) {
			this.type = type;
		}
	}

	public static class LexemeNumb extends Lexeme {
		int value;

		public LexemeNumb(LexemeType type, int value) {
			super(type);
			this.value = value;
		}
	}

	public static class LexemeDice extends LexemeNumb {
		int currentValue = 1;
		int id;

		public LexemeDice(LexemeType type, int value, int id) {
			super(type, value);
			this.id = id;
		}

		@Override
		public String toString() {
			return "id" + id + " curr =" + currentValue;
		}
	}

	public static class LexemeBuffer {
		private int pos;

		public List<Lexeme> lexemes;

		public LexemeBuffer(String str) {
			this.lexemes = lexAnalyze(str);
		}

		public Lexeme next() {
			return lexemes.get(pos++);
		}

		public void back() {
			pos--;
		}

		public void resetPos() {
			pos = 0;
		}

		public int getPos() {
			return pos;
		}
	}

	public static List<Lexeme> lexAnalyze(String expText) {
		ArrayList<Lexeme> lexemes = new ArrayList<>();
		int pos = 0;
		int id = 0;
		StringBuilder sb = new StringBuilder();
		while (pos < expText.length()) {
			char c = expText.charAt(pos);
			switch (c) {
			case '(':
				lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET));
				pos++;
				continue;
			case ')':
				lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET));
				pos++;
				continue;
			case '+':
				lexemes.add(new Lexeme(LexemeType.OP_PLUS));
				pos++;
				continue;
			case '-':
				lexemes.add(new Lexeme(LexemeType.OP_MINUS));
				pos++;
				continue;
			case '*':
				lexemes.add(new Lexeme(LexemeType.OP_MUL));
				pos++;
				continue;
			case '>':
				lexemes.add(new Lexeme(LexemeType.GREATER));
				pos++;
				continue;
			case 'd':
				sb.setLength(0);
				do {
					sb.append(c);
					pos++;
					if (pos >= expText.length()) {
						break;
					}
					c = expText.charAt(pos);

				} while (c <= '9' && c >= '0');
				lexemes.add(new LexemeDice(LexemeType.DICE, Integer.parseInt((sb.toString().replace('d', '0'))), id++));
				continue;
			default:
				sb.setLength(0);
				do {
					sb.append(c);
					pos++;
					if (pos >= expText.length()) {
						break;
					}
					c = expText.charAt(pos);
				} while (c <= '9' && c >= '0');
				lexemes.add(new LexemeNumb(LexemeType.NUMBER, Integer.parseInt(sb.toString())));
			}
		}
		lexemes.add(new Lexeme(LexemeType.EOF));
		numberOfDice = id;
		return lexemes;
	}

	public static void expr(LexemeBuffer lexemes) {
		TreeMap<Integer, Integer> tree1 = new TreeMap<>();
		int amount = 0;

		while (!end) {
			transfer = true;
			lexemes.resetPos();
			int an = geater(lexemes);
			Object o = tree1.get(an);
			if (o != null) {
				tree1.replace(an, ((Integer) o) + 1);
			} else {
				tree1.put(an, 1);
			}
			amount++;

			if (numberOfDice == 0) {
				end = true;
			}

		}
		int key = tree1.firstKey();
		for (int i = 0; i < tree1.size() - 1; i++) {
			System.out.print(key);
			System.out.printf(" %.2f\n", (float) (100 * tree1.get(key)) / amount);
			key = tree1.higherKey(key);
		}
		System.out.print(key);
		System.out.printf(" %.2f\n", (float) (100 * tree1.get(key)) / amount);
	}

	public static int geater(LexemeBuffer lexemes) {
		int value = plusminus(lexemes);
		while (true) {
			Lexeme lexeme = lexemes.next();
			switch (lexeme.type) {
			case GREATER:
				value = (value > plusminus(lexemes) ? 1 : 0);
				break;
			case EOF:
			case RIGHT_BRACKET:
				lexemes.back();
				return value;
			default:
				throw new RuntimeException(
						"Unexpected token: " + lexeme.toString() + " at position: " + lexemes.getPos());
			}
		}
	}

	public static int plusminus(LexemeBuffer lexemes) {
		int value = multdiv(lexemes);
		while (true) {
			Lexeme lexeme = lexemes.next();
			switch (lexeme.type) {
			case OP_PLUS:
				value += multdiv(lexemes);
				break;
			case OP_MINUS:
				value -= multdiv(lexemes);
				break;
			case EOF:
			case GREATER:
			case RIGHT_BRACKET:
				lexemes.back();
				return value;
			default:
				throw new RuntimeException(
						"Unexpected token: " + lexeme.toString() + " at position: " + lexemes.getPos());
			}
		}
	}

	public static int multdiv(LexemeBuffer lexemes) {
		int value = factor(lexemes);
		while (true) {
			Lexeme lexeme = lexemes.next();
			switch (lexeme.type) {
			case OP_MUL:
				value *= factor(lexemes);
				break;
			case OP_DIV:
				value /= factor(lexemes);
				break;
			case EOF:
			case RIGHT_BRACKET:
			case OP_PLUS:
			case OP_MINUS:
			case GREATER:
				lexemes.back();
				return value;
			default:
				throw new RuntimeException("Unexpected token: " + lexeme.type + " at position: " + lexemes.getPos());
			}
		}
	}

	public static int factor(LexemeBuffer lexemes) {
		Lexeme lexeme = lexemes.next();
		switch (lexeme.type) {
		case NUMBER:
			return ((LexemeNumb) lexeme).value;
		case DICE:

			if (transfer) {
				((LexemeDice) lexeme).currentValue++;
				if ((((LexemeDice) lexeme).currentValue) > (((LexemeDice) lexeme).value)) {
					((LexemeDice) lexeme).currentValue = 1;
					transfer = true;
				} else {
					transfer = false;
				}

			}
			if (((LexemeDice) lexeme).id == numberOfDice - 1) {

				if (transfer) {
					end = true;
				}
			}

			return ((LexemeDice) lexeme).currentValue;
		case LEFT_BRACKET:
			int value = geater(lexemes);
			lexeme = lexemes.next();
			if (lexeme.type != LexemeType.RIGHT_BRACKET) {
				throw new RuntimeException("Unexpected token: " + lexeme.type + " at position: " + lexemes.getPos());
			}
			return value;
		default:
			throw new RuntimeException("Unexpected token: " + lexeme.toString() + " at position: " + lexemes.getPos());
		}
	}

	public static void pars(String str) {

	}

}