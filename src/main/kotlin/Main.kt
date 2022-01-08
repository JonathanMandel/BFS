import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File

/**
 * Solution to the HackerRank challenge "Breadth First Search: Shortest Reach"
 */
fun main(args: Array<String>) {
    val infn = "./input01.txt"
    // val outfn = "./output01.txt"
    BFSFileDriver(infn)
}

fun BFSFileDriver(inFn: String) {
    val buf: BufferedReader = File(inFn).bufferedReader()
    // val oBuf: BufferedWriter = File(outFn).bufferedWriter()
    val q = buf.readLine().trim().toInt()
    for (qi in 1..q) {
        val line1 = buf.readLine().trimEnd().split(" ")
        val n = line1[0].toInt()
        val m = line1[1].toInt()

        val edges = Array<Array<Int>>(m) {
            buf.readLine().trimEnd().split(" ").map { it.toInt() }.toTypedArray()
        }

        val s = buf.readLine().trim().toInt()

        val ans = bfs(n, m, edges, s)

        // oBuf.write(ans.joinToString(" "))
        // oBuf.newLine()
        println(ans.joinToString(" "))
    }

    buf.close()
    // oBuf.close()
}

fun BFSDriver() {
    val n = 5
    val m = 3
    val edges = arrayOf( arrayOf(5, 2), arrayOf(5, 3), arrayOf(3, 4) )
    val startVertex = 5
    val result = bfs(n, m, edges, startVertex)
    println(result.joinToString())
}

data class Node(
    val vertex: Int,
    var level: Int,
    var visited: Boolean = false,
    val edges: MutableList<Int>
)

/**
 * Consider an undirected graph where each edge weighs 6 units.
 * Each of the nodes is labeled consecutively from 1 to n.
 * You will be given a number of queries. For each query, you
 * will be given a list of edges describing an undirected graph.
 * After you create a representation of the graph, you must
 * determine and report the shortest distance to each of the
 * other nodes from a given starting position using the breadth-
 * first search algorithm (BFS). Return an array of distances
 * from the start node in node number order. If a node is
 * unreachable, return -1 for that node.
 *
 * n is # of vertices, 2 <= n <= 1000
 * m is # of edges, 1 <= m <= (n*(n-1))/2
 * s is the start vertex
 */

fun bfs(n: Int, m: Int, edges: Array<Array<Int>>, s: Int): Array<Int> {

    // Build the tree. One Node for each vertex. Vertexes are 1-based, so
    // our tree has an unused 0 node.
    val tree = Array<Node>(n+1){ i -> Node(vertex = i, level = 0, edges = mutableListOf()) }
    val uniqEdges = edges.distinct()
    for (e in uniqEdges) {
        val vertex = e[0]
        val edge = e[1]
        tree[vertex].edges.add(edge)
        tree[edge].edges.add(vertex) // edges go both ways
    }

    // Now that we have the tree, do the BFS.
    val Q = ArrayDeque<Int>()
    Q.add(s)
    tree[s].visited = true
    while (!Q.isEmpty()) {
        val v = Q.removeFirst()
        for (e in tree[v].edges) {
            if (!tree[e].visited) {
                tree[e].visited = true
                tree[e].level = tree[v].level + 1
                Q.add(e)
            }
        }
    }

    // Any un-visited nodes are not reachable from s.
    val result = mutableListOf<Int>()
    val ztree = tree.drop(1)
    for (n in ztree) {
        if (!n.visited) result.add(-1)
        else if (n.level != 0) result.add(n.level * 6)
    }

    return result.toTypedArray()
}
