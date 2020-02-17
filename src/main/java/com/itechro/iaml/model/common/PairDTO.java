package com.itechro.iaml.model.common;

import java.io.Serializable;

/**
 * Pair for front end
 *
 * @author : chamara
 */
public class PairDTO<L, R> implements Serializable {

	private L left;

	private R right;

	private PairDTO(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public static <L, R> PairDTO<L, R> of(L left, R right) {
		return new PairDTO<L, R>(left, right);
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PairDTO<?, ?> pairDTO = (PairDTO<?, ?>) o;

		if (left != null ? !left.equals(pairDTO.left) : pairDTO.left != null) return false;
		return right != null ? right.equals(pairDTO.right) : pairDTO.right == null;
	}

	@Override
	public int hashCode() {
		int result = left != null ? left.hashCode() : 0;
		result = 31 * result + (right != null ? right.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PairDTO{" +
				"left=" + left +
				", right=" + right +
				'}';
	}
}
