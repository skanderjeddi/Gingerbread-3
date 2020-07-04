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

	/**
	 * Self explanatory.
	 */
	public final void copyFrom(final double[][] cells) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] = cells[line][column];
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public final void scale(final double k) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] *= k;
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public final void add(final Matrix matrix) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] += matrix.cells[line][column];
			}
		}
	}

	/**
	 * Self explanatory.
	 */
	public final void subtract(final Matrix matrix) {
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				this.cells[line][column] -= matrix.cells[line][column];
			}
		}
	}

	/**
	 * Self explanatory.
	 */
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

	/**
	 * Self explanatory.
	 */
	public final void transpose() {
		final Matrix transpose = new Matrix(this.columns, this.lines);
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				transpose.cells[column][line] = this.cells[line][column];
			}
		}
		this.copyFrom(transpose.cells);
	}

	/**
	 * Self explanatory.
	 */
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

	/**
	 * Self explanatory.
	 */
	public final boolean isSquare() {
		return this.lines == this.columns;
	}

	/**
	 * Used internally.
	 */
	private final Matrix minor(final int targetLine, final int targetColumn) {
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

	/**
	 * Used internally.
	 */
	private final double[] reduceLine(final int lineIndex) {
		final double[] line = new double[this.cells[lineIndex].length + 1];
		if (this.cells[lineIndex][lineIndex] != -1.0F) {
			final double scaleFactor = this.cells[lineIndex][lineIndex];
			for (int column = 0; column < this.cells[lineIndex].length; column += 1) {
				line[column] = this.cells[lineIndex][column] / scaleFactor;
			}
			line[line.length - 1] = scaleFactor;
			return line;
		} else {
			for (int column = 0; column < this.cells[lineIndex].length; column += 1) {
				line[column] = this.cells[lineIndex][column];
			}
			line[line.length - 1] = 1.0F;
			return line;
		}
	}

	/**
	 * Used internally.
	 */
	private final double[] scaleLine(final int lineIndex, final double scalar) {
		final double[] line = new double[this.cells[lineIndex].length];
		for (int column = 0; column < this.cells[lineIndex].length; column += 1) {
			line[column] = this.cells[lineIndex][column] * scalar;
		}
		return line;
	}

	/**
	 * Used internally.
	 */
	private final double[] subtractLines(final int firstLine, final int secondLine, final double scalar) {
		final double[] diff = new double[this.cells[firstLine].length];
		for (int column = 0; column < this.cells[firstLine].length; column += 1) {
			diff[column] = (this.cells[firstLine][column] - (scalar * this.cells[secondLine][column]));
		}
		return diff;
	}

	/**
	 * Self explanatory. Pretty fast.
	 */
	public final Matrix inverseByGaussianReduction() {
		final Matrix reducedMatrix = new Matrix(this.lines, this.columns);
		final Matrix invertedMatrix = new Matrix(this.lines, this.columns);
		for (int line = 0; line < this.lines; line += 1) {
			for (int column = 0; column < this.columns; column += 1) {
				reducedMatrix.cells[line][column] = this.cells[line][column];
				if (line == column) {
					invertedMatrix.cells[line][column] = 1.0F;
				}
			}
		}
		double[] reducedLine = reducedMatrix.reduceLine(0);
		double factor = reducedLine[reducedLine.length - 1];
		for (int column = 0; column < reducedMatrix.columns; column += 1) {
			reducedMatrix.cells[0][column] = reducedLine[column];
		}
		invertedMatrix.cells[0] = invertedMatrix.scaleLine(0, 1.0 / factor);
		int cursor = 0;
		for (int line = 1; line < reducedMatrix.lines; line += 1) {
			for (int line2 = line; line2 < reducedMatrix.lines; line2 += 1) {
				factor = reducedMatrix.cells[line2][cursor];
				if (factor == 0.0) {
					continue;
				} else {
					reducedMatrix.cells[line2] = reducedMatrix.subtractLines(line2, cursor, factor);
					invertedMatrix.cells[line2] = invertedMatrix.subtractLines(line2, cursor, factor);
				}
			}
			reducedLine = reducedMatrix.reduceLine(line);
			factor = reducedLine[reducedLine.length - 1];
			for (int column = 0; column < reducedMatrix.columns; column += 1) {
				reducedMatrix.cells[line][column] = reducedLine[column];
			}
			invertedMatrix.cells[line] = invertedMatrix.scaleLine(line, 1.0F / factor);
			cursor += 1;
		}
		cursor = this.lines - 1;
		for (int line = this.lines - 2; line >= 0; line -= 1) {
			for (int LineIndex = line; LineIndex >= 0; LineIndex -= 1) {
				factor = reducedMatrix.cells[LineIndex][cursor];
				if (factor == 0) {
					continue;
				} else {
					reducedMatrix.cells[LineIndex] = reducedMatrix.subtractLines(LineIndex, cursor, factor);
					invertedMatrix.cells[LineIndex] = invertedMatrix.subtractLines(LineIndex, cursor, factor);
				}
			}
			cursor -= 1;
		}
		return invertedMatrix;
	}

	@Override
	public String toString() {
		return Arrays.deepToString(this.cells).replace("], ", "]\n");
	}
}
