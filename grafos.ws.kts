import java.util.Deque

interface Graph<T> {
    fun createVertex(data: T): Vertex<T>

    fun addDirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        weight: Double?
    )

    fun addUndirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        weight: Double?
    )

    fun add(
        edge: EdgeType,
        source: Vertex<T>,
        destination: Vertex<T>,
        weight: Double?
    )

    fun edges(source: Vertex<T>): ArrayList<Edge<T>>
    fun weight(
        source: Vertex<T>,
        destination: Vertex<T>
    ): Double?
}

enum class EdgeType {
    DIRECTED,
    UNDIRECTED
}

data class Vertex<T>(val index: Int, val data: T)

data class Edge<T>(
    val source: Vertex<T>,
    val destination: Vertex<T>,
    val weight: Double? = null
)

class AdjacencyList<T>: Graph<T> {

    private val adjacencies: HashMap<Vertex<T>, ArrayList<Edge<T>>> = HashMap()
    private val rules: HashMap<Vertex<T>, ArrayList<Edge<T>>> = HashMap()

    override fun createVertex(data: T): Vertex<T> {
        val vertex = Vertex(adjacencies.count(), data)
        adjacencies[vertex] = ArrayList()
        return vertex
    }

    override fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        val edge = Edge(source, destination, weight)
        adjacencies[source]?.add(edge)
    }

    override fun addUndirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    // more to come ...
    override fun add(edge: EdgeType, source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(destination, source, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    override fun edges(source: Vertex<T>) =
        adjacencies[source] ?: arrayListOf()

    override fun weight(source: Vertex<T>, destination: Vertex<T>): Double? {
        return edges(source).firstOrNull { it.destination == destination }?.weight
    }

    override fun toString(): String {
        return buildString { // 1
            adjacencies.forEach { (vertex, edges) -> // 2
                val edgeString = edges.joinToString { it.destination.data.toString() } // 3
                append("${vertex.data} ---> [ $edgeString ]\n") // 4
            }
        }
    }

    fun dfsUtil() {
        var visited: HashMap<Vertex<T>, Int> = HashMap()
        var topologicalSortVertex: ArrayList<Vertex<T>> = ArrayList()

        adjacencies.forEach { (vertice,edge) ->
            if(visited[vertice] == null ){
                dfs(visited, topologicalSortVertex,  vertice)
            }
        }

        println("Vertices visitados")
        println(visited)

        println("Ordem das visitas")
        println(topologicalSortVertex)

    }

    fun dfs(visited: HashMap<Vertex<T>, Int>, topologicalSortVertex: ArrayList<Vertex<T>>,  vertex: Vertex<T>) {
        visited[vertex] = 1
        println(vertex.data)
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

    graph.add(EdgeType.DIRECTED, meia, sapatos, 300.0)
    graph.add(EdgeType.DIRECTED, calças, sapatos, 500.0)
    graph.add(EdgeType.DIRECTED, cuecas, sapatos, 250.0)
    graph.add(EdgeType.DIRECTED, cuecas, calças, 250.0)
    graph.add(EdgeType.DIRECTED, calças, cinto, 450.0)
    graph.add(EdgeType.DIRECTED, cinto, paleto, 300.0)
    graph.add(EdgeType.DIRECTED, camisa, cinto , 600.0)
    graph.add(EdgeType.DIRECTED, camisa, gravata, 50.0)
    graph.add(EdgeType.DIRECTED, gravata, paleto, 292.0)


    println(graph.toString())

    graph.dfsUtil()
}