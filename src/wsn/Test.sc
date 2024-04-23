import scala.math.random
class System(){
	val beDestr: Double = random
}


val systems: List[System] = List(new System, new System, new System)
systems.map(_.beDestr)
systems.tail.map(_.beDestr)
systems.tail.map(_.beDestr).min


val a1: Array[Double] = Array(0.14, 0.15, 0.6, 4.0)
a1 :++ systems.tail.map(_.beDestr).toArray

