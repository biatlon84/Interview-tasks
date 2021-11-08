package test1;

public class Main {

	public static void main(String[] args) {

		RandomSet set = new RandomSet();
		set.add(88800);
		set.add(88801);
		set.add(88802);
		set.add(88803);
		set.add(88804);
		// set.print();
		set.remove(88802);
		set.remove(88803);
		set.remove(88804);
		set.add(88805);
		set.add(88806);
		System.out.println(set.getRandom());
		set.print();
	}

}
