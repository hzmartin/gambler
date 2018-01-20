package gambler.tools.cli.util;

/**
 * 四方图
 * 
 * @author wangqihui
 *
 */
public class Gann4 {

	public static int[][] Gann4_SIZE13 = { { 145, 144, 143, 142, 141, 140, 139, 138, 137, 136, 135, 134, 133 },
			{ 146, 101, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 132 },
			{ 147, 102, 65, 64, 63, 62, 61, 60, 59, 58, 57, 90, 131 },
			{ 148, 103, 66, 37, 36, 35, 34, 33, 32, 31, 56, 89, 130 },
			{ 149, 104, 67, 38, 17, 16, 15, 14, 13, 30, 55, 88, 129 },
			{ 150, 105, 68, 39, 18, 5, 4, 3, 12, 29, 54, 87, 128 },
			{ 151, 106, 69, 40, 19, 6, 1, 2, 11, 28, 53, 86, 127 },
			{ 152, 107, 70, 41, 20, 7, 8, 9, 10, 27, 52, 85, 126 },
			{ 153, 108, 71, 42, 21, 22, 23, 24, 25, 26, 51, 84, 125 },
			{ 154, 109, 72, 43, 44, 45, 46, 47, 48, 49, 50, 83, 124 },
			{ 155, 110, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 123 },
			{ 156, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 },
			{ 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169 }

	};

	public static int[][] gann4matrix(int size) {
		if (size % 2 != 1) {
			throw new IllegalArgumentException("odd size required");
		}
		int middle = size / 2;
		int[][] matrix = new int[size][size];
		int row = middle, col = middle;
		for (int i = 1; i <= size * size; i++) {
			if (i == 1) {
				matrix[row][col] = i;
				continue;
			}
			if (i == 2) {
				col = col + 1;
				matrix[row][col] = i;
				continue;
			}
			// 边界行列处理
			// left check
			if (col - 1 < 0) {
				if (row + 1 < size) {
					// go down
					row = row + 1;
					matrix[row][col] = i;
				} else {
					// go right
					col = col + 1;
					matrix[row][col] = i;
				}
				continue;
			}
			// right check
			if (col + 1 == size) {
				if (row - 1 >= 0) {
					// go up
					row = row - 1;
					matrix[row][col] = i;
				} else {
					col = col - 1;
					matrix[row][col] = i;
				}
				continue;
			}
			// top check
			if (row - 1 < 0) {
				if (col - 1 >= 0) {
					// go left
					col = col - 1;
					matrix[row][col] = i;
				} else {
					row = row + 1;
					matrix[row][col] = i;
				}
				continue;
			}
			// bottom check
			if (row + 1 == size) {
				if (col + 1 < size) {
					// go right
					col = col + 1;
					matrix[row][col] = i;
				} else {
					row = row - 1;
					matrix[row][col] = i;
				}
				continue;
			}

			// 中间块处理
			int left = matrix[row][col - 1];
			int down = matrix[row + 1][col];
			int right = matrix[row][col + 1];
			int up = matrix[row - 1][col];

			if (left > 0 && down >= 0 && up == 0 && right == 0) {
				// go up
				row = row - 1;
				matrix[row][col] = i;
				continue;

			}

			if (right >= 0 && down > 0 && left == 0 && up == 0) {
				// go left
				col = col - 1;
				matrix[row][col] = i;
				continue;

			}

			if (right > 0 && up >= 0 && down == 0 && left == 0) {
				// go down
				row = row + 1;
				matrix[row][col] = i;
				continue;

			}

			if (left >= 0 && up > 0 && down == 0 && right == 0) {
				// go right
				col = col + 1;
				matrix[row][col] = i;
				continue;

			}

		}
		return matrix;
	}

	public static void main(String[] args) {
		int size = 13;
		int[][] matrix = gann4matrix(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(matrix[i][j] + ",");
			}
			System.out.println();
		}
	}

}
