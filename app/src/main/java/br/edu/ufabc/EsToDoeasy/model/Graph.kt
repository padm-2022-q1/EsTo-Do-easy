package br.edu.ufabc.EsToDoeasy.model
import java.util.Deque

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
class AdjacencyList<T> {
    /**
     * adjacencies is a Dict(map) that models a Graph relationships.
     */
    private val adjacencies: HashMap<T, ArrayList<Edge<T>>> = HashMap()

    fun createVertex(data: T) {
        adjacencies[data] = ArrayList()
    }

    fun addDirectedEdge(source: T, destination: T, weight: Double?) {
        val edge = Edge(source, destination, weight)
        adjacencies[source]?.add(edge)
    }

    fun addUndirectedEdge(source: T, destination: T, weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    fun add(edge: EdgeType, source: T, destination: T, weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(destination, source, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    fun edges(source: T) =
        adjacencies[source] ?: arrayListOf()

    fun weight(source: T, destination: T): Double? {
        return edges(source).firstOrNull { it.destination == destination }?.weight
    }

    override fun toString(): String {
        return buildString { // 1
            adjacencies.forEach { (vertex, edges) -> // 2
                val edgeString = edges.joinToString { it.destination.toString() } // 3
                append("${vertex} ---> [ $edgeString ]\n") // 4
            }
        }
    }


    /**
     * Utils functions for dfs.
     * returns a topological order that satisfies dependencies.
     * TODO: implements GTD (EAT THAT FROG) params for sort a TopologicalSort by Priority, by difficulty ?.
     */
    fun dfsUtil(): ArrayList<T> { // O(V + E )
        var visited: HashMap<T, Int> = HashMap()
        var topologicalSortVertex: ArrayList<T> = ArrayList()

        adjacencies.forEach { (vertice,edge) ->
            if(visited[vertice] == null ){
                dfs(visited, topologicalSortVertex,  vertice)
            }
        }
        return topologicalSortVertex
    }
    /**
     * dfs : infos in https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/.
     */
    fun dfs(visited: HashMap<T, Int>, topologicalSortVertex: ArrayList<T>,  vertex: T) {
        visited[vertex] = 1
        adjacencies[vertex]?.forEach { it ->
            if (visited[it.destination] == null)
                dfs(visited, topologicalSortVertex,  it.destination)
        }
        topologicalSortVertex.add(vertex)
    }
}


fun main() {
    println("Teste Grafos  em kotlin")
    val graph = AdjacencyList<String>()
    val meia = graph.createVertex("meia")
    val cuecas = graph.createVertex("cuecas")
    val calças = graph.createVertex("calças")
    val sapatos = graph.createVertex("sapatos")
    val cinto = graph.createVertex("cinto")
    val camisa = graph.createVertex("camisa")
    val gravata = graph.createVertex("gravata")
    val paleto = graph.createVertex("paleto")
    val relogio = graph.createVertex("relogio")

    graph.add(EdgeType.DIRECTED, "meia", "sapatos", 300.0)
    graph.add(EdgeType.DIRECTED, "calças", "sapatos", 500.0)
    graph.add(EdgeType.DIRECTED, "cuecas", "sapatos", 250.0)
    graph.add(EdgeType.DIRECTED, "cuecas", "calças", 250.0)
    graph.add(EdgeType.DIRECTED, "calças", "cinto", 450.0)
    graph.add(EdgeType.DIRECTED, "cinto", "paleto", 300.0)
    graph.add(EdgeType.DIRECTED, "camisa", "cinto" , 600.0)
    graph.add(EdgeType.DIRECTED, "camisa", "gravata", 50.0)
    graph.add(EdgeType.DIRECTED, "gravata", "paleto", 292.0)

    println(graph.toString())
    println(graph.dfsUtil())
}