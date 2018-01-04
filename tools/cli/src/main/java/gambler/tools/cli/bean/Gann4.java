package gambler.tools.cli.bean;

/**
 * 四方图
 * 
 * @author wangqihui
 *
 */
public class Gann4 {

	public int[][] initGann4() {
		int size = 13;
		int[][] gann4 = new int[size][size];
		gann4[0]  = new int[] { 145, 144, 143, 142, 141, 140, 139, 138, 137, 136, 135, 134, 133 };
		gann4[1]  = new int[] { 146, 101, 100,  99,  98,  97,  96,  95,  94,  93,  92,  91, 132 };
		gann4[2]  = new int[] { 147, 102,  65,  64,  63,  62,  61,  60,  59,  58,  57,  90, 131 };
		gann4[3]  = new int[] { 148, 103,  66,  37,  36,  35,  34,  33,  32,  31,  56,  89, 130 };
		gann4[4]  = new int[] { 149, 104,  67,  38,  17,  16,  15,  14,  13,  30,  55,  88, 129 };
		gann4[5]  = new int[] { 150, 105,  68,  39,  18,   5,   4,   3,  12,  29,  54,  87, 128 };
		gann4[6]  = new int[] { 151, 106,  69,  40,  19,   6,   1,   2,  11,  28,  53,  86, 127 };
		gann4[7]  = new int[] { 152, 107,  70,  41,  20,   7,   8,   9,  10,  27,  52,  85, 126 };
		gann4[8]  = new int[] { 153, 108,  71,  42,  21,  22,  23,  24,  25,  26,  51,  84, 125 };
		gann4[9]  = new int[] { 154, 109,  72,  43,  44,  45,  46,  47,  48,  49,  50,  83, 124 };
		gann4[0]  = new int[] { 155, 110,  73,  74,  75,  76,  77,  78,  79,  80,  81,  82, 123 };
		gann4[11] = new int[] { 156, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
		gann4[12] = new int[] { 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169 };
		return gann4;

	}

	public static void main(String[] args) {
		Gann4 gann4 = new Gann4();
		int[][] gantable = gann4.initGann4();
		for (int i = 0; i < gantable.length; i++) {
			for (int j = 0; j < gantable[0].length; j++) {
				System.out.print(gantable[i][j] + ",");
			}
			System.out.println();
		}
	}

}
