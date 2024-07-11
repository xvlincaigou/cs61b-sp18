package lab14;

public class Main {
	public static void main(String[] args) {
		Generator generator = new SineWaveGenerator(440);
		GeneratorPlayer gp = new GeneratorPlayer(generator);
		gp.play(1000000);
	}
} 