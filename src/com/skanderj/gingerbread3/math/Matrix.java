package com.skanderj.gingerbread3.math;

import java.util.Arrays;

/**
 *
 * @author Skander
 *
 */
public class Matrix {
	public static Matrix identity(final int size) {
		final Matrix identityMatrix = new Matrix(size, size);
		for (int cursor = 0; cursor < size; cursor += 1) {
			identityMatrix.cells[cursor][cursor] = 1.0F;
		}
		return identityMatrix;
	}

	public static Matrix random(final int lines, final int columns, final double scale) {
		final Matrix randomMatrix = new Matrix(lines, columns);
		for (int line = 0; line < lines; line += 1) {
			for (int column = 0; column < columns; column += 1) {
				randomMatrix.cells[line][column] = (Math.random() > 0.5 ? 1 : -1) * Math.random() * scale;
			}
		}
		return randomMatrix;
	}

	private final int lines, columns;
	private final double[][] cells;

	public Matrix(final int lines, final int columns) {
		this.lines = lines;
		this.columns = columns;
		this.cells = new double[lines][columns];
		for (int line = 0; line < lines; line += 1) {
			for (int column = 0; column < columns; column += 1) {
				this.cells[line][column] = 0;
			}
		}
	}

	public final void copyFrom(final double[][] cells) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] = cells[line][column];
			}
		}
	}

	public final void scale(final double k) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] *= k;
			}
		}
	}

	public final void add(final Matrix matrix) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] += matrix.cells[line][column];
			}
		}
	}

	public final void subtract(final Matrix matrix) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] -= matrix.cells[line][column];
			}
		}
	}

	public final void multiply(final Matrix matrix) {
		Matrix product = new Matrix(this.lines, matrix.columns);
		if (this.columns == matrix.lines) {
			product = new Matrix(this.lines, matrix.columns);
			for (int line = 0; line < product.lines; line += 1) {
				for (int column = 0; column < product.columns; column += 1) {
					for (int cursor = 0; cursor < this.columns; cursor += 1) {
						product.cells[line][column] += (this.cells[line][cursor] * matrix.cells[cursor][column]);
					}
				}
			}
			this.copyFrom(product.cells);
		}
	}

	public final void transpose() {
		final Matrix transpose = new Matrix(this.columns, this.lines);
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				transpose.cells[column][line] = this.cells[line][column];
			}
		}
		this.copyFrom(transpose.cells);
	}

	public final double determinant() {
		final boolean square = this.isSquare();
		if (square) {
			if (this.lines == 2) {
				return ((this.cells[0][0] * this.cells[1][1]) - (this.cells[0][1] * this.cells[1][0]));
			}
			double determinant = 0.0D;
			for (int line = 0; line < this.lines; line += 1) {
				determinant += Math.pow(-1, line) * this.cells[0][line] * this.minor(0, line).determinant();
			}
			return determinant;
		} else {
			return 0;
		}
	}

	public final boolean isSquare() {
		return this.lines == this.columns;
	}

	public Matrix minor(final int targetLine, final int targetColumn) {
		final Matrix minor = new Matrix(this.lines - 1, this.columns - 1);
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; (line != targetLine) && (column < this.columns); column += 1) {
				if (column != targetColumn) {
					minor.cells[line < targetLine ? line : line - 1][column < targetColumn ? column : column - 1] = this.cells[line][column];
				}
			}
		}
		return minor;
	}

	public final double[] reduceLine(final int i) {
		final double[] line = new double[this.cells[i].length + 1];
		if (this.cells[i][i] != -1.0F) {
			final double f = this.cells[i][i];
			for (int j = 0; j < this.cells[i].length; j += 1) {
				line[j] = this.cells[i][j] / f;
			}
			line[line.length - 1] = f;
			return line;
		} else {
			for (int j = 0; j < this.cells[i].length; j += 1) {
				line[j] = this.cells[i][j];
			}
			line[line.length - 1] = 1.0F;
			return line;
		}
	}

	public final double[] scaleLine(final int i, final double scalar) {
		final double[] line = new double[this.cells[i].length];
		for (int j = 0; j < this.cells[i].length; j += 1) {
			line[j] = this.cells[i][j] * scalar;
		}
		return line;
	}

	public final double[] subtractLines(final int j, final int i, final double scalar) {
		final double[] diff = new double[this.cells[j].length];
		for (int k = 0; k < this.cells[j].length; k += 1) {
			diff[k] = (this.cells[j][k] - (scalar * this.cells[i][k]));
		}
		return diff;
	}

	public final Matrix inverseByGaussianReduction() {
		final Matrix reduc = new Matrix(this.cells.length, this.cells[0].length);
		final Matrix inv = new Matrix(this.cells.length, this.cells[0].length);
		for (int i = 0; i < this.cells.length; i += 1) {
			for (int j = 0; j < this.cells[i].length; j += 1) {
				reduc.cells[i][j] = this.cells[i][j];
				if (i == j) {
					inv.cells[i][j] = 1.0F;
				}
			}
		}
		double[] rLine = reduc.reduceLine(0);
		double f = rLine[rLine.length - 1];
		for (int j = 0; j < reduc.cells[0].length; j += 1) {
			reduc.cells[0][j] = rLine[j];
		}
		inv.cells[0] = inv.scaleLine(0, 1.0 / f);
		int c = 0;
		for (int k = 1; k < reduc.cells.length; k += 1) {
			for (int i = k; i < reduc.cells.length; i += 1) {
				f = reduc.cells[i][c];
				if (f == 0.0) {
					continue;
				} else {
					reduc.cells[i] = reduc.subtractLines(i, c, f);
					inv.cells[i] = inv.subtractLines(i, c, f);
				}
			}
			rLine = reduc.reduceLine(k);
			f = rLine[rLine.length - 1];
			for (int j = 0; j < reduc.cells[0].length; j += 1) {
				reduc.cells[k][j] = rLine[j];
			}
			inv.cells[k] = inv.scaleLine(k, 1 / f);
			c += 1;
		}
		c = this.cells.length - 1;
		for (int k = this.cells.length - 2; k >= 0; k -= 1) {
			for (int i = k; i >= 0; i -= 1) {
				f = reduc.cells[i][c];
				if (f == 0) {
					continue;
				} else {
					reduc.cells[i] = reduc.subtractLines(i, c, f);
					inv.cells[i] = inv.subtractLines(i, c, f);
				}
			}
			c -= 1;
		}
		return inv;
	}

	@Override
	public String toString() {
		return Arrays.deepToString(this.cells).replace("], ", "]\n");
	}
}
