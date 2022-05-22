package br.edu.ufabc.EsToDoeasy.model

/**
 * Basic Graph interface.
 */
//interface Graph<T> {
//
//    /**
//     * create vertex.
//     */
//    fun createVertex(data: T): Vertex<T>
//
//    /**
//     * add directed edge.
//     */
//    fun addDirectedEdge(
//        source: Vertex<T>,
//        destination: Vertex<T>,
//        weight: Double?
//    )
//
//    /**
//     * add undirected edge.
//     */
//    fun addUndirectedEdge(
//        source: Vertex<T>,
//        destination: Vertex<T>,
//        weight: Double?
//    )
//
//    /**
//     * add Complete Vertex.
//     */
//    fun add(
//        edge: EdgeType,
//        source: Vertex<T>,
//        destination: Vertex<T>,
//        weight: Double?
//    )
//
//    /**
//     * retuns all indicent edges for a fiven vertex.
//     */
//    fun edges(source: Vertex<T>): ArrayList<Edge<T>>
//
//    /**
//     * weight for a edge, when is usefull.
//     */
//    fun weight(
//        source: Vertex<T>,
//        destination: Vertex<T>
//    ): Double?
//}

/**
 * Edge Types can be directed or undirected.
 */
enum class EdgeType {
    DIRECTED,
    UNDIRECTED
}

/**
 * Vertex class, carry index and data.
 */
data class Vertex<T>(val index: Int, val data: T)

/**
 * Edge class, carry 2 vertex and weight.
 */
data class Edge<T>(
    val source: T,
    val destination: T,
    val weight: Double? = null
)

/**
 * Graph implemented with AdjacencyList by Time complexity.
 */
class AdjacencyList<T : Comparable<T>> {
    /**
     * adjacencies is a Dict(map) that models a Graph relationships.
     */
    private val adjacencies = sortedMapOf<T, ArrayList<Edge<T>>>()

    fun createVertex(data: T) {
        adjacencies[data] = ArrayList()
    }

    private fun addDirectedEdge(source: T, destination: T, weight: Double?) {
        val edge = Edge(source, destination, weight)
        adjacencies[source]?.add(edge)
    }

    private fun addUndirectedEdge(source: T, destination: T, weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    fun add(edge: EdgeType, source: T, destination: T, weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    private fun edges(source: T) =
        adjacencies[source] ?: arrayListOf()

    fun weight(source: T, destination: T): Double? {
        return edges(source).firstOrNull { it.destination == destination }?.weight
    }

    override fun toString(): String {
        return buildString { // 1
            adjacencies.forEach { (vertex, edges) -> // 2
                val edgeString = edges.joinToString { it.destination.toString() } // 3
                append("$vertex ---> [ $edgeString ]\n") // 4
            }
        }
    }


    /**
     * Utils functions for dfs.
     * returns a topological order that satisfies dependencies.
     */
    fun dfsUtil(ordering: List<T>): ArrayList<T> { // O(V + E)
        val visited: HashMap<T, Int> = HashMap()
        val topologicalSortVertex: ArrayList<T> = ArrayList()

        ordering.forEach { vertex ->
            if (visited[vertex] == null) {
                dfs(visited, topologicalSortVertex, vertex)
            }
        }
        return topologicalSortVertex
    }

    /**
     * dfs : infos in https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/.
     */
    fun dfs(visited: HashMap<T, Int>, topologicalSortVertex: ArrayList<T>, vertex: T) {
        visited[vertex] = 1
        adjacencies[vertex]?.forEach { it ->
            if (visited[it.destination] == null)
                dfs(visited, topologicalSortVertex, it.destination)
        }
        topologicalSortVertex.add(vertex)
    }
}
