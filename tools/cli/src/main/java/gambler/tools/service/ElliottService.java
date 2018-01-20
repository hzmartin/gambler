package gambler.tools.service;

import gambler.tools.cli.util.Ratio;

public class ElliottService extends AbstractService {

	public void printWaveRuler(float start, float end, float start2) {
		float height = end - start;
		Ratio[] allGlodenRatios = Ratio.allGlodenRatios();
		int celllen = 12;
		int size = 2;
		String format = "|%-" + celllen + ".3f";
		for (Ratio r : allGlodenRatios) {
			fill(celllen, size, '-');

			System.out.format(format, r.getRatio());
			System.out.format(format, (r.getRatio() * height + start2));

			System.out.println("|");
		}

		for (Ratio r : allGlodenRatios) {
			fill(celllen, size, '-');

			float ratio = r.getRatio() + 1;
			System.out.format(format, ratio);
			System.out.format(format, (ratio * height + start2));

			System.out.println("|");
		}
		
		for (Ratio r : allGlodenRatios) {
			fill(celllen, size, '-');

			float ratio = r.getRatio() + 2;
			System.out.format(format, ratio);
			System.out.format(format, (ratio * height + start2));

			System.out.println("|");
		}

		fill(celllen, size, '-');

	}

	public static void main(String[] args) {
		ElliottService service = new ElliottService();
		service.printWaveRuler(3.64f, 40.10f, 25.73f);
	}

}
