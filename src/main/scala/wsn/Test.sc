val L_var: Double = 8.0

val theta: Array[Array[Double]] = Array(
	Array(0, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var),
	Array(0, 0, 0, 1.0 / 2.0, 1.0 / 2.0, 0, 0, 0, 0),
	Array(0, 0, 0, 1.0 / 2.0, 1.0 / 2.0, 0, 0, 0, 0),
	Array(0, 0, 0, 0, 0, 1.0 / 2.0, 0, 1.0 / 2.0, 0),
	Array(0, 0, 0, 0, 0, 1.0 / 2.0, 0, 1.0 / 2.0, 0),
	Array(1.0, 0, 0, 0, 0, 0, 0, 0, 0),
	Array(1.0, 0, 0, 0, 0, 0, 0, 0, 0),
	Array(0, 0, 0, 0, 0, 1.0 / 3.0, 1.0 / 3.0, 0, 1.0 / 3.0),
	Array(1.0, 0, 0, 0, 0, 0, 0, 0, 0),
)
var oldTheta: Array[Array[Double]] = theta.map(_.clone)
val newTheta: Array[Array[Double]] = oldTheta.map(_.clone)

theta(0)(0) = 999
theta
oldTheta
newTheta

oldTheta(0)(0) = 111
theta
oldTheta
newTheta