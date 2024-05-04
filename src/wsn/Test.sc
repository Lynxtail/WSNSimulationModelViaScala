import slash.

val L: Double = 8.0
val theta: Array[Array[Double]] = Array(
	Array(0, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L),
	Array(.5, 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
	Array(.5, 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
	Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
	Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
	Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
	Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
	Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1))),
	Array(1, 0, 0, 0, 0, 0, 0, 0, 0)
)

var thetaNormalized = new Matrix(theta)

for (i <- 0 until thetaNormalized.getRowDimension;
     j <- 0 until thetaNormalized.getColumnDimension) {
	if (i == j) thetaNormalized.set(i, j, thetaNormalized.get(i, j) - 1)
}

thetaNormalized

var omega: Array[Double] = Array.fill[Double](L.toInt + 1)(1 / (L + 1))
var oldOmega = Array.fill[Double](L.toInt + 1)(0)
oldOmega(0) = 1
var b = Array.fill[Double](L.toInt + 1)(0)
val eps = 0.0000001

var k = 1

def calculateNorm(omega: Array[Double], oldOmega: Array[Double]): Double = {
	var newOmega = new Array[Double](omega.length)
	for (i <- newOmega.indices) {
		newOmega(i) = omega(i) - oldOmega(i)
	}
	newOmega.normF / oldOmega.normF
}

while (norm(omega - oldOmega) / norm(oldOmega) > eps && k <= 500) {
	oldOmega = omega.clone()
	for (j <- 0 to L) {
		if (thetaNormalized(j)(j) != 0) {
			omega(j) = (b(j) -
			  (thetaNormalized.slice(0, j).map(_.zip(omega.slice(0, j)).map { case (a, b) => a * b }.sum) +
				thetaNormalized.slice(j + 1, L + 1).map(_.zip(oldOmega.slice(j + 1, L + 1)).map { case (a, b) => a * b }.sum)) / thetaNormalized(j)(j)
		} else {
			omega(j) = 0
		}
	}
	k += 1
}


omega = omega.map(_ / omega.sum)

/*
old_omega = np.copy(omega)
for j in range(L + 1):
if theta[j][j] != 0:
omega[j] = (b[j] -
(sum([theta[i][j] * omega[i] for i in range(j)]) +
sum([theta[i][j] * old_omega[i] for i in range(j + 1, L + 1)]))) / theta[j][j]
else:
omega[j] = 0
k += 1
omega = omega / np.sum(omega)
*/

println(s"k = $k\nOmega: ${omega.mkString(",")}\nCheck (~1): ${omega.sum}")
omega