val L: Int = 8
val L_var: Double = L.toDouble

val theta: Array[Array[Double]] = Array(
	Array(0, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var),
	Array(.5, 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
	Array(.5, 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
	Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
	Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
	Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
	Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
	Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1))),
	Array(1, 0, 0, 0, 0, 0, 0, 0, 0)
)


def checkMatrix(theta: Array[Array[Double]]): Boolean = {
	val excepted = scala.collection.mutable.ListBuffer[Int]()

	for (i <- 0 until theta.length) {
		if (theta(i)(i) == 1) excepted.append(i)
	}

	var visited = Array.ofDim[Boolean](L + 1)

	def dfs(start: Int): Unit = {
		for ((v, ind) <- theta(start).zipWithIndex) {
			if (v > 0) {
				visited(start) = true
				if (!visited(ind)) {
					// print(ind, end=' ')
					dfs(ind)
				}
			}
		}
	}

	var ans : Boolean = true
	for (i <- 0 until L + 1) {
		if (!excepted.contains(i)) {
			visited = Array.fill(L + 1)(false)
			for (m <- excepted) {
				visited(m) = true
			}
			if (!visited(i)) {
				// print(f'\n\tдля {i}:', end=' ')
				dfs(i)
				// print()
				if (!visited.forall(_ == true)) ans = false
			}
		}
	}

	if (visited.isEmpty){
		ans = false
	} else {
		ans = visited.forall(_ == true)
	}
	ans
}

var b = Array.fill[Double](L)(1)
b(1) = 0

def changeTheta(): Array[Array[Double]] = {
	var oldTheta: Array[Array[Double]] = theta.map(_.clone)
	val newTheta: Array[Array[Double]] = oldTheta.map(_.clone)

	for (m <- b.indices) {
		if (b(m) == 0) {
			for (i <- 0 to L;
			     k <- 0 to L) {
				if (i != m + 1 && k != m + 1) {
					if (oldTheta(i)(m + 1) != 1) {
						newTheta(i)(k) = oldTheta(i)(k) / (1 - oldTheta(i)(m + 1))
					}
				} else if (k != m + 1) {
					newTheta(m + 1)(k) = 0
				} else if (i != m + 1) {
					newTheta(i)(m + 1) = 0
				}
			}
			newTheta(m + 1)(m + 1) = 1
		}
		oldTheta = newTheta.map(_.clone)
	}
	newTheta
}

checkMatrix(theta)
val newTheta = changeTheta()
checkMatrix(newTheta)